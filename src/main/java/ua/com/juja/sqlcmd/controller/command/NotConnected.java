package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 19.03.17.
 */
public class NotConnected implements Command {
    private final View view;
    private final DBManager dbManager;

    public NotConnected(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return !dbManager.isConnected();
    }

    @Override
    public void process(String userInput) {
        view.printOut("You can`t use this command until no DB connection present");
    }
}
