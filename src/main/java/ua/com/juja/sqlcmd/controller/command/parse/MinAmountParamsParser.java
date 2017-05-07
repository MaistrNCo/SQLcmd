package ua.com.juja.sqlcmd.controller.command.parse;

import java.util.List;
public class MinAmountParamsParser extends ExactAmountParamsParser{
    @Override
    public boolean checkParamsAmount(List<String> paramsList, String pattern) {
        return paramsList.size() >= parseInputString(pattern).size();
    }
}
