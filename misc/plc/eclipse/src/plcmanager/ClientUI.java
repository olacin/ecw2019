package plcmanager;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class ClientUI extends JFrame {
	private static final long serialVersionUID = 1L;
	  private SuperClass client;
	  private String plc_name;
	  private String server_ip;
	  private JLabel is_plc_up_label;
	  JTextField host_field;
	  
	  public ClientUI() {
	    this.server_ip = "176.31.135.50";
	    this.is_plc_up_label = new JLabel("Server status: ");
	    
	    this.plc_id = (new Random()).nextLong() & 0xFFFFFFFFFFFFFFFFL;
	    initUI();
	    this.client = new SuperClass(this.server_ip, 20001);
	    refreshInfoLoop();
	  }
	  JButton host_submit; JLabel label_plc_name; JLabel label_lock_1_status; JLabel label_lock_2_status; JLabel label_lock_3_status; JLabel flag; long plc_id;
	  public void refreshInfoLoop() {
	    Timer timer = new Timer();
	    TimerTask timerTask = new TimerTask()
	      {
	        public void run() {
	          ClientUI.this.refreshInfo();
	        }
	      };
	    
	    timer.schedule(timerTask, 2000L, 2000L);
	  }
	  
	  public void refreshInfo() {
	    is_server_up();
	    setPLCName(getPLCName());
	    setLock1Status();
	    setLock2Status();
	    setLock3Status();
	  }
	  
	  private void initUI() {
	    setTitle("PLC manager");
	    setExtendedState(6);
	    
	    initTabs();
	    
	    setDefaultCloseOperation(3);
	    setLocationRelativeTo(null);
	  }
	  
	  private JPanel createSettingsTab() {
	    JPanel jPanel1 = new JPanel();
	    JPanel jPanel2 = new JPanel();
	    JPanel jPanel3 = new JPanel();
	    
	    jPanel1.setLayout(new GridLayout(10, 1));
	    
	    jPanel2.add(new JLabel("IP serveur: "));
	    this.host_field = new JTextField(this.server_ip);
	    jPanel2.add(this.host_field);
	    
	    this.host_submit = new JButton("Save");
	    this.host_submit.addActionListener(new ActionListener()
	        {
	          public void actionPerformed(ActionEvent param1ActionEvent) {
	            System.out.println("Changed Host => " + ClientUI.this.host_field.getText());
	            ClientUI.this.client.setConn(ClientUI.this.host_field.getText(), 20001);
	          }
	        });
	    jPanel2.add(this.host_submit);
	    jPanel1.add(jPanel2);
	    
	    jPanel3.add(this.is_plc_up_label);
	    jPanel1.add(jPanel3);
	    
	    return jPanel1;
	  }
	  
	  private JPanel createPlcTab() {
	    JPanel jPanel1 = new JPanel();
	    JPanel jPanel2 = new JPanel();
	    JPanel jPanel3 = new JPanel();
	    JPanel jPanel4 = new JPanel();
	    JPanel jPanel5 = new JPanel();
	    
	    jPanel1.setLayout(new GridLayout(10, 1));
	    
	    jPanel2.setLayout(new GridLayout(1, 1));
	    this.label_plc_name = new JLabel("PLC name: ");
	    jPanel2.add(this.label_plc_name);
	    
	    jPanel3.setLayout(new GridLayout(1, 1));
	    this.label_lock_1_status = new JLabel("Lock #1 status: ");
	    jPanel3.add(this.label_lock_1_status);
	    
	    jPanel4.setLayout(new GridLayout(1, 1));
	    this.label_lock_2_status = new JLabel("Lock #2 status: ");
	    jPanel4.add(this.label_lock_2_status);
	    
	    jPanel5.setLayout(new GridLayout(1, 1));
	    this.label_lock_3_status = new JLabel("Lock #3: ");
	    jPanel5.add(this.label_lock_3_status);
	    
	    jPanel1.add(jPanel2);
	    jPanel1.add(jPanel3);
	    jPanel1.add(jPanel4);
	    jPanel1.add(jPanel5);
	    
	    return jPanel1;
	  }
	  
	  private void initTabs() {
	    JTabbedPane jTabbedPane = new JTabbedPane();
	    
	    JPanel jPanel1 = createSettingsTab();
	    JPanel jPanel2 = createPlcTab();
	    
	    jTabbedPane.add("Settings", jPanel1);
	    jTabbedPane.add("PLC", jPanel2);
	    
	    getContentPane().add(jTabbedPane);
	  }
	  
	  private String getPLCName() {
	    byte[] arrayOfByte = this.client.read_var(this.plc_id, 0, 24);
	    if (arrayOfByte == null)
	      return "Null"; 
	    return new String(arrayOfByte);
	  }

	  
	  private void setPLCName(String paramString) { this.label_plc_name.setText("PLC name: " + paramString); }

	  
	  private void setLock1Status() {
		this.client.write_var(plc_id, 32, 32);
	    byte[] arrayOfByte = this.client.check_lock(this.plc_id, (byte)1);
	    if (arrayOfByte == null) {
	      this.label_lock_1_status.setText("Lock #1 status: Null");
	    } else if (arrayOfByte[0] == 1) {
	      this.label_lock_1_status.setText("Lock #1 status: open");
	    } else {
	      this.label_lock_1_status.setText("Lock #1 status: closed");
	    } 
	  } private void setLock2Status() {
		this.client.write_var(plc_id, 35, 35);
	    byte[] arrayOfByte = this.client.check_lock(this.plc_id, (byte)2);
	    if (arrayOfByte == null) {
	      this.label_lock_2_status.setText("Lock #2 status: Null");
	    } else if (arrayOfByte[0] == 1) {
	      this.label_lock_2_status.setText("Lock #2 status: open");
	    } else {
	      this.label_lock_2_status.setText("Lock #2 status: closed");
	    } 
	  }
	  
	  private void setLock3Status() {
		  //this.client.write_var(this.plc_id, (byte)-1);
	    byte[] arrayOfByte = this.client.check_lock(this.plc_id, (byte)-1);
	    if (arrayOfByte == null) {
	      this.label_lock_3_status.setText("Lock #3: Null");
	    } else {
	      this.label_lock_3_status.setText("Lock #3: " + new String(arrayOfByte));
	    } 
	  }
	  private void is_server_up() {
	    if (this.client.is_up().booleanValue()) {
	      this.is_plc_up_label.setText("Server status: up");
	    } else {
	      
	      this.is_plc_up_label.setText("Server status: down");
	    } 
	  }
}

