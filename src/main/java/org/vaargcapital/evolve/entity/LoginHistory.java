package org.vaargcapital.evolve.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("login_history")
public class LoginHistory {
    @Id
    private UUID id;
    private String nickname;
    private String ip;
    private LocalDateTime ts;
    private Boolean success;

    public LoginHistory() {
    }

    public LoginHistory(UUID id, String nickname, String ip, LocalDateTime ts, Boolean success) {
        this.id = id;
        this.nickname = nickname;
        this.ip = ip;
        this.ts = ts;
        this.success = success;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
