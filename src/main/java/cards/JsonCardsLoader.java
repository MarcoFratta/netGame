package cards;

import javafx.scene.image.Image;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsonCardsLoader {

    public static final String PACKAGE = "/images/";

    public static String loadFromPath(String path , List<Card> list){
        String cardType = null;
        try {
            System.out.println("Json path->"+path);
            String dataFolfder = new DeckLoader().getImagesPath();
            File file = new File(path);
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;
             cardType = (String) jsonObject.get("value");
            JSONArray js = (JSONArray) (jsonObject.get("seeds"));
            for (JSONObject object : (Iterable<JSONObject>) js) {
                String seed = (String) object.get("seed");
                JSONArray cards = (JSONArray) (object.get("cards"));
                for (JSONObject card : (Iterable<JSONObject>) cards) {
                    boolean c = Boolean.parseBoolean((String) card.get("c"));
                    boolean b = Boolean.parseBoolean((String) card.get("b"));
                    boolean e = Boolean.parseBoolean((String) card.get("e"));
                    int v = Integer.parseInt((String) card.get("v"));
                    int h = Integer.parseInt((String) card.get("h"));
                    int d = Integer.parseInt((String) card.get("d"));
                    int num = Integer.parseInt((String) card.get("num"));
                    int id = Integer.parseInt((String) card.get("id"));
                    String n = (String) card.get("value");
                    String url = "file:" + dataFolfder + "\\" + cardType + "\\" + id + ".png";
                    System.out.println(url);
                    list.add(new CardImpl(seed, n, num, new Image(url), h, v, d, c, b, id, e));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading deck file" + path);
        }

        return cardType;
    }

    public static boolean isValid(File path, File folder) {
        JSONParser parser = new JSONParser();
        int cardsNum;
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            return false;
        }
        try {
            JSONObject jsonObject = (JSONObject) obj;
            String cardType = (String) jsonObject.get("value");
            JSONArray js = (JSONArray) (jsonObject.get("seeds"));
            for (JSONObject object : (Iterable<JSONObject>) js) {
                JSONArray cards = (JSONArray) (object.get("cards"));
                for (JSONObject card : (Iterable<JSONObject>) cards) {
                    int id = Integer.parseInt((String) card.get("id"));
                    if (!new File(folder + "\\" + cardType + "\\" + id + ".png").exists()) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
