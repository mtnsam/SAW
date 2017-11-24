package saw1;

/**
 * Server
 * @author Samantha Bennett
 */

import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;

class UDPStopAndWaitServer{
	private static final int BUFFER_SIZE = 1024;   
	private static final int PORT = 6789;      //What does this port number refer to?

	public static void main(String[] args) throws IOException {
		// Create server socket
		DatagramSocket serverSocket = new DatagramSocket( PORT );

		// Set up byte arrays for sending/receiving data
        byte[] receiveData = new byte[ BUFFER_SIZE ];
        byte[] dataForSend = new byte[ BUFFER_SIZE ];

        // Infinite loop to check for connections 
        while(true)
        {

        	// Get received packet
        	DatagramPacket received = new DatagramPacket( receiveData, receiveData.length );
          	serverSocket.receive( received );

          	// Get message from the packet
          	int message = ByteBuffer.wrap(received.getData( )).getInt();

            Random random = new Random( );
            int chance = random.nextInt( 100 );

            // .5% of responding to the message
            if( ((chance % 2) == 0) ){
              System.out.println("FROM CLIENT: " + message);

              // Get packet's IP and port
              InetAddress IPAddress = received.getAddress();
              int port = received.getPort();

              // Convert message to uppercase 
              dataForSend = ByteBuffer.allocate(4).putInt( message ).array();

              // Send packet data back to the client
              DatagramPacket packet = new DatagramPacket( dataForSend, dataForSend.length, IPAddress, port );
              serverSocket.send( packet ); 
            } else {
              System.out.println( "Packet with sequence number, "+ message + " ,was dropped");
            }
       	}
	}
}
