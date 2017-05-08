package ua.com.juja.maistrenko.sqlcmd.controller.command.parse;

import java.util.List;

public interface Parser {
    List<String> parseInputString(String userInput);
    boolean isHelpNeeded(List <String> paramsList);
    boolean checkParamsAmount(List <String> paramsList,String pattern);
}
