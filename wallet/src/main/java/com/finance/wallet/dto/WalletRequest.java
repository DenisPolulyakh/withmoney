package com.finance.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WalletRequest<T> {
    private T body;
    private String entityOperationType;
}
