package integration.utils;

public class Config {
    public static final String BASE_URL = "https://cloud-api.yandex.net";
    public static final String TOKEN =
            System.getenv("YANDEX_TOKEN");


    static {

        if (TOKEN == null || TOKEN.isBlank()) {

            throw new RuntimeException(
                    "YANDEX_TOKEN is missing"
            );
        }
    }
}