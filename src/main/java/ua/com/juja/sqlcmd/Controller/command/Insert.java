package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.RowData;
import ua.com.juja.sqlcmd.View.View;

import java.util.Arrays;

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
        RowData insertData = new RowData((insertParams.length - 2) / 2);
        for (int ind = 0; ind < (insertParams.length - 2); ind += 2) {
            insertData.addColumnValue(insertParams[ind + 2], insertParams[ind + 3]);
        }
        dbManager.insert(insertParams[1], insertData);
        view.printOut(" added new row to table " + insertParams[1]
                + "  which has values: "+ Arrays.toString(insertData.getValues()));
    }
}
