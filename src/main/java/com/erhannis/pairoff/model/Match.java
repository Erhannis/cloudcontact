/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.erhannis.pairoff.model;

import java.util.Date;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity("match_tbl")
public class Match {
    
    @Id
    public ObjectId id;
    
    @Reference
    @Indexed
    public User userA;
    // If null, userA has not confirmed match
    public Date dateMatchedA;
    
    @Reference
    @Indexed
    public User userB;
    // If null, userB has not confirmed match
    public Date dateMatchedB;
    
    public Match() {
        
    }
    
    public Match(User userA, User userB) {
		this.userA = userA;
        this.userB = userB;
    }
    
    public boolean matchConfirmed() {
        if (dateMatchedA == null) {
            return false;
        }
        if (dateMatchedB == null) {
            return false;
        }
        return true;
    }
    
    //TODO Should this return the newest Match Text, or the Match Text in existence at the time the Match was confirmed?
    
    public String getMatchTextA() {
        if (!matchConfirmed()) {
            return null; //TODO Empty string?
        }
        return userA.matchInfo.entrySet().stream().sorted((o1, o2) -> {
            return o1.getKey().compareTo(o2.getKey());
        }).findFirst().map(e -> e.getValue()).orElse(userA.email);
    }

    public String getMatchTextB() {
        if (!matchConfirmed()) {
            return null; //TODO Empty string?
        }
        return userB.matchInfo.entrySet().stream().sorted((o1, o2) -> {
            return o1.getKey().compareTo(o2.getKey());
        }).findFirst().map(e -> e.getValue()).orElse(userB.email);
    }
    
    @Override
    public String toString() {
        return "Match{"+id+":" + userA + ", " + userB + "}";
    }
}
