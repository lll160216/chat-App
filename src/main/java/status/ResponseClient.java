/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package status;

import user.Peer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class ResponseClient {

    public static boolean isUserExist(String name, ArrayList<Peer> listPeer) {
        int size = listPeer.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                Peer peer = listPeer.get(i);
                if (name.equals(peer.getNamePeer())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Lay danh sach peer
    public static ArrayList<Peer> getAllUser(String ls) {
        ArrayList<Peer> listPeer = new ArrayList<>();
        StringTokenizer Str1 = new StringTokenizer(ls, "-");
        while (Str1.hasMoreElements()) {
            listPeer.add(getPeer(Str1.nextToken()));
        }
        return listPeer;
    }

    // Test List Peer
    public static void test(ArrayList<Peer> ls) {
        int size = ls.size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            Peer pr = ls.get(i);
            System.out.print("Peer " + i + ":  ");
            System.out.println("Name: " + pr.getNamePeer() + " Host: " + pr.getHostPeer() + " Port: " + pr.getPortPeer());
        }
    }

    // Lay 1 peer tu Strtokenizer
    public static Peer getPeer(String PeerWithSpace) {
//        Peer peer = new Peer();
        StringTokenizer Str = new StringTokenizer(PeerWithSpace);
        String name = "";
        String host = "";
        int port = 0;
        while (Str.hasMoreElements()) {
            name = Str.nextToken();
            host = Str.nextToken();
            port = Integer.parseInt(Str.nextToken());
        }
        Peer peer = new Peer(name, host, port);
        return peer;
    }

    public static int show(JFrame frame, String msg, boolean type) {
        if (type) {
            return JOptionPane.showConfirmDialog(frame, msg, null, JOptionPane.YES_NO_OPTION);
        }
        JOptionPane.showMessageDialog(frame, msg);
        return -1;
    }
}
