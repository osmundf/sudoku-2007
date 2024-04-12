package test;

import javax.swing.*;
import javax.swing.UIManager.*;

public class Test {

  /**
   * Static entry point.
   *
   * @param arguments String array of execution passed parameters.
   */
  public static void main(String[] arguments) {
    //		LookAndFeel [] looks = UIManager.getAuxiliaryLookAndFeels ();
    LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();

    if (looks != null) {
      for (int i = 0; i < looks.length; i++) {
        System.out.println(looks[i].getClassName());
      }
    }
  }
}
