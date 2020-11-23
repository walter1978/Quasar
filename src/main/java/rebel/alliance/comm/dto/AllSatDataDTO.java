package rebel.alliance.comm.dto;

import java.util.List;

public class AllSatDataDTO {
    private List<SatDataDTO> satellites;

    public AllSatDataDTO() {
        // required for ModelMapper
    }

    public AllSatDataDTO(List<SatDataDTO> satellites) {
        this.satellites = satellites;
    }

    public List<SatDataDTO> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<SatDataDTO> satellites) {
        this.satellites = satellites;
    }
}
