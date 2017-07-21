package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

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
        view.write("No DB connection present. Available commands is: help, exit, connect or connect with parameters");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
