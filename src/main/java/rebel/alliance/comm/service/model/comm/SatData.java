package rebel.alliance.comm.service.model.comm;

import rebel.alliance.comm.service.model.Sat;

import java.util.Objects;

public class SatData {
    private Sat sat;
    private float distance;
    private String[] message;

    public SatData() {
        // required for ModelMapper
    }

    public SatData(Sat sat, float distance, String[] message) {
        this.sat = sat;
        this.distance = distance;
        this.message = message;
    }

    public Sat getSat() {
        return sat;
    }

    public void setSat(Sat sat) {
        this.sat = sat;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SatData satData = (SatData) o;
        return sat == satData.sat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sat);
    }
}
