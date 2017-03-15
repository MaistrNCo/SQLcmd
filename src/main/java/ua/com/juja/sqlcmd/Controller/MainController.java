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
                doClear(command);
            } else if (command.startsWith("drop")) {
                doDrop(command);
            } else if (command.startsWith("create")) {
                doCreate(command);
            } else if (command.startsWith("insert")) {
                doInsert(command);
            } else if (command.startsWith("update")) {
                doUpdate(command);
            } else if (command.startsWith("update")) {
                doDelete(command);
            } else if (command.equals("exit")) {
                console.showData("Goodbye");
                break;
            } else {
                console.showData("unknown instruction, try more");
            }
        }
    }

    private void doDelete(String command) {
        String[] delParams = prepareParams(command,4);
        dbManager.delete(delParams[1],delParams[2],delParams[3]);
    }


    private void doUpdate(String command) {
        String[] updParams = prepareParams(command,6);
        RowData insData = new RowData((updParams.length-4)/2);
        for (int ind = 0; ind < (updParams.length-4)/2; ind+=2) {
            insData.addColumnValue(updParams[ind+4],updParams[ind+5]);
        }
        dbManager.update(updParams[1],updParams[2],updParams[3],insData);
    }

    private void doInsert(String command) {
        String[] insertParams = prepareParams(command,4);
        RowData insertData = new RowData((insertParams.length-2)/2);
        for (int ind = 0; ind < (insertParams.length-2)/2; ind+=2) {
            insertData.addColumnValue(insertParams[ind+2],insertParams[ind+3]);
        }
        dbManager.insert(insertParams[1],insertData);
    }

    private void doCreate(String command) {
        String[] createParams = prepareParams(command,3);
        //TODO implement table creation
    }

    private void doDrop(String command) {
        String tableName = prepareParams(command,2)[1];
        //TODO implement drop
    }

    private void doClear(String command) {
        String tableName = prepareParams(command,2)[1];
        dbManager.clear(tableName);
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

                "\t clear|tableName \n" +
                "\t\t to delete all data from table 'tableName' \n" +

                "\t drop|tableName \n" +
                "\t\t to delete table 'tableName' \n" +

                "\t create|tableName|column1|column2|...|columnN \n" +
                "\t\t to create table 'tableName' with defined columns\n" +

                "\t insert|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t\t to insert data to table 'tableName' \n" +

                "\t update|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t\t to update records in table 'tableName' which value in 'column1' equal 'value1'\n" +

                "\t delete|tableName|column|value \n" +
                "\t\t to delete records in table 'tableName' which value in 'column' equal 'value'\n" +

                "\t exit\n" +
                "\t\t to leave console\n";
        console.showData(message);
    }

    private String[] prepareParams(String data, int expected) {
        String[] params = data.split("\\|");
        if (params.length < expected) {
            throw new IllegalArgumentException("Wrong number of parameters, expected minimum is : "
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
}
