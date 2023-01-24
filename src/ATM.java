import java.net.*;
import java.io.*;
import java.util.*;


class Object {
    String username = "";
    String password = "";
    double amount = 500;
}
public class ATM
{
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private ArrayList<Object> userList= new ArrayList<>();
    
    // constructor with port
    public ATM(int port)
    {
        Object user = new Object();
        user.username = "Freya";
        user.password = "password";
        user.amount = 1000;
        userList.add(user);

        user = new Object();
        user.username = "Thrud";
        user.password = "password";
        user.amount = 200;
        userList.add(user);

        user = new Object();
        user.username = "Sif";
        user.password = "password";
        user.amount = 30000;

        userList.add(user);
// starts server and waits for a connection
        try {
            server = new ServerSocket(port);
            socket = server.accept();
            System.out.println("Server established");
// takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));

            out = new DataOutputStream(socket.getOutputStream());
            String text = "";
            Object auth = new Object();
            Random error = new Random();
// reads message from client until "Over" is sent
                while (true) {

                    try {
                        text = in.readUTF();
                        File db = new File("store");
                        FileWriter wr = new FileWriter("store");
                        PrintWriter pw = new PrintWriter(wr);
                        pw.write("");
                        pw.flush();

                        if (text.equals("Over")) {
                            out.writeUTF(text);
                            break;
                        } else if (text.contains("#U#")) {
                            for (int i = 0; i <= 3; i++) {
                                if (i == 3) {
                                    out.writeUTF("#U#False");
                                    break;
                                }
                                if (text.substring(3).equals(userList.get(i).username)) {
                                    out.writeUTF("#U#True");
                                    auth = userList.get(i);
                                    break;
                                }
                            }
                        } else if (text.contains("#P#")) {
                            if (auth.password.equals(text.substring(3))) {
                                out.writeUTF("#P#True");
                            } else {
                                out.writeUTF("#P#False");
                                auth = new Object();
                            }
                        } else if (text.contains("#b#")) {
                            out.writeUTF(String.valueOf(auth.amount));
                        } else if (text.contains("#l#")) {
                            try {
                                pw.flush();
                                pw.print("");
                                pw.close();
                                wr.close();
                            } catch (Exception exception) {
                                System.out.println(exception);
                            }
                        } else if (text.contains("#c#")) {
                            while(true) {
                                if(error.nextInt(100) > 98) {
                                    try {
                                        int index = text.lastIndexOf('#');
                                        Scanner sc = new Scanner(db);
                                        double credit_amount = Double.parseDouble(text.substring(index + 1));
                                        boolean flag = true;
                                        while (sc.hasNextLine()) {
                                            String data = sc.nextLine();
                                            if (data.equals(text)) {
                                                out.writeUTF("Already processed");
                                                sc.close();
                                                flag = false;
                                                break;
                                            }
                                        }
                                        if (flag) {
                                            sc.close();
                                        } else {
                                            continue;
                                        }

                                        auth.amount += credit_amount;
                                        wr.write(text + '\n');
                                        wr.close();
                                        out.writeUTF(credit_amount + " has been added successfully" + '\n' + "Current Balance: " + auth.amount);
                                        break;
                                    } catch (NumberFormatException exception) {
                                        System.out.println(exception);
                                    } catch (IOException exception) {
                                        System.out.println("Error");
                                        System.out.println(exception);
                                    }
                                }
                                else {
                                    out.writeUTF("failed");
                                }
                            }

                        } else if (text.contains("#d#")) {
                            while(true) {
                                if (error.nextInt(100) > 30) {
                                    try {
                                        int index = text.lastIndexOf('#');
                                        double debit_amount = Double.parseDouble(text.substring(index + 1));
                                        if (debit_amount > auth.amount) {
                                            wr.write(text + '\n');
                                            wr.close();
                                            out.writeUTF("Insufficient balance");
                                        }
                                        else {
                                            Scanner sc = new Scanner(db);
                                            boolean flag = true;
                                            while (sc.hasNextLine()) {
                                                String data = sc.nextLine();
                                                if (data.equals(text)) {
                                                    out.writeUTF("Already processed");
                                                    sc.close();
                                                    flag = false;
                                                    break;
                                                }
                                            }


                                            if (flag) {
                                                sc.close();
                                            } else {
                                                continue;
                                            }

                                            auth.amount -= debit_amount;
                                            wr.write(text + '\n');
                                            wr.close();
                                            out.writeUTF(debit_amount + " has been debited successfully" + '\n' + "Current Balance: " + auth.amount);
                                            break;
                                        }
                                    } catch (NumberFormatException exception) {
                                        System.out.println(exception);
                                    } catch (IOException exception) {
                                        System.out.println("Error");
                                        System.out.println(exception);
                                    }
                                } else {
                                    out.writeUTF("failed");
                                }
                            }
                        }

                    } catch (IOException i) {
                        System.out.println(i);
                    }

            }
// close connection
            socket.close();
            in.close();
            out.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
    public static void main(String args[])
    {
        ATM atm = new ATM(5000);
    }
}