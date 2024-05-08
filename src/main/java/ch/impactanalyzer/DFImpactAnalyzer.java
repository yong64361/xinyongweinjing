package ch.impactanalyzer;

import ch.impactanalyzer.collections.FeaturesList;
import com.jeantessier.dependency.ClassNode;
import com.jeantessier.dependency.FeatureNode;
import com.jeantessier.dependency.Node;
import com.jeantessier.dependency.NodeFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 *
 */
public class DFImpactAnalyzer extends ImpactNotifier
{
    static final Logger log = LoggerFactory.getLogger(DFImpactAnalyzer.class);
    private final NodeFactory nodeFactory;

    FeaturesList<String> uniqueFeatures = new FeaturesList<String>();

    public DFImpactAnalyzer(NodeFactory nodeFactory)
    {
        this.nodeFactory = nodeFactory;
    }

    /**
     *
     * @param feature
     * @return
     * @throws DFAnalyzerException
     */
    public ImpactStats getImpactStatsByFeatureName(String className, String feature) throws DFAnalyzerException
    {
        List<ImpactStats> stats = getImpactStatsWithValidation(className, feature);

        if (stats.size() > 1)
        {
            log.warn("More then one feature have been returned by given feature name. Name = {}", feature);
            log.warn("Only first item from the list will be returned. List size = {}. List<ImpactStatus> to string: {}",
                    stats.size(), stats.toString());
        }

        return stats.get(0);
    }

    public List<ImpactStats> getImpactStatsForAllClasses() throws DFAnalyzerException
    {
        List<ImpactStats> info = new ArrayList<ImpactStats>();
        Map<String, ClassNode> classes = getNodeFactory().getClasses();

        for (String className : classes.keySet())
        {
            info.addAll(getImpactStatsWithValidation(className, null));
        }

        return info;
    }

    /**
     *
     * @return
     * @throws DFAnalyzerException
     */
    public List<ImpactStats> getImpactStatsForAllFeatures(String className)  throws DFAnalyzerException
    {
        return getImpactStatsWithValidation(className, null);
    }

    private List<ImpactStats> getImpactStatsWithValidation(String className, String feature) throws DFAnalyzerException
    {
        List<ImpactStats> stats = calculateTransitiveDependencies(className, feature);

        if (stats == null)
        {
            throw new NullPointerException("Impact transitive calculator returned Null");
        }

        if (stats.isEmpty())
        {
            // it could be OK if all dependencies are filtered out. For example java.* packages are not interesting
            log.debug("Empty impact statistics information generated.");
        }

        return stats;
    }

    public int traverseInbound(Node source, Node dependent, int loopCount)
    {
        if (loopCount > getUniqueFeatures().getRelationsTreeLength())
        {
            getUniqueFeatures().setRelationsTreeLength(loopCount);
        }

        log.debug("Node: {}. loopCount: {}", dependent.getName(), loopCount);
        if (getUniqueFeatures().contains(dependent.getName()))
        {
            log.debug("DUPLICATE excluded: " + dependent.getName());
            return loopCount;
        }

        log.debug("[{}] source: {}, dependent: {}", loopCount, source, dependent);

        if (dependent instanceof FeatureNode)
        {
            FeatureNode fNode = (FeatureNode)dependent;


            /* DF shows Variable < constructor relations even if the variable is not really used in the constructor so probably it makes sense to exclude constructors as well or workaround it in some way:
             (fNode.getSimpleName().equals(fNode.getClassNode().getSimpleName() + "()") && loopCount > 0) ||
            */
            // exclude java.*
            if (fNode.getName().startsWith("java"))
            {
                log.debug("EXCLUDE: " + fNode);
                return loopCount;
            }
        }

        notifyUniqueDependency(source, dependent, loopCount);

        // add only dependencies. If source is null it's root of the hierarchy
        if (source != null)
        {
            getUniqueFeatures().add(dependent.getName(), loopCount);
        }

        Collection<Node> nodes = dependent.getInboundDependencies();
        for (Node n : nodes)
        {
            traverseInbound(dependent, n, loopCount + 1);
        }

        return getUniqueFeatures().getRelationsTreeLength();
    }

    protected List<ImpactStats> calculateTransitiveDependencies(String className, String feature) throws DFAnalyzerException
    {
        Map<String, ClassNode> classes =  getNodeFactory().getClasses();
        List<ImpactStats> impactStatsList = new ArrayList<ImpactStats>();
        ClassNode classNode = classes.get(className);

        if (classNode == null)
        {
            throw new DFAnalyzerException("Can't find the class by name. Class: " + className);
        }

        log.trace("calculateTransitiveDependencies. Start");

        if (StringUtils.isNoneEmpty(feature))
        {
            FeatureNode featureNode = classNode.getFeature(feature);

            if (featureNode == null)
            {
                throw new DFAnalyzerException("Can't find the feature by name in the specified class. Feature: " + feature);
            }

            impactStatsList.add(getImpactStatus(featureNode));
        }
        else
        {
            Collection<FeatureNode> features = classNode.getFeatures();

            for (FeatureNode featureNode : features)
            {
                // exclude java.* packages
                if (featureNode.getName().startsWith("java"))
                {
                    log.debug("EXCLUDE: " + featureNode.getName());
                    continue;
                }

                impactStatsList.add(getImpactStatus(featureNode));
            }
        }

        log.trace("calculateTransitiveDependencies. Stop");
        log.debug("Impact stats list size: {}", impactStatsList.size());

        return impactStatsList;
    }

    private ImpactStats getImpactStatus(FeatureNode featureNode)
    {
        int count = traverseInbound(null, featureNode, 0);

        // if all dependencies were filtered relations length is 0 as well even if it took many iterations to figure it out
        if (getUniqueFeatures().size() == 0)
        {
            count = 0;
        }

        ImpactStats impactStats = new ImpactStats(featureNode,
                getUniqueFeatures().size(),
                count,
                getUniqueFeatures().getDirectRelationsCount());
        log.debug("Impact level: {}", impactStats.getTransitiveRelationsLength());
        log.debug("Impact count: {}", impactStats.getTransitiveRelationsCount());
        log.debug("Direct Impact count: {}", impactStats.getDirectRelationsCount());
        getUniqueFeatures().clear();

        notifyImpactStatusCreated(impactStats);

        return impactStats;
    }

    public NodeFactory getNodeFactory()
    {
        return nodeFactory;
    }

    public FeaturesList<String> getUniqueFeatures()
    {
        return uniqueFeatures;
    }
}
