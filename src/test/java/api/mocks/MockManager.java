package api.mocks;

public interface MockManager {
    void start();
    void stop();
    int getPort();
    void registerStubs();
    void reset();
}
