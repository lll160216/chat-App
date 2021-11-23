/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package status;

import java.io.Serializable;
import java.util.ArrayList;
import user.Peer;

/**
 *
 * @author ASUS
 */  

public class RequestSever implements Serializable{
    public final String request;
    public String data;
    public static final String MESSAGE = "";
    public static final String  PLAYONE = "<PLAY_ONE_TWO_THREE>";
    public static final String ACCEPT_PLAY = "<ACCEPT_PLAY_ONE_TWO_THREE>";
    public static final String SEND_GUEST_TURN = "SEND_MY_TURN"; // Gửi kéo búa lá
    public static final String SEND_RESULT = "SEND_LOSE_WIN";
    
    public RequestSever(String request){
        this.request = request;
    }
    public RequestSever(String request, String data){
        this.request = request;
        this.data = data;
    }
    public String getRequest(){
        return request;
    }
    public String getData(){
        return data;
    }
    public static String createAcount(String name, int port){
        return name + ";" + port ;
    }
    public static ArrayList<Peer> Disconnect(ArrayList<Peer> listPeers,String name){
        int size = listPeers.size();
        for(int i=0 ; i<size;i++){
            Peer peer = listPeers.get(i);
            if(peer.getNamePeer().equals(name)){
                listPeers.remove(i);
                break;
            }
        }
        return listPeers;       
    }
    public static String freshIP(String IPAddress){
        IPAddress = IPAddress.replaceAll("/","");
        return IPAddress;
    }
}
