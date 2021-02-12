package cards;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Card implements Serializable {

    protected final String seed;
    protected final String name;
    protected final int number;
    protected final MovementManager movementManager;

    public Card(final String s,final String name, final int n, final int h ,
                final int v , final int d, final boolean c, final boolean b )
    {
        this.name = name;
        this.seed =s;
        this.number=n;
        movementManager = new MovementManager(h,v,d,c,b);


    }
    public Card()
    {
        this("","",0,0,0,0,false,true);
    }
    public String getSeed()
    {
        return seed;
    }
    public int getNumber()
    {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number == card.number && seed.equals(card.seed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed, number);
    }

    @Override
    public String toString()
    {
        return number +" "+ seed;
    }

    public MovementManager getMovementManager(){
        return this.movementManager;
    }

}
