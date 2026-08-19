package integration.tests.steps.interfaces;

import io.restassured.response.Response;

public interface TrashSteps {
    Response restoreFile( String path);
}
