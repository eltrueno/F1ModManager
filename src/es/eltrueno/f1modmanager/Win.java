package es.eltrueno.f1modmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class Win extends JFrame {


    public Win() {
        super("F1 Mod Manager - by el_trueno");
        setSize(600, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        try{
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Container modsroute_container = getContentPane();
        modsroute_container.setLayout(new FlowLayout());

        JLabel modsroute_label = new JLabel("Mods path:");
        JTextField modsroute_textfield = new JTextField(40);
        JButton modsroute_changebutton = new JButton("Change path");
        modsroute_changebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(!modsroute_textfield.getText().isEmpty() && modsroute_textfield.getText()!=null){
                    chooser.setCurrentDirectory(new File(modsroute_textfield.getText()));
                }
                if (chooser.showOpenDialog(modsroute_changebutton) == JFileChooser.APPROVE_OPTION){
                    modsroute_textfield.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        modsroute_container.add(modsroute_label);
        modsroute_container.add(modsroute_textfield);
        modsroute_container.add(modsroute_changebutton);


        Container gameroute_container = new Container();
        this.add(gameroute_container);
        gameroute_container.setLayout(new FlowLayout());

        JLabel game_label = new JLabel("Game path:");
        JTextField game_textfield = new JTextField(40);
        JButton game_changebutton = new JButton("Change path");
        game_changebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if(!game_textfield.getText().isEmpty() && game_textfield.getText()!=null){
                    chooser.setCurrentDirectory(new File(game_textfield.getText()));
                }
                if (chooser.showOpenDialog(game_changebutton) == JFileChooser.APPROVE_OPTION){
                    game_textfield.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        gameroute_container.add(game_label);
        gameroute_container.add(game_textfield);
        gameroute_container.add(game_changebutton);

        Container applymods_container = new Container();
        this.add(applymods_container);
        applymods_container.setLayout(new GridLayout());
        JButton applymods_button = new JButton("APPLY MODS TO GAME");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setSize(30, 10);
        applymods_button.setToolTipText("Copy the mod folders and files to the f1 game folder");
        applymods_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!game_textfield.getText().isEmpty() && !modsroute_textfield.getText().isEmpty()){
                    Thread thread = new Thread("mod_install"){
                        public void run(){
                            try {
                                final String mods_path = modsroute_textfield.getText();
                                final String game_path = game_textfield.getText();

                                applymods_button.setEnabled(false);
                                progressBar.setVisible(true);
                                progressBar.setIndeterminate(true);
                                progressBar.setString("Calculating...");
                                progressBar.setStringPainted(true);

                                /*try {
                                    sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }*/

                                progressBar.setIndeterminate(false);
                                progressBar.setMinimum(1);

                                ArrayList<File> listf = listf(mods_path);
                                progressBar.setMaximum(listf.size());
                                applymods_button.disable();
                                for(File file : listf){
                                    progressBar.setString("Copyying "+file.getName()+"... "+(listf.indexOf(file)+1)+" of "+listf.size()+" ("+((listf.indexOf(file)+1)*100)/listf.size()+"%)");
                                    progressBar.setValue((listf.indexOf(file)+1));
                                    /*try {
                                        sleep(1000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }*/
                                    if(file.isFile()){
                                        String fdir = file.getPath().substring(mods_path.toCharArray().length);
                                        try {
                                            System.out.println("coppying "+file.getPath()+" to "+game_path+fdir);
                                            a(file, new File(game_path+fdir));
                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }
                                    }
                                }
                            }
                            finally {
                                applymods_button.setEnabled(true);
                                progressBar.setStringPainted(false);
                                progressBar.setValue(0);
                                applymods_button.setText("APPLY MODS TO GAME");
                                JOptionPane.showMessageDialog(applymods_button, "Installation finished.", "F1 Mod Manager", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    };
                    thread.start();
                    /*synchronized (thread){
                        try {
                            thread.wait(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }*/
                }else{
                    JOptionPane.showMessageDialog(applymods_button, "Paths must be defined!.", "F1 Mod Manager", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        applymods_container.add(applymods_button);
        applymods_container.add(progressBar);

    }

    public ArrayList<File> listf(String directoryName) {
        File directory = new File(directoryName);

        ArrayList<File> resultList = new ArrayList<File>();

        // get all the files from a directory
        File[] fList = directory.listFiles();
        resultList.addAll(Arrays.asList(fList));
        for (File file : fList) {
            if (file.isFile()) {
                //System.out.println(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        return resultList;
    }

    public void a(File file , File target){
        InputStream is = null;
        OutputStream os = null;
        try {
            if(!target.exists()){
                new File(target.getParent()).mkdirs();
                new File(target.getPath()).createNewFile();
            }
            is = new FileInputStream(file);
            os = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile(File sourceLocation , File targetLocation)
            throws IOException {

        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            File[] files = sourceLocation.listFiles();
            for (File file : files) {
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(targetLocation + "/" + file.getName());

                // Copy the bits from input stream to output stream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }

}
