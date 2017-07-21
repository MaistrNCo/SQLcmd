package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Insert;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InsertTest {
    private View view;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        DBManager dbManager = mock(DBManager.class);
        command = new Insert(dbManager, view);
    }

    @Test
    public void insertCanProcessTrue() {
        assertTrue(command.canProcess("insert|werwert"));
    }

    @Test
    public void insertCanProcessFalse() {
        assertFalse(command.canProcess("insert"));
    }

    @Test
    public void insertProcessHelp() {
        try {
            command.process("insert|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("insert|tableName|column1|value1|column2|value2|...|columnN|valueN " +
                "- to add new data row in table 'tableName'");
    }

    @Test
    public void insertProcessWrongParamsAmount() {
        try {
            command.process("insert|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("insert|tableName|column1|value1","insert|tableName");
    }

    @Test
    public void insertProcessSuccessful() {
        try {
            command.process("insert|tableName|column1|value1");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("added new row to table tableName  which has values: [value1]");
    }

}
