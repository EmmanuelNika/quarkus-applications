package com.arxcess.inventory.handlers;

import javax.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.util.List;

public class CustomError {

    private String errorId;

    private LocalDateTime time;

    private Status status;

    private List<ErrorMessage> errors;

    public String getErrorId() {

        return errorId;
    }

    public void setErrorId(String errorId) {

        this.errorId = errorId;
    }

    public LocalDateTime getTime() {

        return time;
    }

    public void setTime(LocalDateTime time) {

        this.time = time;
    }

    public Status getStatus() {

        return this.status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public List<ErrorMessage> getErrors() {

        return errors;
    }

    public void setErrors(List<ErrorMessage> errors) {

        this.errors = errors;
    }

}
