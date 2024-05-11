/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flooding;

import java.util.Stack;

/**
 *
 * @author Mostafa
 */
public interface FileSharingProtocol {

    Node send(Message message, Stack<Node> back_propagation);

}
