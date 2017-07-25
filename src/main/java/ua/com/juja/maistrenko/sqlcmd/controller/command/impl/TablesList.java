package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.MinAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;

public class TablesList implements Command {
    private static final String DESCRIPTION = "list - to get tables list in connected database";
    private final Parser parser = new MinAmountParamsParser();
    private final DBManager dbManager;
    private final View view;

    public TablesList(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("list");
    }

    @Override
    public void process(String userInput) {
        List<String> params = parser.parseInputString(userInput);

        if (parser.isHelpNeeded(params)) {
            view.write(DESCRIPTION);
            return;
        }
        view.write(dbManager.getTablesList().toString());
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
