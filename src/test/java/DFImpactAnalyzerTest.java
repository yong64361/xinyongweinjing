import ch.impactanalyzer.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import junit.framework.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class DFImpactAnalyzerTest extends Assert
{
    static final Logger log = LoggerFactory.getLogger(DFImpactAnalyzerTest.class);
    static final String XML = "src/test/resources/df.xml";

    @Test
    public void testAnalyzer() throws DependencyLoaderException, IOException, DFAnalyzerException
    {
        DFImpactAnalyzer analyzer;
        List<ImpactStats> impactStatsList;
        ImpactStats impactStats;

        DependencyLoader loader = new DependencyLoader();
        loader.loadNodeFactoryFromXML(XML);

        analyzer = new DFImpactAnalyzer(loader.getNodeFactory());
        TextDependencyPrinter listener = new TextDependencyPrinter();
        analyzer.addListener(listener);

        impactStatsList = analyzer.getImpactStatsForAllClasses();
        assertNotNull(impactStatsList);
        assertTrue(impactStatsList.size() > 0);
        assertEquals(impactStatsList.size(), 12);
        assertTrue(StringUtils.isNoneEmpty(listener.getOutput()));
        log.debug("\n" + listener.getOutput());


        listener.getBuffer().setLength(0);
        impactStats = analyzer.getImpactStatsByFeatureName("ch.com.Main", "test()");
        assertNotNull(impactStats);
        assertEquals(impactStats.getTransitiveRelationsCount(), 0);
        assertEquals(impactStats.getTransitiveRelationsLength(), 0);
        assertTrue(StringUtils.isNoneEmpty(listener.getOutput()));
        log.debug("\n" + listener.getOutput());

        listener.getBuffer().setLength(0);
        impactStats = analyzer.getImpactStatsByFeatureName("ch.com.Utils", "SQL");
        assertNotNull(impactStats);
        assertEquals(impactStats.getTransitiveRelationsCount(), 6);
        assertEquals(impactStats.getTransitiveRelationsLength(), 4);
        assertTrue(StringUtils.isNoneEmpty(listener.getOutput()));
        log.debug("\n" + listener.getOutput());
    }
}
