package cards;

import javafx.scene.image.Image;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class Card {
    
    // numero e seme ES. K fiori = 13 "fiori"
    private final String seed;
    private final int number;
    private final Image image;
    private final Map<Direction, Integer> movements;
    private final boolean combinate;
            
    public Card(final String s, final int n, final Image im,
                final int h , final int v ,final int d,final boolean c )
    {
        this.seed =s;
        this.number=n;
        this.image =im;
        this.movements = new EnumMap<>(Direction.class);
        this.movements.put(Direction.HORIZONTAL,h);
        this.movements.put(Direction.VERTICAL,v);
        this.movements.put(Direction.DIAGONAL,d);
        this.combinate = c;

    }
    public Card()
    {
        this("",0,null,0,0,0,false);
    }
    public String getSeed()
    {
        return seed;
    }
    public int getNumber()
    {
        return number;
    }
    public Image getImage()
    {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number == card.number && seed.equals(card.seed) && Objects.equals(image, card.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seed, number, image);
    }

    @Override
    public String toString()
    {
        return number +" "+ seed+(image==null ? "Image not loaded" :"L");
    }

    public int getMov(Direction d){
        return movements.get(d);
    }
          
}
