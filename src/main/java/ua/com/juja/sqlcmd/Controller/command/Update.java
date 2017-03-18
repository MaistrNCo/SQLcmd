package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.RowData;
import ua.com.juja.sqlcmd.View.View;

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
        return userInput.startsWith("update|");
    }

    @Override
    public void process(String userInput) {
        String[] updParams = prepareParams(userInput, 6);
        RowData insData = new RowData((updParams.length - 4) / 2);
        for (int ind = 0; ind < (updParams.length - 4) / 2; ind += 2) {
            insData.addColumnValue(updParams[ind + 4], updParams[ind + 5]);
        }
        dbManager.update(updParams[1], updParams[2], updParams[3], insData);
        view.printOut(" data in table " + updParams[1] + " updated");
    }
}
