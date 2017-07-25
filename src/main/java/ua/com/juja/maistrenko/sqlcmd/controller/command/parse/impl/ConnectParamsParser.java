package ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl;

import java.util.List;

public class ConnectParamsParser extends ExactAmountParamsParser {

    private static final int INDEX_FIRST_PARAM = 0;
    private static final int INDEX_SERVER_NAME = 1;
    private static final int INDEX_SERVER_PORT = 2;
    private static final int PARAMS_AMOUNT_WITHOUT_SERVER = 4;

    @Override
    public boolean checkParamsAmount(List<String> paramsList, String pattern) {
        if (super.checkParamsAmount(paramsList, pattern)) {
            return true;
        } else {
            //else pattern reduce by one parameter
            return super.checkParamsAmount(paramsList, pattern.substring(1));
        }

    }

    @Override
    public List<String> parseInputString(String userInput) {
        List<String> result = super.parseInputString(userInput);
        if (result.size() == PARAMS_AMOUNT_WITHOUT_SERVER) {
            result.set(INDEX_FIRST_PARAM, "");           //port
            result.add(INDEX_FIRST_PARAM, "");  //srv address
            return result;
        }
        String[] address = result.get(INDEX_SERVER_NAME).split(":");
        if (address.length > 1) {
            result.set(INDEX_SERVER_NAME, address[0].trim());
            result.add(INDEX_SERVER_PORT, address[1].trim());
        } else {
            result.add(INDEX_SERVER_PORT, "");
        }
        return result.subList(INDEX_SERVER_NAME, result.size());
    }
}
