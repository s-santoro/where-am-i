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
public class LocationRepository {

    private final JPAApi jpaApi;

    @Inject
    public LocationRepository(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public CompletionStage<Location> add(Location location) {

        return supplyAsync(() -> wrap(em -> insert(em, location)));
    }

    public CompletionStage<Stream<Location>> findAll() {

        return supplyAsync(() -> wrap(em -> findAll(em)));
    }

    public CompletionStage<Location> find(Long id) {

        return supplyAsync(() -> wrap(em -> find(em, id)));
    }

    private <T> T wrap(Function<EntityManager, T> function) {

        return jpaApi.withTransaction(function);
    }

    private Location insert(EntityManager em, Location location) {
        em.persist(location);
        return location;
    }

    private Stream<Location> findAll(EntityManager em) {
        // get ResultList from returned Query
        List<Location> locations = em.createQuery("SELECT l FROM locations l", Location.class).getResultList();
        // Return ResultList as continuous Stream (asynchronous implementation needs streams)
        return locations.stream();
    }

    private Location find(EntityManager em, Long id) {
        return em.find(Location.class, id);
    }
}
