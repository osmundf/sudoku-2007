package units;

/**
 * @author ZoftWhere
 * @version 31 October 2007
 */
public class Solver {
  public static int[] firstSolution = new int[81];

  public static boolean isSolved(int[] board) {
    // Creat local variables.
    int c = 0;
    int r = 0;
    int b = 0;
    int s = 0;
    int i = 0;

    // Check for zero-value entries.
    for (i = 0; i <= 80; i++) {
      if ((board[i] & 0xff) == 0) return false;
    }

    // Create cross-reference tables.
    int[] columns = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] rows = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] blocks = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Update cross-reference tables.
    for (i = 0; i <= 80; i++) {
      c = (i % 9);
      r = (int) (i / 9);
      b = (int) (c / 3) + 3 * (int) (r / 3);
      s = 1 << ((board[i] & 0xff) % 10 - 1);

      if ((columns[c] & s) != 0) return false;
      else columns[c] |= s;

      if ((rows[r] & s) != 0) return false;
      else rows[r] |= s;

      if ((blocks[b] & s) != 0) return false;
      else blocks[b] |= s;
    }

    // The board complies with the cross-referencing requirements.
    return true;
  }

  public static int sudokuSolve(int[] userBoard) {
    // Creat local variables.
    int[] board = new int[81];
    int c = 0;
    int r = 0;
    int b = 0;
    int s = 0;
    int n = 0;
    int i = 0;
    int found = 0;

    // Create cross-reference tables.
    int[] columns = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] rows = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] blocks = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    // Fill in specified numbers.
    // Take note that solver uses numbers 10..19
    for (i = 0; i <= 80; i++) {
      if ((userBoard[i] >= 1) && (userBoard[i] <= 9)) {
        board[i] = userBoard[i];
      } else {
        board[i] = 10;
      }
    }

    // Update cross-reference tables.
    for (i = 0; i <= 80; i++) {
      if (board[i] < 10) {
        c = (i % 9);
        r = (int) (i / 9);
        b = (int) (c / 3) + 3 * (int) (r / 3);
        s = 1 << (board[i] % 10 - 1);

        if (((columns[c] & s) != 0) || ((rows[r] & s) != 0) || ((blocks[b] & s) != 0)) {
          return -1;
        }

        columns[c] |= s;
        rows[r] |= s;
        blocks[b] |= s;
      }
    }

    // Go to first ten-value square.
    i = 0;
    while ((i <= 80) && (board[i] != 10)) {
      i += 1;
    }

    // Return 1 if already solved (no ten-value).
    if (i == 81) {
      return 1;
    }

    // Setup initial column, row and block values.
    c = (i % 9);
    r = (int) (i / 9);
    b = (int) (c / 3) + 3 * (int) (r / 3);
    n = 10;

    // Start solving process.
    while ((i >= 0) && (found <= 1)) {
      // Options exhausted.
      if (n == 19) {
        // Clear current square.
        board[i] = 10;

        // Go to previous entry square
        do {
          i -= 1;
        } while ((i >= 0) && (board[i] <= 9));

        // Update cross-reference
        if (i >= 0) {
          n = board[i];
          s = 1 << (n % 10 - 1);

          c = (i % 9);
          r = (int) (i / 9);
          b = (int) (c / 3) + 3 * (int) (r / 3);

          columns[c] ^= s;
          rows[r] ^= s;
          blocks[b] ^= s;
        }
      }
      // Next option.
      else {
        n += 1;
        s = 1 << (n % 10 - 1);

        if (((columns[c] & s) == 0) && ((rows[r] & s) == 0) && ((blocks[b] & s) == 0)) {
          // Store entry.
          board[i] = n;

          // Update cross-reference.
          columns[c] |= s;
          rows[r] |= s;
          blocks[b] |= s;

          // Go to next ten-value square.
          do {
            i += 1;
          } while ((i <= 80) && (board[i] <= 9));

          // Not solved: update necessary cross-reference variabls.
          if (i <= 80) {
            n = board[i];
            s = 1 << (n % 10 - 1);

            c = (i % 9);
            r = (int) (i / 9);
            b = (int) (c / 3) + 3 * (int) (r / 3);
          }
          // Solution found: update necessary variables.
          else {
            // Update found solution variable
            found += 1;

            // Store first solution matrix.
            if (found == 1) {
              for (i = 0; i <= 80; i++) {
                firstSolution[i] = board[i] % 10;
              }
            }

            // Go to previous entry square
            do {
              i -= 1;
            } while ((i >= 0) && (board[i] <= 9));

            // Update cross-reference
            if (i >= 0) {
              columns[c] ^= s;
              rows[r] ^= s;
              blocks[b] ^= s;
            }
          }
        } // Valid cross-reference condition.
      } // Options if/else condition.
    } // Solution while loop.

    return found;
  }
}
