package com.vlteam.vlxbookapplication;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleDatabaseManager {
    private Connection connection;
    private String fileName;

    public SimpleDatabaseManager(String serverName) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + serverName + ".db");
            fileName = serverName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName + ".db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createSimpleTable(String tableName, List<String> dataStruct) {
        connect();
        if (dataStruct.isEmpty()) {
            return;
        }
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        for (String data : dataStruct) {
            queryBuilder.append(data).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(")");
        String query = queryBuilder.toString();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void dropData(String tableName) {
        connect();
        String query = "DROP TABLE " + tableName;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    /**
     * Hàm lưu dữ liệu
     */
    public void save(String tableName, Map<String, Object> data) {
        connect(); //
        // Xây dựng câu truy vấn INSERT dựa trên tên bảng và dữ liệu được truyền vào
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder valuesBuilder = new StringBuilder(") VALUES (");

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            queryBuilder.append(entry.getKey()).append(",");
            valuesBuilder.append("?,");
        }

        queryBuilder.deleteCharAt(queryBuilder.length() - 1); // Xoá dấu phẩy cuối cùng
        valuesBuilder.deleteCharAt(valuesBuilder.length() - 1); // Xoá dấu phẩy cuối cùng

        String query = queryBuilder.append(valuesBuilder).append(")").toString();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            int parameterIndex = 1;
            for (Object value : data.values()) {
                preparedStatement.setObject(parameterIndex++, value);
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect(); //
    }

    public void insert(String tableName, List<String> columnNames, List<Object> dataSets) {
        try (Connection connection = getConnection()) {
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();

            for (String column : columnNames) {
                columns.append(column).append(",");
                placeholders.append("?,");
            }

            String columnStr = columns.substring(0, columns.length() - 1);
            String placeholderStr = placeholders.substring(0, placeholders.length() - 1);

            // Tạo chuỗi placeholders với số lượng dấu ? tương ứng với kích thước của dataSets
            String fullPlaceholderStr = new String(new char[dataSets.size()]).replace("\0", "?,");
            fullPlaceholderStr = fullPlaceholderStr.substring(0, fullPlaceholderStr.length() - 1);

            String query = "INSERT INTO " + tableName + " (" + columnStr + ") VALUES (" + fullPlaceholderStr + ")";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                int count = 1;
                for (Object data : dataSets) {
                    preparedStatement.setObject(count, data);
                    count++;
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý lỗi theo nhu cầu của bạn
        }
    }


    public void removeByKey(String tableName, String key) {
        connect();
        String query = "DELETE FROM " + tableName + " WHERE " + key + " = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, key);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public Object getDataByKey(String tableName, String columnName, String columKey, String key) {
        connect();

        // Xây dựng câu truy vấn SELECT dựa trên tên bảng và điều kiện được truyền vào
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + columKey + " = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, key);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Thay đổi "columnName" thành tên cột cụ thể bạn muốn lấy dữ liệu
                    return resultSet.getObject(columnName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        return null;
    }


    public Object getKeybyData(String tableName, String keyReturn, Map<String, Object> conditions) {
        connect();
        // Xây dựng câu truy vấn SELECT dựa trên tên bảng và điều kiện được truyền vào
        StringBuilder queryBuilder = new StringBuilder("SELECT " + keyReturn + " FROM ").append(tableName).append(" WHERE ");

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            queryBuilder.append(entry.getKey()).append(" = ? AND ");
        }
        queryBuilder.delete(queryBuilder.length() - 5, queryBuilder.length()); // Xoá "AND " cuối cùng
        String query = queryBuilder.toString();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int parameterIndex = 1;
            for (Object value : conditions.values()) {
                preparedStatement.setObject(parameterIndex++, value);
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(keyReturn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }

    public Object getDataByColumn(String tableName, String columnName, String keyColumn, Object keyValue) {
        connect();
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE " + keyColumn + " = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, keyValue);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getObject(columnName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }


    public void addDataByColum(String tableName, String columName, String keyValue, Object key, Object data) {
        connect();
        String query = "INSERT INTO " + tableName + " (" + keyValue + ", " + columName + ") VALUES (?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, key);
            preparedStatement.setObject(2, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void updateDataByColum(String tableName, String columnName, Object newValue, String keyColumn, Object key) {
        connect();
        String query = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE " + keyColumn + " = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, newValue);
            preparedStatement.setObject(2, key);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }
}
