package com.arxcess.inventory.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Provider
public class ErrorMapper implements ExceptionMapper<Exception> {

    static final String STATIC_FILES = "./static/error-logs/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapper.class.getName());

    @Inject
    javax.inject.Provider<ContainerRequestContext> containerRequestContextProvider;

    private static void appendToFile(Exception systemException, CustomError error) {

        String fileName = error.getTime().toLocalDate() + "-log.txt";

        String message = """
                         Error Id: %s
                         Path: %s
                         Date: %s
                         Title: %s
                         Message: %s
                         """.formatted(error.getErrorId(), error.getPath(), error.getTime(), error.getTitle(), error.getMessage());

        Path targetLocation = Paths.get(STATIC_FILES);

        if (!Files.exists(targetLocation)) {
            try {
                Files.createDirectories(targetLocation);

            } catch (IOException ex) {
                LOGGER.error("Exception {}!", ex.getMessage());
            }
        }

        File logFile = new File(STATIC_FILES + fileName);

        try (FileWriter newFile = new FileWriter(logFile, true)) {

            newFile.write(message);
            BufferedWriter buffFile = new BufferedWriter(newFile);
            PrintWriter printFile = new PrintWriter(buffFile, true);
            systemException.printStackTrace(printFile);

        } catch (IOException e) {
            LOGGER.error("Exception {}!", e.getMessage());
        }

    }

    @Override
    public Response toResponse(Exception exception) {

        LOGGER.error("Failed to handle request. {}", exception.getMessage());

        CustomError error = new CustomError();
        error.setErrorId(UUID.randomUUID().toString());
        error.setTime(LocalDateTime.now());
        error.setMessage(exception.getMessage());
        error.setPath(containerRequestContextProvider.get().getUriInfo().getAbsolutePath().toString());

        int code = 500;

        if (exception instanceof WebApplicationException ex) {
            code = ex.getResponse().getStatus();
            error.setStatus(Response.Status.BAD_REQUEST);
            error.setTitle("Error processing request!");


        }

        if (exception instanceof NotFoundException ex) {
            code = ex.getResponse().getStatus();
            error.setStatus(Response.Status.NOT_FOUND);
            error.setTitle("Resource not found!");

        }

        appendToFile(exception, error);

        return Response.status(code)
                .entity(error)
                .build();
    }

}
