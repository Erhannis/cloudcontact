/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smatt.cc.auth;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 *
 * @author smatt
 */
@Entity("user_tbl")
public class User {
    
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String email = "";
    private String salt = "";
    private String verifier = "";
    private String token = "";
    @Embedded
    public UserInfo info = new UserInfo();
    
    
    public User() {
        
    }
    
    public User(String email, String salt, String verifier ) {
		this.salt = salt;
		this.verifier = verifier;
		this.email = email;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
    
    
    @Override
    public String toString() {
        return "USER " + getId() + " (" + info.name + ", " + info.dob + ") : " + getEmail();
    }
}
