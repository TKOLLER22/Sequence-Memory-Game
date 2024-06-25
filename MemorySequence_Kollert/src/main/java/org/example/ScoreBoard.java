package org.example;

/**
 * Třída ScoreBoard představuje skóre a úroveň hráče v hře na paměť se sekvencí.
 * Uchovává informace o aktuální úrovni a skóre hráče, umožňuje resetování skóre
 * a přechod na další úroveň s příslušným zvýšením skóre.
 */
public class ScoreBoard {
    private int level;
    private int score;
    private static final int LEVEL_SCORE_BONUS_RATIO = 10;
    private static final int MINIMUM_SCORE_INCREASE = 1;

    /**
     * Inicializuje skóre a level na výchozí hodnoty.
     */
    public ScoreBoard() {
        level = 0;
        score = 0;
    }

    /**
     * Resetuje skóre a level na výchozí hodnoty.
     */
    public void reset() {
        level = 0;
        score = 0;
    }

    /**
     * Přechází na další level a zvyšuje skóre hráče podle bonusového poměru.
     * První level nemá bonus.
     */
    public void nextLevel() {
        level++;
        if (level != 1) {
            score += level * LEVEL_SCORE_BONUS_RATIO;
        }
    }

    /**
     * Zvyšuje skóre hráče o minimální přírůstek na základě aktuálního levlu.
     */
    public void increaseScore() {
        score += level * MINIMUM_SCORE_INCREASE;
    }

    /**
     * Vrátí aktuální level hráče.
     *
     * @return Aktuální level hráče.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Vrátí aktuální skóre hráče.
     *
     * @return Aktuální skóre hráče.
     */
    public int getScore() {
        return score;
    }
}
