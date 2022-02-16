/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("user_tbl")
public class User {
    public static enum Gender {
        CISMALE,
        CISFEMALE,
        TRANSMALE,
        TRANSFEMALE,
        OTHER;
        
        @Override
        public String toString() {
            switch (this) {
                case CISMALE: return "cismale";
                case CISFEMALE: return "cisfemale";
                case TRANSMALE: return "transmale";
                case TRANSFEMALE: return "transfemale";
                case OTHER: return "other";
                default: return "ERROR"; //???
            }
        }
    }
    
    @Id
    public ObjectId id;
    
    @Indexed(options = @IndexOptions(unique = true))
    public String email = ""; // Required //SENSITIVE
    public String salt = ""; //TODO Are these sensitive?
    public String verifier = "";
    public String token = "";
    
    public String name = ""; // Required //SENSITIVE //TODO This is sensitive info - do we need it?
    public Date dob = new Date(); // Required //SENSITIVE
    //TODO Unique?
    public String phone; // Optional //SENSITIVE
    public Map<Date, String> matchInfo = new HashMap<>(); // Required //SENSITIVE
    public Gender gender; // Required //SENSITIVE
    public Set<Gender> attractedTo = new HashSet<>(); // Required //SENSITIVE
    
    public User() {
        
    }
    
    public User(String email, String salt, String verifier ) {
		this.salt = salt;
		this.verifier = verifier;
		this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{"+id+":" + name + ", " + gender + ", " + dob +", "+ email + ", "+phone+"}"; //SECURITY //TODO Should we have these sensitive fields here?
    }
}
