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
    public String email = "";
    public String salt = "";
    public String verifier = "";
    public String token = "";
    
    public String name = ""; //TODO This is sensitive info - do we need it?
    public Date dob = new Date();
    
    
    public User() {
        
    }
    
    public User(String email, String salt, String verifier ) {
		this.salt = salt;
		this.verifier = verifier;
		this.email = email;
    }
    
    @Override
    public String toString() {
        return "USER " + id + " (" + name + ", " + dob + ") : " + email;
    }
}
