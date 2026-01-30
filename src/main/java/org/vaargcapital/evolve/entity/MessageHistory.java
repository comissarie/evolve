package org.vaargcapital.evolve.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("message_history")
public class MessageHistory {
    @Id
    private UUID id;
    private String nickname;
    private String ip;
    private String message;
    private LocalDateTime ts;

    public MessageHistory() {
    }

    public MessageHistory(UUID id, String nickname, String ip, String message, LocalDateTime ts) {
        this.id = id;
        this.nickname = nickname;
        this.ip = ip;
        this.message = message;
        this.ts = ts;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTs() {
        return ts;
    }

    public void setTs(LocalDateTime ts) {
        this.ts = ts;
    }
}
