package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import models.Session;
import play.Logger;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Result;
import services.SessionService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Controller for handling session-routes.
 *
 * Implemented methods are:
 *      get all sessions
 *      get session with given id
 *      create new session
 */
public class SessionController extends Controller {

    private final SessionService sessionService;
    private final HttpExecutionContext ec;

    @Inject
    public SessionController(SessionService sessionService, HttpExecutionContext ec) {
        this.ec = ec;
        this.sessionService = sessionService;
    }

    @SuppressWarnings("Duplicates")
    public CompletionStage<Result> getSessions(String query) {
        return sessionService.get(query).thenApplyAsync(sessionStream -> {
            if(sessionStream == null) {
                Logger.error("Error occurred at {}",
                    Thread.currentThread().getStackTrace()[1]);
                return status(404, "Error: Resource not found!");
            }
            return ok(Json.toJson(sessionStream.collect(Collectors.toList())));
        }, ec.current());
    }

    public CompletionStage<Result> getSession(long id) {
        return sessionService.get(id).thenApplyAsync(session -> {
            if (session == null) {
                Logger.error("Error occurred at {}",
                        Thread.currentThread().getStackTrace()[1]);
                return status(404, "Error: Resource not found");
            }
            return ok(Json.toJson(session));
        });
    }

    public CompletionStage<Result> createNewSession() {
        final JsonNode json = Json.toJson(request().body().asJson());
        if (json.isNull() || !validateSession(json)) {
            CompletableFuture.supplyAsync(() -> {
                Logger.error("Error occurred at {}",
                        Thread.currentThread().getStackTrace()[1]);
                return status(404, "Error: Resource not found!");
            });
        }
        Session sessionToPersist = Json.fromJson(json, Session.class);
        return sessionService.add(sessionToPersist)
                .thenApplyAsync(session -> ok(Json.toJson(session)), ec.current());
    }

    /**
     * Validate session if fields are set correct.
     * @param json with session-parameters
     * @return {@code true} if validation successfull, otherwise {@code false}
     */
    // TODO: update class-comments if implemented
    private boolean validateSession(JsonNode json) {
        // timestamp-format: "2016-11-16"

        return true;
    }
}
