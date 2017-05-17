package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Insert implements Command {
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
        String[] insertParams = prepareParams(userInput, 4);
        RowData insertData = new RowData();
        for (int ind = 0; ind < (insertParams.length - 2); ind += 2) {
            insertData.put(insertParams[ind + 2], insertParams[ind + 3]);
        }
        dbManager.insert(insertParams[1], insertData);
        view.write(" added new row to table " + insertParams[1]
                + "  which has values: " + insertData.getValues().toString());
    }
}
