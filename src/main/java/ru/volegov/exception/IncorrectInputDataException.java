package ru.volegov.exception;

import java.sql.SQLException;

public class IncorrectInputDataException extends SQLException {
    public IncorrectInputDataException(String reason) {
        super(reason);
    }
}
