package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.View.View;

import java.util.Arrays;

/**
 * Created by maistrenko on 18.03.17.
 */
public class List implements Command {
    private final DBManager dbManager;
    private final View view;

    public List(DBManager dbManager, View view) {
        this.dbManager = dbManager;
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("list");
    }

    @Override
    public void process(String userInput) {
        view.printOut(Arrays.toString(dbManager.getTablesList()));
    }
}
