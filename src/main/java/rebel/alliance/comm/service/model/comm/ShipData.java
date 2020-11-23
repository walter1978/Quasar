package rebel.alliance.comm.service.model.comm;

import java.util.Objects;

public class ShipData {
    private double x;
    private double y;
    private String[] message;

    public ShipData(double x, double y, String[] message) {
        this.x = x;
        this.y = y;
        this.message = message;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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
        ShipData shipData = (ShipData) o;
        return Double.compare(shipData.x, x) == 0 &&
                Double.compare(shipData.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
