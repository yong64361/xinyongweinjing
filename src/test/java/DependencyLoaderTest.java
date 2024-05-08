import ch.impactanalyzer.DependencyLoader;
import ch.impactanalyzer.DependencyLoaderException;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


/**
 *
 */
public class DependencyLoaderTest extends Assert
{
    final String XML = "src/test/resources/df.xml";
    final String JARS = "src/test/resources/jars/";
    final String CLASSES = "src/test/resources/classes/";

    @Test
    public void testLoader() throws DependencyLoaderException, IOException
    {
        Collection<String> path = new ArrayList<String>();

        DependencyLoader xmlLoader = new DependencyLoader();
        DependencyLoader jarsLoader = new DependencyLoader();
        DependencyLoader classesLoader = new DependencyLoader();

        assertNotNull(xmlLoader.getNodeFactory());

        xmlLoader.loadNodeFactoryFromXML(XML);
        assertFalse(xmlLoader.getNodeFactory().getClasses().isEmpty());

        path.add(JARS);
        jarsLoader.loadNodeFactoryFromJava (path);
        assertFalse(jarsLoader.getNodeFactory().getClasses().isEmpty());

        path.clear();
        path.add(CLASSES);
        classesLoader.loadNodeFactoryFromJava (path);
        assertFalse(classesLoader.getNodeFactory().getClasses().isEmpty());

        assertEquals(classesLoader.getNodeFactory().getClasses().size(), jarsLoader.getNodeFactory().getClasses().size());
        assertEquals(xmlLoader.getNodeFactory().getClasses().size(), jarsLoader.getNodeFactory().getClasses().size());
    }

    @Test  (expected = DependencyLoaderException.class)
    public void testExceptions() throws DependencyLoaderException
    {
        DependencyLoader loader = new DependencyLoader();
        loader.loadNodeFactoryFromXML("fake.xml");
    }
}
