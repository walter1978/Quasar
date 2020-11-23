package rebel.alliance.comm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rebel.alliance.comm.Constants;
import rebel.alliance.comm.service.LocationService;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.location.Distance;
import rebel.alliance.comm.service.model.location.ShipLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LocationServiceImpl implements LocationService {
    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);
    private Map<Sat, Float> distanceMap = new HashMap<>();
    private ShipLocation kenobiPos = new ShipLocation(-500, -200);
    private ShipLocation skywalkerPos = new ShipLocation(100, -100);
    private ShipLocation satoPos = new ShipLocation(500, 100);

    @Override
    public Optional<ShipLocation> getLocation(List<Distance> distanceList) {
        final Optional<Distance> distanceToKenobi = getDistance(distanceList, Sat.KENOBI);
        final Optional<Distance> distanceToSkywalker = getDistance(distanceList, Sat.SKYWALKER);
        final Optional<Distance> distanceToSato = getDistance(distanceList, Sat.SATO);

        if (isValidDistance(distanceToKenobi) && isValidDistance(distanceToSkywalker) && isValidDistance(distanceToSato)) {
            return Optional.ofNullable(calculateTrilateration(distanceToKenobi.get().getDistance(), distanceToSkywalker.get().getDistance(), distanceToSato.get().getDistance()));
        }

        return Optional.empty();
    }

    @Override
    public void updateDistance(Distance distance) {
        if (isValidDistance(Optional.ofNullable(distance))) {
            distanceMap.put(distance.getSat(), distance.getDistance());
        }
    }

    @Override
    public Optional<ShipLocation> getLocation() {
        if (distanceMap.size() == Constants.SATELLITE_COUNT) {
            final Optional<ShipLocation> shipLocationOpt = Optional.of(calculateTrilateration(distanceMap.get(Sat.KENOBI), distanceMap.get(Sat.SKYWALKER), distanceMap.get(Sat.SATO)));
            if (shipLocationOpt.isPresent()) {
                distanceMap.clear();
                return shipLocationOpt;
            }
        } else {
            logger.warn(String.format("There is not enough data to start the location calculation, distance count: %s", distanceMap.size()));
        }

        return Optional.empty();
    }

    private ShipLocation calculateTrilateration(float distanceToKenobi, float distanceToSkywalker, float distanceToSato) {
        try {
            // applies a common method used to calculate a position using Trilateration
            final double A = 2 * satoPos.getX() - 2 * kenobiPos.getX();
            final double B = 2 * satoPos.getY() - 2 * kenobiPos.getY();
            final double C = Math.pow(distanceToKenobi, 2) - Math.pow(distanceToSato, 2) - Math.pow(kenobiPos.getX(), 2) + Math.pow(satoPos.getX(), 2) - Math.pow(kenobiPos.getY(), 2) + Math.pow(satoPos.getY(), 2);
            final double D = 2 * skywalkerPos.getX() - 2 * satoPos.getX();
            final double E = 2 * skywalkerPos.getY() - 2 * satoPos.getY();
            final double F = Math.pow(distanceToSato, 2) - Math.pow(distanceToSkywalker, 2) - Math.pow(satoPos.getX(), 2) + Math.pow(skywalkerPos.getX(), 2) - Math.pow(satoPos.getY(), 2) + Math.pow(skywalkerPos.getY(), 2);

            final double x = (C * E - F * B) / (E * A - B * D);
            final double y = (C * D - A * F) / (B * D - A * E);

            return new ShipLocation(x, y);
        } catch (Exception e) {
            logger.error("Error calculating the location", e);
        }

        return null;
    }

    private Optional<Distance> getDistance(List<Distance> distanceList, Sat sat) {
        return distanceList.stream().filter(distance -> distance.getSat().equals(sat)).findFirst();
    }

    private boolean isValidDistance(Optional<Distance> distanceOpt) {
        return distanceOpt.isPresent() && distanceOpt.get().getDistance() >= 0;
    }
}
