package ua.com.juja.sqlcmd.Controller.command;

/**
 * Created by maistrenko on 16.03.17.
 */
public interface Command {
    boolean canProcess(String userInput);
    void process(String userInput);
}
