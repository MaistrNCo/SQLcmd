package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.MinAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;
import java.util.List;

public class Create implements Command {
    private static final String DESCRIPTION = "create|tableName|column1|column2|...|columnN - " +
            "to create table 'tableName' with defined columns";
    private static final String COMMAND_PATTERN = "create|tableName|column1";
    private static final int TABLE_NAME_INDEX = 1;
    private Parser parser = new MinAmountParamsParser();
    private final View view;
    private final DBManager dbManager;

    public Create(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("create|");
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
        dbManager.create(params.get(TABLE_NAME_INDEX), params.subList(TABLE_NAME_INDEX + 1, params.size()));
        view.write(" created table " + params.get(TABLE_NAME_INDEX) +
                " with columns " + dbManager.getColumnsNames(params.get(TABLE_NAME_INDEX)).toString());

    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
