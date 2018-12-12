package services;

import com.google.inject.Inject;
import models.Location;
import models.LocationRepository;
import models.SessionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public class DefaultLocationService implements LocationService {

    private final List<Location> locations;
    private final LocationRepository locationRepository;

    @Inject
    public DefaultLocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
        locations = new ArrayList<>();
    }

    /**
     * Return's Stream of all Locations.
     *
     * @return Stream of all Locations nested in a CompletionStage
     */
    @Override
    public CompletionStage<Stream<Location>> get() {
        return locationRepository.findAll();
    }

    /**
     * Returns Location with given identifier.
     *
     * @param id Location identifier
     * @return Location with given identifier or {@code null} nested in a CompletionStage
     */
    @Override
    public CompletionStage<Location> get(Long id) {
        return locationRepository.find(id);
    }

    /**
     * Adds the given Location.
     *
     * @param location to add
     * @return added Location nested in a CompletionStage
     */
    @Override
    public CompletionStage<Location> add(Location location) {
        return locationRepository.add(location);
    }
}
