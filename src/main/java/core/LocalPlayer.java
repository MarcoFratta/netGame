package core;

import core.Player;

import java.io.Serializable;
import java.util.Objects;

public class LocalPlayer implements Player, Serializable {


    private static final long serialVersionUID = -1668034314290220957L;
    private final int id;
    private final String name;

    public LocalPlayer(int id,String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        LocalPlayer that = (LocalPlayer) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return String.valueOf(this.getId());
    }
}
