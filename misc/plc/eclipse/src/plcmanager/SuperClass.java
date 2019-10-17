package plcmanager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SuperClass {
	private DatagramSocket client;
	  private InetAddress address;
	  
	  public SuperClass(String paramString, int paramInt) {
	    this.secret_key = new byte[] { 43, 77, -70, 58, -108, -39, -41, 118, -28, -5, -73, -105, -71, -15, -28, -83, -27, 1, 34, -101, -35, -56, -51, -10, 99, 59, 112, -105, 123, 5, 43, -28, 107, -65, -98, 123, -115, 122, -84, -36, 69, -32, 84, 27, 88, -9, 55, -62, 99, -91, -77, 82, 91, -11, 119, 103, -80, 84, 52, 119, 29, 11, -4, 120 };

	    
	    setConn(paramString, paramInt);
	  }
	  private int port; private byte[] secret_key;
	  public void setConn(String paramString, int paramInt) {
	    try {
	      this.client = new DatagramSocket();
	      this.address = InetAddress.getByName(paramString);
	    }
	    catch (SocketException socketException) {
	      System.out.println("SocketException");
	      System.exit(1);
	    }
	    catch (UnknownHostException unknownHostException) {
	      System.out.println("Invalid IP => UnknownHostException");
	      System.exit(1);
	    } 
	    this.port = paramInt;
	  }
	  
	  private static String bytesToHex(byte[] paramArrayOfByte) {
	    StringBuffer stringBuffer = new StringBuffer();
	    for (byte b = 0; b < paramArrayOfByte.length; b++) {
	      String str = Integer.toHexString(0xFF & paramArrayOfByte[b]);
	      if (str.length() == 1) stringBuffer.append('0'); 
	      stringBuffer.append(str);
	    } 
	    return stringBuffer.toString();
	  }
	  
	  private byte[] hmac(byte[] paramArrayOfByte) {
	    try {
	      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
	      return messageDigest.digest(paramArrayOfByte);
	    }
	    catch (NoSuchAlgorithmException noSuchAlgorithmException) {
	      System.out.println(noSuchAlgorithmException);
	      return null;
	    } 
	  }
	  
	  private byte[] send_and_rcv(byte[] paramArrayOfByte) {
	    DatagramPacket datagramPacket1 = new DatagramPacket(paramArrayOfByte, paramArrayOfByte.length, this.address, this.port);
	    
	    datagramPacket1.setData(paramArrayOfByte);
	    
	    try {
	      this.client.send(datagramPacket1);
	    }
	    catch (IOException iOException) {
	      System.out.println("Error while sendind request");
	      return null;
	    } 
	    
	    byte[] arrayOfByte1 = new byte[128];
	    DatagramPacket datagramPacket2 = new DatagramPacket(arrayOfByte1, 128, this.address, this.port);
	    
	    try {
	      this.client.setSoTimeout(3000);
	      this.client.receive(datagramPacket2);
	      this.client.setSoTimeout(0);
	    }
	    catch (IOException iOException) {
	      System.out.println("Can't connect to host");
	      return null;
	    } 
	    
	    int i = datagramPacket2.getLength();
	    arrayOfByte1 = datagramPacket2.getData();
	    
	    byte[] arrayOfByte2 = new byte[i];
	    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
	    
	    return arrayOfByte2;
	  }

	  
	  private byte[] build_header(long paramLong, byte[] paramArrayOfByte) {
	    byte[] arrayOfByte1 = new byte[42 + paramArrayOfByte.length + 64];
	    arrayOfByte1[0] = 1;
	    arrayOfByte1[1] = (byte)(int)(paramLong >> 56 & 0xFFL);
	    arrayOfByte1[2] = (byte)(int)(paramLong >> 48 & 0xFFL);
	    arrayOfByte1[3] = (byte)(int)(paramLong >> 40 & 0xFFL);
	    arrayOfByte1[4] = (byte)(int)(paramLong >> 32 & 0xFFL);
	    arrayOfByte1[5] = (byte)(int)(paramLong >> 24 & 0xFFL);
	    arrayOfByte1[6] = (byte)(int)(paramLong >> 16 & 0xFFL);
	    arrayOfByte1[7] = (byte)(int)(paramLong >> 8 & 0xFFL);
	    arrayOfByte1[8] = (byte)(int)(paramLong >> 0 & 0xFFL);
	    byte[] arrayOfByte2 = hmac(paramArrayOfByte);
	    System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 9, arrayOfByte2.length);
	    arrayOfByte1[41] = (byte)paramArrayOfByte.length;
	    System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 42, paramArrayOfByte.length);
	    System.arraycopy(this.secret_key, 0, arrayOfByte1, 42 + paramArrayOfByte.length, this.secret_key.length);
	    return arrayOfByte1;
	  }
	  
	  public byte[] read_var(long paramLong, int paramInt1, int paramInt2) {
	    byte[] arrayOfByte1 = { 0, 1, (byte)paramInt1, (byte)paramInt2 };
	    byte[] arrayOfByte2 = build_header(paramLong, arrayOfByte1);
	    byte[] arrayOfByte3 = send_and_rcv(arrayOfByte2);
	    if (arrayOfByte3 == null)
	      return null; 
	    //System.out.println(bytesToHex(arrayOfByte3));
	    byte b = arrayOfByte3[45];
	    byte[] arrayOfByte4 = new byte[b];
	    System.arraycopy(arrayOfByte3, 46, arrayOfByte4, 0, b);
	    //System.out.println(bytesToHex(arrayOfByte4));
	    
	    return arrayOfByte4;
	  }


	  /* plcId, {1, 2, -1} */
	  public byte[] write_var(long paramLong, byte paramByte) {
		  byte[] arrayOfByte1 = { 0, 2, paramByte };
		    byte[] arrayOfByte2 = build_header(paramLong, arrayOfByte1); // header - 43 char
		    //System.out.println(arrayOfByte2.length);
		    byte[] arrayOfByte4 = new byte[arrayOfByte2.length + 3];
		    arrayOfByte4[43] = 1;//arrayOfByte3[45];
		    arrayOfByte4[44] = 1;//arrayOfByte3[45];
		    arrayOfByte4[45] = 1;//arrayOfByte3[45];
		    byte[] arrayOfByte3 = send_and_rcv(arrayOfByte4);
		    System.out.println(arrayOfByte3.length);
		    System.out.println(bytesToHex(arrayOfByte3));
		    return arrayOfByte3;
		    /*
		    if (arrayOfByte3 == null)
		      return null; 
		    if (paramByte == -1) {
		      byte[] arrayOfByte = new byte[arrayOfByte3.length - 45];
		      System.arraycopy(arrayOfByte3, 45, arrayOfByte, 0, arrayOfByte3.length - 45);
		      return arrayOfByte;
		    } 
		    
		    byte[] arrayOfByte4 = new byte[2];
		    arrayOfByte4[0] = arrayOfByte3[45];
		    arrayOfByte4[1] = 0;
		    return arrayOfByte4;*/
	  }

	  
	  public byte[] check_lock(long paramLong, byte paramByte) {
	    byte[] arrayOfByte1 = { 0, 3, paramByte };
	    byte[] arrayOfByte2 = build_header(paramLong, arrayOfByte1);
	    //System.out.println(arrayOfByte2.length);
	    byte[] arrayOfByte3 = send_and_rcv(arrayOfByte2);
	    //System.out.println("Header : " + bytesToHex(arrayOfByte2));
	    //System.out.println("Lock " + (int)paramByte + " : " + bytesToHex(arrayOfByte3));
	    if (arrayOfByte3 == null)
	      return null; 
	    if (paramByte == -1) { // third lock
	      byte[] arrayOfByte = new byte[arrayOfByte3.length - 45];
	      System.arraycopy(arrayOfByte3, 45, arrayOfByte, 0, arrayOfByte3.length - 45);
	      //System.out.println(arrayOfByte.length);
	      return arrayOfByte;
	    } 
	    
	    byte[] arrayOfByte4 = new byte[2];
	    arrayOfByte4[0] = arrayOfByte3[45];
	    arrayOfByte4[1] = 0;
	    //System.out.println(bytesToHex(arrayOfByte4));
	    return arrayOfByte4;
	  }

	  
	  public Boolean is_up() {
	    byte[] arrayOfByte = { 0, 0 };
	    return Boolean.valueOf((send_and_rcv(arrayOfByte) != null));
	  }
}



