package api.mocks.WireMock;


import api.mocks.MockManager;
import com.github.tomakehurst.wiremock.WireMockServer;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockManager implements MockManager {
    private WireMockServer server;

    @Override
    public void start() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.start();
        com.github.tomakehurst.wiremock.client.WireMock.configureFor("localhost", server.port());
    }

    @Override
    public void stop() {
        if (server != null) server.stop();
    }

    @Override
    public int getPort() {
        return server.port();
    }

    @Override
    public void registerStubs() {

        stubFor(put(urlPathEqualTo("/v1/disk/resources"))
                .withQueryParam("path", matching(".+"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"href\":\"https://disk.example.com/folder\"}")));

        stubFor(put(urlPathEqualTo("/v1/disk/resources"))
                .withQueryParam("path", equalTo(""))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"FieldValidationError\",\"message\":\"Field validation error\"}")));

        stubFor(get(urlPathEqualTo("/v1/disk/resources"))
                .withQueryParam("path", matching(".+"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"DiskNotFoundError\",\"message\":\"Resource not found\"}")));

        stubFor(delete(urlPathEqualTo("/v1/disk/resources"))
                .withQueryParam("path", matching(".+"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"DiskNotFoundError\",\"message\":\"Resource not found\"}")));

        stubFor(post(urlPathEqualTo("/v1/disk/resources/copy"))
                .withQueryParam("from", matching(".+"))
                .withQueryParam("path", matching(".+"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\":\"DiskNotFoundError\",\"message\":\"Resource not found\"}")));
    }

    @Override
    public void reset() {
        if (server != null) server.resetMappings();
    }
}