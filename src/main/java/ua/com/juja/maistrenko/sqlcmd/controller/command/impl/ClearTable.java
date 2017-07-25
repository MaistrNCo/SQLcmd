package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class ClearTable implements Command {

    private static final String DESCRIPTION = "clear|tableName - to delete all data in table 'tableName'";
    private static final String COMMAND_PATTERN = "clear|tableName";
    private static final int TABLE_NAME_INDEX = 1;
    private final Parser parser = new ExactAmountParamsParser();
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

        if (!parser.checkParamsAmount(params, COMMAND_PATTERN)) {
            view.writeWrongParamsMsg(COMMAND_PATTERN, userInput);
            return;
        }

        int result = dbManager.clear(params.get(TABLE_NAME_INDEX));
        if (result > 0) {
            view.write(result + " data rows deleted from table " + params.get(TABLE_NAME_INDEX));
        } else {
            view.write("Nothing deleted from table " + params.get(TABLE_NAME_INDEX));
        }
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

}
