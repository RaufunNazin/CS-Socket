import java.net.*;
import java.io.*;
public class Client_upper_prime
{
    // initialize socket and input output streams
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    // constructor to put ip address and port
    public Client_upper_prime(String address, int port)
    {
// establish a connection
        try
        {
            socket = new Socket("10.33.2.84", 5003);
            System.out.println("Connected");
// takes input from terminal
            input = new DataInputStream(System.in);
// sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
// string to read message from input
        String line = "";
        String message= "";
// keep reading until "Over" is input
        while (!line.equals("Over"))
        {
            try
            {
                line = input.readLine();
                out.writeUTF(line);
                in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                message = in.readUTF();
                System.out.println(message);
            }
            catch(IOException i)

            {
                System.out.println(i);
            }
        }
// close the connection
        try
        {
            input.close();
            out.close();
            socket.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public static void main(String args[])
    {
        Client_upper_prime client = new Client_upper_prime("10.33.2.84", 5003);
    }
}
