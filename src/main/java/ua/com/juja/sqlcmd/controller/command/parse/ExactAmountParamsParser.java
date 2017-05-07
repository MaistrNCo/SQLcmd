package ua.com.juja.sqlcmd.controller.command.parse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maistrenko on 07.05.17.
 */
public class ExactAmountParamsParser implements Parser{

    public static final int INDEX_HELP_PARAM_WAITED = 1;

    @Override
    public List<String> parseInputString(String userInput) {
        String[] resultArr = userInput.split("\\|");
        List <String> result = new ArrayList<>();
        for (String current : resultArr) {
            result.add(current.trim());
        }
        return result;
    }

    @Override
    public boolean isHelpNeeded(List<String> paramsList) {
        return paramsList.get(INDEX_HELP_PARAM_WAITED).equalsIgnoreCase("help");
    }

    @Override
    public boolean checkParamsAmount(List<String> paramsList, String pattern) {

        return paramsList.size() == parseInputString(pattern).size();
    }
}
