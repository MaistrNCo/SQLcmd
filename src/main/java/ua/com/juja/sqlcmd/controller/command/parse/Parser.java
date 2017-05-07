package ua.com.juja.sqlcmd.controller.command.parse;

import java.util.List;

public interface Parser {
    List<String> parseInputString(String userInput);
    boolean isHelpNeeded(List <String> paramsList);
    boolean checkParamsAmount(List <String> paramsList,int waitedAmount);
}
