package org.example;

import java.util.Random;

/**
 * Třída Pattern představuje herní vzor nebo sekvenci čísel v hře na paměť se sekvencí.
 * Uchovává informace o délce sekvence a generuje náhodný herní vzor.
 */
public class Pattern {
    private int length;
    private int[] pattern;
    private Random random;

    /**
     * Inicializuje herní vzor s danou délkou a vytvoří náhodnou sekvenci čísel.
     *
     * @param len Délka herního vzoru.
     */
    public Pattern(int len) {
        length = len;
        pattern = new int[length];
        random = new Random();
        createPattern();
    }

    /**
     * Vytváří náhodný herní vzor pomocí generátoru náhodných čísel.
     */
    private void createPattern() {
        for (int i = 0; i < length; i++) {
            pattern[i] = random.nextInt(9);
        }
    }

    /**
     * Vrátí číslo v herním vzoru na daném indexu.
     *
     * @param index Index v herním vzoru.
     * @return Číslo na daném indexu, nebo -1 pokud index není platný.
     */
    public int getAtIndex(int index) {
        if (index < 0 || index >= length)
            return -1;
        return pattern[index];
    }

    /**
     * Vrátí délku herního vzoru.
     *
     * @return Délka herního vzoru.
     */
    public int getLength() {
        return length;
    }
}
