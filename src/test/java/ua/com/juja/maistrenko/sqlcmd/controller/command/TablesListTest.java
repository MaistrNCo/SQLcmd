package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.TablesList;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TablesListTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new TablesList(dbManager, view);
    }

    @Test
    public void tablesListCanProcessTrue() {
        assertTrue(command.canProcess("list"));
    }

    @Test
    public void tablesListCanProcessFalse() {
        assertFalse(command.canProcess("list|postgre"));
    }

    @Test
    public void tablesListProcessHelp() {
        try {
            command.process("list|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("list - to get tables list in connected database");
    }

    @Test
    public void tablesListProcessSuccessful() {
        Set<String> testSet = new LinkedHashSet<>();
        testSet.add("table1");
        testSet.add("table2");
        testSet.add("table3");
        Mockito.when(dbManager.getTablesList()).thenReturn(testSet);
        try {
            command.process("list");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("[table1, table2, table3]");
    }

}
