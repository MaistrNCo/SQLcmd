package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.controller.command.parse.ExactAmountParamsParser;
import ua.com.juja.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

import java.util.List;

public class ClearTable implements Command {

    public static final String DESCRIPTION = "clear|tableName - to delete all data in table 'tableName'";
    public static final String COMMAND_PATTERN = "clear|tableName";
    private static final int TABLE_NAME_INDEX = 1;
    private Parser parser = new ExactAmountParamsParser();
    private final DBManager dbManager;
    private final View view;

    public ClearTable(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("clear|");
    }

    @Override
    public void process(String userInput) {
        List<String> params = parser.parseInputString(userInput);
        if (!parser.checkParamsAmount(params,COMMAND_PATTERN)) {
            view.write("Wrong parameters amount");
            return;
        }

        if (parser.isHelpNeeded(params)) {
            view.write(DESCRIPTION);
            return;
        }

        dbManager.clear(params.get(TABLE_NAME_INDEX));
        view.write("table " + params.get(TABLE_NAME_INDEX) + " cleared successfully");
    }
}
