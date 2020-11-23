package rebel.alliance.comm.dto;

public class SingleSatDataDTO {
    private float distance;
    private String[] message;

    public SingleSatDataDTO() {
        // required for ModelMapper
    }

    public SingleSatDataDTO(float distance, String[] message) {
        this.distance = distance;
        this.message = message;
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
