package ua.com.juja.maistrenko.sqlcmd.model.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MySQLdbManager implements DBManager {

    private static final int LAST_AND_LENGTH = 4;
    private final String PROPERTIES_PATH = "config/mysql.properties";

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
                    conSettings.getDataBase(), conSettings.getUsername()), e);
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
        String selectTableSQL = "select * from " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectTableSQL)) {
            int columnCount = resultSet.getMetaData().getColumnCount();
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
            //nothing
        }
        return result;
    }

    @Override
    public int clear(String tableName) {
        int result;
        String deleteRowsSQL = "delete from " + tableName;
        try (Statement statement = connection.createStatement()) {
            result = statement.executeUpdate(deleteRowsSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't clear table " + tableName, e);
        }
        return result;
    }


    @Override
    public void drop(String tableName) {
        String dropTableSQL = "drop table if exists " + tableName;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't drop table " + tableName, e);
        }
    }

    @Override
    public void create(String tableName, List<String> columnNames) {
        StringBuilder createTableSQL = new StringBuilder("create table if not exists " + tableName +
                "(" + columnNames.get(0) + " serial not null primary key ");

        for (int ind = 1; ind < columnNames.size(); ind++) {
            createTableSQL.append(", ").append(columnNames.get(ind)).append(" text");
        }
        createTableSQL.append(")");
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create table " + tableName, e);
        }
    }

    @Override
    public int delete(String tableName, RowData conditionData) {
        int result;
        String conditionString = buildCondition(conditionData);
        String deleteRowsSQL = "delete from " + tableName + " where "
                + conditionString;
        try (Statement statement = connection.createStatement()) {
            result = statement.executeUpdate(deleteRowsSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't delete records from  table " + tableName, e);
        }
        return result;
    }

    @Override
    public int insert(String tableName, RowData rowData) {
        int result;
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
            result = statement.executeUpdate(insertRowSQL);
        } catch (MySQLIntegrityConstraintViolationException e) {
            throw new RuntimeException("Couldn't make insert to table " + tableName + " row with defined primary key already exist", e);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't make insert to table " + tableName, e);
        }
        return result;
    }

    @Override
    public int update(String tableName, RowData conditionData, RowData newValue) {
        int result;
        try (Statement statement = connection.createStatement()) {
            String conditionString = buildCondition(conditionData);
            StringBuilder values = new StringBuilder();
            Set<String> colNames = newValue.getNames();
            List<Object> colValues = newValue.getValues();
            int ind = 0;
            for (String column : colNames) {
                values.append((ind != 0) ? "," : "");
                values.append(column).append(" = '").append(colValues.get(ind)).append("'");
                ind++;
            }
            String updateSQL = "update " + tableName +
                    " set " + values + " where " + conditionString;
            result = statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Can't updateTableByCondition table " + tableName, e);
        }
        return result;
    }

    @Override
    public void createDB(String name) {
        try (Statement st = connection.createStatement()) {
            String sql = "create database if not exists " + name;
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(" Couldn't create new database", e);
        }
    }

    @Override
    public void dropDB(String name) {
        try (Statement st = connection.createStatement()) {
            String sql = "drop database if exists " + name;
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(" Couldn't drop database " + name, e);
        }
    }

    @Override
    public String getPropertiesPath() {
        return PROPERTIES_PATH;
    }

    private String buildCondition(RowData conditionData) {
        if (conditionData.isEmpty()) {
            return "";
        }
        StringBuilder conditionStr = new StringBuilder();
        Set<String> columns = conditionData.getNames();
        for (String column : columns) {
            conditionStr.append(column).append(" = '").append(conditionData.get(column)).append("' and ");
        }
        return conditionStr.substring(0, conditionStr.length() - LAST_AND_LENGTH);
    }
}
