package integration.helper;

import integration.models.ApiResponse;
import integration.utils.Config;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiHelper {

    private final HttpClient client;
    private final String baseUrl;
    private final String token;

    public ApiHelper() {

        client = HttpClient.newHttpClient();

        baseUrl = Config.BASE_URL;

        token = Config.TOKEN;
    }

    public ApiResponse get(String endpoint)
            throws IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(baseUrl + endpoint))
                .header("Authorization", "OAuth " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ApiResponse(response.statusCode(), response.body());
    }

    public ApiResponse post(String endpoint, String body) throws IOException, InterruptedException {

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()

                .uri(URI.create(baseUrl + endpoint))
                .header("Authorization", "OAuth " + token);
        if (body == null) {

            requestBuilder.POST(HttpRequest.BodyPublishers.noBody());

        } else {

            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(body));


        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ApiResponse(response.statusCode(), response.body());
    }

    public ApiResponse delete(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()

                .uri(URI.create(baseUrl + endpoint))
                .header("Authorization", "OAuth " + token)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());
    }

    public ApiResponse put(String endpoint, String body) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Authorization", "OAuth " + token);
        if (body == null) {

            requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());

        } else {

            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(body));


        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new ApiResponse(response.statusCode(), response.body());
    }



}