package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Update;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.model.RowData;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateTest {
    private View view;
    private Command command;
    private DBManager dbManager;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Update(dbManager, view);
    }

    @Test
    public void updateCanProcessTrue() {
        assertTrue(command.canProcess("update|werwert"));
    }

    @Test
    public void updateCanProcessFalse() {
        assertFalse(command.canProcess("update"));
    }

    @Test
    public void updateProcessHelp() {
        try {
            command.process("update|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN " +
                "- to update data in rows of table 'tableName' selected by condition: conditionalColumn == conditionalValue");
    }

    @Test
    public void updateProcessWrongParamsAmount() {
        try {
            command.process("update|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("update|tableName|conditionalColumn|conditionalValue|column1|value1|" +
                "...|columnN|valueN ", "update|tableName");
    }

    @Test
    public void updateProcessWrongParamsAmountMore() {
        try {
            command.process("update|tableName|column1|value1|column2|value2|value3");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg(
                "update|tableName|conditionalColumn|conditionalValue|column1|value1|...|columnN|valueN ",
                "update|tableName|column1|value1|column2|value2|value3"
        );
    }

    @Test
    public void updateProcessSuccessful() {
        Mockito.when(dbManager.update(isA(String.class), isA(RowData.class), isA(RowData.class))).thenReturn(1);
        try {
            command.process("update|tableName|column1|value1|column2|value2");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("data in table tableName updated");
    }

}
