package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by maistrenko on 18.03.17.
 */
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
        view.write(Arrays.toString(dbManager.getTablesList()));
    }
}
