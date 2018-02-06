
/**
 * Socket programming example: TCP Multi-client Server
 * DATS/ITPE2410 Networking and Cloud Computing, Spring 2018
 * Raju Shrestha, HiOA
 * */
import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EchoUcaseServerTCPMultiClient {

    public static void main(String[] args) throws IOException {
        int portNumber = 5555; // Default port to use

        if (args.length > 0) {
            if (args.length == 1) {
                portNumber = Integer.parseInt(args[0]);
            } else {
                System.err.println("Usage: java EchoUcaseServerMutiClients [<port number>]");
                System.exit(1);
            }
        }

        System.out.println("Hi, I am the EchoUCase Multi-client TCP server.");

        try (
                // Create server socket with the given port number
                ServerSocket serverSocket
                = new ServerSocket(portNumber);) {
            String receivedText;
            // continuously listening for clients
            while (true) {
                // create and start a new ClientServer thread for each connected client
                ClientServer clientserver = new EchoUcaseServerTCPMultiClient.ClientServer(serverSocket.accept());
                clientserver.start();
            }
        } catch (IOException e) {

            System.out.println("Exception occurred when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }

    }

    /**
     * *
     * This class serves a client in a separate thread
     */
    static class ClientServer extends Thread {

        Socket connectSocket;
        InetAddress clientAddr;
        int serverPort, clientPort;

        public ClientServer(Socket connectSocket) {
            this.connectSocket = connectSocket;
            clientAddr = connectSocket.getInetAddress();
            clientPort = connectSocket.getPort();
            serverPort = connectSocket.getLocalPort();
        }

        public void run() {
            try (
                    // Create server socket with the given port number
                    PrintWriter out
                    = new PrintWriter(connectSocket.getOutputStream(), true);
                    // Stream reader from the connection socket
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connectSocket.getInputStream()));) {

                String receivedText;
                // read from the connection socket
                while (((receivedText = in.readLine()) != null)) {
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

                        Scanner inFile = new Scanner(new File("C:/Users/aksel/Documents/NetBeansProjects/Datanettverk/src/datanettverk/konversjon.csv"));
                        inFile.useDelimiter(",");
                        String test = inFile.next();

                        while (inFile.hasNext() && !(test.equals(part2))) {
                            test = inFile.next();
                        }
                        if (test.equals(part2)) { // Invalid currency type would get assigned the last string from the while loop, so we need to check.
                            stringCurrency = inFile.next();
                            currency = Double.parseDouble(stringCurrency);
                            sum = currency * Double.parseDouble(part1);

                            Scanner inFileTwo = new Scanner(new File("C:/Users/aksel/Documents/NetBeansProjects/Datanettverk/src/datanettverk/konversjon.csv"));
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
                                outText = sum + " " + part2 + " required for buying " + part1 + " " + part3;
                                out.println(outText);
                            }
                            inFileTwo.close();
                        } else {
                            outText = "Error";
                            out.println(outText);
                        }
                        inFile.close();
                    }
                    // Write the converted uppercase string to the connection socket
                  /*  String outText = ProcessString(receivedText);

                    out.println(outText); */
                    System.out.println("I (Server) [" + connectSocket.getLocalAddress().getHostAddress() + ":" + serverPort + "] > " + outText);
                }

                // close the connection socket
                connectSocket.close();

            } catch (IOException e) {
                System.out.println("Exception occurred when trying to communicate with the client " + clientAddr.getHostAddress());
                System.out.println(e.getMessage());
            }
        }

        /**
         * *
         * Process the input string and returns.
         *
         * @param intext Input text
         * @return processed text
         */
        private String ProcessString(String intext) {
            String outtext = intext.toUpperCase();

            return outtext;
        }
    }
}
