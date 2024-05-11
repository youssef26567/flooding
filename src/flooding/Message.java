/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flooding;

import java.io.Serializable;

/**
 *
 * @author Mostafa
 */
public class Message implements Serializable {

    private final String key;
    private int ttl;

    public Message(String key, int ttl) {
        this.key = key;
        this.ttl = ttl;
    }

    public int TTL() {
        return this.ttl;
    }

    public void decrementTTL() {
        --this.ttl;
    }

    public String key() {
        return this.key;
    }

    public boolean expired() {
        return this.ttl == 0;
    }
}
