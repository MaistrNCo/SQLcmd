package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Clear implements Command {

    private final DBManager dbManager;
    private final View view;

    public Clear(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("clear|");
    }

    @Override
    public void process(String userInput) {
        String tableName = prepareParams(userInput, 2)[1];
        dbManager.clear(tableName);
        view.printOut("table " + tableName + " cleared successfully");
    }
}
