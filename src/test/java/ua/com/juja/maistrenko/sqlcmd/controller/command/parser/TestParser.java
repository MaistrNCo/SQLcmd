package ua.com.juja.maistrenko.sqlcmd.controller.command.parser;

import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.ExactAmountParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.Parser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.ConnectParamsParser;
import ua.com.juja.maistrenko.sqlcmd.controller.command.parse.MinAmountParamsParser;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class TestParser {
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
    public void testParserConnectHelp(){
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  help ");
        assertTrue(parser.isHelpNeeded(list));

        list = parser.parseInputString("connect|  HELP ");
        assertTrue(parser.isHelpNeeded(list));
    }

    @Test
    public void testParserCheckAmount(){
        Parser parser = new ExactAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  help ");
        assertTrue(parser.checkParamsAmount(list,"connect| param1"));
        assertFalse(parser.checkParamsAmount(list,"connect|param1|param2 "));
    }

    @Test
    public void testParserCheckMinAmount(){
        Parser parser = new MinAmountParamsParser();
        List<String> list = parser.parseInputString("connect|  localhost|sqlcmd|postgres | postgres ");
        assertTrue(parser.checkParamsAmount(list,"connect|param1|param2|param3 "));
        assertTrue(parser.checkParamsAmount(list,"connect|param1|param2|param3| param4 "));
        assertFalse(parser.checkParamsAmount(list,"connect|  param1|param2|param3 |param4| param5 "));
    }
}
