package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class TablesList implements Command {
    private final DBManager dbManager;
    private final View view;

    public TablesList(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("list");
    }

    @Override
    public void process(String userInput) {
        view.write(dbManager.getTablesList().toString());
    }
}
