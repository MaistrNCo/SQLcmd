package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.model.RowData;
import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Find implements Command {
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
        String[] params = prepareParams(userInput, 2);
        String tableName = params[1];
        String[] columnsNames = dbManager.getColumnsNames(tableName);
        RowData[] rowDatas = dbManager.selectAllFromTable(tableName);
        String header = "|";
        for (String colName : columnsNames) {
            header += colName + "\t|";
        }
        view.write(header);
        for (RowData row : rowDatas) {
            String str = "|";
            for (Object val : row.getValues()) {
                str += val + "\t|";
            }
            view.write(str);
        }
    }
}
