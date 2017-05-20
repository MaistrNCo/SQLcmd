package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.MinAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class Drop implements Command {
    private static final String DESCRIPTION = "drop|tableName - to delete table 'tableName' with all contained data";
    private static final String COMMAND_PATTERN = "drop|tableName";
    private static final int TABLE_NAME_INDEX = 1;
    private Parser parser = new ExactAmountParamsParser();
    private final View view;
    private final DBManager dbManager;

    public Drop(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("drop|");
    }

    @Override
    public void process(String userInput) {
        List<String> params = parser.parseInputString(userInput);
        if (parser.isHelpNeeded(params)) {
            view.write(DESCRIPTION);
            return;
        }

        if (!parser.checkParamsAmount(params, COMMAND_PATTERN)) {
            view.writeWrongParamsMsg(COMMAND_PATTERN, userInput);
            return;
        }
        dbManager.drop(params.get(TABLE_NAME_INDEX));
        view.write("Table " + params.get(TABLE_NAME_INDEX) + " deleted from database successfully");
    }
}
