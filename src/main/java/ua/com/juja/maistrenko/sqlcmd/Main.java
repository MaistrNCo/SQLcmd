package ua.com.juja.maistrenko.sqlcmd;

import ua.com.juja.maistrenko.sqlcmd.controller.MainController;
import ua.com.juja.maistrenko.sqlcmd.model.MySQLdbManager;
import ua.com.juja.maistrenko.sqlcmd.model.PostgresDBManager;
import ua.com.juja.maistrenko.sqlcmd.view.Console;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view  = new Console();
        MainController controller = null;
        boolean quit = false;
        view.write("Hello, SQLcmd program started.");
        view.write("Please choose type of SQL connection, type: ");
        view.write("\t 1 - for PostreSQL");
        view.write("\t 2 - for MySQL");
        view.write("or q - to close program");

        while (!quit&&controller==null){
            String userInput = view.read();
            if (userInput.equals("1")) {
                controller = new MainController(new PostgresDBManager(), view);
                controller.run();
            } else if (userInput.equals("2")) {
                controller = new MainController(new MySQLdbManager(), view);
                controller.run();
            }  else if (userInput.equals("q")) {
                quit = true;
            }
        }
    }
}
