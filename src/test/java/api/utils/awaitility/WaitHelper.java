package api.utils.awaitility;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class WaitHelper {

    public static void waitUntil(Callable<Boolean> condition) {
        await()
                .atMost(3, TimeUnit.SECONDS)
                .pollInterval(500, TimeUnit.MILLISECONDS)
                .until(condition);
    }

}
