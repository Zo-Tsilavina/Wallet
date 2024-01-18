package AutoCrudOperation;

import JDBC.ConnectionDB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class AutoCrudOp<T> {

    private final ConnectionDB connectionDB = new ConnectionDB();
    private final Class<T> entityClass;

    public AutoCrudOp(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public List<T> findAll() throws SQLException {

        String FIND_ALL_QUERY = "SELECT {COLUMNS} FROM {TABLE}";

        String table = camelToSnakeCase(entityClass.getSimpleName());
        List<String> attributes = getAttributes();

        FIND_ALL_QUERY = FIND_ALL_QUERY.replace("{COLUMNS}", String.join(", ", attributes)).replace("{TABLE}", table);

        List<T> result = new ArrayList<>();

        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
                ResultSet resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()){
                T object = createEntityFromResultSet(resultSet);
                result.add(object);
                while (resultSet.next()) {
                     object = createEntityFromResultSet(resultSet);
                    result.add(object);
                }
            }else {
                throw new SQLException("No " + table + " found");
            }
        }

        return result;
    }

    public T findById(int id) throws SQLException{

        String FIND_BY_ID_QUERY = "SELECT {COLUMNS} FROM {TABLE} WHERE id = {ID}";

        String table = camelToSnakeCase(entityClass.getSimpleName());
        String strId = String.valueOf(id);
        List<String> attributes = getAttributes();

        FIND_BY_ID_QUERY = FIND_BY_ID_QUERY.replace("{COLUMNS}", String.join(", ", attributes))
                                            .replace("{TABLE}", table)
                                            .replace("{ID}", strId);

        T object = null;

        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY);
                ResultSet resultSet = statement.executeQuery();
        ) {
                if (resultSet.next()){
                    object = createEntityFromResultSet(resultSet);
                }else {
                    throw new RuntimeException("No " + table + " with id = " + strId);
                }
        }
        return object;
    }
    public T save(T toSave) {

        String INSERT_QUERY = "INSERT INTO {TABLE} ({INSERT_COLUMNS}) VALUES ({VALUES})";
        String UPDATE_QUERY = "UPDATE {TABLE} SET {VALUES} WHERE id = {ID}";
        String FIND_BY_ID_QUERY = "SELECT {COLUMNS} FROM {TABLE} WHERE id = {ID}";

        String table = camelToSnakeCase(entityClass.getSimpleName());
        List<String> attributes = getAttributes();

        String insertValues = getInsertValues(toSave);

        String updateValues = getUpdateValues(toSave);

        INSERT_QUERY = INSERT_QUERY.replace("{INSERT_COLUMNS}", String.join(", ", attributes))
                                .replace("{TABLE}", table)
                                .replace("{VALUES}", insertValues);

        String toSaveId;

        try {
            Field toSaveIdField = entityClass.getDeclaredField("id");
            Field name = entityClass.getDeclaredField("transactionsId");

            toSaveIdField.setAccessible(true);
            name.setAccessible(true);
            toSaveId = toSaveIdField.get(toSave).toString();

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);        }

            FIND_BY_ID_QUERY = FIND_BY_ID_QUERY.replace("{COLUMNS}", String.join(", ", attributes))
                                                .replace("{TABLE}", table)
                                                .replace("{ID}", toSaveId);
        try (
                Connection connection = connectionDB.getConnection();
                PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)

        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UPDATE_QUERY = UPDATE_QUERY.replace("{ID}", toSaveId)
                                                .replace("{TABLE}", table)
                                                .replace("{VALUES}", updateValues);
                    try (
                            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY);
                    ) {
                        updateStatement.executeUpdate();
                    }
                } else {

                    INSERT_QUERY = INSERT_QUERY.replace("{INSERT_COLUMNS}", String.join(", ", attributes))
                                                .replace("{TABLE}", table)
                                                .replace("{VALUES}", insertValues);

                    try (
                            PreparedStatement insertStatement = connection.prepareStatement(INSERT_QUERY)
                    ) {
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toSave;

    }

    private String getUpdateValues(T toSave) {
        StringBuilder updateValues = new StringBuilder();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            String entityFieldName = field.getName();
            String fieldName = camelToSnakeCase(entityFieldName);

            if (!entityFieldName.equals("id")) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(toSave);
                    updateValues.append(fieldName).append(" = ").append(getFormattedValue(value)).append(", ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (updateValues.length() > 0) {
            updateValues.setLength(updateValues.length() - 2);
        }

        return updateValues.toString();
    }

    private String getFormattedValue(Object value) {
        if (value == null) {
            return "NULL";
        } else if (value instanceof String || value instanceof Date) {
            return "'" + value + "'";
        } else if (value instanceof List<?> listValue) {
            if (listValue.isEmpty()) {
                return "NULL";
            } else if (listValue.get(0) instanceof Integer) {
                return "'" + String.join(",", listValue.stream().map(Object::toString).toArray(String[]::new)) + "'";
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            return value.toString();
        }
    }

    private String getInsertValues(T toSave) {
        StringBuilder values = new StringBuilder();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            try {
                    field.setAccessible(true);
                    Object value = field.get(toSave);
                    values.append(getFormattedValue(value)).append(", ");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
            }
        }
        if (values.length() > 0) {
            values.setLength(values.length() - 2);
        }
        return  values.toString();
    }


    private List<String> getAttributes() {
        List<String> attributes = new ArrayList<>();
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            attributes.add(camelToSnakeCase(field.getName()));
        }

        return attributes;
    }

    private T createEntityFromResultSet(ResultSet resultSet) {
        T entity = null;
        try {
            Constructor<?> constructor = entityClass.getDeclaredConstructor();
            constructor.setAccessible(true);

            entity = (T) constructor.newInstance();

            Field[] fields = entityClass.getDeclaredFields();

            for (Field field : fields) {
                String columnName = camelToSnakeCase(field.getName());
                field.setAccessible(true);

                if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(entity, resultSet.getInt(columnName));
                } else if (field.getType() == String.class) {
                    field.set(entity, resultSet.getString(columnName));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    field.set(entity, resultSet.getDouble(columnName));
                } else if (field.getType() == Timestamp.class) {
                    field.set(entity, resultSet.getTimestamp(columnName));
                } else if (field.getType() == List.class) {
                    List<Integer> list = fetchListFromDatabase(columnName, resultSet);
                    field.set(entity, list);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<Integer> fetchListFromDatabase(String columnName, ResultSet resultSet) throws SQLException {

        String columnValue = resultSet.getString(columnName);
        List<Integer> values = null;

        if (columnValue != null) {

            values = Arrays.stream(columnValue.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
        return values;
    }

    private String camelToSnakeCase(String camelCase) {

        StringBuilder result = new StringBuilder();

        char[] arrayLetters = camelCase.toCharArray();

        for (int i = 0; i < arrayLetters.length; i++) {
            char letter = arrayLetters[i];
            if (i <= 1){
                result.append(Character.toLowerCase(letter));
            }else if (Character.isUpperCase(letter)) {
                result.append('_').append(Character.toLowerCase(letter));
            } else {
                result.append(letter);
            }
        }

        return result.toString();
    }

}
