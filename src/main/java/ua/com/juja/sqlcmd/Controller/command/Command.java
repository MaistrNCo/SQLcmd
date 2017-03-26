package ua.com.juja.sqlcmd.Controller.command;

import java.io.IOException;

/**
 * Created by maistrenko on 16.03.17.
 */
public interface Command {
    boolean canProcess(String userInput);
    void process(String userInput) throws IOException;

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
