package ua.com.juja.sqlcmd.Model;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by maistrenko on 02.03.17.
 */
public class PostgresDBManager implements DBManager {
    private Connection connection;

    @Override
    public String[] loadFromIni(String fileName) throws FileNotFoundException{
        String[] result = new String[5];
        int caught  = 0;
        try{

            FileReader file = new FileReader(fileName);
            BufferedReader br = new BufferedReader(file);
            String curStr;
            System.out.println("found file " + fileName);
            while((curStr = br.readLine())!=null){
                String[] splited  = curStr.split(":");
                System.out.println(Arrays.toString(splited));
                switch(splited[0]){
                    case "server":{
                        result[0] = splited[1];
                        caught++;
                        break;
                    }
                    case "port":{
                        result[1] = splited[1];
                        caught++;
                        break;
                    }
                    case "base":{
                        result[2] = splited[1];
                        caught++;
                        break;
                    }
                    case "username":{
                        result[3] = splited[1];
                        caught++;
                        break;
                    }
                    case "password":{
                        result[4] = splited[1];
                        caught++;
                        break;
                    }
                }
            }


        }catch(FileNotFoundException e){

            throw new RuntimeException("file Postgres.ini not found ",e);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (caught==5) return result;
        else return new String[0];
    }

    @Override
    public void connect(ConnectionSettings conSettings) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Where is your PostgreSQL JDBC Driver? ",e);
        }

        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:postgresql://"+ conSettings.getAddress(),
                    conSettings.getUsername(),conSettings.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Connection to database %s for user %s failed!",
                    conSettings.getUsername(),conSettings.getPassword()),e);
        }
    }

    @Override
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

    @Override
    public RowData[] selectAllFromTable(String tableName)  {

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
            return dataTable;
        } catch (SQLException e) {
            e.printStackTrace();
            return new RowData[0];
        }

    }

    @Override
    public int getRowCount(String tableName) throws SQLException {
        Statement statement;
        statement = connection.createStatement();
        String selectRowCount = "SELECT COUNT (*) from " + tableName;
        ResultSet resCount = statement.executeQuery(selectRowCount);
        resCount.next();
        return resCount.getInt("count");
    }

    @Override
    public String[] getColumnsNames(String tableName){
        try {
            DatabaseMetaData metadata = connection.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
            ArrayList<String> result = new ArrayList<>();

            int index = 0 ;
            while (resultSet.next()) {
                String name = resultSet.getString("COLUMN_NAME");
             //   String type = resultSet.getString("TYPE_NAME");
             //   int size = resultSet.getInt("COLUMN_SIZE");
                result.add(name);
             //   System.out.println("Column name: [" + name + "]; type: [" + type + "]; size: [" + size + "]");
            }
            return result.toArray( new String[result.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    @Override
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

    @Override
    public void drop(String tableName) {
        String dropTableSQL = "drop table " +tableName ;
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(dropTableSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String tableName, String[] columnNames) {
        String createTableSQL = "CREATE TABLE " + tableName +
                "(ID INT PRIMARY KEY     NOT NULL";

        for (String column:columnNames) {
            createTableSQL+=", " + column + " text";
        }
        createTableSQL+=")";
//                " NAME           TEXT    NOT NULL, " +
//                " AGE            INT     NOT NULL, " +
//                " ADDRESS        CHAR(50), " +
//                " SALARY         REAL)";

        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String tableName, String conditionName, String conditionValue) {
        String deleteRowsSQL = "delete from " +tableName +" where "+ conditionName +" = '"+conditionValue +"'";
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(deleteRowsSQL);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(String tableName, RowData rd) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String columnNames = "";
            String values = "";
            for (String colName:rd.getNames()) {
                columnNames = columnNames.concat(((columnNames.length()>0)?",":"")+colName);
            }
            for (Object colValue:rd.getValues()) {
                values = values.concat(((values.length()>0)?",":"")+"'"+colValue.toString()+"'");
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

    @Override
    public void update(String tableName, String conditionName, String conditionValue, RowData newValue) {
        Statement statement;
        try {
            statement = connection.createStatement();
            String values ="";

            String[] colNames = newValue.getNames();
            Object[] colValues = newValue.getValues();


            for (int ind=0;ind<colNames.length;ind++) {
            //    if(colNames[ind]==conditionName) continue;
                values = values +((ind!=0)?",":"")+ colNames[ind]+" = '" + colValues[ind]+"'";
            }
            String updateSQL = "update " + tableName +" set " + values +" where "+ conditionName +" = '"+conditionValue+"'";
            statement.executeUpdate(updateSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePrepared(String tableName, String conditionName, String conditionValue, RowData newValue) {
        Statement statement;
        try {
            String[] colNames = newValue.getNames();
            String columns ="";
            for (int ind=0;ind<colNames.length;ind++) {
                if(colNames[ind]!=conditionName) {
                    columns = columns + ((ind != 0) ? "," : "") + colNames[ind] + " = ?";
                }
            }
            String updateTableSQL = "UPDATE USERS SET " + columns + " WHERE " + conditionName + "= '?'";
            PreparedStatement preparedStatement = connection.prepareStatement(updateTableSQL);
            Object[] colValues = newValue.getValues();
            int ind;
            for (ind=0;ind<colNames.length;ind++) {
                if(colNames[ind]!=conditionName) {
                    preparedStatement.setString(ind+1, colValues[ind].toString());
                }
            }

            preparedStatement.setInt(ind+1, Integer.parseInt(conditionValue)  );
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
