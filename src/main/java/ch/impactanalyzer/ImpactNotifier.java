package ch.impactanalyzer;

import com.jeantessier.dependency.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ImpactNotifier
{

    private List<IDFImpactListener> listeners = new ArrayList<IDFImpactListener>();

    public List<IDFImpactListener> getListeners()
    {
        return listeners;
    }

    public void addListener(IDFImpactListener listener)
    {
        getListeners().add(listener);
    }

    public void notifyImpactStatusCreated(ImpactStats impactStats)
    {
        for (IDFImpactListener listener : listeners)
        {
            listener.impactStatusCreated(impactStats);
        }
    }

    public void notifyUniqueDependency(Node feature, Node dependantFeature, int level)
    {
        for (IDFImpactListener listener : listeners)
        {
            listener.uniqueDependency(feature, dependantFeature, level);
        }
    }
}
