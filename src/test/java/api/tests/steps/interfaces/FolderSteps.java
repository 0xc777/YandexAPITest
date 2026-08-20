package api.tests.steps.interfaces;

import io.restassured.response.Response;

public interface FolderSteps {

    Response createFolder (String folderPath);
}
