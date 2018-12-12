package models;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This class interacts with the Postgresql-Database by utilizing JPA and EntityManager API.
 * It creates another layer(repository-pattern) for accessing the database.
 */
public class SessionRepository {

    private final JPAApi jpaApi;

    @Inject
    public SessionRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public CompletionStage<Session> add(Session session) {

        return supplyAsync(() -> wrap(em -> insert(em, session)));
    }

    public CompletionStage<Stream<Session>> findAll() {

        return supplyAsync(() -> wrap(em -> findAll(em)));
    }

    public CompletionStage<Session> find(Long id) {

        return supplyAsync(() -> wrap(em -> find(em, id)));
    }

    private <T> T wrap(Function<EntityManager, T> function) {

        return jpaApi.withTransaction(function);
    }

    private Session insert(EntityManager em, Session session) {
        em.persist(session);
        return session;
    }

    // UserRepository needs access to this function, in order to delete user with all references.
    protected Session delete(EntityManager em, Long id) {
        Session sessionToDelete = find(em, id);
        em.remove(sessionToDelete);
        return sessionToDelete;
    }

    // UserRepository needs access to this function, in order to delete user with all references.
    protected Stream<Session> findAll(EntityManager em) {
        // get ResultList from returned Query
        List<Session> Sessions = em.createQuery("SELECT s FROM sessions s", Session.class).getResultList();
        // Return ResultList as continuous Stream (asynchronous implementation needs streams)
        return Sessions.stream();
    }

    private Session find(EntityManager em, Long id) {
        return em.find(Session.class, id);
    }
}
