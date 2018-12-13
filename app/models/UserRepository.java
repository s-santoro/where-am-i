package models;

import com.google.inject.Inject;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * This class interacts with the Postgresql-Database by utilizing JPA and EntityManager API.
 * It creates another layer(repository-pattern) for accessing the database.
 */
public class UserRepository {

    private final JPAApi jpaApi;
    private final SessionRepository sessionRepository;

    @Inject
    public UserRepository(JPAApi jpaApi, SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.jpaApi = jpaApi;
    }

    public CompletionStage<User> add(User user) {

        return supplyAsync(() -> wrap(em -> insert(em, user)));
    }

    public CompletionStage<Stream<User>> findAll() {

        return supplyAsync(() -> wrap(em -> findAll(em)));
    }

    public CompletionStage<User> find(Long id) {

        return supplyAsync(() -> wrap(em -> find(em, id)));
    }

    public CompletionStage<User> findByName(String name) {

        return supplyAsync(() -> wrap(em -> find(em, name)));
    }

    public CompletionStage<User> update(User user) {

        return supplyAsync(() -> wrap(em -> update(em, user)));
    }

    public CompletionStage<User> delete(Long id) {

        return supplyAsync(() -> wrap(em -> delete(em, id)));
    }

    private <T> T wrap(Function<EntityManager, T> function) {

        return jpaApi.withTransaction(function);
    }

    private User insert(EntityManager em, User user) {
        em.persist(user);
        return user;
    }

    private User update(EntityManager em, User user) {
        em.merge(user);
        return user;
    }

    /**
     * Deletes user with given id.
     * First all sessions with a reference to this user have to be deleted
     * in order to delete the user (or else Postgres-Exception fires).
     */
    private User delete(EntityManager em, Long id) {
        User userToDelete = find(em, id);
        Stream<Session> sessions = sessionRepository.findAll(em);
        Stream<Session> sessionsFiltered = sessions.filter(session -> session.getUser_fk() == id);
        sessionsFiltered.forEach(session -> sessionRepository.delete(em, session.getId()));
        em.remove(userToDelete);
        return userToDelete;
    }

    private Stream<User> findAll(EntityManager em) {
        // get ResultList from returned Query
        List<User> Users = em.createQuery("SELECT u FROM users u", User.class).getResultList();
        // Return ResultList as continuous Stream (asynchronous implementation needs streams)
        return Users.stream();
    }

    private User find(EntityManager em, Long id) {
        return em.find(User.class, id);
    }

    private User find(EntityManager em, String name) {
        String query = "SELECT u FROM users u WHERE username=\'" + name + "\'";
        List<User> users = em.createQuery(query, User.class).getResultList();
        return users.get(0) != null ? users.get(0) : null;
    }
}
