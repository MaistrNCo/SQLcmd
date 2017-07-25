package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.MinAmountParamsParserWithParity;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class Update implements Command {
    private static final String DESCRIPTION = "update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN " +
            "- to update data in rows of table 'tableName' selected by condition: conditionalColumn == conditionalValue";
    private static final String COMMAND_PATTERN = "update|tableName|column1|value1|column2|value2";
    private static final int TABLE_NAME_INDEX = 1;
    private final Parser parser = new MinAmountParamsParserWithParity();
    private final View view;
    private final DBManager dbManager;

    public Update(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("update|");
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

        RowData conditionData = parser.convertToRowData(params, TABLE_NAME_INDEX + 1, TABLE_NAME_INDEX + 3);
        RowData rowData = parser.convertToRowData(params, TABLE_NAME_INDEX + 3, params.size());

        if (dbManager.update(params.get(TABLE_NAME_INDEX), conditionData, rowData) > 0) {
            view.write("data in table " + params.get(TABLE_NAME_INDEX) + " updated");
        } else {
            view.write("data in table " + params.get(TABLE_NAME_INDEX) + " wan`t updated, check input data please");
        }
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
