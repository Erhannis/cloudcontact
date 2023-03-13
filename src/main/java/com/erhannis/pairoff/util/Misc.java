/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.erhannis.pairoff.util;

/**
 *
 * @author erhannis
 */
public class Misc {
    public static Integer tryParseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Throwable e) {
            return null;
        }
    }
}
