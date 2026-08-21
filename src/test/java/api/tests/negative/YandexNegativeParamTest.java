package api.tests.negative;

import api.tests.assertions.ErrorConditions;
import api.tests.dto.ErrorResponse;
import api.tests.steps.interfaces.FileSteps;
import api.tests.steps.interfaces.ResourceSteps;
import api.utils.StepsFactory;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;
import java.util.stream.Stream;

import static api.constants.Endpoints.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YandexNegativeParamTest {

    private final FileSteps fileSteps = StepsFactory.getFileSteps();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceSteps();

    @ParameterizedTest(name = "[{index}] overwrite=''{0}'' → 400")
    @ValueSource(strings = {"yes", "no", "truee", "falsee", "-1", "+0","1-","0+","+1","-0"," "," true"," false"})
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Невалидные значения overwrite возвращают 400")
    @Story("Файлы")
    void return400ForInvalidOverwrite(String overwrite) {

        String path = "/test_file_" + UUID.randomUUID() + ".txt";

        Response overResponse = fileSteps.sendOverwrite(overwrite,path);
        assertThat(overResponse.statusCode()).isEqualTo(400);
        assertThat(overResponse.as(ErrorResponse.class)).is(ErrorConditions.FieldValidationError());
    }

    @ParameterizedTest(name = "[{index}] {0} {1} → 405")
    @MethodSource("dataFor405Test")
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Неподдерживаемый метод  405")
    @Story("Ресурсы")
    void methodsFor405(String endpoint, String method) {
        Response endpointResponse = resourceSteps.sendEndpoint(endpoint,method);
        assertThat(endpointResponse.statusCode()).isEqualTo(405);
        assertThat(endpointResponse.as(ErrorResponse.class)).is(ErrorConditions.MethodNotAllowedError());
    }
    static Stream<Arguments> dataFor405Test() {
        return Stream.of(
                Arguments.of(RESOURCES, "POST"),
                Arguments.of(DISK, "PUT"),
                Arguments.of(DISK, "DELETE"),
                Arguments.of(UPLOAD, "DELETE")
        );
    }

}
