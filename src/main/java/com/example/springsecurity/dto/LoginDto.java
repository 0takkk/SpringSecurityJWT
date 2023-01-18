package com.example.springsecurity.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
