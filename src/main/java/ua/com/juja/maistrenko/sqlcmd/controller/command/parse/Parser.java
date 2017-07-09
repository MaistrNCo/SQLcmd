package ua.com.juja.maistrenko.sqlcmd.controller.command.parse;

import ua.com.juja.maistrenko.sqlcmd.model.RowData;

import java.util.List;

public interface Parser {
    List<String> parseInputString(String userInput);

    boolean isHelpNeeded(List<String> paramsList);

    boolean isHelpNeeded(String userInput);

    boolean checkParamsAmount(List<String> paramsList, String pattern);

    RowData convertToRowData(List<String> params, int beginIndex, int endIndex);
}
