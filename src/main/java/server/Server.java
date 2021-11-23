/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import user.Peer;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import status.ResponseClient;

/**
 *
 * @author ASUS
 */
public class Server {
    private ArrayList<Peer> dataPeer = null; /// Danh sách 
    private ServerSocket server;
    private Socket connection;
    private ObjectOutputStream obOutputClient;
    private ObjectInputStream obInputStream;
    public boolean isStop = false, isExit = false;
    //Intial server socket
    public Server(int port) throws Exception {
        server = new ServerSocket(port);
        dataPeer = new ArrayList<>();
        (new WaitForConnect()).start();
    }

    private boolean waitForConnection() throws Exception {
//		Tạo ra 1 Server cho phép client kết nối và Update User
        connection = server.accept();
        obInputStream = new ObjectInputStream(connection.getInputStream());

        String namePort = (String) obInputStream.readObject();
        System.out.println(namePort);
        String name = getName(namePort);
        System.out.println("name: " + name);
        int port = Integer.parseInt(getPort(namePort));
        System.out.println("port: " + port);
        boolean isExist = ResponseClient.isUserExist(name, dataPeer);
        System.out.println(isExist);
        if (!isExist) {
            //TO DO
            saveNewPeer(name, connection.getInetAddress().toString(), port);
            ServerGUI.updateMessage(name, port);
            ServerGUI.increaseNumberClient();
            return true;
        }

        return true;
    }

//    public void stopserver() throws Exception {
//        isStop = true;
//        server.close();
//        connection.close();
//    }

    private String sendSession() {
        int size = dataPeer.size();
        String ls = "";
        for (int i = 0; i < size; i++) {
            {
                Peer peer = dataPeer.get(i);

                ls += peer.getNamePeer() + "  ";
                ls += peer.getHostPeer() + "  ";
                ls += peer.getPortPeer() + "  ";
            }
            ls += "- ";
        }
        return ls; //
    }

    public  void stopServer() throws Exception { // Đóng Server
        isStop = true;
        server.close();
        connection.close();
    }

    public class WaitForConnect extends Thread {

        @Override
        public void run() {
            try {
                while (!isStop) {
                    if (waitForConnection()) {
                        if (isExit) {
                            isExit = false;
                        } else {
                            obOutputClient = new ObjectOutputStream(connection.getOutputStream());
                            obOutputClient.writeObject(sendSession());
                            obOutputClient.flush();
                            obOutputClient.close();
                        }
                    } else {
                        obOutputClient = new ObjectOutputStream(connection.getOutputStream());
                        obOutputClient.writeObject("Deny");
                        obOutputClient.flush();
                        obOutputClient.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            {

            }
        }
    }

    private void saveNewPeer(String user, String ip, int port) throws Exception {
        Peer newPeer = new Peer();
        if (dataPeer.isEmpty()) {
            dataPeer = new ArrayList<>();
        }
        newPeer.setPeer(user, ip, port); 
        dataPeer.add(newPeer);
    }

    public String getPort(String namePort) {//Anh:1222
        String portPeer = "";
        StringTokenizer str = new StringTokenizer(namePort, ";");
        while (str.hasMoreElements()) {
            portPeer = str.nextToken();
        }
        return portPeer;
    }

    public String getName(String namePort) {
        String namePeer = "";
        StringTokenizer str = new StringTokenizer(namePort, ";");
        namePeer = str.nextToken();
        return namePeer;
    }
}
