package services;

import com.google.inject.ImplementedBy;
import models.Session;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(DefaultSessionService.class)
public interface SessionService {

    /**
     * Return's list of all Sessions.
     * Filters the stream if filter-query is not null.
     * @return Stream of all Sessions nested in a CompletionStage
     */
    CompletionStage<Stream<Session>> get(String query);

    /**
     * Returns Session with given identifier.
     * @param id Session identifier
     * @return Session with given identifier or {@code null} nested in a CompletionStage
     */
    CompletionStage<Session> get(final Long id);

    /**
     * Adds the given Session.
     * @param session to add
     * @return added Session nested in a CompletionStage
     */
    CompletionStage<Session> add(final Session session);
    
}
