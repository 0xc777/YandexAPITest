package integration.steps;

import integration.helper.ApiHelper;
import integration.models.ApiResponse;
import org.junit.jupiter.api.*;
import io.qameta.allure.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class YandexTestsSteps {
    private final ApiHelper apiHelper = new ApiHelper();
    @Step("Отправка get")
    public ApiResponse getDiskInfo(String endpoint) throws Exception{
        ApiResponse apiResponse = apiHelper.get(endpoint);
        return apiResponse;
    }
    @Step("Создание")
    public ApiResponse createResource(String endpoint,String body)throws Exception {
        ApiResponse apiResponse = apiHelper.put(endpoint,body);
        return apiResponse;
    }
    @Step("Удаление")
    public ApiResponse deleteResource(String endpoint) throws Exception{
        ApiResponse apiResponse = apiHelper.delete(endpoint);
        return apiResponse;
    }
    @Step("Копирование")
    public ApiResponse copyResource(String endpoint,String body) throws Exception{
       ApiResponse apiResponse = apiHelper.post(endpoint,body);
       return  apiResponse;
    }

}
