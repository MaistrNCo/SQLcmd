package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class ConnectDef implements Command {
    private static final String DESCRIPTION = "connect - to connect to SQL server with settings" +
            " saved in properties file.";
    private final View view;
    private final DBManager dbManager;

    public ConnectDef(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("connect");
    }

    @Override
    public void process(String userInput) {
        ConnectionSettings connectionSettings = new ConnectionSettings();
        connectionSettings.getProperties(dbManager.getPropertiesPath());
        dbManager.connect(connectionSettings);
        view.write("Successful connection!!");
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
