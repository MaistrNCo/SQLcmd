package ua.com.juja.sqlcmd.Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by maistrenko on 02.03.17.
 */
public class MySQLdbManager implements DBManager {
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
                    "jdbc:mysql://" + conSettings.getAddress(),
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
    public String[] getTablesList() {


        String[] result = new String[100];
        int index = 0;


        try {
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, "%", null);
            while (rs.next()) {
                result[index++] = rs.getString(3);
            }
            result = Arrays.copyOf(result, index, String[].class);
        } catch (SQLException e) {
            System.out.println("Tables list is not available");
            result = new String[0];
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
        int result = 0;
        String selectRowCount = "SELECT COUNT(*) from " + tableName;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectRowCount);
            while (resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Couldn't get row count for  table " + tableName, e);
        }
        return result;
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
    public void updatePrepared(String tableName, String conditionName, String conditionValue, RowData newValue) {
        //   Statement statement;
        String[] colNames = newValue.getNames();
        String columns = "";
        for (int ind = 0; ind < colNames.length; ind++) {
            if (colNames[ind] != conditionName) {
                columns = columns + ((ind != 0) ? "," : "") + colNames[ind] + " = ?";
            }
        }
        String updateTableSQL = "UPDATE " + tableName + " SET " + columns + " WHERE " + conditionName + " = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL)) {

            Object[] colValues = newValue.getValues();
            int ind;
            for (ind = 0; ind < colNames.length; ind++) {
                if (colNames[ind] != conditionName) {
                    preparedStatement.setString(ind + 1, colValues[ind].toString());
                }
            }

            preparedStatement.setInt(ind + 1, Integer.parseInt(conditionValue));
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Couldn't update table " + tableName, e);
        }
    }

    @Override
    public void createDB(String name) {
        try (Statement st = connection.createStatement()) {

            String sql = "CREATE DATABASE " + name;
            st.executeUpdate(sql);
            System.out.println("Database created successfully...");

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
