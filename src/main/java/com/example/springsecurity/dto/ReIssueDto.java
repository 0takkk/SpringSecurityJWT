package com.example.springsecurity.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReIssueDto {

    private String name;
    private String refreshToken;
}
