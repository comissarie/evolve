package org.vaargcapital.evolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.vaargcapital.evolve.repository.AppUserRepository;
import org.vaargcapital.evolve.repository.LoginHistoryRepository;
import org.vaargcapital.evolve.repository.MessageHistoryRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class EvolveApiTest {

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("app")
            .withUsername("app")
            .withPassword("app");

    private static final MockWebServer MOCK_WEB_SERVER = new MockWebServer();

    static {
        POSTGRES.start();
        try {
            MOCK_WEB_SERVER.start();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () -> String.format(
                "r2dbc:postgresql://%s:%d/%s",
                POSTGRES.getHost(),
                POSTGRES.getFirstMappedPort(),
                POSTGRES.getDatabaseName()
        ));
        registry.add("spring.r2dbc.username", POSTGRES::getUsername);
        registry.add("spring.r2dbc.password", POSTGRES::getPassword);
        registry.add("spring.flyway.url", POSTGRES::getJdbcUrl);
        registry.add("spring.flyway.user", POSTGRES::getUsername);
        registry.add("spring.flyway.password", POSTGRES::getPassword);
        registry.add("app.googleSheets.generalBaseUrl", () -> MOCK_WEB_SERVER.url("/general").toString());
        registry.add("app.googleSheets.disBaseUrl", () -> MOCK_WEB_SERVER.url("/punishment").toString());
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private MessageHistoryRepository messageHistoryRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @AfterAll
    static void cleanup() throws IOException {
        MOCK_WEB_SERVER.shutdown();
        POSTGRES.stop();
    }

    @Test
    void loginInsertsHistory() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/login")
                        .queryParam("ip", "127.0.0.1")
                        .queryParam("nickname", "Player_One")
                        .build())
                .exchange()
                .expectStatus().isOk();

        StepVerifier.create(loginHistoryRepository.count())
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void disCodeStoresMessageHistory() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/dis-code")
                        .queryParam("nickname", "PlayerTwo")
                        .queryParam("message", "Hello")
                        .queryParam("ip", "10.0.0.2")
                        .build())
                .exchange()
                .expectStatus().isOk();

        StepVerifier.create(messageHistoryRepository.count())
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void accessCallsGoogleAndUpdatesEmail() {
        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(200));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access")
                        .queryParam("grantor", "Leader")
                        .queryParam("grantee", "Member")
                        .queryParam("email", "member@example.com")
                        .queryParam("level", 2)
                        .build())
                .exchange()
                .expectStatus().isOk();

        StepVerifier.create(appUserRepository.findByNickname("Member"))
                .assertNext(user -> assertThat(user.getEmail()).isEqualTo("member@example.com"))
                .verifyComplete();
    }

    @Test
    void accessReturnsBadGatewayOnGoogleFailure() {
        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(500));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/access")
                        .queryParam("grantor", "Leader2")
                        .queryParam("grantee", "Member2")
                        .queryParam("email", "member2@example.com")
                        .queryParam("level", 2)
                        .build())
                .exchange()
                .expectStatus().isBadGateway();
    }

    @Test
    void punishmentReturnsBadGatewayOnGoogleFailure() {
        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(500));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/punishment")
                        .queryParam("submittedBy", "Moderator")
                        .queryParam("violator", "Offender")
                        .queryParam("ptype", 1)
                        .queryParam("violation", "Spam")
                        .build())
                .exchange()
                .expectStatus().isBadGateway();
    }

    @Test
    void punishmentReturnsOkOnGoogleSuccess() {
        MOCK_WEB_SERVER.enqueue(new MockResponse().setResponseCode(200));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/punishment")
                        .queryParam("submittedBy", "Moderator2")
                        .queryParam("violator", "Offender2")
                        .queryParam("ptype", 2)
                        .queryParam("violation", "Abuse")
                        .build())
                .exchange()
                .expectStatus().isOk();

        StepVerifier.create(appUserRepository.findByNickname("Offender2").flatMap(user -> Mono.just(user.getNickname())))
                .expectNext("Offender2")
                .verifyComplete();
    }
}
