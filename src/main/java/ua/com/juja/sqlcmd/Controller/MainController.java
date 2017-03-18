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
                    new Help(view),
                    new Exit(view),
                    new List(dbManager,view),
                    new Find(dbManager,view),
                    new Clear(dbManager,view),
                    new Drop(dbManager,view),
                    new Create(dbManager,view),
                    new Insert(dbManager,view),
                    new Update(dbManager,view),
                    new Delete(dbManager,view),
                    new WrongInput(view)
                    };
    }

    public void run() {


        ConnectDB();   //TODO move connection to commands list
        while (true) {
            view.printOut("input command please or 'help' to see commands list");
            String input = view.getInput();

            for(Command command:commands) {
                if(command.canProcess(input)) {
                    command.process(input);
                    break;
                }
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
