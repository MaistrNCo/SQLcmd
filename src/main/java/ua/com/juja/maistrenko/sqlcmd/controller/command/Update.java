package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Update implements Command {
    private final View view;
    private final DBManager dbManager;

    public Update(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.startsWith("updateTableByCondition|");
    }

    @Override
    public void process(String userInput) {
        String[] updParams = prepareParams(userInput, 6);
        RowData insData = new RowData((updParams.length - 4) / 2);
        for (int ind = 0; ind < (updParams.length - 4) / 2; ind += 2) {
            insData.addColumnValue(updParams[ind + 4], updParams[ind + 5]);
        }
        dbManager.updateTableByCondition(updParams[1], updParams[2], updParams[3], insData);
        view.write(" data in table " + updParams[1] + " updated");
    }
}
