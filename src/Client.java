import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.random.RandomGenerator;

public class Client
{
    private Socket socket = null;
    private DataInputStream input = null;
    private DataInputStream input2 = null;
    private DataOutputStream out = null;
    public Client(String address, int port)
    {
        try
        {
            socket = new Socket(address, port);
            System.out.println("Connected");
            input = new DataInputStream(System.in);
            input2 = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
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
        String text = "";
        String response = "";
        String Username = "";
        String Password = "";
        String authentication = "False";

        try
        {
            Integer reqID = 0;
            while(true) {
                    System.out.printf("Username:");
                    text = input.readLine();
                    Username = text;
                    text = "#U#" + text;
                    out.writeUTF(text);
                    text = "";
                    response = input2.readUTF();
                    if (response.contains("#U#True")) {
                        System.out.printf("Password:");
                        text = input.readLine();
                        Password = text;
                        text = "#P#" + text;
                        out.writeUTF(text);
                        text = "";
                        response = input2.readUTF();
                        if (response.contains("#P#True")) {
                            System.out.println("Log In Successful");
                            while (true) {
                                reqID++;
                                String reqCode = "#" + reqID + "#";
                                System.out.println("Enter c to Credit");
                                System.out.println("Enter d to Debit");
                                System.out.println("Enter b for Balance");
                                System.out.println("Enter l to Logout");
                                text = input.readLine();
                                if (text.contains("l")) {
                                    text = "#l#" + reqCode + text;
                                    out.writeUTF(text);
                                    System.out.println(Username + " has been logged out");
                                    break;
                                } else if (text.equals("b")) {
                                    text = "#b#" + reqCode;
                                    out.writeUTF(text);
                                    System.out.print("Balance: ");
                                    response = input2.readUTF();
                                    System.out.println(response);
                                } else if (text.equals("c")) {
                                    System.out.println("Enter amount:");
                                    int cRetry = 1;
                                    String credit = "";
                                    credit = input.readLine();
                                    if (Integer.parseInt(credit) < 0) {
                                        System.out.println("Invalid Amount");
                                    } else if (Integer.parseInt(credit) == 0) {
                                        System.out.println("Cannot credit");
                                    } else {
                                        while(true) {
                                            text = "#c#" + reqCode + credit;
                                            out.writeUTF(text);
                                            response = input2.readUTF();
                                            if (!response.equals("failed")) {
                                                break;
                                            } else {
                                                cRetry++;
                                            }
                                        }
                                        System.out.println("request successful after " + cRetry + " attempts");
                                        System.out.println(response);
                                    }
                                } else if (text.equals("d")) {
                                    System.out.println("Enter amount:");
                                    int dRetry = 1;
                                    String debit = "";
                                    debit = input.readLine();
                                    if (Integer.parseInt(debit) < 0) {
                                        System.out.println("Invalid Amount");
                                    } else if (Integer.parseInt(debit) == 0) {
                                        System.out.println("Cannot debit");
                                    } else {
                                        while(true) {
                                            text = "#d#" + reqCode + debit;
                                            out.writeUTF(text);
                                            response = input2.readUTF();
                                            if (!response.equals("failed")) {
                                                break;
                                            } else {
                                                dRetry++;
                                            }
                                        }
                                        System.out.println("request successful after " + dRetry + " attempts");
                                        System.out.println(response);
                                    }
                                }
                            }
                        } else {
                            System.out.println("Incorrect password");
                        }
                    } else {
                        System.out.println("Username does not exist");
                    }
            }

        }
        catch(IOException i)

        {
            System.out.println(i);
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
        try {
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String args[])
    {
        Client client = new Client("192.168.0.104", 5000);
    }
}