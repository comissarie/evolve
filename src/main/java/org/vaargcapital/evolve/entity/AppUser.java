package org.vaargcapital.evolve.entity;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("app_user")
public class AppUser {
    @Id
    private UUID id;
    private String nickname;
    private String email;
    private String vkId;

    public AppUser() {
    }

    public AppUser(UUID id, String nickname, String email, String vkId) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.vkId = vkId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVkId() {
        return vkId;
    }

    public void setVkId(String vkId) {
        this.vkId = vkId;
    }
}
