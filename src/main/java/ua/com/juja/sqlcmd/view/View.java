package ua.com.juja.sqlcmd.view;

/**
 * Created by maistrenko on 12.03.17.
 */
public interface View {
    void write(String message);

    String read();
}
