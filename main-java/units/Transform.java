package units;

/**
 * @author ZoftWhere
 * @updated Transform.java 2007 ZoftWhere 1:09:26 PM
 */
public class Transform {

  /**
   * @param board
   */
  public static void displayPuzzle(int[] board) {
    if (board.length != 81) {
      System.err.println("There is an error!! ... displayPuzzle");
    } else {
      for (int i = 1; i <= 81; i++) {

        if (board[i - 1] == 0) System.out.print(' ');
        else System.out.print(board[i - 1]);

        if (((i - 1) % 9) == 8) System.out.println();
        else System.out.print(',');
      }
    }
  }

  /**
   * @param board int [81]
   */
  public static void lesser(int[] board) {
    int[] replaceList = new int[10];
    int next = 1;
    int position = 0;

    while ((position <= 80) && (next <= 9)) {
      if ((board[position] > 0) && (board[position] < 10) && (replaceList[board[position]] == 0)) {
        replaceList[board[position]] = next;
        next += 1;
      }
      position += 1;
    }

    for (position = 0; position <= 80; position++) {
      if ((board[position] > 0) && (board[position] < 10)) {
        board[position] = replaceList[board[position]];
      }
    }
  }

  /**
   * @param matrix1 int [81]
   * @param matrix2 int [81]
   * @return boolean (whether matrices have equal contents).
   */
  public static boolean matrixEqual(int[] matrix1, int[] matrix2) {
    int i = 0;

    while ((i <= 80) && (matrix1[i] == matrix2[i])) {
      i++;
    }

    if (i == 81) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param replaceList int[9]
   * @param permutation 0...362879
   */
  public static void transformer(int[] replaceList, int permutation) {
    int tempPermutation = permutation;
    // Setup defaults for permutation.
    int[] numberList = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    int choice = 0;

    int modulus = 1;
    for (int i = 2; i <= 9; i++) {
      modulus = modulus * i;
    }
    tempPermutation %= modulus;

    // Create replace list.
    for (int i = 9; i >= 1; i--) {
      modulus = modulus / i;
      choice = (tempPermutation / modulus) % i;

      replaceList[9 - i] = numberList[choice];

      for (int j = choice; j <= 7; j++) numberList[j] = numberList[j + 1];
    }
  }

  /**
   * @param board
   * @param permutation int 0...362879
   */
  public static void transformNumbers(int[] board, int permutation) {
    // Setup defaults for permutation.
    int[] replaceList = new int[9];
    transformer(replaceList, permutation);

    // Replace numbers in board
    for (int i = 0; i <= 80; i++) {
      if (board[i] != 0) {
        board[i] = replaceList[board[i] - 1];
      }
    }
  }

  /**
   * @param orignalBoard int [81]
   * @param replaceBoard int [81]
   * @param permutation int 0...3359231
   */
  public static void transformPositions(int[] originalBoard, int[] replaceBoard, int permutation) {
    int tempPermutation = permutation;
    int[] x = new int[9];
    int[] y = new int[9];

    int columns = tempPermutation % 6;
    tempPermutation /= 6;
    columns += (tempPermutation % 2) * 24;
    tempPermutation /= 2;
    columns += (tempPermutation % 3) * 120;
    tempPermutation /= 3;
    columns += (tempPermutation % 2) * 450;
    tempPermutation /= 2;
    columns += (tempPermutation % 2) * 5040;
    tempPermutation /= 2;
    columns += (tempPermutation % 3) * 40320;
    tempPermutation /= 3;
    columns += (tempPermutation % 3) * 138240;
    tempPermutation /= 3;

    int rows = tempPermutation % 6;
    tempPermutation /= 6;
    rows += (tempPermutation % 2) * 24;
    tempPermutation /= 2;
    rows += (tempPermutation % 3) * 120;
    tempPermutation /= 3;
    rows += (tempPermutation % 2) * 450;
    tempPermutation /= 2;
    rows += (tempPermutation % 2) * 5040;
    tempPermutation /= 2;
    rows += (tempPermutation % 3) * 40320;
    tempPermutation /= 3;
    rows += (tempPermutation % 3) * 138240;
    tempPermutation /= 3;

    int rotation = tempPermutation % 2;
    tempPermutation /= 2;

    transformer(x, columns);
    transformer(y, rows);

    if (rotation == 0) {
      for (int i = 0; i <= 80; i++) {
        replaceBoard[i] = originalBoard[x[i % 9] + 9 * y[i / 9] - 10];
      }
    } else {
      for (int i = 0; i <= 80; i++) {
        replaceBoard[i] = originalBoard[y[i % 9] + 9 * x[i / 9] - 10];
      }
    }
  }

  /**
   * @param board int [81]
   * @param permutation int 0...3359231
   */
  public static void transformPositions(int[] board, int permutation) {
    int[] replaceBoard = new int[81];

    transformPositions(board, replaceBoard, permutation);

    for (int i = 0; i <= 80; i++) {
      board[i] = replaceBoard[i];
    }
  }

  /**
   * @param board int [81]
   * @param row int 1...9
   * @param input String
   */
  public static void setRow(int[] board, int row, String input) {
    int position = (row - 1) * 9;
    char piece = ' ';

    if (input.length() != 9) {
      System.err.print("input for row ");
      System.err.print(row);
      System.err.println(" to short/long");
    } else {
      for (int loop = 1; loop <= 9; loop++) {
        piece = input.charAt(loop - 1);
        board[position + loop - 1] = piece - 48;
      }
    }
  }

  public static void setBoard(
      int[] board,
      String row1,
      String row2,
      String row3,
      String row4,
      String row5,
      String row6,
      String row7,
      String row8,
      String row9) {
    setRow(board, 1, row1);
    setRow(board, 2, row2);
    setRow(board, 3, row3);
    setRow(board, 4, row4);
    setRow(board, 5, row5);
    setRow(board, 6, row6);
    setRow(board, 7, row7);
    setRow(board, 8, row8);
    setRow(board, 9, row9);
  }
}
