package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteTest {
    private View view;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        DBManager dbManager = mock(DBManager.class);
        command = new Delete(dbManager, view);
    }

    @Test
    public void testDeleteCanProcessTrue() {
        assertTrue(command.canProcess("delete|werwert"));
    }

    @Test
    public void testDeleteCanProcessFalse() {
        assertFalse(command.canProcess("delete"));
    }

    @Test
    public void testDeleteProcessHelp() {
        try {
            command.process("delete|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("delete|tableName|column1|value1|column2|value2... - " +
                "to delete data in table 'tableName' where column1 = value1, column2 = value2 and so on");
    }

    @Test
    public void testDeleteProcessWrongParamsAmount() {
        try {
            command.process("delete|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("delete|tableName|column|value","delete|tableName");
    }

    @Test
    public void testDeleteProcessSuccessful() {
        try {
            command.process("delete|tableName|column1|value");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("deleted data from table tableName");
    }

}
