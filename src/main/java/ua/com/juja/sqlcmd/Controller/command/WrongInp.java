package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.View.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class WrongInp implements Command {
    private final View view;

    public WrongInp(View view) {
        this.view =view;
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
