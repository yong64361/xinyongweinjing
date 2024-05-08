package ch.impactanalyzer;

import com.jeantessier.dependency.FeatureNode;

/**
 *
 */
public class ImpactStats
{
    private final FeatureNode featureNode;

    private int transitiveRelationsLength = 0;
    private int transitiveRelationsCount = 0;
    private int directRelationsCount = 0;


    public ImpactStats(FeatureNode featureNode, int transitiveRelationsCount, int transitiveRelationsLength, int directRelationsCount)
    {
        this.featureNode = featureNode;
        setTransitiveRelationsCount(transitiveRelationsCount);
        setTransitiveRelationsLength(transitiveRelationsLength);
        setDirectRelationsCount(directRelationsCount);
    }

    public FeatureNode getFeatureNode()
    {
        return featureNode;
    }

    public String getFeatureName()
    {
        return getFeatureNode().getName();
    }

    public int getTransitiveRelationsCount()
    {
        return transitiveRelationsCount;
    }

    public void setTransitiveRelationsCount(int transitiveRelationsCount)
    {
        this.transitiveRelationsCount = transitiveRelationsCount;
    }

    public int getTransitiveRelationsLength()
    {
        return transitiveRelationsLength;
    }

    public void setTransitiveRelationsLength(int transitiveRelationsLength)
    {
        this.transitiveRelationsLength = transitiveRelationsLength;
    }

    public int getDirectRelationsCount()
    {
        return directRelationsCount;
    }

    public void setDirectRelationsCount(int directRelationsCount)
    {
        this.directRelationsCount = directRelationsCount;
    }
}
