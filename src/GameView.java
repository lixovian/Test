import com.wizylab.duck2d.*;

import java.awt.*;

import static com.wizylab.duck2d.Mouse.*;

public class GameView implements View {

    public static int boardWidth = 38, boardHeight = 32, cellsX = 200, cellsY = 74, cellsGap = 20;

    private boolean isDragging = false;
    private int startDragX = 0, startDragY = 0, endDragX = 0, endDragY = 0;

    private int drawX1 = 0, drawX2 = 0, drawY1 = 0, drawY2 = 0;

    private int playersNum = 4, turnNum = 0;

    private int minCaptureNum = 3, maxCaptureNum = 18;

    private int captureNum = 0;

    private int startSquareWidth = 2, startSquareHeight = 2;

    private String[] playersNames = {"Фиол.", "Синий", "Желтый", "Белый"};

    private Player[] players;

//        g.putImage("dice/b_0", 15, 460, 165, 165);

//        g.putImage("exit/b_0", 15, 638, 58, 58);

//        g.putImage("miss/b_0", 87, 638, 92, 58);

    private static final Rectangle[] BUTTONS = {
            new Rectangle(15, 460, 165, 165),
            new Rectangle(15, 638, 58, 58),
            new Rectangle(121, 638, 92, 58)
    };
    private int buttonsValue = -1;

    private int time = 0, maxTime = 2000;

    @Override
    public void onShow() {
        players = new Player[playersNum];

        captureNum = -1;

        for (int i = 0; i < playersNum; i++) {
            players[i] = new Player(i);
        }

        players[0].add(0, 0, startSquareWidth, startSquareHeight);
        players[1].add(boardWidth - startSquareWidth - 1, 0, boardWidth - 1, startSquareHeight);
        if (playersNum > 2)
            players[2].add(boardWidth - startSquareWidth - 1, boardHeight - startSquareHeight - 1, boardWidth - 1, boardHeight - 1);
        if (playersNum > 3) players[3].add(0, boardHeight - startSquareHeight - 1, startSquareWidth, boardHeight - 1);
    }

    @Override
    public void onTimer(long l) {
        buttonsValue = -1;
        for (int i = 0; i < BUTTONS.length; i++)
            if (BUTTONS[i].contains(x(), y())) buttonsValue = i;

        if (time < maxTime) time += l;

        if (hasClick(MouseButton.LEFT)) {
            if (buttonsValue == 0 && captureNum == -1)
                captureNum = (int) Math.round(minCaptureNum + Math.random() * (maxCaptureNum - minCaptureNum));
            if (buttonsValue == 1) Game.show(MenuView.class);
            if (buttonsValue == 2 && time >= maxTime) {
                turnNum++;
                captureNum = -1;
                time = 0;
                if (turnNum >= playersNum) turnNum = 0;
            }

            if (!isDragging) {
                isDragging = true;

                startDragX = x();
                startDragY = y();

                if (startDragX < cellsX) startDragX = cellsX;
                else if (startDragX > cellsX + cellsGap * (boardWidth - 1))
                    startDragX = cellsX + cellsGap * (boardWidth - 1);

                if (startDragY < cellsY) startDragY = cellsY;
                else if (startDragY > cellsY + cellsGap * (boardHeight - 1))
                    startDragY = cellsY + cellsGap * (boardHeight - 1);
            }

            endDragX = x();
            endDragY = y();

            if (endDragX < cellsX) endDragX = cellsX;
            else if (endDragX > cellsX + cellsGap * (boardWidth - 1)) endDragX = cellsX + cellsGap * (boardWidth - 1);

            if (endDragY < cellsY) endDragY = cellsY;
            else if (endDragY > cellsY + cellsGap * (boardHeight - 1)) endDragY = cellsY + cellsGap * (boardHeight - 1);

        } else if (isDragging) {
            isDragging = !isDragging;

            if (doBoxTouchAnotherBoxes() && !doBoxOverlapAnotherBoxes() && drawX1 - drawX2 != 0 && drawY1 - drawY2 != 0 && captureNum >= Math.abs((drawX1 - drawX2) * (drawY1 - drawY2))) {
                players[turnNum].add(drawX1, drawY1, drawX2, drawY2);

                captureNum -= Math.abs((drawX1 - drawX2) * (drawY1 - drawY2));
//                time = 0;

//                turnNum++;
//                if (turnNum >= playersNum) turnNum = 0;

//                captureNum = (int) Math.round(minCaptureNum + Math.random() * (maxCaptureNum - minCaptureNum));
//                captureNum = -1;
            }

            int sumOfScores = 0;
            for (Player player : players) {
                sumOfScores += player.score;
            }

            if (sumOfScores == boardWidth * boardHeight) gameOver();
        }
    }

    private boolean doBoxOverlapAnotherBoxes() {
        Box temp = new Box(drawX1, drawY1, drawX2, drawY2);
        for (int i = 0; i < playersNum; i++) {
            for (Box box : players[i].boxes) {
                if (Box.doBoxesOverlap(temp, box)) return true;
            }
        }

        return false;
    }

