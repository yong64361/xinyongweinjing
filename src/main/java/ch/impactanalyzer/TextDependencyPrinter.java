package ch.impactanalyzer;

import com.jeantessier.dependency.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class TextDependencyPrinter implements IDFImpactListener
{

    static final Logger log = LoggerFactory.getLogger(TextDependencyPrinter.class);

    private StringBuilder buffer = new StringBuilder();

    /**
     * Could be overwritten to use different buffer. getBuffer() should be considered for overwriting.
     * @param str String to write into buffer.
     */
    protected void printToBuffer(String str)
    {
        getBuffer().append(str);
    }

    /**
     * Method used to write into a buffer in the printToBuffer(String str) method.
     * @return
     */
    public StringBuilder getBuffer()
    {
        return buffer;
    }

    public String getOutput()
    {
        return getBuffer().toString();
    }

    public static String getIdent(int loopCount)
    {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < loopCount; i++)
        {
            s.append(' ');
        }
        return s.toString();
    }

    @Override
    public void uniqueDependency(Node source, Node dependent, int level)
    {
        log.debug("uniqueDependency. Source: {}, dependent: {}, level: {}", source, dependent, level);

        if (source == null)
        {
            printToBuffer(dependent.toString() + '\n');
        }
        else
        {
            printToBuffer(TextDependencyPrinter.getIdent(level) + " << " + dependent + '\n');
        }

        log.debug("buffer: {}", getBuffer().length());
    }

    @Override
    public void impactStatusCreated(ImpactStats impactStats)
    {
        log.debug("impactStatusCreated. Transitive relations count: {}, Transitive relations length: {}",
                impactStats.getTransitiveRelationsCount(), impactStats.getTransitiveRelationsLength());
        printToBuffer("Direct relations count:" + impactStats.getDirectRelationsCount() + "\n");
        printToBuffer("Transitive relations count:" + impactStats.getTransitiveRelationsCount() + '\n');
        printToBuffer("Transitive relations length:" + impactStats.getTransitiveRelationsLength() + "\n\n");

    }
}
