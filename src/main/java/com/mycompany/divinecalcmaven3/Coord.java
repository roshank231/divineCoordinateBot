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
public class Coord implements Comparable<Coord>{
 
    int x;
    int y;
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    

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

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int compareTo(Coord compareCoord) {
        int compareValue=((Coord)compareCoord).getValue();
        /* For Ascending order*/
        return this.value-compareValue;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
    
}
