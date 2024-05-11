/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flooding;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;



public class Flooding {

        public static void main(String[] args) {
        try {
            Server n1 = new Node("localhost", 3000);
            n1.start();
                        // Server n2 = new Node("192.168.205.117", 3003);
                        // n2.start();

            System.out.println(n1.neighbors_());
            System.out.println(n1.toString_());
             Server n2 = new Node("localhost",3001);
             n2.start();
             Server n3 = new Node("localhost", 3002);
             n3.start();
//            System.out.printf("%s%n%s%n%s%n",n1.neighbors_(),n2.neighbors_(),n3.neighbors_());
             System.out.println(n1.broadcast(new Message("hello.txt", 5), n1));
//            System.out.println(n3.has("hello.txt"));
// n1.broadcast(new Message("hello hima.txt", 5), n1);
        } catch (AlreadyBoundException | RemoteException e) {
            e.printStackTrace(System.out);
        }
    }

}
