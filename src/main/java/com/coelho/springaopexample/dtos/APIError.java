package com.coelho.springaopexample.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class APIError {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);

    @Builder.Default
    private Collection<APIErrorMessage> messages = List.of();

}