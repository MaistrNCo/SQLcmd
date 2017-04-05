package ua.com.juja.sqlcmd.Controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.View.View;

import static org.junit.Assert.assertTrue;

/**
 * Created by maistrenko on 05.04.17.
 */
public class ExitTest {

    @Test
    public void testExitCanProcessTrue(){
        View view = Mockito.mock(View.class);
        Command command = new Exit(view);
        assertTrue(command.canProcess("exit"));
    }

    @Test
    public void testExitCanProcessFalse(){
        View view = Mockito.mock(View.class);
        Command command = new Exit(view);
        assertTrue(!command.canProcess("Exit"));
    }
    @Test
    public void testExitProcess(){
        View view = Mockito.mock(View.class);
        Command command = new Exit(view);
        try {
            command.process("exit");
        } catch (NormalExitException e) {
            //
        }
        Mockito.verify(view).printOut("Goodbye, to see soon. ");

    }



}
