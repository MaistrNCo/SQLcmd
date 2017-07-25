package ua.com.juja.maistrenko.sqlcmd.controller.command.parser;

import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.ConnectParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.impl.MinAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class ParserTest {
    @Test
    public void testParser() {
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  localhost|sqlcmd|postgres | postgres ");
        assertEquals("connect", list.get(0));
        assertEquals("localhost", list.get(1));
        assertEquals("sqlcmd", list.get(2));
        assertEquals("postgres", list.get(3));
        assertEquals("postgres", list.get(4));
    }

    @Test
    public void testConnectionParser() {
        Parser parser = new ConnectParamsParser();
        List<String> list = parser.parseInputString("connect|  localhost|sqlcmd|postgres | postgres ");

        assertEquals("localhost", list.get(0));
        assertEquals("", list.get(1));
        assertEquals("sqlcmd", list.get(2));
        assertEquals("postgres", list.get(3));
        assertEquals("postgres", list.get(4));

        list = parser.parseInputString("connect|  localhost : 5432| sqlcmd|postgres |postgres ");

        assertEquals("localhost", list.get(0));
        assertEquals("5432", list.get(1));
        assertEquals("sqlcmd", list.get(2));
        assertEquals("postgres", list.get(3));
        assertEquals("postgres", list.get(4));
    }

    @Test
    public void testParserConnectHelp() {
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  help ");
        assertTrue(parser.isHelpNeeded(list));

        list = parser.parseInputString("connect|  HELP ");
        assertTrue(parser.isHelpNeeded(list));
    }

    @Test
    public void testParserCheckAmount() {
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  help ");
        assertTrue(parser.checkParamsAmount(list, "connect| param1"));
        assertFalse(parser.checkParamsAmount(list, "connect|param1|param2 "));
    }

    @Test
    public void testParserCheckMinAmount() {
        Parser parser = new MinAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  localhost|sqlcmd|postgres | postgres ");
        assertTrue(parser.checkParamsAmount(list, "connect|param1|param2|param3 "));
        assertTrue(parser.checkParamsAmount(list, "connect|param1|param2|param3| param4 "));
        assertFalse(parser.checkParamsAmount(list, "connect|  param1|param2|param3 |param4| param5 "));
    }

    @Test
    public void testParserConvertToRowDataWrong() {
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("name|Mary|id|0001 | password ");
        assertEquals(null,parser.convertToRowData(list,0,list.size()));
    }

    @Test
    public void testParserConvertToRowData() {
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("name|Mary|id|0001 |password|ASbn06! ");
        RowData testRow = new RowData();
        testRow.put("name","Mary");
        testRow.put("id","0001");
        testRow.put("password","ASbn06!");
        assertEquals(testRow.toString(),parser.convertToRowData(list,0,list.size()).toString());
    }
}
