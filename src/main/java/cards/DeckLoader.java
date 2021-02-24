package cards;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeckLoader {

    public static final String DECKS_FOLDER = ".Guru/Decks";
    public static final String IMAGES_FOLDER = ".Guru/Images";

    public String getDefPath() {
        return new File(System.getProperty("user.home"), DECKS_FOLDER).getPath();
    }

    public String getImagesPath() {
        return new File(System.getProperty("user.home"), IMAGES_FOLDER).getPath();
    }


    public List<String> loadDecks() throws ClassNotFoundException {
        File recordsDir = new File(System.getProperty("user.home"), DECKS_FOLDER);
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        File imagesDir = new File(System.getProperty("user.home"), IMAGES_FOLDER);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        List<File> decks = this.getDecksNames(recordsDir);
        if (decks.isEmpty()) {
            return Collections.emptyList();
        }

        return decks.stream().filter(f -> JsonCardsLoader.isValid(f, imagesDir))
                .map(f -> f.getName().replace(".json", ""))
                .collect(Collectors.toList());
    }

    private List<File> getDecksNames(File file) {
        return Arrays.stream(Objects.requireNonNull(file.listFiles())).collect(Collectors.toList());
    }

    public String getJsonFilePath(String s) {
        return this.getDefPath() + "\\" + s + ".json";
    }


}
