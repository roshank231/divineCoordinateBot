/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.divinecalcmaven3;
/**
 *
 * @author rosha
 */
public class userCoord {
    
    int x;
    int y;
    
    String facing;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }
    
    


    public userCoord(int x, int y, String facing) {
        this.x = x;
        this.y = y;
        this.facing = facing;
    }
    
    
    
    
    
    
}
