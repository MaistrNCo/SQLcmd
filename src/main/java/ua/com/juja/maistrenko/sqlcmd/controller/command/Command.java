package ua.com.juja.maistrenko.sqlcmd.controller.command;

public interface Command {

    boolean canProcess(String userInput);

    void process(String userInput);

    String getDescription();

}
