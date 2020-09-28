package es.eltrueno.f1modmanager;


import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;

public class Main {

    private static Frame frame = null;

    public static void main(String[] args) {
        Win win = new Win();
        win.setVisible(true);
        win.setResizable(false);
        frame = win;
    }
}
