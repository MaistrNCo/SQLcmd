package ua.com.juja.sqlcmd.Controller;

import ua.com.juja.sqlcmd.Controller.command.*;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.View.Console;

/**
 * Created by maistrenko on 12.03.17.
 */
public class MainController {
    private Console view;
    private Command[] commands;

    public MainController(DBManager dbManager, Console view) {
        this.view = view;
        this.commands = new Command[] {
                    new Help(view),
                    new Exit(view),
                    new ConnectDef(dbManager,view),
                    new Connect(dbManager,view),
                    new NotConnected(dbManager,view),
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
        view.printOut(" Hi, program started  ");
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






}
