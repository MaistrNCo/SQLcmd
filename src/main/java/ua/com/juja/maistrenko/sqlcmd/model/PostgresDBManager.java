package ua.com.juja.maistrenko.sqlcmd.model;

import java.sql.*;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class PostgresDBManager implements DBManager {

    private Connection connection;

    @Override
    public void connect(ConnectionSettings connSettings) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Where is your PostgreSQL JDBC Driver? ", e);
        }

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + connSettings.getAddress(),
                    connSettings.getUsername(), connSettings.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Connection to database %s for user %s failed!",
                    connSettings.getUsername(), connSettings.getPassword()), e);
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
        Set <String> result = new LinkedHashSet<>();
        String[] types = {"TABLE"};
        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null,
                    null, "%", types);
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
             ResultSet rs = statement.executeQuery(selectTableSQL)) {
            int columnCount = rs.getMetaData().getColumnCount();
            int ind = 0;
            while (rs.next()) {
                RowData currRow = new RowData(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    currRow.addColumnValue(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                dataTable.add(currRow);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't print table " + tableName, e);
        }
        return dataTable;

    }

    @Override
    public int getRowCount(String tableName) {
        String selectRowCount = "SELECT COUNT (*) from " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet resCount = statement.executeQuery(selectRowCount)) {
            resCount.next();
            return resCount.getInt("count");
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get row count for  table " + tableName, e);
        }
    }

    @Override
    public Set<String> getColumnsNames(String tableName) {
        Set<String> result = new LinkedHashSet<>();
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
            while (resultSet.next()) {
                result.add(resultSet.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            //
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
    public void create(String tableName, String[] columnNames) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + tableName +
                "(ID SERIAL NOT NULL PRIMARY KEY ";

        for (String column : columnNames) {
            createTableSQL += ", " + column + " text";
        }
        createTableSQL += ")";
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(createTableSQL);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't create table " + tableName, e);
        }
    }

    @Override
    public void delete(String tableName, String conditionName, String conditionValue) {
        String deleteRowsSQL = "delete from " + tableName + " where " + conditionName + " = '" + conditionValue + "'";
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate(deleteRowsSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't delete records from  table " + tableName, e);
        }
    }

    @Override
    public void insert(String tableName, RowData rd) {
        try (Statement statement = connection.createStatement()) {
            String columnNames = "";
            String values = "";
            for (String colName : rd.getNames()) {
                columnNames = columnNames.concat(((columnNames.length() > 0) ? "," : "") + colName);
            }
            for (Object colValue : rd.getValues()) {
                values = values.concat(((values.length() > 0) ? "," : "") + "'" + colValue.toString() + "'");
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
            String values = "";
            String[] colNames = newValue.getNames();
            Object[] colValues = newValue.getValues();
            for (int ind = 0; ind < colNames.length; ind++) {
                values = values + ((ind != 0) ? "," : "") + colNames[ind] + " = '" + colValues[ind] + "'";
            }
            String updateSQL = "update " + tableName + " set " + values + " where " + conditionName + " = '" + conditionValue + "'";
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't update table " + tableName, e);
        }
    }

    @Override
    public void createDB(String name) {
        String getDBlist = "SELECT * from pg_catalog.pg_database where datname = '" + name + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resCount = statement.executeQuery(getDBlist)) {
            if (!resCount.next()) {
                String sql = "CREATE DATABASE " + name;
                statement.executeUpdate(sql);
                System.out.println("Database created successfully...");
            }
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
