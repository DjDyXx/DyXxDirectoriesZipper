import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
//        вместо ZIPFOLDERS
//        ArrayList<String> listOfFiles = new ArrayList<>();
//        File dir1 = new File(".\\");
//        Collections.addAll(listOfFiles, dir1.list());
//        String name = "";
//        for (int file = 0; file < listOfFiles.size(); file++) {
//            name = listOfFiles.get(file);
//            zipThisFolder(name);
//        }
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import java.io.File;
import javax.swing.*;

public class Main {
    private static long totalSizeOfFolders;
    private static long startTimeOfProgramm;
    private static long timeSpentOfProgram;
    private static File dir = new File("F:\\test\\Новая папка (3)");

    public static void main(String[] args) {
        startTimeOfProgramm = System.currentTimeMillis();
        totalSizeOfFolders = getDirSize(dir) / 1024 / 1024;
        ZipFolders zip = new ZipFolders();
        ProgressBar progressBar = new ProgressBar();
        zip.start();
        progressBar.start();
        timeSpentOfProgram = (System.currentTimeMillis() - startTimeOfProgramm) / 60000;
    }

    public static long getDirSize(File dir) {
        long size = 0;
        if (dir.isFile()) {
            size = dir.length();
        } else {
            File[] subFiles = dir.listFiles();
            for (File file : subFiles) {
                if (file.isFile()) {
                    size += file.length();
                } else {
                    size += getDirSize(file);
                }
            }
        }
        return size;
    }

    private static class ZipFolders extends Thread {
        @Override
        public void run() {
            String name = "";
            File[] subFiles = dir.listFiles();
            for (File file : subFiles) {
                name = file.getName();
                zipThisFolder(name);
            }
        }
    }

    public static void zipThisFolder(String name) {
        try {
            ZipFile zipFile = new ZipFile("F:\\test\\Новая папка (3)\\" + name + ".zip");
            String folderToZip = "F:\\test\\Новая папка (3)\\" + name;
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_FASTEST);
            zipFile.addFolder(folderToZip, parameters);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    private static class ProgressBar extends Thread {
            @Override
            public void run() {
                Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension frameSize = new Dimension(290, 140);
                final JFrame myFrame = new JFrame("Архивирует...");
                myFrame.setBounds(ss.width / 2 - frameSize.width / 2,
                        ss.height / 2 - frameSize.height / 2,
                        frameSize.width, frameSize.height);
                myFrame.setDefaultCloseOperation(3);
                myFrame.setVisible(true);
                myFrame.setResizable(false);
                JPanel panel = new JPanel();
                JProgressBar progressBar = new JProgressBar();
                progressBar.setStringPainted(true);
                progressBar.setMinimum(0);
                progressBar.setMaximum((int) totalSizeOfFolders);
                if (progressBar.getValue() < totalSizeOfFolders){
                    int cashSize = ((int) getDirSize(dir)/1024/1024 - (int)totalSizeOfFolders)/(int)totalSizeOfFolders;
                    progressBar.setValue(cashSize);
                }
                if (progressBar.getValue() == totalSizeOfFolders){
                    myFrame.setVisible(false);
                    showFinishWindow(timeSpentOfProgram);
                }
                panel.add(progressBar);
                myFrame.add(panel);
            }
    }

    private static void showFinishWindow(long timeSpent) {
        //размер экрана и размер окна
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(290, 140);

        final JFrame myFrame = new JFrame("Архиватор");
        myFrame.setBounds(ss.width / 2 - frameSize.width / 2,
                ss.height / 2 - frameSize.height / 2,
                frameSize.width, frameSize.height);
        myFrame.setAlwaysOnTop(true);
        myFrame.setDefaultCloseOperation(3);
        myFrame.setVisible(true);
        myFrame.setResizable(false);
        JPanel panel = new JPanel();

        //Установка панели на фрейме и центральной надписи о времени архивирования
        JLabel centerLabel = new JLabel("Программа выполнялась за " + timeSpent + " минут");
        Dimension centerLabelSize = new Dimension(280, 50);
        centerLabel.setBounds(frameSize.width / 2 - centerLabelSize.width / 2,
                frameSize.height / 2 - centerLabelSize.height / 2,
                centerLabelSize.width, centerLabelSize.height);
        Font font = new Font("Century Gothic", Font.BOLD, 14);
        centerLabel.setFont(font);


        //Установка кнопки для закрытия панели
        JButton okButton = new JButton("OK");
        okButton.setVisible(true);
        Dimension okButtonSize = new Dimension(220, 50);
        okButton.setPreferredSize(okButtonSize);
        centerLabel.setBounds(frameSize.width / 2 - centerLabelSize.width / 2,
                0,
                okButtonSize.width, okButtonSize.height);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                myFrame.dispose();
            }
        });
        panel.add(centerLabel);
        panel.add(okButton);
        myFrame.add(panel);
    }
}
