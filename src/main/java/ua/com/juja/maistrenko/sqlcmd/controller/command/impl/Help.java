package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.MainController;
import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Set;

public class Help implements Command {
    private static final String DESCRIPTION = "help - to display commands list and usage hints";
    private final View view;
    private final MainController mainController;

    public Help(View view, MainController mainController) {
        this.view = view;
        this.mainController = mainController;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("help");
    }

    @Override
    public void process(String userInput) {
        view.write("Commands full list :");

        Set<Command> commands = mainController.getCommands();
        for (Command command : commands) {
            view.writeCommandDescription(command.getDescription());
        }
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
