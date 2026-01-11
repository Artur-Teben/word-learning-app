package com.artur.vocab.wordlearningapp.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWordRequest {

    @NotBlank(message = "Word must not be blank")
    @Size(max = 255, message = "Word is too long")
    private String text;

    @Size(max = 2000, message = "Context is too long")
    private String context;
}
