package monitoring;

public interface Monitor {

    void check() throws CheckFailedException;

    long getLastRan();

    long getLastSuccess();

    long getLastFailed();

    State getState();

}
