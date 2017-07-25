package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.MinAmountParamsParserWithParity;
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
    private final Parser parser = new MinAmountParamsParserWithParity();
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
            String patternFromDescription = DESCRIPTION.substring(0, DESCRIPTION.indexOf('-'));
            view.writeWrongParamsMsg(patternFromDescription, userInput);
            return;
        }

        RowData rowData = parser.convertToRowData(params, TABLE_NAME_INDEX + 1, params.size());

        if (dbManager.insert(params.get(TABLE_NAME_INDEX), rowData) > 0) {
            view.write("added new row to table " + params.get(TABLE_NAME_INDEX)
                    + "  which has values: " + rowData.getValues().toString());
        } else {
            view.write("data in table " + params.get(TABLE_NAME_INDEX)
                    + " wan`t added, check input data please");
        }
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
