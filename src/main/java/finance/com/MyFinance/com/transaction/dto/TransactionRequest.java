package finance.com.MyFinance.com.transaction.dto;

import finance.com.MyFinance.com.transaction.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionRequest(
        @Min(1) long amountCents,
        @NotNull TransactionType type,
        @NotBlank String category,
        String description
) {}