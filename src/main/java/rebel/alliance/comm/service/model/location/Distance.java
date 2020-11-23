package rebel.alliance.comm.service.model.location;

import rebel.alliance.comm.service.model.Sat;

import java.util.Objects;

public class Distance {
    private Sat sat;
    private float distance;

    public Distance(Sat sat, float distance) {
        this.sat = sat;
        this.distance = distance;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance that = (Distance) o;
        return sat == that.sat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sat);
    }
}
