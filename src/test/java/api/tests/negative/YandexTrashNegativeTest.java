package api.tests.negative;

import api.tests.assertions.ErrorConditions;
import api.tests.dto.ErrorResponse;
import api.tests.steps.assured.TrashStepsAssured;
import api.tests.steps.interfaces.TrashSteps;
import api.utils.StepsFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class YandexTrashNegativeTest {

    private final TrashSteps trashSteps = StepsFactory.getTrashSteps();

    @Test
    @Tag("negative")
    @Severity(SeverityLevel.NORMAL)
    @Description("Восстановление несуществующего файла 404")
    @DisplayName("Восстановление несуществующего файла ")
    @Story("Корзина")
    void return404RestoreNonExistentFile() {

        String fileName = "/non_existent_" + UUID.randomUUID() + ".txt";

        Response response = trashSteps.restoreFile(fileName);
        assertThat(response.getStatusCode()).isEqualTo(404);
        ErrorResponse errorResponse = response.as(ErrorResponse.class);
        assertThat(errorResponse).is(ErrorConditions.diskNotFound());
    }
}
