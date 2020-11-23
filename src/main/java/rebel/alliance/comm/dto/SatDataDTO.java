package rebel.alliance.comm.dto;

public class SatDataDTO {
    private String name;
    private float distance;
    private String[] message;

    public SatDataDTO() {
        // required for ModelMapper
    }

    public SatDataDTO(String name, float distance, String[] message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }
}
