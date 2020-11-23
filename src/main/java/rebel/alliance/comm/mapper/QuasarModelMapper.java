package rebel.alliance.comm.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import rebel.alliance.comm.dto.AllSatDataDTO;
import rebel.alliance.comm.dto.SatDataDTO;
import rebel.alliance.comm.dto.ShipDataDTO;
import rebel.alliance.comm.dto.SingleSatDataDTO;
import rebel.alliance.comm.exception.InvalidInputDataException;
import rebel.alliance.comm.service.model.Sat;
import rebel.alliance.comm.service.model.comm.AllSatData;
import rebel.alliance.comm.service.model.comm.SatData;
import rebel.alliance.comm.service.model.comm.ShipData;
import rebel.alliance.comm.service.model.location.Distance;
import rebel.alliance.comm.service.model.message.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuasarModelMapper extends ModelMapper {
    public SatData mapToSatData(SatDataDTO satDataDTO) {
        final SatData satData = map(satDataDTO, SatData.class);
        validateSatName(satDataDTO.getName().toUpperCase());
        satData.setSat(Sat.valueOf(satDataDTO.getName().toUpperCase()));
        return satData;
    }

    public SatData mapToSatData(SingleSatDataDTO singleSatDataDTO, String sat) {
        final SatData satData = map(singleSatDataDTO, SatData.class);
        validateSatName(sat.toUpperCase());
        satData.setSat(Sat.valueOf(sat.toUpperCase()));
        return satData;
    }

    public AllSatData mapToAllSatData(AllSatDataDTO allSatDataDTO) {
        if (allSatDataDTO == null || allSatDataDTO.getSatellites() == null || allSatDataDTO.getSatellites().isEmpty()) {
            throw new InvalidInputDataException();
        }
        final List<SatData> satDataList = allSatDataDTO.getSatellites().stream().map(this::mapToSatData).collect(Collectors.toList());
        final AllSatData allSatData = new AllSatData(satDataList);
        return allSatData;
    }

    public ShipDataDTO mapToShipDataDTO(ShipData shipData) {
        return map(shipData, ShipDataDTO.class);
    }

    public Distance mapToDistance(SatData satData) {
        return new Distance(satData.getSat(), satData.getDistance());
    }

    public List<Distance> mapToDistanceList(AllSatData allSatData) {
        if (allSatData != null) {
            return allSatData.getSatDataList().stream()
                    .map(satData -> mapToDistance(satData))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Message mapToMessage(SatData satData) {
        return new Message(satData.getSat(), satData.getMessage());
    }

    public List<Message> mapToMessageList(AllSatData allSatData) {
        if (allSatData != null) {
            return allSatData.getSatDataList().stream()
                    .map(satData -> mapToMessage(satData))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void validateSatName(String name) {
        if (name != null && !Arrays.stream(Sat.values()).map(Enum::name).collect(Collectors.toList()).contains(name.toUpperCase())) {
            throw new InvalidInputDataException();
        }
    }
}
