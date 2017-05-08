package ua.com.juja.maistrenko.sqlcmd.controller.command;

/**
 * Created by maistrenko on 16.03.17.
 */
public interface Command {
    boolean canProcess(String userInput);

    void process(String userInput);

    default String[] prepareParams(String data, int expected) {
        String[] params = data.split("\\|");
        if (params.length < expected) {
            throw new IllegalArgumentException("Wrong number of parameters, expected minimum is : "
                    + expected
                    + ", actual is "
                    + params.length);
        }
        return params;
    }
}
