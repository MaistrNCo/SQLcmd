package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class ConnectDef implements Command {
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
        try {
            ConnectionSettings connectionSettings = new ConnectionSettings();
            connectionSettings.getSettingsFromFile("config/postgres.properties");
            dbManager.connect(connectionSettings);
            view.write("Successful connection!!");
        } catch (Exception e) {
            view.write("Can`t connect, " + e.getMessage());
        }

    }

}
