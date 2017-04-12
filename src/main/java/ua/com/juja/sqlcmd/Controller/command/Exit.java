package ua.com.juja.sqlcmd.Controller.command;

import ua.com.juja.sqlcmd.View.View;

/**
 * Created by maistrenko on 16.03.17.
 */
public class Exit implements Command {

    private View view;

    public Exit(View console) {
        this.view = console;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("exit");
    }

    @Override
    public void process(String userInput) {
        view.printOut("Goodbye, to see soon. ");
        throw new NormalExitException();
    }
}
