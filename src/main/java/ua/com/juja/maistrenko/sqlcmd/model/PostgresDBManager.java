package ua.com.juja.maistrenko.sqlcmd.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PostgresDBManager implements DBManager {

    private Connection connection;

    @Override
    public void connect(ConnectionSettings connSettings) throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection(
                "jdbc:postgresql://" + connSettings.getAddress(),
                connSettings.getUsername(), connSettings.getPassword());
    }

    @Override
    public void disconnect() throws SQLException {
        connection.close();
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public String[] getTablesList() throws SQLException {

        String[] result = new String[100];
        int index = 0;
        String[] types = {"TABLE"};

        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null,
                    null, "%", types);
            while (rs.next()) {
                result[index++] = rs.getString(3);
            }
            result = Arrays.copyOf(result, index, String[].class);
        } catch (SQLException e) {
            throw e;
        }
        return result;
    }

    @Override
    public RowData[] selectAllFromTable(String tableName) {
        String selectTableSQL = "SELECT * from " + tableName;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectTableSQL)) {
            int count = getRowCount(tableName);
            int columnCount = rs.getMetaData().getColumnCount();
            //System.out.println("row count : "+count + " col count : " +columnCount);

            RowData[] dataTable = new RowData[count];
            int ind = 0;
            while (rs.next()) {
                RowData currRow = new RowData(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    currRow.addColumnValue(rs.getMetaData().getColumnName(i), rs.getString(i));
                }
                dataTable[ind++] = currRow;
            }
            return dataTable;
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't print table " + tableName, e);
        }

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
    public String[] getColumnsNames(String tableName) {
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
            ArrayList<String> result = new ArrayList<>();
            while (resultSet.next()) {
                String name = resultSet.getString("COLUMN_NAME");
                //   String type = resultSet.getString("TYPE_NAME");
                //   int size = resultSet.getInt("COLUMN_SIZE");
                result.add(name);
                //   System.out.println("Column name: [" + name + "]; type: [" + type + "]; size: [" + size + "]");
            }
            return result.toArray(new String[result.size()]);
        } catch (SQLException e) {
            return new String[0];
        }
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
    public void updateTableByCondition(String tableName, String conditionName, String conditionValue, RowData newValue) {
        try (Statement statement = connection.createStatement()) {
            String values = "";
            String[] colNames = newValue.getNames();
            Object[] colValues = newValue.getValues();
            for (int ind = 0; ind < colNames.length; ind++) {
                values = values + ((ind != 0) ? "," : "") + colNames[ind] + " = '" + colValues[ind] + "'";
            }
            String updateSQL = "updateTableByCondition " + tableName + " set " + values + " where " + conditionName + " = '" + conditionValue + "'";
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't updateTableByCondition table " + tableName, e);
        }
    }

    @Override
    public void createDB(String name) throws SQLException {
        String getDBlist = "SELECT * from pg_catalog.pg_database where datname = '" + name + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resCount = statement.executeQuery(getDBlist)) {
            if (!resCount.next()) {
                String sql = "CREATE DATABASE " + name;
                statement.executeUpdate(sql);
                System.out.println("Database created successfully...");
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void dropDB(String name) throws SQLException {
        try (Statement st = connection.createStatement()) {

            String sql = "DROP DATABASE " + name;
            st.executeUpdate(sql);
            System.out.println("Database " + name + " dropped successfully...");

        } catch (SQLException e) {
            throw e;
        }
    }


}
