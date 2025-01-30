/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.service;

/**
 *
 * @author HP-MEDALFA
 */
public class Message {
    public String message;
    public Integer status;
    public Integer type;
    
    public Message(String message, Integer status, Integer type) {
        this.message = message;
        this.status = status;
        this.type = type;
    }

    public Message() {
    }
    
    @Override 
    public String toString(){
        return "Mensaje: "+this.message+" |status: "+status +" |type: "
                +type;
    }
    
}
