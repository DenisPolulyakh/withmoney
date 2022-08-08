package com.finance.wallet.dto.enums;

public enum EntityOperationType {
    CREATE("Создать"),
    SAVE("Изменить"),
    DELETE("Удалить");

    private String description;

    EntityOperationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
