package org.vaargcapital.evolve.service;

import java.net.URI;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.vaargcapital.evolve.config.GoogleSheetsProperties;
import org.vaargcapital.evolve.entity.LoginHistory;
import org.vaargcapital.evolve.entity.MessageHistory;
import org.vaargcapital.evolve.repository.AppUserRepository;
import org.vaargcapital.evolve.repository.IpAddressRepository;
import org.vaargcapital.evolve.repository.LoginHistoryRepository;
import org.vaargcapital.evolve.repository.MessageHistoryRepository;
import org.vaargcapital.evolve.repository.UserIpRepository;
import reactor.core.publisher.Mono;

@Service
public class EvolveService {

    private final AppUserRepository appUserRepository;
    private final IpAddressRepository ipAddressRepository;
    private final UserIpRepository userIpRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final MessageHistoryRepository messageHistoryRepository;
    private final WebClient webClient;
    private final GoogleSheetsProperties googleSheetsProperties;

    public EvolveService(
            AppUserRepository appUserRepository,
            IpAddressRepository ipAddressRepository,
            UserIpRepository userIpRepository,
            LoginHistoryRepository loginHistoryRepository,
            MessageHistoryRepository messageHistoryRepository,
            WebClient webClient,
            GoogleSheetsProperties googleSheetsProperties
    ) {
        this.appUserRepository = appUserRepository;
        this.ipAddressRepository = ipAddressRepository;
        this.userIpRepository = userIpRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.messageHistoryRepository = messageHistoryRepository;
        this.webClient = webClient;
        this.googleSheetsProperties = googleSheetsProperties;
    }

    public Mono<Void> handleLogin(String ip, String nickname) {
        return appUserRepository.insertIfNotExists(nickname)
                .then(ipAddressRepository.insertIfNotExists(ip))
                .then(Mono.zip(
                        appUserRepository.findByNickname(nickname),
                        ipAddressRepository.findByIp(ip)
                ))
                .flatMap(tuple -> userIpRepository.insertUserIpIfNotExists(tuple.getT1().getId(), tuple.getT2().getId()))
                .then(loginHistoryRepository.save(new LoginHistory(null, nickname, ip, LocalDateTime.now(), true)))
                .then();
    }

    public Mono<Void> handleDisCode(String nickname, String message, String ip) {
        return messageHistoryRepository.save(new MessageHistory(null, nickname, ip, message, LocalDateTime.now()))
                .then();
    }

    public Mono<Void> handleAccess(String grantor, String grantee, String email, Integer level) {
        Mono<Void> upsertUsers = appUserRepository.insertIfNotExists(grantor)
                .then(appUserRepository.insertIfNotExists(grantee));

        Mono<Void> updateEmail = StringUtils.hasText(email)
                ? appUserRepository.updateEmailByNickname(grantee, email).then()
                : Mono.empty();

        String emailValue = StringUtils.hasText(email) ? email : "";
        URI uri = UriComponentsBuilder.fromUriString(googleSheetsProperties.generalBaseUrl())
                .queryParam("action", "access_add")
                .queryParam("grantor", grantor)
                .queryParam("grantee", grantee)
                .queryParam("email", emailValue)
                .queryParam("level", level)
                .build(true)
                .toUri();

        Mono<Void> call = webClient.get()
                .uri(uri)
                .exchangeToMono(response -> response.statusCode().is2xxSuccessful()
                        ? Mono.empty()
                        : Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Google Sheets error")));

        return upsertUsers.then(updateEmail).then(call);
    }

    public Mono<Void> handlePunishment(String submittedBy, String violator, Integer ptype, String violation) {
        Mono<Void> upsertUsers = appUserRepository.insertIfNotExists(submittedBy)
                .then(appUserRepository.insertIfNotExists(violator));

        URI uri = UriComponentsBuilder.fromUriString(googleSheetsProperties.disBaseUrl())
                .queryParam("action", "punishment")
                .queryParam("submittedBy", submittedBy)
                .queryParam("violator", violator)
                .queryParam("ptype", ptype)
                .queryParam("violation", violation)
                .build(true)
                .toUri();

        Mono<Void> call = webClient.get()
                .uri(uri)
                .exchangeToMono(response -> response.statusCode().is2xxSuccessful()
                        ? Mono.empty()
                        : Mono.error(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Google Sheets error")));

        return upsertUsers.then(call);
    }
}
