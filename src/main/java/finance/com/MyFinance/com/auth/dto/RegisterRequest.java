package finance.com.MyFinance.com.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @JsonProperty("fullName")
        @NotBlank String fullName,

        @Email
        @NotBlank
        String email,

        @Size(min = 6)
        String password
) {}