/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import user.Peer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.JFrame;
import status.RequestSever;
import status.ResponseClient;

/**
 *
 * @author ASUS
 */
public class Client {

    public static ArrayList<Peer> clientArray = null;
    private InetAddress IPServer;
    private int portServer = 1602;
    private String nameUser = "";
    private boolean isStop = false;
    private static int portClient = 5000;
    private int timeOut = 10000;
    private Socket socketClient;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;
    private ClientServer server; // Tạo 1 server mới cho 2 client chat với nhau 1 ROOM
    private String dataUser;

    public Client(String ip, int port, String name, String dataUser) throws Exception {
        IPServer = InetAddress.getByName(ip);
        nameUser = name;
        portClient = port;
        this.dataUser = dataUser;
        clientArray = ResponseClient.getAllUser(dataUser);
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateFriend();
            }
        }).start();
        server = new ClientServer(nameUser);
        (new Request()).start();
    }

    public void upDateFriend() throws IOException, ClassNotFoundException {
        socketClient = new Socket();
        SocketAddress addressServer = new InetSocketAddress(IPServer, portServer);
        socketClient.connect(addressServer);
        // Sendrequest
        String namePort = RequestSever.createAcount(nameUser, portClient);
        serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
        serverOutputStream.writeObject(namePort);
        serverOutputStream.flush();
        serverInputStream = new ObjectInputStream(socketClient.getInputStream());
        String msg = (String) serverInputStream.readObject();
        serverInputStream.close();
        clientArray = ResponseClient.getAllUser(msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateFriend();
            }
        }).start();
    }

    public static int getPort() {
        return portClient;
    }

    public class Request extends Thread {

        @Override
        public void run() {
            while (!isStop) {
                try {
                    Thread.sleep(timeOut);
                    upDateFriend();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initChat(String IP, int port, String guest) throws Exception {
        final Socket connclient = new Socket(InetAddress.getByName(RequestSever.freshIP(IP)), port); //InetAddress.getByName(RequestSever.freshIP(IP)
        ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream());
        sendrequestChat.writeObject(nameUser);
        sendrequestChat.flush();
        ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
        String msg = (String) receivedChat.readObject();
        if (msg.equals("Deny")) {
            MainGUI.request("Your friend denied connect with you!", false);
            connclient.close();
            return;
        }
//        new ChatGUI(nameUser, guest, connclient, portClient).setVisible(true);
        new ChatGUI(nameUser, guest, connclient, portClient,false);
    }

    public void updateFriend() {
        int size = clientArray.size();
        MainGUI.resetList();
        int i = 0;
        while (i < size) {
            if (!clientArray.get(i).getNamePeer().equals(nameUser)) {
                MainGUI.updateFriendMainGui(clientArray.get(i).getNamePeer());
            }
            i++;
        }
    }

    public static int request(String msg, boolean type) {
        JFrame frameMessage = new JFrame();
        return ResponseClient.show(frameMessage, msg, type);
    }
}
