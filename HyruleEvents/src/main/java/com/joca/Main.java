package com.joca;

import com.joca.frontend.FramePrincipal;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        FramePrincipal mainFrame = new FramePrincipal();
        mainFrame.setVisible(true);
    }
}
