package ua.com.juja.sqlcmd.controller.command;

import ua.com.juja.sqlcmd.view.View;

/**
 * Created by maistrenko on 18.03.17.
 */
public class Help implements Command {
    private final View view;

    public Help(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String userInput) {
        return userInput.equals("help");
    }

    @Override
    public void process(String userInput) {
        view.printOut("Commands full list :");
        String message = "\t help\n" +
                "\t\t to display commands list \n" +

                "\t connect \n" +
                "\t\t to connect DB with saved parameters (Postgres.ini)\n" +

                "\t connect|dbName|userName|password \n" +
                "\t\t to connect DB with your parameters\n" +

                "\t list \n" +
                "\t\t to display tables list in DB\n" +

                "\t find|tableName \n" +
                "\t\t to display data from table 'tableName' \n" +

                "\t clear|tableName \n" +
                "\t\t to delete all data from table 'tableName' \n" +

                "\t drop|tableName \n" +
                "\t\t to delete table 'tableName' \n" +

                "\t create|tableName|column1|column2|...|columnN \n" +
                "\t\t to create table 'tableName' with defined columns\n" +

                "\t insert|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t\t to insert data to table 'tableName' \n" +

                "\t update|tableName|column1|value1|column2|value2|...|columnN|valueN \n" +
                "\t\t to update records in table 'tableName' which value in 'column1' equal 'value1'\n" +

                "\t delete|tableName|column|value \n" +
                "\t\t to delete records in table 'tableName' which value in 'column' equal 'value'\n" +

                "\t exit\n" +
                "\t\t to leave console\n";
        view.printOut(message);
    }
}
