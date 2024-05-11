/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flooding;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

/**
 *
 * @author Mostafa
 */
public interface Server extends Remote {

    public String ip() throws RemoteException;

    public int PORT() throws RemoteException;

    public boolean has(String key) throws RemoteException;

    public File get(String key) throws RemoteException;

    public boolean add_neighbor(Server n) throws RemoteException;

    public void add_file(String key, File file) throws RemoteException;

    public void start() throws RemoteException;

    public Server broadcast(Message msg, Server sender) throws RemoteException;
    
    public byte[] download(String filename) throws RemoteException;
    
    public Set<Server> neighbors_() throws RemoteException;
    
    public String toString_() throws RemoteException;
}
