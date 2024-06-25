package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Třída MemorySequence představuje implementaci hry na paměť se sekvencí blikajících tlačítek.
 * Hra obsahuje tlačítka, skóre, a umožňuje hráči opakovat sekvenci a postupovat na vyšší úroveň.
 */
public class MemorySequence extends JPanel implements ActionListener, MouseListener {

    private static MemorySequence memorySequence;
    private ScoreBoard scoreBoard;
    private Pattern pattern;
    private JButton button;
    private ButtonsC[] buttons;
    private Timer timer;

    // Konstanty
    private static final int WIDTH   = 800;
    private static final int HEIGHT  = 800;
    private static final int BUTTON_SPACE_WIDTH = 300;
    private static final int BUTTON_SPACE_HEIGHT = 300;
    private static final int BUTTON_SPACE_OFFSET = (HEIGHT - BUTTON_SPACE_HEIGHT) / 2;
    private static final int BUTTON_SPACING = 20;
    private static final int BUTTON_WIDTH = BUTTON_SPACE_WIDTH / 2 - BUTTON_SPACING *2;
    private static final int BUTTON_HEIGHT = BUTTON_SPACE_HEIGHT / 2 - BUTTON_SPACING *2;
    private static final int NUM_BUTTONS = 9;
    private static final int START_BUTTON_WIDTH = 120;
    private static final int START_BUTTON_HEIGHT = 50;
    private static final int DELAY = 20;

