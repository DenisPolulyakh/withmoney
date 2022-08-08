package com.finance.wallet.dto.enums;

public enum OperationType {
    DEBIT("Приход"),
    CREDIT("Расход"),
    RESERVE("Резерв");

    private String description;

    OperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
