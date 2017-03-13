package ua.com.juja.sqlcmd.Controller;

import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.PostgresDBManager;
import ua.com.juja.sqlcmd.Model.RowData;
import ua.com.juja.sqlcmd.View.View;

import java.util.Arrays;

/**
 * Created by maistrenko on 12.03.17.
 */
public class MainController {

    private DBManager dbManager;
    private View console;

    public MainController(DBManager dbManager, View console) {
        this.dbManager = dbManager;
        this.console = console;
    }

    private void ConnectDB() {
        dbManager = new PostgresDBManager();
        console.showData("Hello !");
        console.showData("Input data please in format 'database|username|password' :");
        while (true) {

            try {
                String data = console.getData();
                String[] params = prepareParams(data, 3);
                dbManager.connect(params[0], params[1], params[2]);
                console.showData("Successful connection!!");
                break;
            } catch (Exception e) {
                showErrorMesage(e);
            }
        }
    }

    private String[] prepareParams(String data, int expected) {
        String[] params = data.split("\\|");
        if (params.length != expected) {
            throw new IllegalArgumentException("Wrong number of parameters, expected: "
                    + expected
                    + ", actual is "
                    + params.length);
        }
        return params;
    }

    public void showErrorMesage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        console.showData("Unsuccessful operation by reason: " + errorReason);
        console.showData("try again please");
    }

    public void run() {
        ConnectDB();
        console.showData("input command please or 'help' to see commands list");
        while (true) {
            String command = console.getData();
            if (command.equals("help")) {
                showCommandList();
            } else if (command.equals("list")) {
                getTablesList();
            } else if (command.startsWith("find")) {
                getTableContent(command);
            } else if (command.startsWith("clear")) {
                // TODO implement method clear
            } else if (command.startsWith("drop")) {
                // TODO implement method drop
            } else if (command.startsWith("create")) {
                // TODO implement method create
            } else if (command.startsWith("insert")) {
                // TODO implement method insert
            } else if (command.startsWith("update")) {
                // TODO implement method update
            } else if (command.equals("exit")) {
                console.showData("Goodbye");
                break;
            } else {
                console.showData("unknown instruction, try more");
            }
        }
    }

    private void getTableContent(String command) {
        String[] params = prepareParams(command, 2);
        String tableName = params[1];
        String[] columnsNames = dbManager.getColumnsNames(tableName);
        RowData[] rowDatas = dbManager.selectAllFromTable(tableName);
        String header = "|";
        for (String colName : columnsNames) {
            header += colName + "\t|";
        }
        console.showData(header);
        for (RowData row : rowDatas) {
            String str ="|";
            for (Object val:row.getValues()) {
                str += val + "\t|";
            }
            console.showData(str);
        }
    }

    private void getTablesList() {
        console.showData(Arrays.toString(dbManager.getTablesList()));
    }

    private void showCommandList() {
        console.showData("Commands full list :");
        String message = "\t help\n" +
                "\t\t to display commands list \n" +

                "\t list \n" +
                "\t\t to display tables list in DB\n" +

                "\t find|tableName \n" +
                "\t\t to display data from table 'tableName' \n" +

                "\t exit\n" +
                "\t\t to leave console\n";
        console.showData(message);
    }
}
