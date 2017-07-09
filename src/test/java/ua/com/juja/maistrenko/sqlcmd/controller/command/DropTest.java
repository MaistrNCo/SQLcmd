package ua.com.juja.maistrenko.sqlcmd.controller.command;


import org.junit.Before;
import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DropTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Drop(dbManager, view);
    }

    @Test
    public void testDropCanProcessTrue() {
        assertTrue(command.canProcess("drop|werwert"));
    }

    @Test
    public void testDropCanProcessFalse() {
        assertFalse(command.canProcess("drop"));
    }

    @Test
    public void testDropProcessHelp() {
        try {
            command.process("drop|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("drop|tableName - to delete table 'tableName' with all contained data");
    }

    @Test
    public void testDropProcessWrongParamsAmount() {
        try {
            command.process("drop|tableName|sdfg");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("drop|tableName","drop|tableName|sdfg");
    }

    @Test
    public void testDropProcessSuccessful() {
        try {
            command.process("drop|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Table tableName deleted from database successfully");
    }

}


