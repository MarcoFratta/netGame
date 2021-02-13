package cards;

import javafx.scene.image.Image;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

public class JsonCardsLoader {

    public static final String PACKAGE = "/img_carte/";

    public static String loadFromPath(String path , List<Card> list){
        String cardType = null;
        try {
            System.out.println("Json path->"+path);
            File file = new File(path);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;
             cardType = (String) jsonObject.get("value");
            JSONArray js = (JSONArray) (jsonObject.get("seeds"));
            for (JSONObject object : (Iterable<JSONObject>) js) {
                String seed = (String) object.get("seed");
                JSONArray cards = (JSONArray) (object.get("cards"));
                for(JSONObject card : (Iterable<JSONObject>) cards ){
                    boolean c = Boolean.parseBoolean((String) card.get("c"));
                    boolean b = Boolean.parseBoolean((String) card.get("b"));
                    int v = Integer.parseInt((String) card.get("v"));
                    int h = Integer.parseInt((String) card.get("h"));
                    int d = Integer.parseInt((String) card.get("d"));
                    int num = Integer.parseInt((String) card.get("num"));
                    int id = Integer.parseInt((String) card.get("id"));
                    String n = (String) card.get("value");
                    URL url = Deck.class.getResource(PACKAGE+cardType+"/"+n+"_"+seed+".png");
                    list.add(new CardImpl(seed,n,num,new Image(url.toString()),h,v,d,c,b,id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading deck file"+path);
        }

        return cardType;
    }



}