    private boolean doBoxTouchAnotherBoxes() {
        Box temp = new Box(drawX1, drawY1, drawX2, drawY2);
        for (Box box : players[turnNum].boxes) {
            if (Box.doBoxesTouch(temp, box)) return true;
        }

        return false;
    }

    private void gameOver() {
        Game.show(MenuView.class);
    }

    @Override
    public void onDraw(Graph g) {
        g.setColor(new Color(0x242424));
        g.fillRect(0, 0, Environment.get("window.width"), Environment.get("window.height"));

        drawCells(g);

        for (Player player : players) player.draw(g);

        if (isDragging) {
            float x1 = startDragX, y1 = startDragY, x2 = endDragX, y2 = endDragY;

            if (x1 > x2) {
                float temp = x2;
                x2 = x1;
                x1 = temp;
            }

            if (y1 > y2) {
                float temp = y2;
                y2 = y1;
                y1 = temp;
            }

            x1 = roundNumberToCells(x1, cellsX);
            x2 = roundNumberToCells(x2, cellsX);
            y1 = roundNumberToCells(y1, cellsY);
            y2 = roundNumberToCells(y2, cellsY);

            drawX1 = (int) ((x1 - cellsX) / cellsGap);
            drawX2 = (int) ((x2 - cellsX) / cellsGap);
            drawY1 = (int) ((y1 - cellsY) / cellsGap);
            drawY2 = (int) ((y2 - cellsY) / cellsGap);

            Color outlineColor, fillingColor, textColor;
            if (doBoxTouchAnotherBoxes() && !doBoxOverlapAnotherBoxes() && captureNum >= Math.abs((drawX1 - drawX2) * (drawY1 - drawY2)) && drawX1 - drawX2 != 0 && drawY1 - drawY2 != 0) {
                fillingColor = new Color(0x5E40E024, true);
                outlineColor = new Color(0x21AD1C);
                textColor = new Color(0xFF98FF96, true);
            } else {
                fillingColor = new Color(0x5EE02427, true);
                outlineColor = new Color(0xAD1C1E);
                textColor = new Color(0xFAA9AA);
            }

            g.setColor(fillingColor);
            g.fillRect((int) x1, (int) y1, (int) x2, (int) y2);

            g.setColor(outlineColor);
            g.rect((int) x1, (int) y1, (int) x2, (int) y2);

            if (drawX1 - drawX2 != 0 && drawY1 - drawY2 != 0) {
                g.setColor(textColor);
                g.setFont(new Font("Calibri", Font.PLAIN, 22));
                g.ctext((int) x1, (int) y1, (int) x2, (int) y2, String.valueOf(Math.abs((drawX1 - drawX2) * (drawY1 - drawY2))));
            }
        }

        g.setColor(Player.fillings[turnNum]);
        g.fillCircle(133, 119, 30);

        g.setColor(Player.outlines[turnNum]);
        g.circle(133, 119, 30);

        g.setColor(new Color(0xFFDCDCDC, true));

        g.setFont(new Font("Calibri Light", Font.PLAIN, 40));
        g.ctext(0, 0, Environment.get("window.width"), 74, "Игра в режиме Hotseat");

        g.setFont(new Font("Calibri", Font.PLAIN, 36));
        g.ctext(34, 103, 95, 135, "Ход");

        g.ctext(41, 170, 160, 202, "Игроки:");

        g.setFont(new Font("Calibri", Font.PLAIN, 32));
        int y = 222;
        for (int i = 0; i < playersNum; i++) {
            g.setColor(Player.outlines[i]);
            g.ctext(20, y, 180, y + 22, playersNames[i] + " (" + players[i].score + ")");
            y += 35;
        }

        g.putImage("dice", 40, 365, 50, 50);

        g.setFont(new Font("Calibri", Font.PLAIN, 40));
        g.text(100, 402, (captureNum != -1) ? String.valueOf(captureNum) : "??");

        String hover = "2";
        g.putImage("dice/b_" + ((captureNum != -1) ? "3" : ((buttonsValue == 0) ? hover : "0")), 15, 460, 165, 165);

        g.putImage("exit/b_" + ((buttonsValue == 1) ? hover : "0"), 15, 638, 92, 58);

        g.putImage("miss/b_" + ((time < maxTime) ? "3" : ((buttonsValue == 2) ? hover : "0")), 121, 638, 58, 58);
    }

    private float roundNumberToCells(float a, int sub) {
        a -= sub;
        a /= cellsGap;
        a = Math.round(a);
        a *= cellsGap;
        a += sub;

        return a;
    }

    private void drawCells(Graph g) {
        g.setColor(new Color(0xB1D0E0));
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                if (j < boardHeight - 1)
                    g.line(cellsX + i * cellsGap, cellsY + j * cellsGap, cellsX + i * cellsGap, cellsY + (j + 1) * cellsGap);
                if (i < boardWidth - 1)
                    g.line(cellsX + i * cellsGap, cellsY + j * cellsGap, cellsX + (i + 1) * cellsGap, cellsY + j * cellsGap);
            }
        }

        g.setColor(new Color(0x22A1E6));
        for (int i = 0; i < boardWidth; i++) {
            for (int j = 0; j < boardHeight; j++) {
                g.fillCircle(cellsX + i * cellsGap, cellsY + j * cellsGap, 2);
            }
        }
    }
}