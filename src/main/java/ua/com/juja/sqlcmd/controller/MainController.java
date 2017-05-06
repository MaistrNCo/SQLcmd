package ua.com.juja.sqlcmd.controller;

import ua.com.juja.sqlcmd.controller.command.*;
import ua.com.juja.sqlcmd.model.DBManager;
import ua.com.juja.sqlcmd.view.View;

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
                new List(dbManager, view),
                new Find(dbManager, view),
                new Clear(dbManager, view),
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
        view.printOut("Hi, program started  ");
        while (true) {
            view.printOut("input command please or 'help' to see commands list");
            String input = view.getInput();
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

    public void showErrorMessage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        view.printOut("Unsuccessful operation by reason: " + errorReason);
        view.printOut("try again please");
    }


}
