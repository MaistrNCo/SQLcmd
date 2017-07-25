package ua.com.juja.maistrenko.sqlcmd.controller.command.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.view.View;

public class WrongInput implements Command {
    private final View view;

    public WrongInput(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return true;
    }

    @Override
    public void process(String userInput) {
        view.write("unknown command, try again please or use 'help' to see available commands");
    }

    @Override
    public String getDescription() {
        return null;
    }
}
