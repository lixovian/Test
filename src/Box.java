import com.wizylab.duck2d.Graph;

import java.awt.*;

public class Box {
    private int x1, x2, y1, y2;
    private int area;

    Box(int x1, int y1, int x2, int y2) {
        set(x1, y1, x2, y2);
        area = Math.abs((x1 - x2) * (y1 - y2));
    }

    public void set(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public void draw(Graph g, Color outline, Color filling) {
        int tempX1 = GameView.cellsX + x1 * GameView.cellsGap;
        int tempY1 = GameView.cellsY + y1 * GameView.cellsGap;
        int tempX2 = GameView.cellsX + x2 * GameView.cellsGap;
        int tempY2 = GameView.cellsY + y2 * GameView.cellsGap;

        g.setColor(filling);
        g.fillRect(tempX1, tempY1, tempX2, tempY2);

        g.setColor(outline);
        g.rect(tempX1, tempY1, tempX2, tempY2);

        g.setColor(new Color(0xDBDBDB));
        g.setFont(new Font("Calibri", Font.PLAIN, 22));
        g.ctext(tempX1, tempY1, tempX2, tempY2, String.valueOf(area));
    }

    public static boolean doBoxesOverlap(Box b1, Box b2){
        return (b1.x2 > b2.x1 && b2.x2 > b1.x1) && (b1.y2 > b2.y1 && b2.y2 > b1.y1);
    }

    public static boolean doBoxesTouch(Box b1, Box b2){
        return (b1.x2 >= b2.x1 && b2.x2 >= b1.x1) && (b1.y2 >= b2.y1 && b2.y2 >= b1.y1) && (b1.y1 == b2.y1 || b1.y2 == b2.y2 || b1.x1 == b2.x1 || b1.x2 == b2.x2 || b1.x1 == b2.x2 || b1.x2 == b2.x1 || b1.y1 == b2.y2 || b1.y2 == b2.y1);
    }
}
