package MyPackage;
import java.awt.Font;
import java.awt.event.*;  
import javax.swing.*;


public class Board {
    
    public static final int BUTTON_FONT_SIZE = 50; 
    public static final int STATUS_FONT_SIZE = 20; 
    public static final String WELCOME_MESSAGE = "Play Tic Tac Toe! X is first."; 

    int width = 3; 
    int height = 3; 

    int pxHeight = 500; 
    int pxWidth = 500; 
    int pxHeightOffset = 100; 

    JButton[][] array; 
    JLabel status; 
    JButton resetBtn; 
    JFrame frame; 

    char xChar = 'X'; 
    char oChar = 'O'; 

    char current = xChar; 

    public Board() {
        array = new JButton[width][height];

        frame = new JFrame("Tic Tac Toe");
        frame.setSize(pxWidth, pxHeight + pxHeightOffset);
        frame.getContentPane().setLayout(null);

        createButtons();
        configureResetBtn();
        configureStatus();

        frame.setVisible(true);
    }

    void iterate(BoardIteration bi) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bi.exectute(x, y);
            } 
        }
    }

    void configureResetBtn() {
        resetBtn = new JButton("Reset");
        resetBtn.setBounds(pxWidth/2 - 50, pxHeight + 5, 100, 30);

        frame.getContentPane().add(resetBtn, SwingConstants.CENTER);

        resetBtn.addActionListener(a -> reset());
    }

    void reset() {
        iterate((int x, int y) -> {
            JButton b = array[x][y]; 

            b.setEnabled(true);
            b.setText("");

            status.setText(WELCOME_MESSAGE);
        });

    }

    void configureStatus() {
        status = new JLabel(WELCOME_MESSAGE, SwingConstants.CENTER); 

        status.setBounds(0, pxHeight, pxWidth,  100);
        status.setFont(new Font(Font.SANS_SERIF, Font.BOLD, STATUS_FONT_SIZE));
        frame.getContentPane().add(status);
    }

    void createButtons() {
        iterate((int x, int y) -> {
            JButton b = createButton(x, y); 
            array[x][y] = b;  
            frame.add(b); 
        }); 
    }
    

    void place(JButton button, int x, int y) {

        button.setText(Character.toString(current));
        button.setEnabled(false);

        if (testDraw()) {onDraw();}
        if (testWin()) {onWin();}

        current = (current == xChar) ? oChar: xChar; 
    }

    void onDraw() {
        status.setText("It's a draw!");
    }

    void onWin() {
        status.setText(Character.toString(current) + "'s won the game!");

        iterate((int x, int y) -> 
            array[x][y].setEnabled(false)
        );
    }

    boolean testWin() { 
        for (int i = 0; i < array.length; i++) {
            if (testRow(1, i, 1, 0) || testRow(i, 1, 0, 1)) {
                return true; 
            }
        }
        return testRow(1, 1, 1, 1) || testRow(1, 1, 1, -1); 
    }

    boolean testDraw() {
        for (JButton[] x : array) {
            for (JButton y : x) {
                if (y.isEnabled()) {
                    return false; 
                }
            }
        }

        return true; 
    }

    boolean testRow(int x, int y, int xDirection, int yDirection) {
        int[] positiveCoords = {x + xDirection, y + yDirection}; 
        int[] negativeCoords = {x - xDirection, y - yDirection};
        
        return ((getButton(negativeCoords).getText().equals(Character.toString(current)))
            && (getButton(positiveCoords).getText().equals(Character.toString(current)))
            && (getButton(new int[] {x, y}).getText().equals(Character.toString(current))));  
    }
    
    JButton getButton(int[] coords) {
        return array[coords[0]][coords[1]]; 
    }

    public JButton createButton(int x, int y) {
        JButton button = new JButton("");

        button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, BUTTON_FONT_SIZE));

        int sizeX = Math.floorDiv(pxWidth, width); 
        int sizeY = Math.floorDiv(pxHeight, height); 

        button.setBounds(x * sizeX, y * sizeY, sizeX, sizeY);

        button.addActionListener((ActionEvent a) -> place(button, x, y));

        return button;  
    }

}
