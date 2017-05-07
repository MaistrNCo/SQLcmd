package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.ConnectionSettings;
import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

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
        String[] params = prepareParams(userInput, 3);
        String[] defParams = {"192.168.1.11", "5432", params[1], params[2], params[3]};
        conSet.setSettings(defParams);
        dbManager.connect(conSet);
        view.write("Successful connection!!");

    }
}
