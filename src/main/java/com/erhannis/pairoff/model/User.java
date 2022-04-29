/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.time.LocalDate;
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

    //TODO Version fields other than matchInfo?
    
    @Indexed(options = @IndexOptions(unique = true))
    public String email = ""; // Required //SENSITIVE
    public String salt = ""; //TODO Are these sensitive?
    public String verifier = "";
    public String token = "";
    
    public boolean acceptedTOS = false; // Required
    public String name = ""; // Required //SENSITIVE //TODO This is sensitive info - do we need it?
    public LocalDate dob = null; // Required //SENSITIVE
    //TODO Unique?
    public String phone; // Optional //SENSITIVE
    public Map<Date, String> matchInfo = new HashMap<>(); // Optional, technically //SENSITIVE
    public Gender gender; // Required //SENSITIVE
    public Set<Gender> attractedTo = new HashSet<>(); // Required //SENSITIVE
    //TODO Once a year, suggest update
    public Integer minAge = null; // Optional // In years
    public Integer maxAge = null; // Optional // In years
    
    public User() {
        
    }
    
    public User(String email, String salt, String verifier ) {
		this.salt = salt;
		this.verifier = verifier;
		this.email = email;
    }
    
    public boolean isComplete() {
        boolean complete = true;
        complete &= (acceptedTOS);
        complete &= (name != null && !name.isEmpty());
        complete &= (dob != null);
        complete &= (gender != null);
        complete &= (!attractedTo.isEmpty());
        return complete;
    }
    
    public String nextStep() {
        if (!acceptedTOS) {
            return "TOS";
        }
        if ((name == null || name.isEmpty()) || (dob == null)) {
            return "DETAILS";
        }
        if ((gender == null) || (attractedTo.isEmpty())) {
            return "DETAILS2";
        }
        return null;
    }
    
    public String getCurrentMatchText(boolean substituteDefault) {
        return matchInfo.entrySet().stream().sorted((o1, o2) -> {
            return -o1.getKey().compareTo(o2.getKey());
        }).findFirst().map(e -> e.getValue()).orElse(substituteDefault ? email : null);
    }
    
    
    @Override
    public String toString() {
        return "User{"+id+":" + name + ", " + gender + ", " + dob +", "+ email + ", "+phone+"}"; //SECURITY //TODO Should we have these sensitive fields here?
    }
}
