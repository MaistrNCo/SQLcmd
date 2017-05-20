package ua.com.juja.maistrenko.sqlcmd.view;

public interface View {
    String read();

    void write(String message);

    void writeCommandDescription(String description);

    void writeWrongParamsMsg(String pattern, String userInput);

    void showExceptionErrorMessage(Exception e);
}
