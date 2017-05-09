package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class Exit implements Command {

    private DBManager dbManager;
    private View view;

    public Exit(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("exit");
    }

    @Override
    public void process(String userInput) {
        if (dbManager.isConnected()) {
            dbManager.disconnect();
        }
        view.write("Goodbye, to see soon. ");
        throw new NormalExitException();
    }
}
