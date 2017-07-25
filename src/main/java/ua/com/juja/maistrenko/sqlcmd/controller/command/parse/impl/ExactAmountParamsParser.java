package ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl;

import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;

import java.util.ArrayList;
import java.util.List;

public class ExactAmountParamsParser implements Parser {

    private static final int INDEX_HELP_PARAM_WAITED = 1;

    @Override
    public List<String> parseInputString(String userInput) {
        String[] resultArr = userInput.split("\\|");
        List<String> result = new ArrayList<>();
        for (String current : resultArr) {
            result.add(current.trim());
        }
        return result;
    }

    @Override
    public boolean isHelpNeeded(List<String> paramsList) {
        if (paramsList.size() > INDEX_HELP_PARAM_WAITED) {
            return paramsList.get(INDEX_HELP_PARAM_WAITED).equalsIgnoreCase("help");
        } else {
            return false;
        }
    }

    @Override
    public boolean isHelpNeeded(String userInput) {
        return userInput.split("\\|")[INDEX_HELP_PARAM_WAITED].equals("help");
    }

    @Override
    public boolean checkParamsAmount(List<String> paramsList, String pattern) {

        return paramsList.size() == parseInputString(pattern).size();
    }

    @Override
    public RowData convertToRowData(List<String> params, int beginIndex, int endIndex) {
        if ((endIndex - beginIndex) % 2 != 0) {
            return null;
        }
        RowData rowData = new RowData();
        for (int i = beginIndex; i < endIndex; i++) {
            rowData.put(params.get(i), params.get(++i));
        }
        return rowData;
    }
}
