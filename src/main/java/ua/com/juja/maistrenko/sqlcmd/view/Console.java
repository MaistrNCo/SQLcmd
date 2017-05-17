package ua.com.juja.maistrenko.sqlcmd.view;

import java.util.Scanner;

public class Console implements View {
    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void writeWrongParamsMsg(String pattern, String userInput) {
        System.out.println("Wrong parameters amount. Must be '"+pattern+"' But was: '"+userInput+"'");
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
