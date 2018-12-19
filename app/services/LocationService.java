package services;

import com.google.inject.ImplementedBy;
import models.Location;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(DefaultLocationService.class)
public interface LocationService {

    /**
     * Returns Stream of all Locations.
     * @return Stream of all Locations nested in a CompletionStage
     */
    CompletionStage<Stream<Location>> get();

    /**
     * Returns Location with given identifier.
     * @param id Location identifier
     * @return Location with given identifier or {@code null} nested in a CompletionStage
     */
    CompletionStage<Location> get(final Long id);

    /**
     * Adds the given Location.
     * @param location to add
     * @return added Location nested in a CompletionStage
     */
    CompletionStage<Location> add(final Location location);
}
