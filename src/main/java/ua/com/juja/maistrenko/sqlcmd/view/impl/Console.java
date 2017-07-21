package ua.com.juja.maistrenko.sqlcmd.view.impl;

import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.List;
import java.util.Scanner;

import static java.lang.String.format;

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
        if (description == null) {
            return;
        }
        String[] message = description.split("-");
        System.out.println("  " + message[0]);
        System.out.println("\t\t" + message[1]);
        System.out.println();
    }

    @Override
    public void writeWrongParamsMsg(String pattern, String userInput) {
        System.out.println("Wrong parameters amount. Must be '" + pattern + "' But was: '" + userInput + "'");
    }

    @Override
    public void printTable(List<RowData> data, String... columns) {
        int[] lengths = findLengths(data, columns);
        StringBuilder printedStr = new StringBuilder("| ");
        StringBuilder rowDivider = new StringBuilder("+");

        for (int i = 0; i < columns.length; i++) {
            printedStr.append(format("%-" + lengths[i] + "s | ", columns[i]));
            for (int j = 0; j < lengths[i]; j++) {
                rowDivider.append("-");
            }
            rowDivider.append("--+");
        }
        write(rowDivider.substring(0));
        write(printedStr.substring(0));
        write(rowDivider.substring(0));
        printedStr.delete(2, printedStr.length());
        for (RowData row : data) {
            for (int i = 0; i < columns.length; i++) {
                printedStr.append(format("%-" + lengths[i] + "s | ", row.get(columns[i])));
            }
            write(printedStr.substring(0));
            printedStr.delete(2, printedStr.length());
        }
        write(rowDivider.substring(0));
    }

    private int[] findLengths(List<RowData> data, String... columns) {
        int[] lengths = new int[columns.length];

        for (int ind = 0; ind < columns.length; ind++) {
            lengths[ind] = columns[ind].length();
        }

        for (RowData row : data) {
            for (int ind = 0; ind < columns.length; ind++) {
                if (row.get(columns[ind]).toString().length() > lengths[ind]) {
                    lengths[ind] = row.get(columns[ind]).toString().length();
                }
            }
        }
        return lengths;
    }

    @Override
    public void showExceptionErrorMessage(Exception e) {
        String errorReason = e.getMessage();
        if (e.getCause() != null) errorReason += "  " + e.getCause().getMessage();
        write("Unsuccessful operation by reason: " + errorReason);
        write("try again please or use 'commandName|help' to see command description");
    }
}
