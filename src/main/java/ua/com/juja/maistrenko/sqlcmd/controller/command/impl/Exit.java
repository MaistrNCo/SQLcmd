package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.NormalExitException;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class Exit implements Command {
    private static final String DESCRIPTION = "exit - to close current program";
    private final DBManager dbManager;
    private final View view;

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

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
