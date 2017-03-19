package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.Model.ConnectionSettings;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.View.View;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by maistrenko on 19.03.17.
 */
public class ConnectDef implements  Command {
    private final View view;
    private final DBManager dbManager;
    public ConnectDef(DBManager dbManager, View view) {
        this.view = view;
        this.dbManager = dbManager;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("connect");
    }

    @Override
    public void process(String userInput) {
        ConnectionSettings conSet = new ConnectionSettings();
        try {
            String[] fromFile = loadFromIni("Postgres.ini");
            if (fromFile.length== 5){
                conSet.setSettings(fromFile);
                dbManager.connect(conSet);
                view.printOut("Successful connection!!");
            }
        }catch (FileNotFoundException e){
            view.showErrorMessage(e);
        }catch (Exception e) {
            view.showErrorMessage(e);

        }
    }

    public String[] loadFromIni(String fileName) throws FileNotFoundException {
        String[] result = new String[5];
        int caught  = 0;
        try{
            FileReader file = new FileReader(fileName);
            BufferedReader br = new BufferedReader(file);
            String curStr;
            System.out.println("found file " + fileName);
            while((curStr = br.readLine())!=null){
                String[] splited  = curStr.split(":");
                System.out.println(Arrays.toString(splited));
                switch(splited[0]){
                    case "server":{
                        result[0] = splited[1];
                        caught++;
                        break;
                    }
                    case "port":{
                        result[1] = splited[1];
                        caught++;
                        break;
                    }
                    case "base":{
                        result[2] = splited[1];
                        caught++;
                        break;
                    }
                    case "username":{
                        result[3] = splited[1];
                        caught++;
                        break;
                    }
                    case "password":{
                        result[4] = splited[1];
                        caught++;
                        break;
                    }
                }
            }
        }catch(FileNotFoundException e){
            //throw new RuntimeException("file Postgres.ini not found ",e);
            view.showErrorMessage(e);
        } catch (IOException e) {
            view.showErrorMessage(e);
        }
        if (caught==5) return result;
        else return new String[0];
    }
}
