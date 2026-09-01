package api.mocks;

import api.utils.Config;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.Tag;

import java.util.Arrays;

public class MockExtension implements BeforeEachCallback, AfterEachCallback {

    private static MockManager manager;

    private static final ThreadLocal<Boolean> started = ThreadLocal.withInitial(() -> false);

    @Override
    public void beforeEach(ExtensionContext context) {
        System.out.println("=== MockExtension: test = " + context.getTestMethod().get().getName());
        boolean globalMockEnabled = Boolean.parseBoolean(System.getProperty("use.mock", "false"));
        if (!globalMockEnabled) {
            return;
        }

        boolean hasMockTag = context.getTestMethod()
                .map(method -> method.getAnnotationsByType(Tag.class))
                .stream()
                .flatMap(Arrays::stream)
                .anyMatch(tag -> tag.value().equals("mock"));

        if (!hasMockTag) {
            Config.setBaseUrl("https://cloud-api.yandex.net");
            return;
        }

        if (manager == null) {
            String managerClass = System.getProperty("mock.manager", "api.mocks.wiremock.WireMockManager");
            try {
                manager = (MockManager) Class.forName(managerClass)
                        .getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create mock manager: " + managerClass, e);
            }
        }

        if (!started.get()) {
            manager.start();
            started.set(true);
            Runtime.getRuntime().addShutdownHook(new Thread(manager::stop));
        }

        Config.setBaseUrl("http://localhost:" + manager.getPort());
        manager.registerStubs();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (started.get()) {
            Config.setBaseUrl("https://cloud-api.yandex.net");
            started.set(false);
        }
    }
}
