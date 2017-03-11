package ua.com.juja.sqlcmd;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by maistrenko on 02.03.17.
 */
public class PostgresDBManager {
    private Connection connection;

    public static void main(String[] argv) throws SQLException{

        PostgresDBManager ourManager = new PostgresDBManager();


        String password = "postgres";
        String user = "postgres";
        String dBase = "sqlcmd";

        ourManager.connect(dBase, user, password);
        Connection connection = ourManager.connection;
        Statement statement = connection.createStatement();


        //delete
            String deleteRowSQL = "delete from users where name like 'baba%' ";
            int numRow = statement.executeUpdate(deleteRowSQL);
            System.out.println(" was deleted " + numRow + " rows");
        //insert
            String insertRowSQL = "insert into users (name,password) values ('baba galamaga','157')";
            statement.executeUpdate(insertRowSQL);
        //update

            String newName = "pass"+ new Random().nextInt();
            String updateSQL = "update users set password = '"+ newName +"' where id = 3";
            statement.executeUpdate(updateSQL);

        //update prepared
            String updateTableSQL = "UPDATE USERS SET NAME = ? WHERE ID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
            preparedStatement.setString(1, "helen ");
            preparedStatement.setInt(2, 3);
        // execute insert SQL statement
            preparedStatement .executeUpdate();

        //select

        ourManager.select("users");

            if (connection != null) {
                System.out.println("You made it, take control your database now!");
                connection.close();
            } else {
                System.out.println("Failed to make connection!");
            }
            statement.close();



    }

    public RowData[] select(String tableName )  {

        Statement statement = null;
        try {
            int count = getRowCount(tableName);

            statement = connection.createStatement();
            String selectTableSQL = "SELECT * from " + tableName;
            ResultSet rs = statement.executeQuery(selectTableSQL);
            int columnCount = rs.getMetaData().getColumnCount();
            //System.out.println("row count : "+count + " col count : " +columnCount);

            RowData[] dataTable = new RowData[count];
            int ind = 0;
            while (rs.next()) {
                RowData currRow = new RowData(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                //    currRow.addColumnValue(rs.getMetaData().getColumnName(i),rs.getNString(i));
                    currRow.addColumnValue(rs.getMetaData().getColumnName(i),rs.getString(i));
                }
                dataTable[ind++] = currRow;
            }
            rs.close();
            connection.close();
            return dataTable;
        } catch (SQLException e) {
            e.printStackTrace();
            return new RowData[0];
        }

    }

    public int getRowCount(String tableName) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String selectRowCount = "SELECT COUNT (*) from " + tableName;
        ResultSet resCount = statement.executeQuery(selectRowCount);
        resCount.next();
        return resCount.getInt("count");
    }

    public String[] getTablesList() {
        String [] result = new String[100];
        int index=0;
        Statement statement = null;
        try {
            statement = connection.createStatement();
            String selectTableList = "SELECT table_name " +
                    "FROM information_schema.tables "+
                    "WHERE table_schema='public' "+
                    "AND table_type='BASE TABLE'";
            ResultSet rSet = statement.executeQuery(selectTableList);
            while (rSet.next()) {
                result[index++]=rSet.getString("table_name");
            }
            result = Arrays.copyOf(result,index,String[].class);
            rSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Statement is not created");
            result = new String[0];
        }
       return result;
    }

    public void connect(String dBase, String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
        }
        connection = null;
        try {
            //connect
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.11:5432/"+dBase, user,password);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    }

    public void clear(String tableName) {
        String deleteRowsSQL = "delete from " +tableName ;
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(deleteRowsSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(String tableName,RowData rd) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String columnNames = "";
            String values = "";
            for (String colName:rd.getNames()) {
                columnNames = columnNames.concat(((columnNames.length()>0)?",":"")+colName);
            }
            for (Object colValue:rd.getValues()) {
                values = values.concat(((values.length()>0)?",":"")+"'"+colValue+"'");
            }

            String insertRowSQL = "insert into " + tableName
                    + " (" + columnNames + ")   values ("
                    + values + ")";
            statement.executeUpdate(insertRowSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
