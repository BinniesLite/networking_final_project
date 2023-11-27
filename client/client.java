package client;

import java.io.*;
import java.net.*;

/* main client class */
public final class client {

    final static String CRLF = "\r\n";
    private Socket controlSocket = null;
    private BufferedReader controlReader = null;
    private BufferedReader userInputReader = null;
    private DataOutputStream controlWriter = null;

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
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void init() {
        try {
            TcpSend tcpSend = new TcpSend(controlSocket);
            TcpRcv tcpRcv = new TcpRcv(controlSocket);

            // create threads for sending & receiving messages
            Thread sendMsgThread = new Thread(tcpSend);
            Thread rcvMsgThread = new Thread(tcpRcv);
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
            // establish the control socket
            controlSocket = new Socket("localhost", 6789);

            // get references to the socket input and output streams
            InputStreamReader isr = new InputStreamReader(controlSocket.getInputStream());
            controlReader = new BufferedReader(isr);
            controlWriter = new DataOutputStream(controlSocket.getOutputStream());
        } 
        catch (UnknownHostException ex) {
            System.out.println("UnknownHostException during connection attempt: " + ex);
        } 
        catch (IOException ex) {
            System.out.println("IOException during connection attempt: " + ex);
        }
    } /* end client.connect() */

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
            while (true) {
                String response = "";
                try {
                    String command = userInputReader.readLine();
                    if ((command != null) && (command != "")) {
                        // send command to the server
                        controlWriter.writeUTF(command);
                    }
                }
                catch (IOException ex) {
                    System.out.println("IOException occured during send message attempt: " + ex);
                }
                System.out.println(response);
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
            } 
            catch (Exception e) {
                System.out.println("Exception occurred in run (Runnable interface) attempt for TcpRcv: " + e);
            }
        }

        public void rcvMsg() {
            while (true) {
                String currentResponse = "";
                try {
                    currentResponse = controlReader.readLine();
                    if (currentResponse != null) {
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
