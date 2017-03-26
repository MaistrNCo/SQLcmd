package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.Model.ConnectionSettings;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.View.View;

/**
 * Created by maistrenko on 19.03.17.
 */
public class Connect implements  Command {
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
        String[] defParams = {"192.168.1.11","5432",params[1],params[2],params[3]};
        conSet.setSettings(defParams);

        try {
            dbManager.connect(conSet);
            view.printOut("Successful connection!!");
        } catch (Exception e) {
            throw new RuntimeException("Couldn`t connect to server 192.168.1.11:5432 to DB: "
                    + params[1] + " user: " + params[2] + " password: " + params[3],e);
        }
    }
}