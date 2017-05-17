package ua.com.juja.maistrenko.sqlcmd.controller.command;

import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;
import java.util.Set;

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
        Set <String> columnsNames = dbManager.getColumnsNames(tableName);
        List<RowData> rowDatas = dbManager.selectAllFromTable(tableName);
        StringBuilder header = new StringBuilder("|");
        for (String colName : columnsNames) {
            header.append(colName + "\t|");
        }
        view.write(header.toString());
        for (RowData row : rowDatas) {
            StringBuilder str = new StringBuilder("|");
            for (Object val : row.getValues()) {
                str.append(val + "\t|");
            }
            view.write(str.toString());
        }
    }
}
