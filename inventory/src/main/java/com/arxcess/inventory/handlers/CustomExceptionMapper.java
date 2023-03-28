package com.arxcess.inventory.handlers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        List<ErrorMessage> messages = new ArrayList<>();
        ErrorMessage message = new ErrorMessage();
        message.setMessage(exception.getMessage());
        message.setSolution(exception.getLocalizedMessage());

        messages.add(message);

        CustomError customError = new CustomError();
        customError.setErrorId(UUID.randomUUID()
                .toString());
        customError.setTime(LocalDateTime.now());
        customError.setStatus(Status.NOT_FOUND);
        customError.setErrors(messages);

        // Modify the error response here
        return Response.status(customError.getStatus())
                .entity(customError)
                .build();
    }

}
