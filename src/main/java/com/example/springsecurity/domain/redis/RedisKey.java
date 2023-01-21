package com.example.springsecurity.domain.redis;

import lombok.Getter;

@Getter
public enum RedisKey {
    REFRESH_TOKEN("REFRESH_TOKEN_");

    private String key;

    RedisKey(String key){
        this.key = key;
    }
}
