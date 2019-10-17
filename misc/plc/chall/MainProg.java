package chall;

import java.awt.EventQueue;

class MainProg
{
  public static void main(String[] paramArrayOfString) {
    EventQueue.invokeLater(() -> {
          ClientUI clientUI = new ClientUI();
          clientUI.setVisible(true);
        });
  }
}

