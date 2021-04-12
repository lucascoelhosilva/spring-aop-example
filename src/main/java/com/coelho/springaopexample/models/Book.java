package com.coelho.springaopexample.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    @NotBlank
    private String name;
    @NotBlank
    private String description;

}
