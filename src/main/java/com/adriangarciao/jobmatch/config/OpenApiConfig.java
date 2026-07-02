package com.adriangarciao.jobmatch.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Central OpenAPI definition: metadata, server list, JWT bearer scheme, and tag order.
 *
 * <p>All API documentation metadata lives here rather than being scattered across
 * controllers. Controllers only declare their {@code @Tag} group and per-operation
 * summaries; the {@code bearerAuth} scheme defined here is applied to the
 * authenticated controllers via {@code @SecurityRequirement("bearerAuth")}.
 *
 * <p>The spec (request/response schemas) is generated at runtime from the existing
 * DTOs and Jakarta validation annotations; no static snapshot is committed.
 */
@Configuration
public class OpenApiConfig {

    public static final String BEARER_SCHEME = "bearerAuth";

    /** Local backend; kept first so it is the default selection during development. */
    @Value("${app.openapi.local-url:http://localhost:8080}")
    private String localUrl;

    /** Deployed backend base URL. Override per environment via OPENAPI_PROD_URL. */
    @Value("${app.openapi.prod-url:}")
    private String prodUrl;

    /**
     * When true, the production server is listed first so Swagger UI selects it by
     * default. Set OPENAPI_PROD_SERVER_FIRST=true on Railway; stays false locally so
     * localhost remains the default for development.
     */
    @Value("${app.openapi.prod-server-first:false}")
    private boolean prodServerFirst;

    @Bean
    public OpenAPI jobMatchOpenAPI() {
        // Swagger UI defaults to the FIRST server in the list. In production we want
        // the https Railway URL first; in local dev we want localhost first.
        Server local = new Server().url(localUrl).description("Local development");
        Server prod = StringUtils.hasText(prodUrl)
                ? new Server().url(prodUrl).description("Production (Railway)")
                : null;

        List<Server> servers = new ArrayList<>();
        if (prodServerFirst && prod != null) {
            servers.add(prod);
            servers.add(local);
        } else {
            servers.add(local);
            if (prod != null) {
                servers.add(prod);
            }
        }

        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Paste the access token returned by /api/auth/login or /api/auth/register.");

        return new OpenAPI()
                .info(new Info()
                        .title("JobMatch API")
                        .version("v1")
                        .description("""
                                JobMatch scores how well a resume matches a job posting using a \
                                deterministic, weighted skill- and text-overlap engine (no external LLM).

                                **The public web demo uses a single endpoint** (`POST /api/ai/analyze`). \
                                Everything else here is a fully built and tested backend \
                                (authentication, resume storage, application tracking) that is \
                                intentionally not surfaced in the demo UI but is fully testable on this \
                                page via **Authorize**.

                                **To test a protected route:**
                                1. Call `POST /api/auth/register` (or `POST /api/auth/login` with the \
                                   seeded demo account) and copy the returned `token`.
                                2. Click **Authorize** (top right), paste the raw token — the `Bearer ` \
                                   prefix is added for you — and confirm the lock closes.
                                3. Call a protected endpoint (e.g. `GET /api/resumes` or `GET /api/me`).

                                A **demo account** is seeded on the deployed server; its credentials are \
                                documented in the project README (`How to test the API`).""")
                        .contact(new Contact()
                                .name("Adrian Garcia")
                                .url("https://github.com/adriangarciao"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://github.com/adriangarciao/JobMatch/blob/main/LICENSE")))
                .servers(servers)
                .components(new Components().addSecuritySchemes(BEARER_SCHEME, bearer))
                // Declared order controls how groups render in Swagger UI: public first.
                .tags(List.of(
                        new Tag().name("Analysis").description("Public resume vs. job posting analysis"),
                        new Tag().name("Auth").description("Registration, login, token refresh, and logout"),
                        new Tag().name("Resumes").description("Upload, parse, and manage resumes (requires JWT)"),
                        new Tag().name("Applications").description("Track job applications (requires JWT)"),
                        new Tag().name("Users").description("Current-user profile and account management (requires JWT)"),
                        new Tag().name("Admin").description("Admin-only operations (requires ADMIN role)")));
    }
}
