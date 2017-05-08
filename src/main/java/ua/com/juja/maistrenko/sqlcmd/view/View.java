package ua.com.juja.maistrenko.sqlcmd.view;

/**
 * Created by maistrenko on 12.03.17.
 */
public interface View {
    void write(String message);

    String read();
}
