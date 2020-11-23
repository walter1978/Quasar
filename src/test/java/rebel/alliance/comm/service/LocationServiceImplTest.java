package rebel.alliance.comm.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import rebel.alliance.comm.service.impl.LocationServiceImpl;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.location.Distance;
import rebel.alliance.comm.service.model.location.ShipLocation;

import java.util.Arrays;
import java.util.Optional;

public class LocationServiceImplTest {
    private LocationService subject;

    @BeforeEach
    public void setup() {
        subject = new LocationServiceImpl();
    }

    @Test
    public void getLocation_validData_locationIsCalculatedWithSuccess() {
        final Distance distanceToKenobi = new Distance(Sat.KENOBI, 845);
        final Distance distanceToSkywalker = new Distance(Sat.SKYWALKER, 500);
        final Distance distanceToSato = new Distance(Sat.SATO, 500);

        final Optional<ShipLocation> positionOpt = subject.getLocation(Arrays.asList(distanceToKenobi, distanceToSkywalker, distanceToSato));
        final ShipLocation shipLocation = positionOpt.get();

        Assert.isTrue(92.53125 ==  shipLocation.getX(), "invalid x position");
        Assert.isTrue(414.9375 == shipLocation.getY(), "invalid y position");
    }

    @Test
    public void getLocation_validData_locationIsCalculatedWithSuccess2() {
        final Distance distanceToKenobi = new Distance(Sat.KENOBI, 570);
        final Distance distanceToSkywalker = new Distance(Sat.SKYWALKER, 120);
        final Distance distanceToSato = new Distance(Sat.SATO, 500);

        final Optional<ShipLocation> positionOpt = subject.getLocation(Arrays.asList(distanceToKenobi, distanceToSkywalker, distanceToSato));
        final ShipLocation shipLocation = positionOpt.get();

        Assert.isTrue(47.875 == shipLocation.getX(), "invalid x position");
        Assert.isTrue(-84.75 == shipLocation.getY(), "invalid y position");
    }

    @Test
    public void getLocation_missingDataFromTwoSatellites_locationCanNoBeCalculated() {
        final Distance distanceToKenobi = new Distance(Sat.KENOBI, 570);

        final Optional<ShipLocation> positionOpt = subject.getLocation(Arrays.asList(distanceToKenobi));

        Assert.isTrue(!positionOpt.isPresent(), "position should not be returned, there is not enough data to calculate it");
    }

    @Test
    public void getLocation_missingDataFromOneSatellite_locationCanNoBeCalculated() {
        final Distance distanceToKenobi = new Distance(Sat.KENOBI, 570);
        final Distance distanceToSkywalker = new Distance(Sat.SKYWALKER, 120);

        final Optional<ShipLocation> positionOpt = subject.getLocation(Arrays.asList(distanceToKenobi, distanceToSkywalker));

        Assert.isTrue(!positionOpt.isPresent(), "position should not be returned, there is not enough data to calculate it");
    }
}