    private static final Color BACKGROUND_COLOR = new Color(200,212,231);
    private static final Color BUTTON_INACTIVE_COLOR = new Color(62, 48, 255);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);


    private boolean gameRunning;
    private boolean gameOver;
    private boolean advancingLevel;
    private boolean displayingPattern;
    private int ticks;
    private int playerPatternIndex;
    private int gamePatternIndex;

    /**
     * Inicializuje objekt MemorySequence, vytváří herní okno a tlačítko pro spuštění hry.
     */
    public MemorySequence() {
        createFrame();
        createStartButton();

        scoreBoard = new ScoreBoard();
        buttons = new ButtonsC[NUM_BUTTONS];
        timer = new Timer(DELAY, this);
        gameRunning = false;
        gameOver = false;
        advancingLevel = false;
        displayingPattern = false;
        ticks = 0;
        initializeButtons();
        repaint();
    }

    /**
     * Vytvoří herní okno.
     */
    private void createFrame() {
        JFrame frame = new JFrame("Memory Sequence");
        frame.setSize(WIDTH, HEIGHT);
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
        frame.setVisible(true);
        frame.add(this);
        frame.addMouseListener(this);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Vytvoří tlačítko pro spuštění hry.
     */
    private void createStartButton() {
        button = new JButton("PLAY!");
        button.setBackground(BUTTON_INACTIVE_COLOR);
        button.setForeground(BACKGROUND_COLOR);
        button.setFocusPainted(false);
        int offset_x = WIDTH/2 - START_BUTTON_WIDTH/2;
        int offset_y = HEIGHT - 110;
        button.setBounds(offset_x, offset_y, START_BUTTON_WIDTH, START_BUTTON_HEIGHT);
        button.addActionListener(new StartButtonListener());
        setLayout(null);
        add(button);
    }

    /**
     * Inicializuje tlačítka na herním poli.
     */
    private void initializeButtons() {
        // Rozmístí tlačítka do 3x3 mřížky
        int firstColumnOffsetX = BUTTON_SPACING + 195;
        int secondColumnOffsetX = WIDTH/2 - BUTTON_WIDTH/2;
        int thirdColumnOffsetX = BUTTON_SPACING + BUTTON_WIDTH + BUTTON_SPACING + BUTTON_WIDTH + BUTTON_SPACING + 195;
        int firstRowOffsetY = BUTTON_SPACE_OFFSET + BUTTON_SPACING - 20;
        int secondRowOffsetY = BUTTON_SPACE_OFFSET + BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING - 20;
        int thirdRowOffsetY = BUTTON_SPACE_OFFSET + BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING + BUTTON_HEIGHT + BUTTON_SPACING - 20;

        buttons[0] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, firstColumnOffsetX, firstRowOffsetY);
        buttons[1] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, secondColumnOffsetX, firstRowOffsetY);
        buttons[2] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, thirdColumnOffsetX, firstRowOffsetY);
        buttons[3] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, firstColumnOffsetX, secondRowOffsetY);
        buttons[4] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, secondColumnOffsetX, secondRowOffsetY);
        buttons[5] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, thirdColumnOffsetX, secondRowOffsetY);
        buttons[6] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, firstColumnOffsetX, thirdRowOffsetY);
        buttons[7] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, secondColumnOffsetX, thirdRowOffsetY);
        buttons[8] = new ButtonsC(BUTTON_INACTIVE_COLOR, BUTTON_WIDTH, BUTTON_HEIGHT, thirdColumnOffsetX, thirdRowOffsetY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (memorySequence != null) {
            paint((Graphics2D) g);
        }
    }

    /**
     * Kreslí herní pole, tlačítka a aktuální stav hry.
     */
    private void paint(Graphics2D g) {
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (int i = 0; i < NUM_BUTTONS; i++) {
            buttons[i].drawButtons(g);
        }

        if (gameRunning && !gameOver) {
            button.setText("HRAJE SE...");
        } else if (!gameRunning && gameOver) {
            button.setText("ZNOVU");
        } else {
            button.setText("HRAJ");
        }

        g.setColor(TEXT_COLOR);
        g.setFont(new Font("Ariel", Font.PLAIN, 23));
        g.drawString("Úroveň:  " + scoreBoard.getLevel(), (WIDTH/2) - 40, 225);
        g.drawString("Skóre:  " + scoreBoard.getScore(), (WIDTH/2) - 45, 200);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Konec hry", (WIDTH / 2) - 40, 100);
        } else if (gameRunning && !displayingPattern && !advancingLevel) {
            g.setColor(Color.BLACK);
            g.drawString("Tvůj tah!", (WIDTH / 2) - 40, 100);
        } else if (gameRunning && advancingLevel) {
            g.setColor(Color.BLACK);
            g.drawString("Úroveň " + scoreBoard.getLevel(), (WIDTH / 2) - 20, 100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameRunning)
            return;

        if (gameOver) {
            gameOver();
            repaint();
            return;
        }

        ticks++;
        if (displayingPattern) {
            if (ticks % 40 == 0) {
                advancePattern();
                ticks = 0;
            }
            else if (ticks % 20 == 0)
                triggerAllFlashing(false);
        }

        else if (advancingLevel) {
            triggerAllFlashing(true);
            if (ticks % 60 == 0) {
                triggerAllFlashing(false);
                startNextLevel();
                ticks = 0;
            }
        }
        else {
            if (ticks % 20 == 0) {
                triggerAllFlashing(false);
                ticks = 0;
            }
        }
        repaint();
    }

    /**
     * Spustí hru a připraví herní prvek.
     */
    private void startGame() {
        timer.start();
        triggerAllFlashing(false);
        gameRunning = true;
        gameOver = false;
        scoreBoard.reset();
        scoreBoard.nextLevel();
        startPattern();
    }

    /**
     * Inicializuje novou herní sekvenci pro aktuální úroveň.
     */
    private void startPattern() {
        pattern = new Pattern(scoreBoard.getLevel());
        gamePatternIndex = 0;
        playerPatternIndex = 0;
        displayingPattern = true;
    }

    /**
     * Zobrazí aktuální část herní sekvence.
     */
    private void advancePattern() {
        if (gamePatternIndex >= pattern.getLength()) {
            displayingPattern = false;
            return;
        }
        buttons[pattern.getAtIndex(gamePatternIndex)].setFlashing(true);
        gamePatternIndex++;
    }

    /**
     * Posune hru na další úroveň.
     */
    private void advanceLevel() {
        advancingLevel = true;
        scoreBoard.nextLevel();
    }

    /**
     * Spustí novou úroveň.
     */
    private void startNextLevel() {
        advancingLevel = false;
        pattern = null;
        startPattern();
    }

    /**
     * Ukončí hru a zobrazí konec hry.
     */
    private void gameOver() {
        alertGameOver(true);
        gameRunning = false;
        gameOver = true;
        timer.stop();
    }

    /**
     * Zobrazí konec hry na všech tlačítcích.
     */
    private void alertGameOver(boolean bool) {
        for (int i = 0; i< NUM_BUTTONS; i++){
            buttons[i].setGameOver(bool);
        }
    }

    /**
     * Aktivuje nebo deaktivuje blikání na všech tlačítkách.
     */
    private void triggerAllFlashing(boolean bool) {
        for (int i = 0; i< NUM_BUTTONS; i++)
            buttons[i].setFlashing(bool);
    }

    /**
     * Posluchač pro tlačítko pro spuštění hry.
     */
    private class StartButtonListener implements ActionListener {
        private StartButtonListener(){}
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameRunning) {
                alertGameOver(false);
                startGame();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameRunning && !displayingPattern && !advancingLevel) {
            int indexOfButtonPressed = buttonWithinCoords(e.getX(), e.getY());
            if (indexOfButtonPressed == -1) {
                return;
            }

            buttons[indexOfButtonPressed].setFlashing(true);
            repaint();
            ticks = 0;

            if (indexOfButtonPressed == pattern.getAtIndex(playerPatternIndex)) {
                scoreBoard.increaseScore();
                playerPatternIndex++;
                if (playerPatternIndex >= pattern.getLength()){
                    advanceLevel();
                }
            } else {
                gameOver = true;
            }
        }
    }

    /**
     * Zjistí, zda bylo stisknuto tlačítko na daných souřadnicích.
     * @param x Souřadnice x.
     * @param y Souřadnice y.
     * @return Index tlačítka, nebo -1 pokud nebylo stisknuto žádné tlačítko.
     */
    private int buttonWithinCoords(int x, int y) {
        for (int i = 0; i< NUM_BUTTONS; i++) {
            if (buttons[i].isWithinCoords(x,y)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    public static void main(String[] args) {
        new MemorySequence();
    }
}
