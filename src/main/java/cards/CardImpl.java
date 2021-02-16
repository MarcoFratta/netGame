package cards;

import javafx.scene.image.Image;

public class CardImpl extends Card {

    private static final long serialVersionUID = 3187039689043617852L;
    // numero e seme ES. K fiori = 13 "fiori"
    private final transient Image image;


    public CardImpl(final String s, final String name, final int n, final Image im,
                    final int h, final int v, final int d, final boolean c, final boolean b, int id, boolean e) {
        super(s, name, n, h, v, d, c, b, id, e);
        this.image = im;
    }

    public String getSeed() {
        return this.seed;
    }

    public int getNumber() {
        return this.number;
    }
    public Image getImage()
    {
        return this.image;
    }
    @Override
    public String toString()
    {
        return this.number +" "+ this.seed +" id->"+ this.id +(this.image ==null ? " Image not loaded" :"L");
    }

          
}
