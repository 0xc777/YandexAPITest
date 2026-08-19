package integration.tests.steps.interfaces;

import io.restassured.response.Response;

public interface PublishSteps {

    Response publishResource(String  path);
    Response getPublicLink(String path);
    Response searchResourceIsPublic(String publicUrl);
    String extractPublicUrl(Response response);
    Response deletePublish(String path);
}
