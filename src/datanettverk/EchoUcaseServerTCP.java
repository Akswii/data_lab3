package datanettverk;

/**
 * Socket programming example: TCP Server DATS/ITPE2410 Networking and Cloud
 * Computing, Spring 2018 Raju Shrestha, HiOA
 *
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoUcaseServerTCP {

    public static void main(String[] args) throws IOException {
        int portNumber = 5555; // Default port to use

        if (args.length > 0) {
            if (args.length == 1) {
                portNumber = Integer.parseInt(args[0]);
            } else {
                System.err.println("Usage: java EchoUcaseServerTCP [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am EchoUCase TCP server");

        // try() with resource makes sure that all the resources are automatically
        // closed whether there is any exception or not!!!
        try (
                // Create server socket with the given port number
                ServerSocket serverSocket
                = new ServerSocket(portNumber);
                // create connection socket, server begins listening
                // for incoming TCP requests
                Socket connectSocket = serverSocket.accept();
                // Stream writer to the connection socket
                PrintWriter out
                = new PrintWriter(connectSocket.getOutputStream(), true);
                // Stream reader from the connection socket
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(connectSocket.getInputStream()));) {
            InetAddress clientAddr = connectSocket.getInetAddress();
            int clientPort = connectSocket.getPort();
            String receivedText;
            // read from the connection socket
            while ((receivedText = in.readLine()) != null) {
                System.out.println("Client [" + clientAddr.getHostAddress() + ":" + clientPort + "] > " + receivedText);

                String outText = "";
                String stringCurrency = "";
                double currency = 0;
                double sum = 0;

                if (!(receivedText.equals(null))) {
                    String split[] = receivedText.split("-"); // 100-nok-usd
                    String part1 = split[0];
                    String part2 = split[1];
                    String part3 = split[2];

                    Scanner inFile = new Scanner(new File("C:/Users/Marku/Downloads/conversfinal.csv"));
                    inFile.useDelimiter(",");
                    String test = inFile.next();

                    while (inFile.hasNext() && !(test.equals(part2))) {
                        test = inFile.next();
                    }
                    if (test.equals(part2)) { // Invalid currency type would get assigned the last string from the while loop, so we need to check.
                        stringCurrency = inFile.next();
                        currency = Double.parseDouble(stringCurrency);
                        sum = currency * Double.parseDouble(part1);

                        Scanner inFileTwo = new Scanner(new File("C:/Users/Marku/Downloads/conversfinal.csv"));
                        inFileTwo.useDelimiter(",");
                        String testTwo = inFileTwo.next();

                        while (inFileTwo.hasNext() && !(testTwo.equals(part3))) {
                            testTwo = inFileTwo.next();
                        }
                        if (testTwo.equals(part3)) {
                            String stringNewCurrency = inFileTwo.next();
                            double newCurrency = Double.parseDouble(stringNewCurrency);
                            double currencyFinal = currency / newCurrency;
                            sum = currencyFinal * Double.parseDouble(part1);
                            outText = sum + " " + part2 + " !required! for buying " + part1 + " " + part3;
                            out.println(outText);
                        }
                        inFileTwo.close();
                    } else {
                        outText = "Error";
                        out.println(outText);
                    }
                    inFile.close();
                }
                System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + portNumber + "] > " + outText);
            }
            System.out.println("I am done, Bye!");
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }
}
