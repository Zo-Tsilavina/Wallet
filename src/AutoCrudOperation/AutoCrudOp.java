package AutoCrudOperation;

import JDBC.ConnectionDB;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            while (resultSet.next()) {
                T object = createEntityFromResultSet(resultSet);
                result.add(object);
            }
        }

        return result;
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
