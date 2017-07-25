package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.ConnectParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.ConnectionSettings;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class Connect implements Command {

    private static final String DESCRIPTION = "connect|serverName:port|dataBase|userName|password - " +
            "for connection to SQL server. You can omit 'port' or 'serverName:port' for default port on localhost";
    private static final String COMMAND_PATTERN = "connect|dataBase|userName|password";
    private final Parser parser = new ConnectParamsParser();
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
        if (parser.isHelpNeeded(userInput)) {
            view.write(DESCRIPTION);
            return;
        }
        List<String> params = parser.parseInputString(userInput);
        if (!parser.checkParamsAmount(params, COMMAND_PATTERN)) {
            view.writeWrongParamsMsg(COMMAND_PATTERN, userInput);
            return;
        }
        ConnectionSettings conSet = new ConnectionSettings(params, dbManager);
        dbManager.connect(conSet);
        view.write("Successful connection!!");

    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
