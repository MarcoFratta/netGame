package cards;

import core.Pair;
import javafx.scene.image.Image;

public class CardImpl extends Card {

    // numero e seme ES. K fiori = 13 "fiori"
    private final transient Image image;

            
    public CardImpl(final String s,final String name, final int n, final Image im,
                    final int h , final int v , final int d, final boolean c ,final boolean b)
    {
        super(s,name,n,h,v,d,c,b);
        this.image =im;
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
    public String toString()
    {
        return number +" "+ seed+(image==null ? "Image not loaded" :"L");
    }

          
}
