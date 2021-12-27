/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.divinecalcmaven3;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 *
 * @author rosha
 */
public class hotkeyListener {
    
    public static String portalBind = "";
    public static String fossilBind = "";

    ArrayList<String> typed = new ArrayList<>();
    public boolean activated = false;
    public boolean activated2 = false;
    
    //public boolean activated3 = false;

    public void reader() {

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

                @Override
                public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {

                    String keyText = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
                    System.out.println(keyText);

                    if (activated2 == true & keyText.equalsIgnoreCase(portalBind)) {
                        Main.runPortal();

                        activated = false;
                        activated2 = false;
                    } else if (activated2 == true & keyText.equalsIgnoreCase(fossilBind)) {
                        Main.runFossil();

                        activated = false;
                        activated2 = false;

                    } else if (activated2 == true & !keyText.equalsIgnoreCase(portalBind) | activated2 == true & !keyText.equalsIgnoreCase(fossilBind)) {
                        activated = false;
                        activated2 = false;
                    }

                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeEvent) {

                    String keyText = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());

                    if (keyText.equalsIgnoreCase("ctrl") | keyText.equalsIgnoreCase("alt")) {
                        typed.add(keyText);
                        activated = true;

                        if (typed.contains("Ctrl") & typed.contains("Alt")) {
                            activated2 = true;
                            System.out.println("its true");
                        }
                    }
                }
            });
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

    }

}
