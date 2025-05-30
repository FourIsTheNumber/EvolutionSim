package components;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static utils.RandomUtils.rollRange;

// Biomes are used to generate environments and their stats.
public enum Biome {
    Desert("Desert", 9, 50, "resources/biomeTiles/desertTile.png",
            List.of("resources/biomeDecorations/cactus1.png",
                    "resources/biomeDecorations/cactus2.png")),
    Forest("Forest", 5, 150, "resources/biomeTiles/forestTile.png",
            List.of("resources/biomeDecorations/tree1.png",
                    "resources/biomeDecorations/tree2.png",
                    "resources/biomeDecorations/tree3.png")),
    Taiga("Taiga", 1, 100, "resources/biomeTiles/taigaTile.png",
            List.of("resources/biomeDecorations/taigahill.png",
                    "resources/biomeDecorations/taigatree1.png",
                    "resources/biomeDecorations/taigatree2.png")),
    Ocean("Ocean", 5, 200, "resources/biomeTiles/oceanTile.png",
            List.of("resources/biomeDecorations/wave1.png",
                    "resources/biomeDecorations/wave2.png",
                    "resources/biomeDecorations/wave3.png")),
    Plains("Plains", 4, 100, "resources/biomeTiles/plainsTile.png",
            List.of("resources/biomeDecorations/grass1.png"));


    public final String name;
    final int baseTemp;
    final int food;
    final String tileResourceLocation;
    final List<String> decorationResources;

    Biome(String name, int baseTemp, int food, String tileResourceLocation, List<String> decorationResources) {
        this.name = name;
        this.baseTemp = baseTemp;
        this.food = food;
        this.tileResourceLocation = tileResourceLocation;
        this.decorationResources = decorationResources;
    }

    public String getTileResourceLocation() {
        return tileResourceLocation;
    }

    public List<String> getDecorationResources() {
        return decorationResources;
    }

    public static Biome getRandomBiome() {
        return Biome.values()[rollRange(0, Biome.values().length)];
    }

    public static boolean isAquaticBiome(Biome b) {
        return b == Ocean;
    }

    public static Biome getNextBiome(Biome b) {
        if (b.ordinal() + 1 >= Biome.values().length) return Biome.values()[0];
        else return Biome.values()[b.ordinal() + 1];
    }

    public static List<Biome> getSimilarBiomes(Biome b) {
        switch (b) {
            case Ocean -> {
                return List.of(Desert, Forest, Taiga, Plains);
            }
            case Desert, Forest, Taiga -> {
                return List.of(Plains);
            }
            case Plains -> {
                return List.of(Forest, Taiga, Desert);
            }
        }
        return null;
    }
}
