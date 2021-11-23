/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import status.RequestSever;
import status.ResponseClient;

/**
 *
 * @author ASUS
 */
public class ChatGUI extends javax.swing.JFrame {

    /**
     * Creates new form ChatGUI
     */
    private boolean isHost;
    private boolean isPlayOOT = false;
    private RoomHost chatHost;
    private RoomGuest chatGuest;
    private Socket socketChat;
    private String nameUser = "", nameGuest = "";
    public boolean isStop = false;
    private int portServer = 0;
    private Onegame hostPlayer;
    private Onegame guestPlayer;
    public String guestTurn;

    public ChatGUI(String user, String guest, Socket socket, int port, boolean isHosts) {
        nameUser = user;
        nameGuest = guest;
        socketChat = socket;
        this.portServer = port;
        this.isHost = isHosts;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ChatGUI window = new ChatGUI(nameUser, nameGuest, socketChat, portServer, isHost, 0);
                    window.setVisible(true);
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ChatGUI(String user, String guest, Socket socket, int port, boolean isHosts, int a)
            throws Exception {
        nameUser = user;
        nameGuest = guest;
        socketChat = socket;
        this.portServer = port;
        this.isHost = isHosts;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Bạn đang chat với  " + nameGuest + " Port: " + port);
        initComponents();
        if (isHost) {
            chatHost = new RoomHost(socketChat, nameUser, nameGuest);
            chatHost.start();
        } else {
            chatGuest = new RoomGuest(socketChat, nameUser, nameGuest);
            chatGuest.start();
        }
//        chatRoom = new Room(socketChat, nameUser, nameGuest);
//        chatRoom.start();
    }

    public class RoomHost extends Thread { // Phongg chat cua Host

        private Socket connect;
        private ObjectOutputStream hostOut;
        private ObjectInputStream hostIn;

        public RoomHost(Socket connection, String nameHost, String Guest) {
            connect = connection;
            nameGuest = Guest;
        }

        @Override
        public void run() {
            while (!isStop) {
                try {
                    hostIn = new ObjectInputStream(connect.getInputStream());
                    Object obj = hostIn.readObject();
                    RequestSever message = (RequestSever) obj;
                    String request = message.getRequest();
                    String data = message.getData();
                    switch (request) {
                        case RequestSever.PLAYONE: // Gửi JPane Yêu cầu chơi
                            int isOke = Client.request(nameGuest + " mời bạn chơi Oẳn tù tì", true);
                            if (isOke == 1) {
                                Client.request(nameGuest + " đã từ chối chơi với bạn!", false);
                            }
                            if (isOke == 0) {
                                hostPlayer = new Onegame(this, nameUser);
                                hostPlayer.setVisible(true);
                                chatHost.sendRequest(new RequestSever(RequestSever.ACCEPT_PLAY, null));
                            }
                            break;
                        case RequestSever.ACCEPT_PLAY: // Gửi yêu cầu đồng ý 
                            hostPlayer = new Onegame(this, nameUser);
                            hostPlayer.setVisible(true);
                            break;
                        case RequestSever.SEND_GUEST_TURN:                           
                            hostPlayer.setGuestTurn(data);
//                            int res = Integer.parseInt(data);                           
//                            switch (res) {
//                                case 0:
//                                    Client.request("DRAW", false);
//                                    break;
//                                case 1:
//                                    Client.request("YOU WIN!", false);
//                                    break;
//                                case -1:
//                                    Client.request("YOU LOSED", false);
//                                    break;
//                               default:
//                                    Client.request("Như", false);
//                            }
//                            String result = Integer.toString(res);
//                            chatHost.sendRequest(new RequestSever(RequestSever.SEND_RESULT, data));
                            break;
                        default:
                            updateChat_receive(request);
                            break;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        public synchronized void sendRequest(RequestSever obj) throws Exception {
            hostOut = new ObjectOutputStream(connect.getOutputStream());
//            String message = obj.toString();
            hostOut.writeObject(obj);
            hostOut.flush();

        }
    }

    public class RoomGuest extends Thread {

        private Socket connect;
        private ObjectOutputStream guestOut;
        private ObjectInputStream guestIn;

        public RoomGuest(Socket connection, String nameHost, String Guest) {
            this.connect = connection;
            nameGuest = Guest;
        }

        @Override
        public void run() {
            while (!isStop) {
                try {
                    guestIn = new ObjectInputStream(connect.getInputStream());
                    Object obj = guestIn.readObject();
                    RequestSever message = (RequestSever) obj;
                    String request = message.getRequest();
                    String data = message.getData();
                    switch (request) {
                        case RequestSever.PLAYONE: // Gửi JPane Yêu cầu chơi
                            int isOke = Client.request(nameGuest + " mời bạn chơi Oẳn tù tì", true);
                            if (isOke == 1) {
                                isPlayOOT = false;
                                Client.request(nameGuest + " đã từ chối chơi với bạn!", false);
                            }
                            if (isOke == 0) {
                                guestPlayer = new Onegame(this, nameUser);
                                guestPlayer.setVisible(true);
                                chatGuest.sendRequest(new RequestSever(RequestSever.ACCEPT_PLAY));
                            }
                            break;
                        case RequestSever.ACCEPT_PLAY:
                            guestPlayer = new Onegame(this, nameUser);
                            guestPlayer.setVisible(true);
                            break;
                        case RequestSever.SEND_RESULT:
                            switch (Integer.parseInt(data)) {
                                case 0:
                                    Client.request("DRAW", false);
                                    break;
                                case 1:
                                    Client.request("YOU LOSED", false);
                                    break;
                                case -1:
                                    Client.request("YOU WIN!", false);
                                    break;
                                default:
                                    Client.request("Như Đầu Bòi", false);
                            }
                            break;

                        default:
                            updateChat_receive(request);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        public synchronized void sendRequest(RequestSever obj) throws Exception {
            guestOut = new ObjectOutputStream(connect.getOutputStream());
//            Object message = obj.toString();
            guestOut.writeObject(obj);
            guestOut.flush();
        }
    }

    public void updateChat_send(String msg) {
        txtChatContent.append("Bạn " + ": " + msg + "\n");
    }

    public void updateChat_receive(String msg) {
        txtChatContent.append(nameGuest + ": " + msg + "\n");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        txtMessage = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatContent = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Send");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txtChatContent.setColumns(20);
        txtChatContent.setRows(5);
        jScrollPane1.setViewportView(txtChatContent);

        jButton2.setText("Caro");

        jButton3.setText("Oẳn Tù Tì");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        btnExit.setText("Exit");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addGap(18, 18, 18))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 70, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnExit)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String msg = txtMessage.getText();
        if (msg.equals("")) {
            return;
        }
        try {
            if (isHost) {
                chatHost.sendRequest(new RequestSever(msg));
                updateChat_send(msg);
                txtMessage.setText("");
            } else {
                chatGuest.sendRequest(new RequestSever(msg));
                updateChat_send(msg);
                txtMessage.setText("");
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if (isHost) {
                chatHost.sendRequest(new RequestSever(RequestSever.PLAYONE));
            } else {
                chatGuest.sendRequest(new RequestSever(RequestSever.PLAYONE));
            }
        } catch (Exception ex) {
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        try {
//            chatRoom.sendRequest("<EXIT>");
        } catch (Exception ex) {
            Logger.getLogger(ChatGUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnExitActionPerformed

    // Chat 
    // Gửi Icon
    // Gửi File
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new ChatGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea txtChatContent;
    private javax.swing.JTextField txtMessage;
    // End of variables declaration//GEN-END:variables
}
