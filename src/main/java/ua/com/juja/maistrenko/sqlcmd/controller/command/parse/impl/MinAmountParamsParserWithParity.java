package ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl;

import java.util.List;

public class MinAmountParamsParserWithParity extends ExactAmountParamsParser {
    @Override
    public boolean checkParamsAmount(List<String> paramsList, String pattern) {
        return paramsList.size() >= parseInputString(pattern).size() &&
                (paramsList.size() % 2 == 0);
    }
}
