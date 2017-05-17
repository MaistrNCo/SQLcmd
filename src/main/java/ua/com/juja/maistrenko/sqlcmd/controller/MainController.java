package ua.com.juja.maistrenko.sqlcmd.controller;

import ua.com.juja.maistrenko.sqlcmd.controller.command.*;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class MainController {
    private View view;
    private Command[] commands;

    public MainController(DBManager dbManager, View view) {
        this.view = view;
        this.commands = new Command[]{
                new Help(view),
                new Exit(dbManager,view),
                new ConnectDef(dbManager, view),
                new Connect(dbManager, view),
                new NotConnected(dbManager, view),
                new TablesList(dbManager, view),
                new Find(dbManager, view),
                new ClearTable(dbManager, view),
                new Drop(dbManager, view),
                new Create(dbManager, view),
                new Insert(dbManager, view),
                new Update(dbManager, view),
                new Delete(dbManager, view),
                new WrongInput(view)
        };
    }

    public void run() {
        try {
            runCommands();
        } catch (NormalExitException e) {

        }
    }

    private void runCommands() {
        view.write("Hi, program started  ");
        while (true) {
            view.write("input command please or 'help' to see commands list");
            String input = view.read();
            for (Command command : commands) {
                try {
                    if (command.canProcess(input)) {
                        command.process(input);
                        break;
                    }
                } catch (NormalExitException e) {
                    throw e;
                    //System.exit(0);
                } catch (Exception e) {
                    showErrorMessage(e);
                    break;
                }
            }
        }
    }

    private void showErrorMessage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        view.write("Unsuccessful operation by reason: " + errorReason);
        view.write("try again please");
    }


}
