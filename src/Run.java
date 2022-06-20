import com.wizylab.duck2d.Environment;
import com.wizylab.duck2d.Game;

public class Run {
    public static void main(String[] args) {
        Environment.put("window.width", 960);
        Environment.put("window.height", 720);

        Game.addView(new GameView(), new MenuView(), new GameOverView(), new RulesView(), new AboutView());
        Game.start(GameView.class);
    }
}