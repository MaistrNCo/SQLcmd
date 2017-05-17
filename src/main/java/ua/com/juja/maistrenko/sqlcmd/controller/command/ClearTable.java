package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class ClearTable implements Command {

    private static final String DESCRIPTION = "clear|tableName - to delete all data in table 'tableName'";
    private static final String COMMAND_PATTERN = "clear|tableName";
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

        if (parser.isHelpNeeded(params)) {
            view.write(DESCRIPTION);
            return;
        }

        if (!parser.checkParamsAmount(params,COMMAND_PATTERN)) {
            view.writeWrongParamsMsg(COMMAND_PATTERN,userInput);
            return;
        }


        dbManager.clear(params.get(TABLE_NAME_INDEX));
        view.write("table " + params.get(TABLE_NAME_INDEX) + " cleared successfully");
    }
}
