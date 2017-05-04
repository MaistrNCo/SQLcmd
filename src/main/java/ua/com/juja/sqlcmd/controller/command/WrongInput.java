package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
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
        view.printOut("unknown instruction, try more");
    }
}
