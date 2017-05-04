package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.model.PostgresDBManager;
import ua.com.juja.sqlcmd.view.Console;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController(new PostgresDBManager(), new Console());
        controller.run();
    }
}
