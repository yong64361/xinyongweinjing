package ch.impactanalyzer;

import com.jeantessier.classreader.LoadEvent;
import com.jeantessier.classreader.LoadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class Slf4JLogListener implements LoadListener
{
    public static final Logger log = LoggerFactory.getLogger(Slf4JLogListener.class);

    @Override
    public void beginSession(LoadEvent loadEvent)
    {
        log.debug("beginSession: {}", loadEvent.getSource());
    }

    @Override
    public void beginGroup(LoadEvent loadEvent)
    {
        log.debug("beginGroup: {}", loadEvent.getGroupName());
    }

    @Override
    public void beginFile(LoadEvent loadEvent)
    {
       log.debug("beginFile: {}", loadEvent.getFilename());
    }

    @Override
    public void beginClassfile(LoadEvent loadEvent)
    {
        log.debug("beginClassfile: {}", loadEvent.getClassfile());
    }

    @Override
    public void endClassfile(LoadEvent loadEvent)
    {
        log.debug("endClassfile: {}", loadEvent.getClassfile());
    }

    @Override
    public void endFile(LoadEvent loadEvent)
    {
        log.debug("endFile: {}", loadEvent.getFilename());
    }

    @Override
    public void endGroup(LoadEvent loadEvent)
    {
        log.debug("endGroup: {}", loadEvent.getGroupName());
    }

    @Override
    public void endSession(LoadEvent loadEvent)
    {
        log.debug("endSession: {}", loadEvent.getSource());
    }
}
