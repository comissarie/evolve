package org.vaargcapital.evolve.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.vaargcapital.evolve.api.EvolveApi;
import org.vaargcapital.evolve.service.EvolveService;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class EvolveController implements EvolveApi {

    private final EvolveService evolveService;

    public EvolveController(EvolveService evolveService) {
        this.evolveService = evolveService;
    }

    @Override
    public Mono<ResponseEntity<Void>> login(@NotBlank String ip, @NotBlank String nickname) {
        return evolveService.handleLogin(ip, nickname)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> disCode(@NotBlank String nickname,
                                              @NotBlank @Size(max = 1000) String message,
                                              @NotBlank String ip) {
        return evolveService.handleDisCode(nickname, message, ip)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> access(@NotBlank String grantor,
                                             @NotBlank String grantee,
                                             String email,
                                             @NotNull Integer level) {
        return evolveService.handleAccess(grantor, grantee, email, level)
                .thenReturn(ResponseEntity.ok().build());
    }

    @Override
    public Mono<ResponseEntity<Void>> punishment(@NotBlank String submittedBy,
                                                 @NotBlank String violator,
                                                 @NotNull Integer ptype,
                                                 @NotBlank String violation) {
        return evolveService.handlePunishment(submittedBy, violator, ptype, violation)
                .thenReturn(ResponseEntity.ok().build());
    }
}
