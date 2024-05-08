package ch.impactanalyzer.ui.commandline;

import ch.impactanalyzer.*;
import com.jeantessier.commandline.CollectingParameterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *  Input:
 *  -in *.xml ./classes: one or several input parameters to load the tree. XML should by in the DepFinder format (http://depfind.sourceforge.net/)
 *  -h: help
 *  -version: version
 */
public class CommandLineUI
{
    static final Logger log = LoggerFactory.getLogger(CommandLineUI.class);
    private CommandLineProcessor commandLine = null;

    public CommandLineUI(String[] args)
    {
        setCommandLine(new CommandLineProcessor(true, new CollectingParameterStrategy()));
        getCommandLine().addOptionalValueSwitch(CommandLineUIInputConst.HELP);
        getCommandLine().addOptionalValueSwitch(CommandLineUIInputConst.VERSION, CommandLineUIInputConst.VERSION_DEFAULT);
        getCommandLine().addMultipleValuesSwitch(CommandLineUIInputConst.INPUT, CommandLineUIInputConst.INPUT_DEFAULT);
        getCommandLine().parse(args);
    }

    public static void main(String[] args)
    {
        try
        {
            CommandLineUI ui = new CommandLineUI(args);
            execute(ui.getCommandLine());
        }
        catch (DFAnalyzerException e)
        {
            log.error("DFAnalyzer error: {}", e.getMessage(), e);
            System.out.println("Dependency analyzer can't produce dependency statistics: " + e.getMessage());
        }
        catch (DependencyLoaderException e)
        {
            log.error("DependencyLoader error: {}", e.getMessage(), e);
            System.out.println("Dependency loader can't load a dependency tree: " + e.getMessage());
        }
        catch (Exception e)
        {
            log.error("Unexpected critical system error: {}", e.getMessage(), e);
            System.out.println("Unexpected critical system error: " + e.getMessage());
        }
    }

    public static void execute(CommandLineProcessor commandLine) throws DependencyLoaderException, DFAnalyzerException
    {
        List<String> inputParams = commandLine.getMultipleSwitch(CommandLineUIInputConst.INPUT);
        DependencyLoader dependencyLoader = new DependencyLoader();
        DFImpactAnalyzer analyzer = null;
        List<ImpactStats> impactStatsList = null;

        if (inputParams == null || !inputParams.isEmpty())
        {
            log.error("Input parameters is empty");
            System.out.println("Please specify source XML or Java files. Use the '" + CommandLineUIInputConst.INPUT + "' key.");
        }

        dependencyLoader.loadNodeFactory(inputParams);
        analyzer = new DFImpactAnalyzer(dependencyLoader.getNodeFactory());
        TextDependencyPrinter listener = new TextDependencyPrinter();
        analyzer.addListener(listener);

        impactStatsList = analyzer.getImpactStatsForAllClasses();

        System.out.println(listener.getOutput());
    }

    public CommandLineProcessor getCommandLine()
    {
        return commandLine;
    }

    public void setCommandLine(CommandLineProcessor commandLine)
    {
        this.commandLine = commandLine;
    }

}
