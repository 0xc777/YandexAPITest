package api.tests.positive;

import api.tests.steps.assured.TrashStepsAssured;
import api.tests.steps.interfaces.FileSteps;
import api.tests.steps.interfaces.FolderSteps;
import api.tests.steps.interfaces.ResourceSteps;
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
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import java.io.IOException;
import java.util.UUID;

public class YandexTrashPositiveTest {

    private final TrashSteps trashSteps = new TrashStepsAssured();
    private final FileSteps fileSteps = StepsFactory.getFileSteps();
    private final FolderSteps folderSteps = StepsFactory.getFolderSteps();
    private final ResourceSteps resourceSteps = StepsFactory.getResourceSteps();

    @Test
    @Tag("positive")
    @Severity(SeverityLevel.NORMAL)
    @Description("Восстановление удаленного файла")
    @DisplayName("Восстановление удаленного файла")
    @Story("Корзина")
    void recoveryFile() throws IOException {
        String folderPath = "/folderTestTrash" + UUID.randomUUID();
        String fileName = "TrashTest"+UUID.randomUUID();
        String filePath = folderPath+"/"+fileName;
        String trashPath = null;

        try{
            Response createFResponse = folderSteps.createFolder(folderPath);
            assertThat(createFResponse.statusCode()).isEqualTo(201);
            fileSteps.uploadFile(filePath,"Hello, TRASH!".getBytes());
            Response searchResponse =fileSteps.searchFile(filePath);
            assertThat(searchResponse.statusCode()).isEqualTo(200);
            trashSteps.deleteFileOnTrash(filePath);
            Response resourceListOnTrash = trashSteps.resourceListOnTrash();
            trashPath = trashSteps.searchFileOnTrash(resourceListOnTrash,filePath);
            trashSteps.restoreFile(trashPath);
            Response searchRestoreResponse =fileSteps.searchFile(filePath);
            assertThat(searchRestoreResponse.statusCode()).isEqualTo(200);

        }
        finally {
            resourceSteps.deleteResource(filePath);
            resourceSteps.deleteResource(folderPath);
        }
    }
}
