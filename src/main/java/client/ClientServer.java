/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.net.ServerSocket;
import client.Client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author ASUS
 */
public class ClientServer {

    private String username = ""; // ten cua nguoi su dung
    private ServerSocket serverPeer; // ten cua Server
    private int portServerClient; // port of Server
    private boolean isStop = false;
    private boolean isHost = true;
    public ClientServer(String name) throws Exception {
        username = name;
        portServerClient = Client.getPort();
        serverPeer = new ServerSocket(portServerClient);
        (new WaitPeerConnect()).start();
    }

    public void exit() throws Exception {
        isStop = true;
        serverPeer.close();
    }

    class WaitPeerConnect extends Thread {

        Socket connection;
        ObjectInputStream getRequest;

        @Override
        public void run() {
            while (!isStop) {
                try {
                    connection = serverPeer.accept();
                    getRequest = new ObjectInputStream(connection.getInputStream());
                    String msg = (String) getRequest.readObject();
                    String nameGuest = msg;
                    int res = MainGUI.request("Account: " + nameGuest + " want to connect with you !", true);
                    ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
                    if (res == 1) {
                        send.writeObject("Deny");
                    } else if (res == 0) {
                        send.writeObject("Oke");
//                        new ChatGUI(username, nameGuest, connection, port).setVisible(true);
                        new ChatGUI(username, nameGuest, connection, portServerClient, true);
                    }
                    send.flush();
                } catch (Exception e) {
                    break;
                }
            }
            try {
                serverPeer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
