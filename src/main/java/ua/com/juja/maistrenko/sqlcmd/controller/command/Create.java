package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Arrays;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Create implements Command {
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
        String[] crtParams = prepareParams(userInput, 3);
        String[] columnNames = Arrays.copyOfRange(crtParams, 2, crtParams.length);
        dbManager.create(crtParams[1], columnNames);
        view.write(" created table " + crtParams[1] +
                " with columns " + Arrays.toString(dbManager.getColumnsNames(crtParams[1])));

    }
}
