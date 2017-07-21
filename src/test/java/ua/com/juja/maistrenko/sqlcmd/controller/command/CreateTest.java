package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Create;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreateTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Create(dbManager, view);
    }

    @Test
    public void createCanProcessTrue() {
        assertTrue(command.canProcess("create|werwert"));
    }

    @Test
    public void createCanProcessFalse() {
        assertFalse(command.canProcess("create"));
    }

    @Test
    public void createProcessHelp() {
        try {
            command.process("create|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("create|tableName|column1|column2|...|columnN - " +
                "to create table 'tableName' with defined columns. WARNING 'column1' will be set as PRIMARY KEY, serial");
    }

    @Test
    public void createProcessWrongParamsAmount() {
        try {
            command.process("create|tableName");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("create|tableName|column1","create|tableName");
    }

    @Test
    public void createProcessSuccessful() {
        Set<String> columnsList = new LinkedHashSet<>();
        columnsList.add("id");
        columnsList.add("column1");
        Mockito.when(dbManager.getColumnsNames("tableName")).thenReturn(columnsList);
        try {
            command.process("create|tableName|column1");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write(" created table tableName with columns [id, column1]");
    }

}
