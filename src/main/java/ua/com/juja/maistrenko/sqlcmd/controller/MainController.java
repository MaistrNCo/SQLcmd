package ua.com.juja.maistrenko.sqlcmd.controller;

import ua.com.juja.maistrenko.sqlcmd.controller.command.*;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.*;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.LinkedHashSet;
import java.util.Set;

public class MainController {
    private final View view;
    private final Set<Command> commands;

    public MainController(DBManager dbManager, View view) {
        this.view = view;
        this.commands = new LinkedHashSet<>();
        this.commands.add(new Help(view, this));
        this.commands.add(new Exit(dbManager, view));
        this.commands.add(new ConnectDef(dbManager, view));
        this.commands.add(new Connect(dbManager, view));
        this.commands.add(new NotConnected(dbManager, view));
        this.commands.add(new TablesList(dbManager, view));
        this.commands.add(new Find(dbManager, view));
        this.commands.add(new ClearTable(dbManager, view));
        this.commands.add(new Drop(dbManager, view));
        this.commands.add(new Create(dbManager, view));
        this.commands.add(new Insert(dbManager, view));
        this.commands.add(new Update(dbManager, view));
        this.commands.add(new Delete(dbManager, view));
        this.commands.add(new WrongInput(view));

    }

    public void run() {
        boolean exitCalled = false;
        view.write("Hi, program started !");
        view.write("Input command please or 'help' to see commands list");

        while (!exitCalled) {
            String userInput = view.read().trim();
            for (Command command : commands) {
                try {
                    if (command.canProcess(userInput)) {
                        command.process(userInput);
                        break;
                    }
                } catch (NormalExitException e) {
                    exitCalled = true;
                    break;
                } catch (Exception e) {
                    view.showExceptionErrorMessage(e);
                    break;
                }
            }
        }
    }

    public Set<Command> getCommands() {
        return commands;
    }

}
