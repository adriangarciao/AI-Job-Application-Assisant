package com.adriangarciao.jobmatch.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the OpenAPI definition: the bearer scheme is registered and the server
 * ordering (which drives Swagger's default "Try it out" target) flips based on the
 * {@code prod-server-first} flag — local first for dev, https prod first when deployed.
 */
class OpenApiConfigTest {

    private static final String LOCAL = "http://localhost:8080";
    private static final String PROD = "https://job-match.up.railway.app";

    private OpenAPI build(String prodUrl, boolean prodServerFirst) {
        OpenApiConfig config = new OpenApiConfig();
        ReflectionTestUtils.setField(config, "localUrl", LOCAL);
        ReflectionTestUtils.setField(config, "prodUrl", prodUrl);
        ReflectionTestUtils.setField(config, "prodServerFirst", prodServerFirst);
        return config.jobMatchOpenAPI();
    }

    @Test
    void localFirst_whenProdServerFirstDisabled() {
        List<Server> servers = build(PROD, false).getServers();
        assertThat(servers).extracting(Server::getUrl).containsExactly(LOCAL, PROD);
    }

    @Test
    void prodFirst_whenProdServerFirstEnabled() {
        List<Server> servers = build(PROD, true).getServers();
        assertThat(servers).extracting(Server::getUrl).containsExactly(PROD, LOCAL);
        // Swagger UI selects the first server by default; on the deployed page that is https prod.
        assertThat(servers.get(0).getUrl()).startsWith("https://");
    }

    @Test
    void onlyLocal_whenNoProdUrlConfigured() {
        List<Server> servers = build("", true).getServers();
        assertThat(servers).extracting(Server::getUrl).containsExactly(LOCAL);
    }

    @Test
    void registersBearerJwtSchemeAndPublicFirstTags() {
        OpenAPI api = build(PROD, false);

        var bearer = api.getComponents().getSecuritySchemes().get(OpenApiConfig.BEARER_SCHEME);
        assertThat(bearer).isNotNull();
        assertThat(bearer.getScheme()).isEqualTo("bearer");
        assertThat(bearer.getBearerFormat()).isEqualTo("JWT");

        // Public, recruiter-facing groups lead the tag order.
        assertThat(api.getTags()).extracting("name")
                .containsSubsequence("Analysis", "Auth");
    }
}
