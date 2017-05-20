package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class Delete implements Command {
    private static final String DESCRIPTION = "delete|tableName|column1|value1|column2|value2... - " +
            "to delete data in table 'tableName' where column1 = value1, column2 = value2 and so on";
    private static final String COMMAND_PATTERN = "delete|tableName|column|value";
    private static final int TABLE_NAME_INDEX = 1;
    private Parser parser = new ExactAmountParamsParser();
    private final DBManager dbManager;
    private final View view;

    public Delete(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("delete|");
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

        RowData conditionData = parser.convertToRowData(params, TABLE_NAME_INDEX + 1, params.size());

        dbManager.delete(params.get(TABLE_NAME_INDEX), conditionData);
        view.write("deleted data from table " + params.get(TABLE_NAME_INDEX));

    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
