package api.utils;

public class Config {
    private static final ThreadLocal<String> baseUrl = ThreadLocal.withInitial(() ->
            System.getProperty("BASE_URL", "https://cloud-api.yandex.net")
    );

    public static final String TOKEN = System.getenv("YANDEX_TOKEN");

    public static String getBaseUrl() {
        return baseUrl.get();
    }
    public static void setBaseUrl(String url) {
        baseUrl.set(url);
    }
    static {
        boolean useMock = Boolean.parseBoolean(System.getProperty("use.mock", "false"));
        if (!useMock && (TOKEN == null || TOKEN.isBlank())) {
            throw new RuntimeException("YANDEX_TOKEN is missing. Set it or run with -Pmock");
        }
    }
}