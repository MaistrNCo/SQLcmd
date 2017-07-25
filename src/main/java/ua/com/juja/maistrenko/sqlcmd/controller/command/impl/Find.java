package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;
import java.util.Set;

public class Find implements Command {
    private static final String DESCRIPTION = "find|tableName - to show all data from table 'tableName'";
    private static final String COMMAND_PATTERN = "find|tableName";
    private static final int TABLE_NAME_INDEX = 1;
    private final Parser parser = new ExactAmountParamsParser();
    private final View view;
    private final DBManager dbManager;

    public Find(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("find|");
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

        dbManager.selectAllFromTable(params.get(TABLE_NAME_INDEX));
        Set<String> columnsNames = dbManager.getColumnsNames(params.get(TABLE_NAME_INDEX));
        List<RowData> rowDatas = dbManager.selectAllFromTable(params.get(TABLE_NAME_INDEX));

        view.printTable(rowDatas, columnsNames.toArray(new String[0]));

    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
