package com.arxcess.inventory.handlers;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CustomErrorHandler {

    private CustomErrorHandler() {

    }

    public static Response notFound(String itemNotFound) {

        List<ErrorMessage> messages = new ArrayList<>();
        ErrorMessage message = new ErrorMessage();
        message.setMessage(itemNotFound);
        message.setSolution("Verify item requested for is valid");

        messages.add(message);

        CustomError customError = new CustomError();
        customError.setErrorId(UUID.randomUUID()
                .toString());
        customError.setTime(LocalDateTime.now());
        customError.setStatus(Status.NOT_FOUND);
        customError.setErrors(messages);

        return Response.status(customError.getStatus()).entity(customError).build();

    }

}
