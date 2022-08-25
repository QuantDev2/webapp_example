package com.example.webapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PartialBankAccountInformation {
    private String accountHolder;
    private String type;
}
