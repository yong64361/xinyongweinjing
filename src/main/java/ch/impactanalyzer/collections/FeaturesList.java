package ch.impactanalyzer.collections;

import java.util.ArrayList;

/**
 *
 */
public class FeaturesList<E> extends ArrayList<E>
{
    int relationsTreeLength = 0;
    int directRelationsCount = 0;

    public int getRelationsTreeLength()
    {
        return relationsTreeLength;
    }

    public void setRelationsTreeLength(int relationsTreeLength)
    {
        this.relationsTreeLength = relationsTreeLength;
    }

    /**
     * Count of relations for length = 1
     * @return
     */
    public int getDirectRelationsCount()
    {
        return directRelationsCount;
    }

    public void setDirectRelationsCount(int directRelationsCount)
    {
        this.directRelationsCount = directRelationsCount;
    }

    @Override
    public void clear()
    {
        super.clear();
        setRelationsTreeLength(0);
        setDirectRelationsCount(0);
    }

    public boolean add(E e, int level)
    {
        if (level == 1)
        {
            directRelationsCount++;
        }

        return super.add(e);
    }
}
