package ch.impactanalyzer;

/**
 *
 */
public class DependencyLoaderException extends Exception
{
    public DependencyLoaderException()
    {
    }

    public DependencyLoaderException(String message)
    {
        super(message);
    }

    public DependencyLoaderException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public DependencyLoaderException(Throwable cause)
    {
        super(cause);
    }
}
