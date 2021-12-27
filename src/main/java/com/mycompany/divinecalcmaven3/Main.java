/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.divinecalcmaven3;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rosha
 *
 *
 */
public class Main {

    public static String directionGlobal = "";
    public static int targetBlockGlobal = 0;
    public static String clipData;
    public static ArrayList<Coord> coordList = new ArrayList<>();
    public static ArrayList<fossilCoord> fossilList = new ArrayList<>();
    public static setBinds sb = new setBinds();

    public static void main(String[] args) {

        System.out.println("test");

        sb.updateBinds();

        Main m = new Main();
        m.toTray();

        //setBinds sb = new setBinds();
        sb.updateBinds();

        hotkeyListener hk = new hotkeyListener();
        hk.reader();

    }

    public void toTray() {

        try {

            if (SystemTray.isSupported()) {
                File f = new File(getDir() + "/src/resources/icon.png");

                Image img = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());

                TrayIcon ti = new TrayIcon(img);

                PopupMenu pop = new PopupMenu();
                MenuItem exit = new MenuItem("Exit");
                MenuItem rebind = new MenuItem("Re-Bind");

                SystemTray tray = SystemTray.getSystemTray();
                tray.add(ti);
                pop.add(exit);
                pop.add(rebind);

                ti.setPopupMenu(pop);

                exit.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sb.dispose();
                        System.exit(0);

                    }
                });

                rebind.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        sb.setVisible(true);

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setBinds() {
        sb.setVisible(true);

    }

    public static String getDir() {

        String userDir = System.getProperty("user.dir");
        String backChar = "\\";
        String forwChar = "/";

        char c = backChar.charAt(0);
        char c2 = forwChar.charAt(0);

        char[] ar = userDir.toCharArray();

        for (int i = 0; i < ar.length; i++) {
            if (ar[i] == c) {
                ar[i] = c2;
            }
        }

        userDir = String.valueOf(ar);

        return userDir;

    }

    public static void runPortal() {

        copy();

        String coordsPaste = clipData;

        Boolean validPaste = false;

        if (coordsPaste.contains("the_nether")) {
            validPaste = true;
        } else {
            paste("No Valid Nether Coord Found on ClipBoard, use f3+c");
        }

        if (validPaste) {

            //System.out.println(coordsPaste);
            fetch(convert(coordsPaste, true).facing, getDir() + "/sheets/Book1.xlsx");

            int x = convert(coordsPaste, false).x;
            int y = convert(coordsPaste, false).y;

            Coord user = new Coord(x, y);

            String output = (Sortbest(user).x + "," + Sortbest(user).y + " (dist: " + Sortbest(user).value + " Blocks) | ");

            coordList.clear();
            //System.out.println("now there are " + coordList.size() + " elements");
            fetch(convert(coordsPaste, false).facing, getDir() + "/sheets/Book2.xlsx");

            String outputOptimal = (Sortbest(user).x + "," + Sortbest(user).y + " (dist: " + Sortbest(user).value + " Blocks) ");

            String fullOutput = output + "" + outputOptimal;

            String goTo = "";

            switch (directionGlobal) {
                case "north":
                    goTo = "neg pos";
                    break;
                case "east":
                    goTo = "pos pos";
                    break;
                case "south":
                    goTo = "pos neg";
                    break;
                case "west":
                    goTo = "neg neg";
                    break;

            }

            paste(fullOutput + "[" + goTo + "]");

            coordList.clear();
        }

    }

    public static void runFossil() {
        copy();

        String coordsPaste = clipData;

        Boolean validPaste = false;

        if (coordsPaste.contains("the_nether")) {
            validPaste = true;
        } else {
            paste("No Valid Nether Coord Found on ClipBoard, use f3+c");
        }

        if (validPaste) {

            fetchDivine(getDir() + "/sheets/Book3.xlsx");

            //System.out.println(getDir() + "/sheets/Book3.xlsx");
            int x = convert(coordsPaste, false).x;
            int y = convert(coordsPaste, false).y;

            Coord user = new Coord(x, y);

            selectScreen2 sc2 = new selectScreen2();

            sc2.setVisible(true);

            while (sc2.isVisible()) {
                System.out.print("");
            }

            sortBestFossil(user, targetBlockGlobal);

            String output = (Sortbest(user).x + "," + Sortbest(user).y + " (dist: " + Sortbest(user).value + " Blocks) | ");

            coordList.clear();
            fossilList.clear();

            fetchDivine(getDir() + "/sheets/Book4.xlsx");

            sortBestFossil(user, targetBlockGlobal);

            String outputOptimal = (Sortbest(user).x + "," + Sortbest(user).y + " (dist: " + Sortbest(user).value + " Blocks) ");

            String fullOutput = output + "" + outputOptimal;

            paste(fullOutput);

            coordList.clear();
            fossilList.clear();

        }

    }

    public static String copy() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            Transferable t = clipboard.getContents(null);

            clipData = (String) t.getTransferData(DataFlavor.stringFlavor);

        } catch (Exception e) {
            System.out.println(e);
        }

        return clipData;
    }

    public static void paste(String out) {
        try {

            //finish later
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection stringSelection = new StringSelection(out);
            clip.setContents(stringSelection, null);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //converts pasted coords into obj
    public static userCoord convert(String rawCoords, boolean gui) {

        String trimmedCoords = rawCoords.substring(43, rawCoords.length());

        //System.out.println(String.valueOf(trimmedCoords));
        String[] dataList = trimmedCoords.split(" ");

        //System.out.println(Arrays.toString(dataList));
        int x = Math.round(Float.valueOf(dataList[0]));
        int y = Math.round(Float.valueOf(dataList[2]));
        int value = 0;

        if (gui == true) {
            selectScreen sc = new selectScreen();

            //sc.setOpacity((float) 0.85);
            sc.setVisible(true);

            while (sc.isVisible()) {
                System.out.print("");
            }
        }

        userCoord uc = new userCoord(x, y, directionGlobal);

        return uc;

    }

    //converts all possible coords into global array
    public static String fetch(String facing, String direc) {
        try {

//            ArrayList<Coord> coordList = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(direc));
            Workbook workbook = new XSSFWorkbook(file);

            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Sheet> sheets = workbook.sheetIterator();

            while (sheets.hasNext()) {
                Sheet sh = sheets.next();
                if (sh.getSheetName().equalsIgnoreCase(facing)) {

                    //System.out.println(direc);
//                    System.out.println("Sheet name is " + sh.getSheetName());
//                    System.out.println("------------");
                    Iterator<Row> iterator = sh.iterator();
                    while (iterator.hasNext()) {
                        Row row = iterator.next();
                        Iterator<Cell> cellIterator = row.iterator();
                        int i = 0;
                        int xValue = 0;
                        int yValue = 0;
                        while (cellIterator.hasNext()) {
                            i++;
                            Cell cell = cellIterator.next();
                            String cellValue = dataFormatter.formatCellValue(cell);

                            if (!cellValue.isBlank()) {
                                if (i == 1) {
                                    xValue = Integer.valueOf(cellValue.toString());

                                } else if (i == 2) {
                                    yValue = Integer.valueOf(cellValue.toString());

                                    Coord co = new Coord(xValue, yValue);

                                    coordList.add(co);

                                    i = 0;
                                }
                            }
                        }

                    }
                } else {
//                    System.out.println("diff name");
                }
            }

            workbook.close();

            for (int i = 0; i < coordList.size(); i++) {
                //System.out.println(String.valueOf(coordList.get(i).x) + "," + String.valueOf(coordList.get(i).y));
            }

        } catch (Exception e) {
            //System.out.println(e);

        }

        return facing;
    }

    public static String fetchDivine(String direc) {
        try {

//            ArrayList<Coord> coordList = new ArrayList<>();
            FileInputStream file = new FileInputStream(new File(direc));
            Workbook workbook = new XSSFWorkbook(file);

            DataFormatter dataFormatter = new DataFormatter();
            Iterator<Sheet> sheets = workbook.sheetIterator();

            while (sheets.hasNext()) {
                Sheet sh = sheets.next();
                //System.out.println(direc);
                Iterator<Row> iterator = sh.iterator();
                while (iterator.hasNext()) {
                    Row row = iterator.next();
                    Iterator<Cell> cellIterator = row.iterator();

                    int i = 0;

                    int targetBlock = 0;

                    int x1 = 0;
                    int y1 = 0;

                    int x2 = 0;
                    int y2 = 0;

                    int x3 = 0;
                    int y3 = 0;

                    while (cellIterator.hasNext()) {
                        i++;
                        Cell cell = cellIterator.next();
                        String cellValue = dataFormatter.formatCellValue(cell);

                        if (!cellValue.isBlank()) {
                            switch (i) {
                                case 1:
                                    targetBlock = Integer.valueOf(cellValue);
                                    break;
                                case 2:
                                    x1 = Integer.valueOf(cellValue);
                                    break;
                                case 3:
                                    y1 = Integer.valueOf(cellValue);
                                    break;
                                case 4:
                                    x2 = Integer.valueOf(cellValue);
                                    break;
                                case 5:
                                    y2 = Integer.valueOf(cellValue);
                                    break;
                                case 6:
                                    x3 = Integer.valueOf(cellValue);
                                    break;
                                case 7:
                                    y3 = Integer.valueOf(cellValue);

                                    fossilCoord fs = new fossilCoord(targetBlock, x1, y1, x2, y2, x3, y3);

                                    fossilList.add(fs);

                                    i = 0;

                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                }

            }

            workbook.close();

            for (int i = 0; i < coordList.size(); i++) {
                //System.out.println(String.valueOf(coordList.get(i).x) + "," + String.valueOf(coordList.get(i).y));
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());

        }

        return direc;

    }

    //calc distance
    public static double calcDist(int x1, int y1, int x2, int y2) {

        double dist = Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));

        return dist;
    }

    //find the closest coordinate
    public static Coord Sortbest(Coord user) {

        for (int i = 0; i < coordList.size(); i++) {
            double dist = calcDist(coordList.get(i).x, coordList.get(i).y, user.x, user.y);

            coordList.get(i).setValue((int) (Math.round(dist)));

//            System.out.println(Double.toString(dist) + " From " + coordList.get(i).x + "," + coordList.get(i).y);
        }

        Collections.sort(coordList);

        return coordList.get(0);
    }

    public static Coord sortBestFossil(Coord user, int targetBlock) {

        double dist = 0;

        for (int i = 0; i < fossilList.size(); i++) {

            if (fossilList.get(i).getTargetBlock() == targetBlock) {

                dist = calcDist(fossilList.get(i).x1, fossilList.get(i).y1, user.x, user.y);
                Coord c1 = new Coord(fossilList.get(i).x1, fossilList.get(i).y1);
                c1.setValue((int) (Math.round(dist)));
                coordList.add(c1);

                dist = calcDist(fossilList.get(i).x2, fossilList.get(i).y2, user.x, user.y);
                Coord c2 = new Coord(fossilList.get(i).x2, fossilList.get(i).y2);
                c2.setValue((int) (Math.round(dist)));
                coordList.add(c2);

                dist = calcDist(fossilList.get(i).x3, fossilList.get(i).y3, user.x, user.y);
                Coord c3 = new Coord(fossilList.get(i).x3, fossilList.get(i).y3);
                c3.setValue((int) (Math.round(dist)));
                coordList.add(c3);
            }

        }

        Collections.sort(coordList);

        return coordList.get(0);
    }

}
