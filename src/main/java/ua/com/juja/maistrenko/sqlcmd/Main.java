package ua.com.juja.maistrenko.sqlcmd;

import ua.com.juja.maistrenko.sqlcmd.controller.MainController;
import ua.com.juja.maistrenko.sqlcmd.model.impl.MySQLdbManager;
import ua.com.juja.maistrenko.sqlcmd.model.impl.PostgresDBManager;
import ua.com.juja.maistrenko.sqlcmd.view.impl.Console;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        MainController controller = null;
        boolean quit = false;
        view.write("Hello, SQLcmd program started.");
        view.write("Please choose type of SQL connection : ");
        view.write("\t 1 - for PostgreSQL");
        view.write("\t 2 - for MySQL");
        view.write("or q - to close program");

        while (!quit && controller == null) {
            String userInput = view.read();
            switch (userInput) {
                case "1":
                    controller = new MainController(new PostgresDBManager(), view);
                    controller.run();
                    break;
                case "2":
                    controller = new MainController(new MySQLdbManager(), view);
                    controller.run();
                    break;
                case "q":
                    quit = true;
                    break;
                default:
                    view.write("wrong input");
                    break;
            }
        }
    }
}
