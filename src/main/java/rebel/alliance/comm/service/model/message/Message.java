package rebel.alliance.comm.service.model.message;

import rebel.alliance.comm.service.model.Sat;

import java.util.Objects;

public class Message {
    private Sat sat;
    private String[] message;

    public Message(Sat sat, String[] message) {
        this.sat = sat;
        this.message = message;
    }

    public Sat getSat() {
        return sat;
    }

    public void setSat(Sat sat) {
        this.sat = sat;
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
        Message that = (Message) o;
        return sat == that.sat;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sat);
    }
}
