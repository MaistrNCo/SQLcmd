package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.ConnectionSettings;
import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;


/**
 * Created by maistrenko on 19.03.17.
 */
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
            ConnectionSettings connSet = new ConnectionSettings();
            connSet.getConfFileSettings("Postgres.ini");
            dbManager.connect(connSet);
            view.write("Successful connection!!");
        } catch (Exception e) {
            throw new RuntimeException("Can`t connect, check your Postgres.ini ", e);
        }

    }

}
