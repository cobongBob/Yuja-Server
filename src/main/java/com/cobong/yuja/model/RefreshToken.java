package com.cobong.yuja.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.cobong.yuja.model.audit.DateAudit;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class RefreshToken extends DateAudit {

    @Id
    private Long userId;
    
    private String refreshToken;

    public RefreshToken updateValue(String token) {
        this.refreshToken = token;
        return this;
    }

    @Builder
    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
