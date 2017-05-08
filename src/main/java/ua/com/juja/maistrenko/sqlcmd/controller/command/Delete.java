package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Delete implements Command {
    private final DBManager dbManager;
    private final View view;

    public Delete(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("delete|");
    }

    @Override
    public void process(String userInput) {
        String[] delParams = prepareParams(userInput, 4);
        dbManager.delete(delParams[1], delParams[2], delParams[3]);
        view.write("deleted data from table " + delParams[1]);

    }
}
