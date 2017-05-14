package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by maistrenko on 19.03.17.
 */
public class Connect implements Command {
    private final View view;
    private final DBManager dbManager;

    public Connect(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("connect|");
    }

    @Override
    public void process(String userInput) {
        ConnectionSettings conSet = new ConnectionSettings();
        String[] params = prepareParams(userInput, 6);
        conSet.setSettings(Arrays.copyOfRange(params,1,params.length));
        dbManager.connect(conSet);
        view.write("Successful connection!!");

    }
}
