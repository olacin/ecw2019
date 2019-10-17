package plcmanager;
import java.awt.EventQueue;

public class MainProg {

	public static void main(String[] paramArrayOfString) {
	    EventQueue.invokeLater(() -> {
	          ClientUI clientUI = new ClientUI();
	          clientUI.setVisible(true);
	        });
	  }

}
