package ua.com.juja.maistrenko.sqlcmd.controller.command.parse;

import java.util.List;

public class ConnectParamsParser extends MinAmountParamsParser{

    private static final int INDEX_SERVER_NAME = 1;

    @Override
    public List<String> parseInputString(String userInput) {
        List <String> result = super.parseInputString(userInput);
        String[] address = result.get(INDEX_SERVER_NAME).split(":");
        if (address.length>1) {
            result.set(INDEX_SERVER_NAME,address[0].trim());
            result.add(INDEX_SERVER_NAME+1,address[1].trim());
        } else {
            result.add(INDEX_SERVER_NAME+1,"");
        }
        return result.subList(INDEX_SERVER_NAME,result.size());
    }
}
