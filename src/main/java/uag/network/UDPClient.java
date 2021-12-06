package uag.network;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] bufferSent;
    private byte[] bufferReceived = new byte[64];

    public UDPClient() throws SocketException, UnknownHostException {
        socket = new DatagramSocket();
        address = InetAddress.getByAddress(new byte[] {(byte) 192, (byte) 168, 100, 32});
    }

    public String sendMessage(String msg)throws IOException {
        bufferSent = msg.getBytes();
        DatagramPacket packet
                = new DatagramPacket(bufferSent, bufferSent.length, address, 4445);
        socket.send(packet);
         packet = new DatagramPacket(bufferReceived, bufferReceived.length);
         socket.receive(packet);
         String received = new String(packet.getData(), 0, packet.getLength());
         return received;
    }

    public void close() {
        socket.close();
    }
}