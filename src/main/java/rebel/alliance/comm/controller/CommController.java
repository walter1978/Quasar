package rebel.alliance.comm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rebel.alliance.comm.dto.*;
import rebel.alliance.comm.mapper.QuasarModelMapper;
import rebel.alliance.comm.service.CommService;
import rebel.alliance.comm.service.model.comm.ShipData;

@RestController
@RequestMapping("/api/v1/comm")
public class CommController {
    @Autowired
    private CommService commService;
    @Autowired
    private QuasarModelMapper modelMapper;

    @PostMapping("/topsecret")
    @ResponseStatus(HttpStatus.OK)
    public ShipDataDTO topSecret(@RequestBody AllSatDataDTO allSatDataDTO) {
        final ShipData shipData = commService.calculateShipData(modelMapper.mapToAllSatData(allSatDataDTO));
        return modelMapper.mapToShipDataDTO(shipData);
    }

    @PostMapping("/topsecret_split/{sat}")
    @ResponseStatus(HttpStatus.OK)
    public void topSecretSplit(@RequestBody SingleSatDataDTO singleSatDataDTO, @PathVariable String sat) {
        commService.updateSatData(modelMapper.mapToSatData(singleSatDataDTO, sat));
    }

    @GetMapping("/topsecret_split")
    @ResponseStatus(HttpStatus.OK)
    public ShipDataDTO topSecretSplit() {
        final ShipData shipData = commService.calculateShipData();
        return modelMapper.mapToShipDataDTO(shipData);
    }
}
