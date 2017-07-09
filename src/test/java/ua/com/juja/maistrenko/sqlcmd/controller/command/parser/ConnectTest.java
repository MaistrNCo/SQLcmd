package ua.com.juja.maistrenko.sqlcmd.controller.command.parser;

import org.junit.Before;
import org.junit.Test;
import ua.com.juja.maistrenko.sqlcmd.controller.command.ClearTable;
import ua.com.juja.maistrenko.sqlcmd.controller.command.Command;
import ua.com.juja.maistrenko.sqlcmd.controller.command.Connect;
import ua.com.juja.maistrenko.sqlcmd.controller.command.NormalExitException;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConnectTest {
    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Connect(dbManager, view);
    }

    @Test
    public void testClearCanProcessTrue() {
        assertTrue(command.canProcess("connect|dataBase|userName|password"));
    }

    @Test
    public void testClearCanProcessHelp() {
        try {
            command.process("connect|help");
        } catch (NormalExitException e) {
            //
        }
        verify(view).write("connect|serverName:port|dataBase|userName|password - " +
                "for connection to SQL server. You can omit 'port' or 'serverName:port' for default port on localhost");
    }
    @Test
    public void testClearCanProcessWrongParamsAmount() {
        try {
            command.process("connect|postgres|postgres");
        } catch (NormalExitException e) {
            //
        }
        verify(view).writeWrongParamsMsg("connect|dataBase|userName|password","connect|postgres|postgres");
    }

    @Test
    public void testClearCanProcessFalse() {
        assertFalse(command.canProcess("connect"));
    }

}
