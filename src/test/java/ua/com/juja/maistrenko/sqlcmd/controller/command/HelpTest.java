package ua.com.juja.maistrenko.sqlcmd.controller.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.maistrenko.sqlcmd.controller.MainController;
import ua.com.juja.maistrenko.sqlcmd.controller.command.impl.Help;
import ua.com.juja.maistrenko.sqlcmd.model.DBManager;
import ua.com.juja.maistrenko.sqlcmd.view.View;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

public class HelpTest {

    private View view;
    private DBManager dbManager;
    private Command command;

    @Before
    public void init() {
        view = mock(View.class);
        dbManager = mock(DBManager.class);
        command = new Help(view, new MainController(dbManager,view));
    }
    @Test
    public void helpCanProcessTrue() {
        assertTrue(command.canProcess("help"));
    }

    @Test
    public void helpCanProcessFalse() {
        assertFalse(command.canProcess("Help"));
        assertFalse(command.canProcess("help|we"));
    }

    @Test
    public void helpProcess() {
        command.process("help");
        Mockito.verify(view).write("Commands full list :");

        Set commands = new MainController(dbManager,view).getCommands();
        Iterator<Command> it = commands.iterator();
        while (it.hasNext()) {
           Mockito.verify(view,atLeastOnce()).writeCommandDescription(it.next().getDescription());
        }
    }
}
