package ua.com.juja.sqlcmd.Controller.command;

import org.junit.Test;
import org.mockito.Mockito;
import ua.com.juja.sqlcmd.View.View;

import static org.junit.Assert.assertTrue;

/**
 * Created by maistrenko on 05.04.17.
 */
public class ExitTest {

    private View view = Mockito.mock(View.class);

    @Test
    public void testExitCanProcessTrue(){
        Command command = new Exit(view);
        assertTrue(command.canProcess("exit"));
    }

    @Test
    public void testExitCanProcessFalse(){
        Command command = new Exit(view);
        assertTrue(!command.canProcess("Exit"));
    }
    @Test
    public void testExitProcess(){
        Command command = new Exit(view);
        try {
            command.process("exit");
        } catch (NormalExitException e) {
            //
        }
        Mockito.verify(view).printOut("Goodbye, to see soon. ");

    }



}
