package simse.modelbuilder.rulebuilder;

import java.awt.event.*;

public class ExitListener extends WindowAdapter {
  public void windowClosing(WindowEvent event) {
    System.exit(0);
  }
}