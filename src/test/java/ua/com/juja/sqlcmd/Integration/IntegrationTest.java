package ua.com.juja.sqlcmd.Integration;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.sqlcmd.Controller.Main;
import ua.com.juja.sqlcmd.Model.DBManager;
import ua.com.juja.sqlcmd.Model.PostgresDBManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;


/**
 * Created by maistrenko on 28.03.2017.
 */

public class IntegrationTest {

    private static ConfigurableInputStream in;
    private static ByteArrayOutputStream out;
    private DBManager dbmanager;

    @Before
    public void setup(){
        dbmanager = new PostgresDBManager();
        in = new ConfigurableInputStream();
        out = new ByteArrayOutputStream();
        System.setIn(in);
        System.setOut(new PrintStream(out));

    }

    @Test
    public void testExit(){
        in.add("exit");
        Main.main(new String[0]);

        assertEquals(" Hi, program started  \r\n" +
                "input command please or 'help' to see commands list\r\n" +
                "Goodbye, to see soon. \r\n",getData());
    }

    public String getData() {
        try{
            String result = new String(out.toByteArray(),"UTF-8");
            return result;
        }catch(UnsupportedEncodingException e){
            return e.getMessage();
        }
    }
}
