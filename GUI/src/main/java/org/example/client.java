package org.example;

import java.io.*;
import java.net.*;

/* main client class */
public final class client {

    final static String CRLF = "r\n";
    private Socket controlSocket = null;
    private BufferedReader controlReader = null;
    private BufferedReader userInputReader = null;
    private DataOutputStream controlWriter = null;
    // private GUI clientGUI = null;

    private Thread sendMsgThread = null;
    private Thread rcvMsgThread = null;

    /* client class constructor */
    public client() {
        // initialize input reader for taking in commands from client (the user in the chat room)
        userInputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String argv[]) {
        try {
            System.out.println("creating new client");
            client newClient = new client();
            newClient.connect();
            newClient.init();
        } catch (Exception e) {
            System.out.println("Exception occurred in client main:" + e);
        }
    }

    public void init() {
        try {
            TcpSend tcpSend = new TcpSend(controlSocket);
            TcpRcv tcpRcv = new TcpRcv(controlSocket);

            // create threads for sending & receiving messages
            sendMsgThread = new Thread(tcpSend);
            rcvMsgThread = new Thread(tcpRcv);
            // start threads for sending & receiving messages
            sendMsgThread.start();
            rcvMsgThread.start();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /* Connect to the server */
public void connect() {
    try {
        controlSocket = new Socket("localhost", 6789);
        controlReader = new BufferedReader(new InputStreamReader(controlSocket.getInputStream()));
        controlWriter = new DataOutputStream(controlSocket.getOutputStream());
    } catch (UnknownHostException ex) {
        System.out.println("UnknownHostException during connection attempt: " + ex);
        close();
    } catch (IOException ex) {
        System.out.println("IOException during connection attempt: " + ex);
        close();
    }
}

public void close() {
    try {
        if (controlSocket != null) controlSocket.close();
        if (controlReader != null) controlReader.close();
        if (controlWriter != null) controlWriter.close();
    } catch (IOException e) {
        System.err.println("Error closing resources: " + e.getMessage());
    }
    if (sendMsgThread != null) sendMsgThread.interrupt();
    if (rcvMsgThread != null) rcvMsgThread.interrupt();
}

    final class TcpSend implements Runnable {
        Socket socket;  // reference to connection socket
        // constructor
        public TcpSend(Socket socket) throws Exception
        {
            this.socket = socket;
        }
    
        // implement the run() method of the Runnable interface
        public void run()
        {
            try {
                sendMsg();
            } 
            catch (Exception e) {
                System.out.println("Exception occurred in run (Runnable interface) attempt for TcpSend: " + e);
            }
        }

        public void sendMsg() {
            while (!Thread.currentThread().isInterrupted()) {
                String response = "";
                try {
                    String command = userInputReader.readLine();
                    if ((command != null) && (command != "")) {
                        // send command to the server
                        controlWriter.writeUTF(command);
                    }
                    if (command == "%exit") {
                        System.out.println("exiting client program");
                        break;
                    }
                    response = controlReader.readLine();
                     if (response != null) {
                   System.out.println(response);
                }
                }
                catch (IOException ex) {
                    System.out.println("IOException occured during send message attempt: " + ex);
                }
               
                
            }
        } /* end sendMsg() */

    } /* end TcpSend class */

    final class TcpRcv implements Runnable {
        Socket socket;  // reference to connection socket
        // constructor
        public TcpRcv(Socket socket) throws Exception
        {
            this.socket = socket;
        }
    
        // implement the run() method of the Runnable interface
        public void run()
        {
            try {
                rcvMsg();
                System.out.println("thread should've died");
            } 
            catch (Exception e) {
                System.out.println("Exception occurred in run (Runnable interface) attempt for TcpRcv: " + e);
            }
        }

        public void rcvMsg() {
            int i = 0;
            while (!Thread.currentThread().isInterrupted()) {
                String currentResponse = "";
                try {
                    i++;
                    //System.out.println("Trying to read" + i);
                    currentResponse = controlReader.readLine();
                    //System.out.println("Finished reading" + i);
                    if (currentResponse != null) {
                        //System.out.println(i + " " + currentResponse);
                        System.out.println(currentResponse);
                    }
                }
                catch (Exception e) {
                    System.out.println("Exception occurred in receive message attempt: " + e);
                }
            }

        } /* end rcvMsg() */

    } /* end TcpRcv class */

} /* end client class */
