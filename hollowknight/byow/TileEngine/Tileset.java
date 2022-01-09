package byow.TileEngine;

import java.awt.Color;
import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset implements Serializable {
    private static final File CWD = new File(System.getProperty("user.dir"));
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you",
            join(CWD.toString(), "/byow/Core/Tiles/Avatar.png").toString());
    public static final TETile WALL = new TETile('#', new Color(216, 128, 128), Color.darkGray,
            "wall", join(CWD.toString(), "/byow/Core/Tiles/BrickTile.png").toString());
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", join(CWD.toString(), "/byow/Core/Tiles/GrassTile.png").toString());
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "grass");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "flower",
            join(CWD.toString(), "/byow/Core/Tiles/Coin.png").toString());
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "sand");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "tree");

    // Joins two file paths together.
    static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }
}

