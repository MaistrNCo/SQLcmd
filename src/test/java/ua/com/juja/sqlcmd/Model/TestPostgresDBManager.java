package ua.com.juja.sqlcmd.Model;

import ua.com.juja.sqlcmd.Controller.command.Command;

import java.io.FileNotFoundException;

/**
 * Created by maistrenko on 07.03.17.
 */
public class TestPostgresDBManager extends TestDBManager {

    @Override
    public void setup() {
        dbManager = new PostgresDBManager();
        boolean notFound = false;
        ConnectionSettings conSet = new ConnectionSettings();
//        if(!notFound) {
//            try {
//                String[] fromFile = dbManager.loadFromIni("Postgres.ini");
//                if (fromFile.length == 5) {
//                    conSet.setSettings(fromFile);
//                }
//            } catch (FileNotFoundException e) {
//                notFound = true;
//            }
//        }else {
            String[] defParams = {"192.168.1.11", "5432", "sqlcmd", "postgres", "postgres"};
            conSet.setSettings(defParams);
//        }
        dbManager.connect(conSet);

    }
}
