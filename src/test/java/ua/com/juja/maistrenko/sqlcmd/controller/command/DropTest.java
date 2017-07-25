package ua.com.juja.maistrenko.sqlcmd.controller.command;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Drop;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DropTest {
    private View view;
    private Command command;
    private DBManager dbManager;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Drop(dbManager, view);
    }

    @Test
    public void dropCanProcessTrue() {
        assertTrue(command.canProcess("drop|werwert"));
    }

    @Test
    public void dropCanProcessFalse() {
        assertFalse(command.canProcess("drop"));
    }

    @Test
    public void dropProcessHelp() {
        try {
            command.process("drop|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("drop|tableName - to delete table 'tableName' with all contained data");
    }

    @Test
    public void dropProcessWrongParamsAmount() {
        try {
            command.process("drop|tableName|sdfg");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("drop|tableName","drop|tableName|sdfg");
    }

    @Test
    public void dropProcessSuccessful() {
        Set<String> listBefore = new HashSet<>();
        listBefore.add("test1");
        listBefore.add("tableName");
        Set<String> listAfter = new HashSet<>();
        listAfter.add("test1");
        Mockito.when(dbManager.getTablesList()).thenReturn(listBefore).thenReturn(listAfter);
        try {
            command.process("drop|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Table tableName deleted from database successfully");
    }
    @Test
    public void dropProcessWrongName() {
        Set<String> listBefore = new HashSet<>();
        listBefore.add("test1");
        listBefore.add("test2");
        Set<String> listAfter = new HashSet<>();
        listAfter.add("test1");
        listAfter.add("test2");
        Mockito.when(dbManager.getTablesList()).thenReturn(listBefore).thenReturn(listAfter);
        try {
            command.process("drop|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("Nothing is deleted");
    }

}


