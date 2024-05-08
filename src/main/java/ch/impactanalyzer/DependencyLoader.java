package ch.impactanalyzer;

import com.jeantessier.classreader.ClassfileLoader;
import com.jeantessier.classreader.LoadListener;
import com.jeantessier.classreader.LoadListenerVisitorAdapter;
import com.jeantessier.classreader.TransientClassfileLoader;
import com.jeantessier.dependency.CodeDependencyCollector;
import com.jeantessier.dependency.NodeFactory;
import com.jeantessier.dependency.NodeLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 */
public class DependencyLoader
{
    static final Logger log = LoggerFactory.getLogger(DependencyLoader.class);
    private NodeFactory nodeFactory = new NodeFactory();
    private LoadListener listener = new Slf4JLogListener();

    public void loadNodeFactory(Collection<String> path) throws DependencyLoaderException
    {
        Collection<String> xmls = new ArrayList<String>();
        Collection<String> javas = new ArrayList<String>();

        for (String s : path)
        {
            if (s.endsWith(".xml"))
            {
                xmls.add(s);
            }
            else
            {
                javas.add(s);
            }
        }

        if (!xmls.isEmpty())
        {
            loadNodeFactoryFromXML(xmls);
        }
        if (!javas.isEmpty())
        {
            loadNodeFactoryFromJava(javas);
        }
    }

    public void loadNodeFactoryFromXML(Collection<String> path) throws DependencyLoaderException
    {
        for (String s : path)
        {
            loadNodeFactoryFromXML(s);
        }
    }

    public void loadNodeFactoryFromXML(String fileName) throws DependencyLoaderException
    {
        try
        {
            loadGraphFromXML(getNodeFactory(), fileName);
        } catch (IOException e)
        {
            throw new DependencyLoaderException("Can't load file. IO error: " + e.getMessage(), e);
        } catch (SAXException e)
        {
            throw new DependencyLoaderException("Can't load file. SAX processing error: " + e.getMessage(), e);
        } catch (ParserConfigurationException e)
        {
            throw new DependencyLoaderException("Can't load file. Parser error: " + e.getMessage(), e);
        }
    }

    public void loadNodeFactoryFromJava(Collection<String> path)
    {
        log.debug("loadNodeFactoryFromJava from the {}", path.toString());

        CodeDependencyCollector collector = new CodeDependencyCollector(getNodeFactory());

        ClassfileLoader loader = new TransientClassfileLoader();
        loader.addLoadListener(new LoadListenerVisitorAdapter(collector));
        loader.addLoadListener(getLogListener());
        loader.load(path);
    }

    public NodeFactory getNodeFactory()
    {
        return nodeFactory;
    }

    private void loadGraphFromXML(NodeFactory factory, String filename) throws IOException, SAXException, ParserConfigurationException
    {
        log.debug("loadGraphFromXML from the {} file", filename);

        NodeLoader loader = new NodeLoader(factory, true);
        loader.load(filename);
    }

    protected LoadListener getLogListener()
    {
        return listener;
    }
}
