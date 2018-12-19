package services;

import com.google.inject.Inject;
import models.Session;
import models.SessionRepository;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * Implementation of SessionService-interface.
 */
public class DefaultSessionService implements SessionService {

    private final SessionRepository sessionRepository;

    @Inject
    public DefaultSessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Returns Stream of all Sessions.
     * Filters the stream if filter-query is not null.
     * @return Stream of all Sessions nested in a CompletionStage
     */
    @Override
    public CompletionStage<Stream<Session>> get(String query) {
        CompletionStage<Stream<Session>> sessions = sessionRepository.findAll();
        if(query == null) {
            return sessions;
        }
        return sessions.thenApplyAsync(sessionStream -> sessionStream.filter(session -> {
            return query.equalsIgnoreCase(String.valueOf(session.getUser_fk()));
        }));
    }

    /**
     * Returns Session with given identifier.
     *
     * @param id Session identifier
     * @return Session with given identifier or {@code null} nested in a CompletionStage
     */
    @Override
    public CompletionStage<Session> get(Long id) {
        return sessionRepository.find(id);
    }

    /**
     * Adds the given Session.
     *
     * @param session to add
     * @return added Session nested in a CompletionStage
     */
    @Override
    public CompletionStage<Session> add(Session session) {
        return sessionRepository.add(session);
    }
}
