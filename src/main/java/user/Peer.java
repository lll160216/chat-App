/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

/**
 *
 * @author ASUS
 */
public class Peer { 
    private String namePeer = "";
    private String hostPeer ="";
    private int portPeer = 0;
    public Peer(){
       
    }
    public Peer(String name, String host, int port) {
		namePeer = name;
		hostPeer = host;
		portPeer = port;
    }
     public void setPeer(String name, String host, int port) {
		namePeer = name;
		hostPeer = host;
		portPeer = port;
    }
    
    public String getNamePeer() {
        return namePeer;
    }

    public String getHostPeer() {
        return hostPeer;
    }

    public int getPortPeer() {
        return portPeer;
    }

    public void setNamePeer(String namePeer) {
        this.namePeer = namePeer;
    }

    public void setHostPeer(String hostPeer) {
        this.hostPeer = hostPeer;
    }

    public void setPortPeer(int portPeer) {
        this.portPeer = portPeer;
    }
    
    
}
