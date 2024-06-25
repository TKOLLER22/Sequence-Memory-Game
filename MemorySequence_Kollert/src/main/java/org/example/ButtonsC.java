package org.example;

import java.awt.*;

/**
 * Třída ButtonsC představuje tlačítko ve hře na paměť se sekvencí.
 * Uchovává informace o barvě, velikosti, pozici, stavu blikání a možném konci hry.
 */
public class ButtonsC {
    private Color color;
    private int SIZE_X;
    private int SIZE_Y;
    private int OFFSET_X;
    private int OFFSET_Y;
    private boolean flashing;
    private static boolean gameOver;
    private static Color gameOverColor = new Color(192, 0, 6);

    /**
     * Inicializuje tlačítko s danými parametry.
     *
     * @param col     Barva tlačítka.
     * @param x       Šířka tlačítka.
     * @param y       Výška tlačítka.
     * @param xOffset X-ová pozice tlačítka.
     * @param yOffset Y-ová pozice tlačítka.
     */
    public ButtonsC(Color col, int x, int y, int xOffset, int yOffset) {
        color = col;
        SIZE_X = x;
        SIZE_Y = y;
        OFFSET_X = xOffset;
        OFFSET_Y = yOffset;
        flashing = false;
        gameOver = false;
    }

    /**
     * Kreslí tlačítko na herním poli podle jeho stavu.
     *
     * @param g Grafický kontext pro kreslení.
     */
    public void drawButtons(Graphics2D g) {
        if (gameOver)
            g.setColor(gameOverColor);
        else if (flashing)
            g.setColor(color);
        else
            g.setColor(color.darker().darker());
        g.fillRect(OFFSET_X, OFFSET_Y, SIZE_X, SIZE_Y);
    }

    /**
     * Kontroluje, zda jsou dané souřadnice uvnitř tlačítka.
     *
     * @param x Souřadnice x.
     * @param y Souřadnice y.
     * @return True, pokud jsou souřadnice uvnitř tlačítka; jinak false.
     */
    public boolean isWithinCoords(int x, int y) {
        if (x < OFFSET_X || y < OFFSET_Y){
            return false;
        }
        if (x < (OFFSET_X + SIZE_X) && y < (OFFSET_Y + SIZE_Y)){
            return true;
        }
        return false;
    }

    /**
     * Nastavuje stav blikání tlačítka.
     *
     * @param bool True pro zapnutí blikání, false pro vypnutí.
     */
    public void setFlashing(boolean bool) {
        flashing = bool;
    }

    /**
     * Nastavuje stav konce hry na všech tlačítcích.
     *
     * @param bool True pro konec hry, false pro pokračování.
     */
    public void setGameOver(boolean bool) {
        gameOver = bool;
    }
}
