package ua.com.juja.sqlcmd.Controller;

import ua.com.juja.sqlcmd.Model.PostgresDBManager;
import ua.com.juja.sqlcmd.View.Console;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Main {
    public static void main(String[] args) {
        MainController controller = new MainController(new PostgresDBManager(), new Console());
        controller.run();
    }
}
