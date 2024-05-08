import ch.impactanalyzer.ui.commandline.CommandLineUI;
import ch.impactanalyzer.ui.commandline.CommandLineUIInputConst;
import junit.framework.Assert;
import org.junit.Test;

import java.util.List;

/**
 *
 */
public class CommandLineTest extends Assert
{
    @Test
    public void testDefaultInputParams()
    {
        List<String> inputParam;

        CommandLineUI commandLine = new CommandLineUI(new String[]{});
        inputParam = commandLine.getCommandLine().getMultipleSwitch(CommandLineUIInputConst.INPUT);

        assertEquals(CommandLineUIInputConst.INPUT_DEFAULT, inputParam.get(0));
    }

    @Test
    public void testSpecifiedParams()
    {
        final String P1 = "test.xml";
        final String P2 = "./java_classes";
        String[] args = new String[]{'-' + CommandLineUIInputConst.INPUT, P1, '-' + CommandLineUIInputConst.INPUT, P2};
        List<String> inputParams;
        String versionParam;

        CommandLineUI commandLine = new CommandLineUI(args);
        inputParams = commandLine.getCommandLine().getMultipleSwitch(CommandLineUIInputConst.INPUT);
        versionParam =  commandLine.getCommandLine().getSingleSwitch(CommandLineUIInputConst.VERSION);

        // check multiple params
        assertTrue(commandLine.getCommandLine().isPresent(CommandLineUIInputConst.INPUT));
        assertEquals(P1, inputParams.get(0));
        assertEquals(P2, inputParams.get(1));

        // empty param
        assertFalse(commandLine.getCommandLine().isPresent(CommandLineUIInputConst.HELP));

        // version check
        assertEquals(CommandLineUIInputConst.VERSION_DEFAULT, versionParam);
    }
}
