package _main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
import units.*;

/**
 * @author ZoftWhere
 * @updated 04 Aug 2008
 */
public class SudokuGUI {

  static int[] defaultBoard = new int[81];
  static int[] board = new int[81];
  static JButton[] buttons = new JButton[81];
  static ImageIcon[] buttonIcons = new ImageIcon[532];
  static JRadioButton[] radios = new JRadioButton[10];
  static int numberSelection = 0;
  static Random random = new Random();
  static int boardNumberPermutation = random.nextInt(362880);
  static int boardPositionPermutation = random.nextInt(3359232);

  /** Action listener created specifically for the menu. */
  private static ActionListener menuAction =
      new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent event) {
          String action = event.getActionCommand();

          // File Menu
          if (action.compareTo("Open") == 0) {
            //
          } else if (action.compareTo("Save") == 0) {
            //
          } else if (action.compareTo("Quit") == 0) {
            System.exit(0);
          }

          // Game Menu
          else if (action.compareTo("New") == 0) {
            // Setup a default board
            for (int i = 0; i <= 80; i++) {
              board[i] = defaultBoard[i];
            }

            // Set new permutation for puzzle.
            boardNumberPermutation = random.nextInt(362880);
            boardPositionPermutation = random.nextInt(3359232);

            // Transform board to new puzzle.
            Transform.transformNumbers(board, boardNumberPermutation);
            Transform.transformPositions(board, boardNumberPermutation);

            // Set the new icons.
            for (int i = 0; i <= 80; i++) {
              buttons[i].setIcon(buttonIcons[board[i]]);
            }
          } else if (action.compareTo("Restart") == 0) {
            // Setup a default board
            for (int i = 0; i <= 80; i++) {
              board[i] = defaultBoard[i];
            }

            // Transform board to puzzle.
            Transform.transformNumbers(board, boardNumberPermutation);
            Transform.transformPositions(board, boardNumberPermutation);

            // Set the new icons.
            for (int i = 0; i <= 80; i++) {
              buttons[i].setIcon(buttonIcons[board[i]]);
            }
          } else if (action.compareTo("Solve") == 0) {
            // Clear user input.
            for (int i = 0; i <= 80; i++) {
              if (board[i] > 9) {
                board[i] = 0;
              }
            }

            // Solve the board
            Solver.sudokuSolve(board);
            for (int i = 0; i <= 80; i++) {
              board[i] = Solver.firstSolution[i];
            }

            // Set the new icons.
            for (int i = 0; i <= 80; i++) {
              buttons[i].setIcon(buttonIcons[board[i]]);
            }
          }

          // Help Menu
          else if (action.compareTo("About") == 0) {

          }

          // Default Catch
          else {
            System.err.println("Action listener attatched to unlisted: " + event.getSource());
          }
        }
      };

  /** Mouse wheel listener for the frame and panels. */
  static MouseWheelListener mouseWheeler =
      new MouseWheelListener() {

        public void mouseWheelMoved(MouseWheelEvent e) {
          int d = e.getWheelRotation();

          if (d > 0) d = 1;
          else if (d < 0) d = -1;

          numberSelection = (numberSelection - d + 10) % 10;
          radios[numberSelection].setSelected(true);
        }
      };

  /** Mouse listener for the number button mouse. */
  static MouseListener buttonMouse =
      new MouseListener() {

        public void mouseClicked(MouseEvent mouseEvent) {
          // Determine which button was clicked.
          int i = Integer.valueOf(mouseEvent.getComponent().getName()).intValue();

          // Update the button ...
          if ((board[i] == 0) || (board[i] >= 10)) {
            if ((mouseEvent.getButton() == 1)) {
              // Clear the button is number selection is zero.
              if (numberSelection == 0) {
                if (board[i] == 0) {
                  board[i] = 0x0001ff00;
                  buttons[i].setIcon(buttonIcons[(board[i] >> 8) + 20]);
                } else {
                  board[i] = 0;
                  buttons[i].setIcon(buttonIcons[0]);
                }
              }
              // Update numbers on button.
              else {
                // If it is a blank button place number selection on button.
                if ((board[i] & 0xff) == 0) {
                  board[i] = board[i] + numberSelection + 10;
                  buttons[i].setIcon(buttonIcons[numberSelection + 10]);
                }
                // If the number on button is same as number selection adjust indices.
                else if ((board[i] & 0xff) == (numberSelection + 10)) {
                  board[i] = (board[i] & 0x00ffff00) ^ (0x01 << (numberSelection + 7));
                  buttons[i].setIcon(buttonIcons[(board[i] >> 8) + 20]);
                }
                // If the number is not the same as the number selection
                // Place number on button and clear indices.
                else {
                  board[i] = numberSelection + 10;
                  buttons[i].setIcon(buttonIcons[numberSelection + 10]);
                }
              }
            } else if (mouseEvent.getButton() == 3) {
              // Clear the button is number selection is zero.
              if (numberSelection == 0) {
                board[i] = 0;
                buttons[i].setIcon(buttonIcons[0]);
              }
              // Update indices on button.
              else {
                if ((board[i] & 0xff) != 0) {
                  board[i] = (0x01 << (numberSelection + 7));
                } else {
                  board[i] = (board[i] & 0x00ffff00) ^ (0x01 << (numberSelection + 7));
                }

                if (board[i] == 0) {
                  buttons[i].setIcon(buttonIcons[0]);
                } else {
                  buttons[i].setIcon(buttonIcons[(board[i] >> 8) + 20]);
                }
              }
            } else {
            }
          }

          // If the puzzle is complete ...
          if (Solver.isSolved(board)) {
            // ... set each number to given numbers.
            for (int j = 0; j <= 80; j++) {
              board[j] = (board[j] & 0xff) % 10;
              buttons[j].setIcon(buttonIcons[board[j]]);
            }
          }
        }

        public void mouseEntered(MouseEvent mouseEvent) {}

        public void mouseExited(MouseEvent mouseEvent) {}

        public void mousePressed(MouseEvent mouseEvent) {}

        public void mouseReleased(MouseEvent mouseEvent) {}
      };

  /** Action listener created specifically for number selection action. */
  static ActionListener radioAction =
      new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          numberSelection = Double.valueOf(e.getActionCommand()).intValue();
        }
      };

  public static void main(String[] Args) {
    // Setup a defualt board
    //		Transform.setRow (defaultBoard, 1, "071040360");
    //		Transform.setRow (defaultBoard, 2, "500601004");
    //		Transform.setRow (defaultBoard, 3, "300508009");
    //		Transform.setRow (defaultBoard, 4, "013000570");
    //		Transform.setRow (defaultBoard, 5, "700000002");
    //		Transform.setRow (defaultBoard, 6, "056000840");
    //		Transform.setRow (defaultBoard, 7, "100904003");
    //		Transform.setRow (defaultBoard, 8, "600705008");
    //		Transform.setRow (defaultBoard, 9, "095020610");

    // Setup board No. 26 (Beeld 2008-02-01)
    Transform.setRow(defaultBoard, 1, "004010000");
    Transform.setRow(defaultBoard, 2, "000008300");
    Transform.setRow(defaultBoard, 3, "708002400");
    Transform.setRow(defaultBoard, 4, "070009600");
    Transform.setRow(defaultBoard, 5, "000081020");
    Transform.setRow(defaultBoard, 6, "000030800");
    Transform.setRow(defaultBoard, 7, "090003000");
    Transform.setRow(defaultBoard, 8, "001724900");
    Transform.setRow(defaultBoard, 9, "507000080");

    // HARD
    //		Transform.setRow (defaultBoard, 1, "005100040");
    //		Transform.setRow (defaultBoard, 2, "600000980");
    //		Transform.setRow (defaultBoard, 3, "010090020");
    //		Transform.setRow (defaultBoard, 4, "070201000");
    //		Transform.setRow (defaultBoard, 5, "020006700");
    //		Transform.setRow (defaultBoard, 6, "503000000");
    //		Transform.setRow (defaultBoard, 7, "700000000");
    //		Transform.setRow (defaultBoard, 8, "040832500");
    //		Transform.setRow (defaultBoard, 9, "000700810");

    // Fiendishly Hard.
    //		Transform.setRow (defaultBoard, 1, "006000800");
    //		Transform.setRow (defaultBoard, 2, "073802590");
    //		Transform.setRow (defaultBoard, 3, "180000026");
    //		Transform.setRow (defaultBoard, 4, "000974000");
    //		Transform.setRow (defaultBoard, 5, "000050000");
    //		Transform.setRow (defaultBoard, 6, "000138000");
    //		Transform.setRow (defaultBoard, 7, "950000083");
    //		Transform.setRow (defaultBoard, 8, "061703450");
    //		Transform.setRow (defaultBoard, 9, "002000900");

    // Transform board to new puzzle.
    for (int i = 0; i <= 80; i++) {
      board[i] = defaultBoard[i];
    }

    Transform.transformNumbers(board, boardNumberPermutation);
    Transform.transformPositions(board, boardPositionPermutation);

    // Set the look and feel.
    // Make sure we have nice window decorations.
    // Create and set up the window.
    try {
      //			UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      //			UIManager.setLookAndFeel ("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
    } catch (Exception e) {
      System.err.println(
          "Couldn't get specified look and feel (" + "System" + "), for some reason.");
      System.err.println("Using the default look and feel.");
      e.printStackTrace();
    }

    JFrame frame = new JFrame("Osmund's Java Sudoku for Beginners");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    JMenuItem fileMenuOpen = new JMenuItem("Open");
    JMenuItem fileMenuSave = new JMenuItem("Save");
    JMenuItem fileMenuQuit = new JMenuItem("Quit");
    JMenu gameMenu = new JMenu("Game");
    JMenuItem gameMenuNew = new JMenuItem("New");
    JMenuItem gameMenuRestart = new JMenuItem("Restart");
    JMenuItem gameMenuSolve = new JMenuItem("Solve");
    JMenu helpMenu = new JMenu("Help");
    JMenuItem helpMenuAbout = new JMenuItem("About");
    JPanel puzzlePanel = new JPanel();
    JPanel[] numberPanels = new JPanel[9];
    JPanel selectionPanel = new JPanel();

    // Setup the menu bar ...

    menuBar.setFocusable(false);
    menuBar.setName("MenuBar");
    menuBar.setLayout(new FlowLayout(0));
    menuBar.setBorder(BorderFactory.createEmptyBorder());

    menuBar.add(fileMenu);
    menuBar.add(gameMenu);
    menuBar.add(helpMenu);

    fileMenu.setMnemonic(KeyEvent.VK_F);
    gameMenu.setMnemonic(KeyEvent.VK_G);
    helpMenu.setMnemonic(KeyEvent.VK_H);

    fileMenu.add(fileMenuOpen);
    fileMenu.add(fileMenuSave);
    fileMenu.add(fileMenuQuit);

    fileMenuOpen.addActionListener(menuAction);
    fileMenuSave.addActionListener(menuAction);
    fileMenuQuit.addActionListener(menuAction);

    gameMenu.add(gameMenuNew);
    gameMenu.add(gameMenuRestart);
    gameMenu.add(gameMenuSolve);

    gameMenuNew.addActionListener(menuAction);
    gameMenuRestart.addActionListener(menuAction);
    gameMenuSolve.addActionListener(menuAction);

    helpMenu.add(helpMenuAbout);

    helpMenuAbout.addActionListener(menuAction);

    // Setup the main puzzle panel ...
    puzzlePanel.setLayout(new GridLayout(3, 3));
    puzzlePanel.setBorder(BorderFactory.createEmptyBorder());
    puzzlePanel.addMouseWheelListener(mouseWheeler);

    // Create the 81 buttons
    for (int i = 0; i <= 80; i++) {
      buttons[i] = new JButton();
      buttons[i].setPreferredSize(new Dimension(42, 42));
      buttons[i].setFocusable(false);
      buttons[i].setName("" + i);
      buttons[i].setActionCommand("" + i);
      buttons[i].addMouseListener(buttonMouse);
      buttons[i].setBorder(BorderFactory.createRaisedBevelBorder());
    }

    // Create the nine block number panels ...
    // Attach to each the correct 9 buttons ...

    for (int p = 0; p <= 8; p++) {
      JPanel tempPanel = new JPanel();
      tempPanel.setLayout(new GridLayout(3, 3));
      tempPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
      numberPanels[p] = tempPanel;
      puzzlePanel.add(numberPanels[p]);

      for (int n = 0; n <= 8; n++) {
        numberPanels[p].add(buttons[(n % 3) + (p % 3) * 3 + (n / 3) * 9 + (p / 3) * 27]);
      }
    }

    // Setup the selection panel.
    // Ensure that the radio buttons are in a group.
    // Add mnemonics for each number.

    selectionPanel.setBorder(BorderFactory.createEmptyBorder());
    selectionPanel.setLayout(new BoxLayout(selectionPanel, 2));
    selectionPanel.addMouseWheelListener(mouseWheeler);

    ButtonGroup radioGroup = new ButtonGroup();

    for (int i = 0; i <= 9; i++) {
      radios[i] = new JRadioButton("" + i);
      radios[i].setName("Radio" + i);
      radios[i].setActionCommand("" + i);
      radios[i].addActionListener(radioAction);
      radioGroup.add(radios[i]);
      selectionPanel.add(radios[i]);
    }

    selectionPanel.setBackground(radios[0].getBackground());

    radios[0].setMnemonic(KeyEvent.VK_0);
    radios[1].setMnemonic(KeyEvent.VK_1);
    radios[2].setMnemonic(KeyEvent.VK_2);
    radios[3].setMnemonic(KeyEvent.VK_3);
    radios[4].setMnemonic(KeyEvent.VK_4);
    radios[5].setMnemonic(KeyEvent.VK_5);
    radios[6].setMnemonic(KeyEvent.VK_6);
    radios[7].setMnemonic(KeyEvent.VK_7);
    radios[8].setMnemonic(KeyEvent.VK_8);
    radios[9].setMnemonic(KeyEvent.VK_9);

    radios[0].setText("�");
    radios[numberSelection].setSelected(true);

    // Load program icon.
    try {
      frame.setIconImage(new ImageIcon(SudokuGUI.class.getResource("/images/icon.png")).getImage());
    } catch (Exception e1) {
      e1.printStackTrace();
    }

    frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
    frame.add(menuBar);
    frame.add(puzzlePanel);
    frame.add(selectionPanel);
    frame.pack();
    frame.setResizable(false);

    // Load button icons.
    try {

      BufferedImage bufferedImage = null;
      BufferedImage indexImage = null;

      int[] blankTile = new int[13 * 13];

      for (int i = 0; i < blankTile.length; i++) blankTile[i] = 0xffffffff;

      // Puzzle given number icons.
      bufferedImage = ImageIO.read(SudokuGUI.class.getResource("/images/_p.png"));
      for (int i = 0; i <= 9; i++)
        buttonIcons[i] = new ImageIcon((Image) (bufferedImage.getSubimage(i * 40, 0, 40, 40)));

      // User placed number icons.
      bufferedImage = ImageIO.read(SudokuGUI.class.getResource("/images/_u.png"));
      for (int i = 0; i <= 9; i++)
        buttonIcons[i + 10] = new ImageIcon((Image) (bufferedImage.getSubimage(i * 40, 0, 40, 40)));

      // User index mark icons.
      indexImage = ImageIO.read(SudokuGUI.class.getResource("/images/full_indice.png"));
      for (int i = 0; i <= 511; i++) {
        bufferedImage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
        bufferedImage.setRGB(
            0, 0, 40, 40, indexImage.getRGB(0, 0, 40, 40, new int[40 * 40], 0, 40), 0, 40);

        if ((i & 0x001) == 0) bufferedImage.setRGB(1, 1, 13, 13, blankTile, 0, 13);
        if ((i & 0x002) == 0) bufferedImage.setRGB(14, 1, 13, 13, blankTile, 0, 13);
        if ((i & 0x004) == 0) bufferedImage.setRGB(27, 1, 13, 13, blankTile, 0, 13);
        if ((i & 0x008) == 0) bufferedImage.setRGB(1, 14, 13, 13, blankTile, 0, 13);
        if ((i & 0x010) == 0) bufferedImage.setRGB(14, 14, 13, 13, blankTile, 0, 13);
        if ((i & 0x020) == 0) bufferedImage.setRGB(27, 14, 13, 13, blankTile, 0, 13);
        if ((i & 0x040) == 0) bufferedImage.setRGB(1, 27, 13, 13, blankTile, 0, 13);
        if ((i & 0x080) == 0) bufferedImage.setRGB(14, 27, 13, 13, blankTile, 0, 13);
        if ((i & 0x100) == 0) bufferedImage.setRGB(27, 27, 13, 13, blankTile, 0, 13);

        buttonIcons[i + 20] = new ImageIcon((Image) bufferedImage);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Attach icons to the buttons.
    for (int i = 0; i <= 80; i++) {
      buttons[i].setIcon(buttonIcons[board[i]]);
    }

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();

    Rectangle screenBounds =
        GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .getBounds();
    frame.setLocation(
        (screenBounds.width - frame.getBounds().width) / 2,
        (screenBounds.height - frame.getBounds().height) / 2);
    frame.setVisible(true);
  }
}
