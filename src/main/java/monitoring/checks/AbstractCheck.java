package monitoring.checks;

import monitoring.CheckFailedException;
import monitoring.Monitor;
import monitoring.State;

public abstract class AbstractCheck implements Monitor {

    protected int timeout;
    protected long lastRan;
    protected long lastSuccess;
    protected long lastFailed;
    protected State state;

    protected void markUp() {
        this.lastSuccess = System.currentTimeMillis();
        this.state = State.UP;
    }

    protected void markDown(final Exception e) throws CheckFailedException {
        this.lastFailed = System.currentTimeMillis();
        this.state = State.DOWN;
        throw new CheckFailedException(e);
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public long getLastRan() {
        return this.lastRan;
    }

    @Override
    public long getLastSuccess() {
        return this.lastSuccess;
    }

    @Override
    public long getLastFailed() {
        return this.lastFailed;
    }

}
