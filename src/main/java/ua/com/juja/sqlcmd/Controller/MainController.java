package ua.com.juja.sqlcmd.Controller;

import ua.com.juja.sqlcmd.Controller.command.Command;
import ua.com.juja.sqlcmd.Controller.command.Exit;
import ua.com.juja.sqlcmd.Model.ConnectionSettings;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.PostgresDBManager;
import ua.com.juja.sqlcmd.Model.RowData;
import ua.com.juja.sqlcmd.View.View;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by maistrenko on 12.03.17.
 */
public class MainController {

    public static final int CONNECTION_PARAMS_NUMBER = 5;
    private DBManager dbManager;
    private View console;
    private Command[] commands;

    public MainController(DBManager dbManager, View console) {
        this.dbManager = dbManager;
        this.console = console;
        this.commands = new Command[] {new Exit(console)};
    }

    public void run() {


        ConnectDB();
        console.printOut("input command please or 'help' to see commands list");
        while (true) {
            String input = console.getInput();
            if (input.equals("help")) {
                showCommandList();
            } else if (input.equals("list")) {
                getTablesList();
            } else if (input.startsWith("find")) {
                getTableContent(input);
            } else if (input.startsWith("clear")) {
                doClear(input);
            } else if (input.startsWith("drop")) {
                doDrop(input);
            } else if (input.startsWith("create")) {
                doCreate(input);
            } else if (input.startsWith("insert")) {
                doInsert(input);
            } else if (input.startsWith("update")) {
                doUpdate(input);
            } else if (input.startsWith("delete")) {
                doDelete(input);
            } else if (commands[0].canProcess(input)) {
                commands[0].process(input);
            } else {
                console.printOut("unknown instruction, try more");
            }
        }
    }

    private void ConnectDB() {
        dbManager = new PostgresDBManager();
        console.printOut("Hello !");
        boolean fileNotFound = false;
        while (true) {
            ConnectionSettings conSet = new ConnectionSettings();
            if(!fileNotFound){
                try {
                    String[] fromFile = dbManager.loadFromIni("Postgres.ini");
                    if (fromFile.length== CONNECTION_PARAMS_NUMBER){
                        conSet.setSettings(fromFile);
                    }
                }catch (FileNotFoundException e){
                    fileNotFound = true;
                }
            }else{
                console.printOut("Input data please in format 'database|username|password' :");
                String data = console.getInput();
                String[] params = prepareParams(data, 3);
                String[] defParams = {"192.168.1.11","5423",params[1],params[2],params[3]};
                conSet.setSettings(defParams);
            }

            try {
                dbManager.connect(conSet);
                console.printOut("Successful connection!!");
                break;
            } catch (Exception e) {
                showErrorMesage(e);
                fileNotFound = true;
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
        String[] crtParams = prepareParams(command,3);
        String[] columnNames = Arrays.copyOfRange(crtParams,2,crtParams.length);
        dbManager.create(crtParams[1],columnNames);
    }

    private void doDrop(String command) {
        String tableName = prepareParams(command,2)[1];
        dbManager.drop(tableName);
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
        console.printOut(header);
        for (RowData row : rowDatas) {
            String str ="|";
            for (Object val:row.getValues()) {
                str += val + "\t|";
            }
            console.printOut(str);
        }
    }

    private void getTablesList() {
        console.printOut(Arrays.toString(dbManager.getTablesList()));
    }

    private void showCommandList() {
        console.printOut("Commands full list :");
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
        console.printOut(message);
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
        console.printOut("Unsuccessful operation by reason: " + errorReason);
        console.printOut("try again please");
    }
}
