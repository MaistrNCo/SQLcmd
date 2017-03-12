package ua.com.juja.sqlcmd.View;

import java.util.Scanner;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Console implements View{
    @Override
    public void showData(String message) {
        System.out.println(message);
    }

    @Override
    public String getData() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
