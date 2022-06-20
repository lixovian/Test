import com.wizylab.duck2d.*;

import java.awt.*;

public class MenuView implements View {
    private static final String[] TITLES = {"Создать игру", "Правила", "Об игре", "Выход"};
    private static final int WIDTH_INC = 60, HEIGHT_INC = 40;
    private static final Rectangle[] BUTTONS = {
            new Rectangle(330, 220, 300, 80),
            new Rectangle(330, 310, 300, 80),
            new Rectangle(330, 400, 300, 80),
            new Rectangle(330, 490, 300, 80)
    };
    private int value = -1;

    @Override
    public void onShow() {

    }


    @Override
    public void onTimer(long l) {
        value = -1;
        for (int i = 0; i < BUTTONS.length; i++)
            if (BUTTONS[i].contains(Mouse.x(), Mouse.y())) value = i;

        if (Mouse.onClick(MouseButton.LEFT)) {
            if (value == 0) Game.show(GameView.class);
            if (value == 1) Game.show(RulesView.class);
            if (value == 2) Game.show(AboutView.class);
            if (value == 3) System.exit(0);
        }

    }

    @Override
    public void onDraw(Graph g) {
        g.setColor(new Color(0x1E2326));
        g.fillRect(0, 0, Environment.get("window.width"), Environment.get("window.height"));

        for (int i = 0; i < BUTTONS.length; i++) {
            Color filling, border, text;
            if (i == value) {
                filling = new Color(0x22A1E6);
                border = new Color(0x007FC4);
                text = new Color(0xEAEAEA);

                g.setFont(new Font("Calibri Light", Font.PLAIN, 30));
            } else {
                filling = new Color(0x3CB6FF);
                border = new Color(0x0094E5);
                text = new Color(0xFFFFFF);

                g.setFont(new Font("Calibri Light", Font.PLAIN, 28));
            }
            Rectangle b = BUTTONS[i];
            g.setColor(filling);

            g.setColor(text);
            g.ctext(b, TITLES[i]);
        }
    }

}
