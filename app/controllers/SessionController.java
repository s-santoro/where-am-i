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
 *   getSessions(Query):    get all sessions (filtering by query possible)
 *   getSession(ID):        get session with given id
 *   createNewSession():    create new session
 *   validateSession(Json): validate json of session (no logic implemented)
 */
public class SessionController extends Controller {

    private final SessionService sessionService;
    private final HttpExecutionContext ec;
    private final CookieChecker cc;

    @Inject
    public SessionController(SessionService sessionService,
            CookieChecker cc,
            HttpExecutionContext ec) {
        this.ec = ec;
        this.cc = cc;
        this.sessionService = sessionService;
    }

    @SuppressWarnings("Duplicates")
    public CompletionStage<Result> getSessions(String query) {
        if (cc.checkCookie(request().cookie("uname")))
        {
            return sessionService.get(query).thenApplyAsync(sessionStream -> {
                if (sessionStream == null)
                {
                    Logger.error("Error occurred at {}", Thread.currentThread().getStackTrace()[1]);
                    return status(404, "Error: Resource not found!");
                }
                return ok(Json.toJson(sessionStream.collect(Collectors.toList())));
            }, ec.current());
        }
        else
        {
            return CompletableFuture.supplyAsync(() -> forbidden());
        }
    }

    public CompletionStage<Result> getSession(long id) {
        if (cc.checkCookie(request().cookie("uname")))
        {
            return sessionService.get(id).thenApplyAsync(session -> {
                if (session == null)
                {
                    Logger.error("Error occurred at {}", Thread.currentThread().getStackTrace()[1]);
                    return status(404, "Error: Resource not found");
                }
                return ok(Json.toJson(session));
            });
        }
        else
        {
            return CompletableFuture.supplyAsync(() -> forbidden());
        }
    }

    public CompletionStage<Result> createNewSession() {
        if (cc.checkCookie(request().cookie("uname")))
        {
            final JsonNode json = Json.toJson(request().body().asJson());
            if (json.isNull() || !validateSession(json))
            {
                CompletableFuture.supplyAsync(() -> {
                    Logger.error("Error occurred at {}", Thread.currentThread().getStackTrace()[1]);
                    return status(404, "Error: Resource not found!");
                });
            }
            Session sessionToPersist = Json.fromJson(json, Session.class);
            return sessionService.add(sessionToPersist)
                    .thenApplyAsync(session -> ok(Json.toJson(session)), ec.current());
        }
        else
        {
            return CompletableFuture.supplyAsync(() -> forbidden());
        }
    }

    /**
     * Validate session if fields are set correct.
     * @param json with session-parameters
     * @return {@code true} if validation successfull, otherwise {@code false}
     */
    private boolean validateSession(JsonNode json) {
        // timestamp-format: "2016-11-16"

        return true;
    }
}
