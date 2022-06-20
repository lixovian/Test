import com.wizylab.duck2d.Graph;

import java.awt.*;
import java.util.ArrayList;

public class Player {
//    private final Color[] outlines = {new Color(0xA600FF), new Color(0x0090FF), new Color(0xB8B8B8), new Color(0xC99D00)};
    public static final Color[] outlines = {new Color(0xDA9CFF), new Color(0x95D3FF), new Color(0xFFE280), new Color(0xFFFFFF)};
    public static final Color[] fillings = {new Color(0xB2AF17FF, true), new Color(0xB01A9BFF, true), new Color(0xB2C69900, true), new Color(0xB2A3A3A3, true)};
    public int number, score = 0;

    public ArrayList<Box> boxes = new ArrayList<>();

    Player(int num) {
        number = num;
    }

    public void add(int x1, int y1, int x2, int y2) {
        boxes.add(new Box(x1, y1, x2, y2));

        score += Math.abs((x1 - x2) * (y1 - y2));
    }

    public void draw(Graph g) {
        for (Box box : boxes) {
            box.draw(g, outlines[number], fillings[number]);
        }
    }
}