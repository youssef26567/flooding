/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flooding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 *
 * @author Mostafa
 */
public class Node extends UnicastRemoteObject implements Server {

    private final String ip_address;
    private final int PORT;
    private final Set<Server> neighbors = new HashSet<>();
    private final Map<String, File> localFiles = new HashMap<>();

    public Node(String ip_address, int PORT) throws AlreadyBoundException, RemoteException {
        this.ip_address = ip_address;
        this.PORT = PORT;
    }

    @Override
    public String ip() {
        return this.ip_address;
    }

    @Override
    public Set<Server> neighbors_() {
        return this.neighbors;
    }

    @Override
    public int PORT() {
        return this.PORT;
    }

    @Override
    public boolean has(String key) {
        return this.localFiles.containsKey(key);
    }

    @Override
    public File get(String key) {
        return this.localFiles.get(key);
    }

    @Override
    public boolean add_neighbor(Server n) {
        return this.neighbors.add(n);
    }

    @Override
    public void add_file(String key, File file) {
        this.localFiles.put(key, file);
    }

    @Override
    public final void start() {
        this.prepare_files();
        this.prepare_neighbors();
        this.start_();
    }

    private void start_() {
        try {
            Registry registry = LocateRegistry.createRegistry(this.PORT);
            registry.bind(this.ip_address + ":" + this.PORT, this);//172.0.0.1:3000
            System.out.println("PORT " + this.PORT + " is running...");
        } catch (AlreadyBoundException | RemoteException e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public Server broadcast(Message msg, Server sender) {
        msg.decrementTTL();

        if (msg.TTL() <= 0) {
            System.out.println("YES is 0");
            return null;
        }
        for (Server n : this.neighbors) {
            try {
            System.out.println("Node PORT is "+n.PORT());
                if (n.PORT() != sender.PORT()) {
                    Registry registry = LocateRegistry.getRegistry(n.ip(), n.PORT());
                    Server stub = (Server)  registry.lookup(n.ip() + ":" + n.PORT());
                    if (stub.has(msg.key())) {
                        System.out.println(n.toString_());
                        return stub;
                    } else {
                        return stub.broadcast(msg, stub);
                    }
                }
            } catch (NotBoundException | RemoteException e) {
                e.printStackTrace(System.out);
            }
        }
        return null;
    }

    private void prepare_neighbors() {
        try {
            File file = new File("src\\flooding\\" + ip_address + "_" + PORT + "\\neighbors.txt");
            if (file.exists()) {
                file.setReadable(true);
                try (FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] sp = line.split(",");
                        String ip = sp[0];
                        int p = Integer.parseInt(sp[1]);
                        Server s = new Node(ip, p);
                        this.neighbors.add(s);
                    }
                } catch (IOException | AlreadyBoundException e) {
                    e.printStackTrace(System.out);
                }
//                System.out.println(this.neighbors);
            } else {
                System.out.println("File doesn't exist");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(System.out);
        }
    }

    private void prepare_files() {
        try (Stream<Path> paths = Files.walk(Paths.get("src\\flooding\\" + ip_address + "_" + PORT + "\\files"))) {
            paths.filter(Files::isRegularFile)
                    //.map(Path::getFileName)
                    .forEach((f) -> {
                        String fileName = f.getFileName().toString();
                        File file = f.toFile();
                        this.localFiles.put(fileName, file);
                    });

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
//        System.out.println(this.localFiles);
    }

    @Override
    public byte[] download(String filename) {

        try {
            File file = new File("src\\flooding\\" + ip_address + "_" + PORT + "\\files" + filename);
            byte[] fileData = new byte[(int) file.length()];

            try (FileInputStream fis = new FileInputStream(file)) {
                fis.read(fileData);
            } catch (IOException ex) {
                ex.printStackTrace(System.out);
            }
            return fileData;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }

    @Override
    public String toString_() {
        return "Host => " + this.ip_address + "\nPort = " + this.PORT + "\n";
    }

}
