package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Update(dbManager, view);
    }

    @Test
    public void testUpdateCanProcessTrue() {
        assertTrue(command.canProcess("update|werwert"));
    }

    @Test
    public void testUpdateCanProcessFalse() {
        assertFalse(command.canProcess("update"));
    }

    @Test
    public void testUpdateProcessHelp() {
        try {
            command.process("update|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN " +
                "- to update data in rows of table 'tableName' selected by condition: conditionalColumn == conditionalValue");
    }

    @Test
    public void testUpdateProcessWrongParamsAmount() {
        try {
            command.process("update|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("update|tableName|column1|value1column2|value2","update|tableName");
    }
    @Test
    public void testUpdateProcessWrongParamsAmountMore() {
        try {
            command.process("update|tableName|column1|value1|column2|value2|value3");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("parameters amount must be paired");
    }

    @Test
    public void testUpdateProcessSuccessful() {
        try {
            command.process("update|tableName|column1|value1|column2|value2");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write(" data in table tableName updated");
    }

}
