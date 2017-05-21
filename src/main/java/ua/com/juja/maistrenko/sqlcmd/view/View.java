package ua.com.juja.maistrenko.sqlcmd.view;

import ua.com.juja.maistrenko.sqlcmd.model.RowData;

import java.util.List;

public interface View {
    String read();

    void write(String message);

    void writeCommandDescription(String description);

    void writeWrongParamsMsg(String pattern, String userInput);

    void printTable(List<RowData> data, String... columns);

    void showExceptionErrorMessage(Exception e);
}
