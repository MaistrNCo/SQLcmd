package ua.com.juja.maistrenko.sqlcmd.model;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MySQLdbManager implements DBManager {
    public static final int LAST_AND = 4;
    public final String DEFAULT_SERVER_ADDRESS = "localhost";
    public final String DEFAULT_SERVER_PORT = "3306";
    public final String DEFAULT_SERVER_DB = "sqlcmd";
    public final String DEFAULT_SERVER_USER = "root";
    public final String DEFAULT_SERVER_PASSWORD = "root";

    private Connection connection;

    @Override
    public void connect(ConnectionSettings conSettings) {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Where is your PostgreSQL JDBC Driver? ", e);
        }

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + conSettings.getAddress() + "?useSSL=false",
                    conSettings.getUsername(), conSettings.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Connection to database %s for user %s failed!",
                    conSettings.getUsername(), conSettings.getPassword()), e);
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("SQL disconnection problem ", e);
        }
    }


    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public Set<String> getTablesList() {
        Set<String> result = new LinkedHashSet<>();
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null,
                    null, "%", null);
            while (rs.next()) {
                result.add(rs.getString(3));
            }
        } catch (SQLException e) {
            System.out.println("Tables list is not available");
        }
        return result;
    }

    @Override
    public List<RowData> selectAllFromTable(String tableName) {
        List<RowData> dataTable = new LinkedList<>();
        String selectTableSQL = "SELECT * from " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectTableSQL)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
            int ind = 0;
            while (resultSet.next()) {
                RowData currRow = new RowData();
                for (int i = 1; i <= columnCount; i++) {
                    currRow.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                }
                dataTable.add(currRow);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't print table " + tableName, e);
        }
        return dataTable;

    }

    @Override
    public Set<String> getColumnsNames(String tableName) {
        Set<String> result = new LinkedHashSet<>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null,
                    null, tableName, null);
            while (resultSet.next()) {
                result.add(resultSet.getString("COLUMN_NAME"));
            }

        } catch (SQLException e) {

        }
        return result;
    }


    @Override
    public void clear(String tableName) {
        String deleteRowsSQL = "delete from " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteRowsSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't clear table " + tableName, e);
        }
    }

    @Override
    public void drop(String tableName) {
        String dropTableSQL = "drop table " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't drop table " + tableName, e);
        }
    }

    @Override
    public void create(String tableName, List<String> columnNames) {
        StringBuilder createTableSQL = new StringBuilder("CREATE TABLE IF NOT EXISTS " +
                tableName + "(id SERIAL NOT NULL PRIMARY KEY ");

        for (String column : columnNames) {
            createTableSQL.append(", " + column + " text");
        }
        createTableSQL.append(")");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL.toString());
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create table " + tableName, e);
        }
    }

    @Override
    public void delete(String tableName, RowData conditionData) {
        StringBuilder conditionStr = new StringBuilder();
        Set<String> columns = conditionData.getNames();
        for (String column : columns) {
            conditionStr.append(column + " = '" + conditionData.get(column) + "' AND ");
        }

        String deleteRowsSQL = "DELETE FROM " + tableName + " WHERE "
                + conditionStr.substring(0, conditionStr.length() - LAST_AND);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteRowsSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't delete records from  table " + tableName, e);
        }
    }

    @Override
    public void insert(String tableName, RowData rowData) {
        try (Statement statement = connection.createStatement()) {
            String columnNames = "";
            String values = "";

            for (String colName : rowData.getNames()) {
                columnNames = columnNames.concat(((columnNames.length() > 0) ? "," : "") + colName);
                values = values.concat(((values.length() > 0) ? "," : "") + "'" + rowData.get(colName).toString() + "'");
            }

            String insertRowSQL = "insert into " + tableName
                    + " (" + columnNames + ")   values ("
                    + values + ")";
            statement.executeUpdate(insertRowSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't make insert to table " + tableName, e);
        }
    }

    @Override
    public void update(String tableName, String conditionName, String conditionValue, RowData newValue) {
        try (Statement statement = connection.createStatement()) {
            StringBuilder values = new StringBuilder();
            Set<String> colNames = newValue.getNames();
            List<Object> colValues = newValue.getValues();
            int ind = 0;
            for (String column : colNames) {
                values.append(((ind != 0) ? "," : "") + column + " = '" + colValues.get(ind) + "'");
                ind++;
            }
            String updateSQL = "update " + tableName +
                    " set " + values + " where " + conditionName + " = '" + conditionValue + "'";
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Can't updateTableByCondition table " + tableName, e);
        }
    }

    @Override
    public void createDB(String name) {
        try (Statement st = connection.createStatement()) {

            String sql = "CREATE DATABASE IF NOT EXISTS " + name;
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(" Couldn't create new database", e);
        }
    }

    @Override
    public void dropDB(String name) {
        try (Statement st = connection.createStatement()) {

            String sql = "DROP DATABASE " + name;
            st.executeUpdate(sql);
            System.out.println("Database " + name + " dropped successfully...");

        } catch (SQLException e) {
            throw new RuntimeException(" Couldn't drop database " + name, e);
        }
    }


}
