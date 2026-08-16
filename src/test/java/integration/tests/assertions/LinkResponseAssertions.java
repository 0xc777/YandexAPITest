package integration.tests.assertions;

import integration.tests.dto.LinkResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class LinkResponseAssertions {

    public static void assertLinkResponse(LinkResponse response, String expectedPath) {
        String decodedHref = URLDecoder.decode(response.getHref(), StandardCharsets.UTF_8);

        assertThat(response.getMethod())
                .as("Метод должен быть GET")
                .isEqualTo("GET");

        assertThat(response.isTemplated())
                .as("Ссылка не должна быть шаблонной")
                .isFalse();

        assertThat(decodedHref)
                .as("Ссылка должна содержать путь к папке")
                .contains(expectedPath);
    }
}