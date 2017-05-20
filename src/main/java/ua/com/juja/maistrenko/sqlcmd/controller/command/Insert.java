package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.MinAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class Insert implements Command {
    private static final String DESCRIPTION = "insert|tableName|column1|value1|column2|value2|...|columnN|valueN " +
            "- to add new data row in table 'tableName'";
    private static final String COMMAND_PATTERN = "insert|tableName|column1|value1";
    private static final int TABLE_NAME_INDEX = 1;
    private Parser parser = new MinAmountParamsParser();
    private final View view;
    private final DBManager dbManager;

    public Insert(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("insert|");
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

        RowData rowData = parser.convertToRowData(params, TABLE_NAME_INDEX + 1, params.size());

        dbManager.insert(params.get(TABLE_NAME_INDEX), rowData);
        view.write("added new row to table " + params.get(TABLE_NAME_INDEX)
                + "  which has values: " + rowData.getValues().toString());
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
