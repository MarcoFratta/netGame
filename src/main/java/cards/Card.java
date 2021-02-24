package cards;

import java.io.Serializable;
import java.util.Objects;

public class Card implements Serializable {

    private static final long serialVersionUID = -4625278940046029434L;
    protected final transient String seed;
    protected final transient String name;
    protected final transient int number;
    protected final transient MovementManager movementManager;
    protected final int id;
    protected final boolean canEat;

    public Card(final String s, final String name, final int n, final int h,
                final int v, final int d, final boolean c, final boolean b, int id, boolean e)
    {
        this.name = name;
        this.seed = s;
        this.canEat = e;
        this.number = n;
        this.id = id;
        this.movementManager = new MovementManager(h,v,d,c,b);
    }


    public String getSeed()
    {
        return this.seed;
    }
    public int getNumber() {
        return this.number;
    }

    public int getId() {
        return this.id;
    }

    public MovementManager getMovementManager() {
        return this.movementManager;
    }

    public boolean canEat() {
        System.out.println("Can eat ->" + canEat);
        return this.canEat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.id == card.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seed, this.number, this.id);
    }
    @Override
    public String toString()
    {
        return this.number +" "+ this.seed +" id->"+ this.id;
    }


}
