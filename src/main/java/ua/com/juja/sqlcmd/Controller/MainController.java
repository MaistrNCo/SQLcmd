package ua.com.juja.sqlcmd.Controller;

import ua.com.juja.sqlcmd.Controller.command.*;
import ua.com.juja.sqlcmd.Model.ConnectionSettings;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.PostgresDBManager;
import ua.com.juja.sqlcmd.Model.RowData;
import ua.com.juja.sqlcmd.View.Console;

import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Created by maistrenko on 12.03.17.
 */
public class MainController {

    public static final int CONNECTION_PARAMS_NUMBER = 5;
    private DBManager dbManager;
    private Console view;
    private Command[] commands;

    public MainController(DBManager dbManager, Console view) {
        this.dbManager = dbManager;
        this.view = view;
        this.commands = new Command[] {
                    new Exit(view),
                    new WrongInput(view),
                    new Help(view),
                    new List(dbManager,view)
                    };
    }

    public void run() {


        ConnectDB();
        view.printOut("input command please or 'help' to see commands list");
        while (true) {
            String input = view.getInput();
            if (commands[2].canProcess(input)) {
                commands[2].process(input);
            } else if (commands[3].canProcess(input)) {
                commands[3].process(input);
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
            } else if(commands[1].canProcess(input)) {
                commands[1].process(input);
            }
        }
    }

    private void ConnectDB() {
        //dbManager = new PostgresDBManager();
        view.printOut("Hello !");
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
                view.printOut("Input data please in format 'database|username|password' :");
                String data = view.getInput();
                String[] params = prepareParams(data, 3);
                String[] defParams = {"192.168.1.11","5423",params[1],params[2],params[3]};
                conSet.setSettings(defParams);
            }

            try {
                dbManager.connect(conSet);
                view.printOut("Successful connection!!");
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
        view.printOut(header);
        for (RowData row : rowDatas) {
            String str ="|";
            for (Object val:row.getValues()) {
                str += val + "\t|";
            }
            view.printOut(str);
        }
    }

    private void getTablesList() {

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
        view.printOut("Unsuccessful operation by reason: " + errorReason);
        view.printOut("try again please");
    }
}
