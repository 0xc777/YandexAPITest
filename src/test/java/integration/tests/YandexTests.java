package integration.tests;
import integration.helper.ApiHelper;
import integration.models.ApiResponse;
import integration.steps.YandexTestsSteps;
import org.junit.jupiter.api.*;
import io.qameta.allure.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class YandexTests {
    private final YandexTestsSteps yandexTestsSteps = new YandexTestsSteps();
    @Test
    @Tag("Smoke")
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Получаем информацию о диске")
    @DisplayName("Получаем информацию о диске")
    void shouldGetDiskInfo() throws Exception {
        ApiResponse apiResponse = yandexTestsSteps.getDiskInfo("/v1/disk");
        Assertions.assertEquals(200,apiResponse.getStatusCode());
    }
    @Test
    @Tag("Integration")
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description ("Создаем папку с именем testPack, потом её удаляем")
    @DisplayName("Успешное создание папки")
    void createPackage() throws Exception {
        ApiResponse apiResponse = yandexTestsSteps.createResource("/v1/disk/resources?path=/testPack",null);
        Assertions.assertEquals(201,apiResponse.getStatusCode());
        apiResponse = yandexTestsSteps.deleteResource("/v1/disk/resources?path=/testPack");
        Assertions.assertTrue(apiResponse.getStatusCode()==200||apiResponse.getStatusCode()==202||apiResponse.getStatusCode()==204);
    }
    @Test
    @Tag("Integration")
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description ("Создаем папку с именем testPack, поотом пытаемся создать еще раз, чтобы получить ошибку 409")
    @DisplayName("Проверка ошибки при создании существующей папки")
    void createDuplicatePackage() throws Exception {
        ApiResponse apiResponse = yandexTestsSteps.createResource("/v1/disk/resources?path=/testPack",null);
        Assertions.assertEquals(201,apiResponse.getStatusCode());
        apiResponse = yandexTestsSteps.createResource("/v1/disk/resources?path=/testPack",null);
        Assertions.assertEquals(409,apiResponse.getStatusCode());
        apiResponse = yandexTestsSteps.deleteResource("/v1/disk/resources?path=/testPack");
        Assertions.assertTrue(apiResponse.getStatusCode()==200||apiResponse.getStatusCode()==202||apiResponse.getStatusCode()==204);
    }
    @Test
    @Tag("Integration")
    @Tag("positive")
    @Severity(SeverityLevel.CRITICAL)
    @Description ("Создаем папку testPack, потом копируем её, дальше удаляем оригинал и дубликат")
    @DisplayName("Успешное копирование папки")
    void copyPackage() throws Exception {
        ApiResponse apiResponse = yandexTestsSteps.createResource("/v1/disk/resources?path=/testPack",null);
        Assertions.assertEquals(201,apiResponse.getStatusCode());
        apiResponse = yandexTestsSteps.copyResource("/v1/disk/resources/copy?from=/testPack&path=/testPackCopy",null);
        Assertions.assertTrue(apiResponse.getStatusCode()==201||apiResponse.getStatusCode()==202);
        apiResponse = yandexTestsSteps.deleteResource("/v1/disk/resources?path=/testPack");
        Assertions.assertTrue(apiResponse.getStatusCode()==200||apiResponse.getStatusCode()==202||apiResponse.getStatusCode()==204);
        apiResponse = yandexTestsSteps.deleteResource("/v1/disk/resources?path=/testPackCopy");
        Assertions.assertTrue(apiResponse.getStatusCode()==200||apiResponse.getStatusCode()==202||apiResponse.getStatusCode()==204);
    }
    @Test
    @Tag("Integration")
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Пытаемся копировать несуществующую папку")
    @DisplayName("Копирование несуществующей папки")
    void copyPackageNegative404() throws Exception {
        ApiResponse apiResponse = yandexTestsSteps.copyResource("/v1/disk/resources/copy?from=/testPack&path=/testPackCopy",null);
        Assertions.assertEquals(404,apiResponse.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({"/v1/test","/v1/v2","/v2/v4"})
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Передаем неверные эндпоинты")
    @DisplayName("Передача неверных эндпоинтов")
    void return404ForInvalidEndpoints(String endpoint)throws Exception{
        ApiResponse apiResponse = yandexTestsSteps.getDiskInfo(endpoint);
        Assertions.assertEquals(404,apiResponse.getStatusCode());
    }
    @ParameterizedTest
    @CsvSource({"/v1/disk/resources?path=/testPack","/v1/disk/resources?path=/testPack2"})
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Создаем две папки -testPack и testPack2")
    @DisplayName("Успешное создание папок")
    void createMorePackage(String endpoint) throws Exception{
        ApiResponse apiResponse = yandexTestsSteps.createResource(endpoint,null);
        Assertions.assertEquals(201,apiResponse.getStatusCode());
        apiResponse = yandexTestsSteps.deleteResource(endpoint);
        Assertions.assertTrue(apiResponse.getStatusCode()==200||apiResponse.getStatusCode()==202||apiResponse.getStatusCode()==204);
    }


}