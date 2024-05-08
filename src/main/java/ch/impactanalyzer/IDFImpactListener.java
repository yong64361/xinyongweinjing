package ch.impactanalyzer;

import com.jeantessier.dependency.Node;

/**
 *
 */
public interface IDFImpactListener
{
    /**
     *
     * @param source Could be NULL if it's root of the dependency tree.
     * @param dependent
     * @param level
     */
    public void uniqueDependency(Node source, Node dependent, int level);
    public void impactStatusCreated(ImpactStats impactStats);
}
