package ua.com.juja.sqlcmd.View;

import java.util.Scanner;

/**
 * Created by maistrenko on 12.03.17.
 */
public class Console implements View{
    @Override
    public void printOut(String message) {
        System.out.println(message);
    }

    @Override
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void showErrorMessage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        printOut("Unsuccessful operation by reason: " + errorReason);
        printOut("try again please");
    }
}
