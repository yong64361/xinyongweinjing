package ch.impactanalyzer.ui.commandline;

import com.jeantessier.commandline.CollectingParameterStrategy;
import com.jeantessier.commandline.CommandLine;
import com.jeantessier.commandline.CommandLineException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 *
 */
public class CommandLineProcessor extends CommandLine
{
    static final Logger log = LoggerFactory.getLogger(CommandLineUI.class);

    public CommandLineProcessor(boolean b, CollectingParameterStrategy collectingParameterStrategy)
    {
        super(b, collectingParameterStrategy);
    }

    @Override
    public Collection<CommandLineException> parse(String[] args)
    {
        if (args == null)
        {
            throw new NullPointerException("No Arguments to parse. args = null");
        }

        debugParams(args);
        return super.parse(args);
    }

    private void debugParams(String[] args)
    {

        for (int i = 0; i < args.length; i++)
        {
            log.debug("Argument [{}] = {}", i, args[i]);
        }

    }
}
