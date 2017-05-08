package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

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
        view.write("You can`t use this command until no DB connection present");
    }
}
