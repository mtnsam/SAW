package saw1;

/**
 * Client
 * @author Samantha Bennett
 */

import java.io.*;
import java.net.*;
import java.nio.*;

class UDPStopAndWaitClient{
	private static final int BUFFER_SIZE = 1024;
	private static final int PORT = 6789; //What does this port number refer to?
	private static final String HOSTNAME = "localhost";  //hostname? what is that?
	private static final int BASE_SEQUENCE_NUMBER = 42;

    public static void main(String args[]) throws Exception{
   		// Create socket
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout( 1000 );

		// The message we're going to send converted to bytes
		Integer sequenceNumber = BASE_SEQUENCE_NUMBER;


		for (int counter = 0; counter < 15; counter++) {
			boolean timedOut = true;

			while( timedOut ){
				sequenceNumber++;

				// Create a byte array for sending and receiving data
				byte[] sendData = new byte[ BUFFER_SIZE ];
				byte[] receiveData = new byte[ BUFFER_SIZE ];

				// Get the IP address of the server
				InetAddress IPAddress = InetAddress.getByName( HOSTNAME );

				System.out.println( "Sending Packet (Sequence Number, " + sequenceNumber + ")" );				
				// Get byte data for message 
				sendData = ByteBuffer.allocate(4).putInt( sequenceNumber ).array();

				try{
					// Send  UDP Packet to the server
					DatagramPacket packet = new DatagramPacket(sendData, sendData.length, IPAddress, 6789);
					socket.send( packet );

					// Receive server's packet
					DatagramPacket received = new DatagramPacket(receiveData, receiveData.length);
					socket.receive( received );
					
					// Get message from server's packet
					int returnMessage = ByteBuffer.wrap( received.getData( ) ).getInt();

					System.out.println( "FROM SERVER:" + returnMessage );
					// If receive ack, stop while loop
					timedOut = false;
				} catch( SocketTimeoutException exception ){
					// If no ack, prepare to resend sequence number
					System.out.println( "Timeout (Sequence Number " + sequenceNumber + ")" );
					sequenceNumber--;
				}
			}	
		}

		socket.close();
   	}
}