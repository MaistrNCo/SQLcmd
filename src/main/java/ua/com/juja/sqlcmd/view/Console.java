package ua.com.juja.sqlcmd.view;

import java.util.Scanner;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Console implements View {
    @Override
    public void printOut(String message) {
        System.out.println(message);
    }

    @Override
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}