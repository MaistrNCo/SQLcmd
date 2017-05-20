package ua.com.juja.maistrenko.sqlcmd.view;

import java.util.Scanner;

public class Console implements View {
    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void writeCommandDescription(String description) {
        if (description == null) { return; }
        String[] message = description.split("-");
        System.out.println("  " + message[0] );
        System.out.println("\t\t" + message[1] );
        System.out.println();
    }

    @Override
    public void writeWrongParamsMsg(String pattern, String userInput) {
        System.out.println("Wrong parameters amount. Must be '" + pattern + "' But was: '" + userInput + "'");
    }

    @Override
    public void showExceptionErrorMessage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        write("Unsuccessful operation by reason: " + errorReason);
        write("try again please");
    }
}
