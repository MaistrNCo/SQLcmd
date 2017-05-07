package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Drop implements Command {
    private final View view;
    private final DBManager dbManager;

    public Drop(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("drop|");
    }

    @Override
    public void process(String userInput) {
        String tableName = prepareParams(userInput, 2)[1];
        dbManager.drop(tableName);
        view.write("Table " + tableName + " deleted from database successfully");
    }
}
