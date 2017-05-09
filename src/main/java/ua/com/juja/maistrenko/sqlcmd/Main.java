package ua.com.juja.maistrenko.sqlcmd;

import ua.com.juja.maistrenko.sqlcmd.controller.MainController;
import ua.com.juja.maistrenko.sqlcmd.model.PostgresDBManager;
import ua.com.juja.maistrenko.sqlcmd.view.Console;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController(new PostgresDBManager(), new Console());
        controller.run();
    }
}
