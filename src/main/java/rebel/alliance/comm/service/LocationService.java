package rebel.alliance.comm.service;

import rebel.alliance.comm.service.model.location.ShipLocation;
import rebel.alliance.comm.service.model.location.Distance;

import java.util.List;
import java.util.Optional;

/**
 * Service for location processing
 *
 */
public interface LocationService {
    /**
     * Calculates the position of a ship based on its distance to 3 satellites
     *
     * @param distanceList distance list
     * @return ship location
     */
    Optional<ShipLocation> getLocation(List<Distance> distanceList);

    /**
     * Updates the distance of a ship to a specific satellite
     *
     * @param distance distance to a specific satellite
     */
    void updateDistance(Distance distance);

    /**
     * Calculates the position of a ship based on its distance to 3 satellites
     * Uses the latest data collected (updateDistance method)
     *
     * @return ship location
     */
    Optional<ShipLocation> getLocation();

}
