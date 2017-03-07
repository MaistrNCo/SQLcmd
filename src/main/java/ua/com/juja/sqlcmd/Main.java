package ua.com.juja.sqlcmd;

import java.sql.*;

/**
 * Created by maistrenko on 02.03.17.
 */
public class Main {
    public static void main(String[] argv) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();
            return;
        }
        Connection connection = null;
        try {
        //connect
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://192.168.1.11:5432/sqlcmd", "postgres",
                    "postgres");

            Statement statement = connection.createStatement();
        //delete
            String deleteRowSQL = "delete from users where name like 'baba%' ";
            int numRow = statement.executeUpdate(deleteRowSQL);
            System.out.println(" was deleted " + numRow + " rows");
        //insert
            String insertRowSQL = "insert into users (name,password) values ('baba galamaga','157')";
            statement.executeUpdate(insertRowSQL);
        //select
            String selectTableSQL = "SELECT id, name from users";
            ResultSet rs = statement.executeQuery(selectTableSQL);
            while (rs.next()) {
                String userid = rs.getString("id");
                String username = rs.getString("name");
                System.out.println("|"+userid+"|" +username);
            }
            rs.close();
            if (connection != null) {
                System.out.println("You made it, take control your database now!");
                connection.close();
            } else {
                System.out.println("Failed to make connection!");
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }

    }
}
