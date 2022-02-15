/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("user_tbl")
public class User {
    
    @Id
    public ObjectId id;
    
    @Indexed(options = @IndexOptions(unique = true))
    public String email = ""; //SENSITIVE
    public String salt = ""; //TODO Are these sensitive?
    public String verifier = "";
    public String token = "";
    
    public String name = ""; //SENSITIVE //TODO This is sensitive info - do we need it?
    public Date dob = new Date(); //SENSITIVE
    //TODO Unique?
    public String phone; //SENSITIVE
    
    
    public User() {
        
    }
    
    public User(String email, String salt, String verifier ) {
		this.salt = salt;
		this.verifier = verifier;
		this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{"+id+":" + name + ", " + dob +", "+ email + ", "+phone+"}"; //SECURITY //TODO Should we have these sensitive fields here?
    }
}
