package finance.com.MyFinance.com.transaction.dto;

import finance.com.MyFinance.com.transaction.TransactionType;

import java.time.Instant;

public record TransactionResponse(
        Long id,
        long amountCents,
        TransactionType type,
        String category,
        String description,
        Instant createdAt
) {}