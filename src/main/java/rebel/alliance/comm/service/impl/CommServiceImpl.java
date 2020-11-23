package rebel.alliance.comm.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rebel.alliance.comm.Constants;
import rebel.alliance.comm.exception.InvalidInputDataException;
import rebel.alliance.comm.exception.LocationProcessingException;
import rebel.alliance.comm.exception.MessageProcessingException;
import rebel.alliance.comm.mapper.QuasarModelMapper;
import rebel.alliance.comm.service.CommService;
import rebel.alliance.comm.service.LocationService;
import rebel.alliance.comm.service.MessagingService;
import rebel.alliance.comm.service.model.comm.AllSatData;
import rebel.alliance.comm.service.model.comm.SatData;
import rebel.alliance.comm.service.model.comm.ShipData;
import rebel.alliance.comm.service.model.location.ShipLocation;

import java.util.Optional;

@Service
public class CommServiceImpl implements CommService {
    private static final Logger logger = LoggerFactory.getLogger(CommServiceImpl.class);
    @Autowired
    private LocationService locationService;
    @Autowired
    private MessagingService messagingService;
    @Autowired
    private QuasarModelMapper modelMapper;

    @Override
    public ShipData calculateShipData(AllSatData allSatData) {
        validate(allSatData);

        final Optional<ShipLocation> shipLocationOpt = locationService.getLocation(modelMapper.mapToDistanceList(allSatData));
        if (shipLocationOpt.isPresent()) {
            final Optional<String[]> messageOpt = messagingService.getMessage(modelMapper.mapToMessageList(allSatData));
            if (messageOpt.isPresent()) {
                return new ShipData(shipLocationOpt.get().getX(), shipLocationOpt.get().getY(), messageOpt.get());
            }
            throw new MessageProcessingException();
        }
        throw new LocationProcessingException();
    }

    @Override
    public void updateSatData(SatData satData) {
        validate(satData);
        locationService.updateDistance(modelMapper.mapToDistance(satData));
        messagingService.updateMessage(modelMapper.mapToMessage(satData));
    }

    @Override
    public ShipData calculateShipData() {
        final Optional<ShipLocation> shipLocationOpt = locationService.getLocation();
        if (shipLocationOpt.isPresent()) {
            final Optional<String[]> messageOpt = messagingService.getMessage();
            if (messageOpt.isPresent()) {
                return new ShipData(shipLocationOpt.get().getX(), shipLocationOpt.get().getY(), messageOpt.get());
            }
            throw new MessageProcessingException();
        }
        throw new LocationProcessingException();
    }

    private void validate(AllSatData allSatData) {
        if (allSatData == null || allSatData.getSatDataList() == null
                || allSatData.getSatDataList().isEmpty()
                || allSatData.getSatDataList().size() != Constants.SATELLITE_COUNT) {
            logger.error(String.format("allSatData or allSatData.satDataList is invalid: %s", allSatData));
            throw new InvalidInputDataException();
        }
        allSatData.getSatDataList().stream().forEach(this::validate);
    }

    private void validate(SatData satData) {
        if (satData.getMessage() == null || satData.getMessage().length == 0
                || satData.getDistance() <= 0) {
            logger.error(String.format("Invalid message or distance: %s", satData));
            throw new InvalidInputDataException();
        }
    }
}
