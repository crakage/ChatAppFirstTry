package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class Server {

    private static DatagramSocket socket;
    private static boolean running;
    private static int ClientID;
    private static ArrayList<ClientInfo> clients = new ArrayList<ClientInfo>();

    public static void start(int port){

        try{
            socket = new DatagramSocket(port);
            running = true;
            listen();
            System.out.println("Server started on port: " + port);

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void broadcast(String message){

    }

    private static void send(String message, InetAddress address, int port){
        try{
            message += "\\e";
            byte[] data = message.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);
            System.out.println("Sent Message To, " +address.getHostAddress()+":"+port);

        } catch (Exception e){
            e.printStackTrace();
        }

    }


    private static void listen(){
        Thread listenThread = new Thread("ChatProgram Listener"){
            public void run(){
                try {
                    while (running){
                        byte[] data = new byte[1024];
                        DatagramPacket packet = new DatagramPacket(data, data.length);
                        socket.receive(packet);

                        String message = new String(data);
                        message = message.substring(0, message.indexOf("\\e"));

                        //MANAGE MESSAGE
                        if(!isCommand(message, packet)) {
                            broadcast(message);
                        }



                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

        }; listenThread.start();

    }

    /*
    * Server command list:
    * \con:[name] -> Connects Client to Server
    * \dis:[id] -> Disconnects Client from Server
    *
     */

    private static boolean isCommand(String message, DatagramPacket packet){
        if (message.startsWith("\\con:")){
            //run connection code


            String name = message.substring(message.indexOf(":"+1));
            clients.add(new ClientInfo(name, ClientID++, packet.getAddress(),packet.getPort()));
            broadcast("User "+name+" Connected!");


            return true;
        }


        return false;
    }

    public static void  stop(){

    }
}

