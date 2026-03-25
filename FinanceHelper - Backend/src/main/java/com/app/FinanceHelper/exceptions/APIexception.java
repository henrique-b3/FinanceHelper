package com.app.FinanceHelper.exceptions;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class APIexception extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public APIexception(String message) {
        super(message);
    }
}
