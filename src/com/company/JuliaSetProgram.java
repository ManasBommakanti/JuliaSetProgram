package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JuliaSetProgram extends JPanel implements AdjustmentListener, ActionListener{
    JFrame frame;
    JFileChooser fileChooser;
    BufferedImage juliaImage;

    int red, green, blue, version;
    JPanel sliderPanel, labelPanel, bigPanel, resetPanel;
    JScrollBar redBar, greenBar, blueBar, aBar, bBar, zoomBar, multiplyBar;
    JTextField versionField;
    JButton resetButton, runAButton, runBButton, runZoomButton, runMultiButton, saveButton;
    JLabel redLabel, greenLabel, blueLabel, aLabel, bLabel, zoomLabel, versionLabel, multiplyLabel;

    RunnerA runnerA;
    RunnerB runnerB;
    RunnerZoom runnerZoom;
    RunnerMultiply runnerMultiply;

    boolean ranA, ranB, ranZoom, ranMulti;
    boolean stopA, stopB, stopZoom, stopMulti;

    boolean maxA, maxB, maxZoom, maxMulti;

    double a;
    double b;
    double multiplier;
    double zoom;

    public JuliaSetProgram(){
        frame = new JFrame("Julia Set Program");
        frame.add(this);
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String currDir = System.getProperty("user.dir");
        fileChooser = new JFileChooser(currDir);

        runnerA = new RunnerA();
        runnerB = new RunnerB();
        runnerZoom = new RunnerZoom();
        runnerMultiply = new RunnerMultiply();

        ranA = ranB = ranZoom = ranMulti = false;
        stopA = stopB = stopZoom = stopMulti = false;

        maxA = maxB = maxZoom = maxMulti = false;

        resetButton = new JButton("Reset Values");
        resetButton.addActionListener(this);

        runAButton = new JButton("Run A");
        runAButton.addActionListener(this);

        runBButton = new JButton("Run B");
        runBButton.addActionListener(this);

        saveButton = new JButton("Save Image");
        saveButton.addActionListener(this);

        runZoomButton = new JButton("Run Zoom");
        runZoomButton.addActionListener(this);

        runMultiButton = new JButton("Run Multiplier");
        runMultiButton.addActionListener(this);

        redBar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 0, 0, 255); //orientation, start value, width of scroller, min value, max value
        red = redBar.getValue();
        redBar.addAdjustmentListener(this);

        greenBar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 0, 0, 255); //orientation, start value, width of scroller, min value, max value
        green = greenBar.getValue();
        greenBar.addAdjustmentListener(this);

        blueBar = new JScrollBar(JScrollBar.HORIZONTAL, 20, 0, 0, 255); //orientation, start value, width of scroller, min value, max value
        blue = blueBar.getValue();
        blueBar.addAdjustmentListener(this);

        versionField = new JTextField("Version 1 set to default. Enter \'1\' or \'2\' in this field to switch versions.");
        version = 1;
        versionField.addActionListener(this);

        aBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000); //orientation, start value, width of scroller, min value, max value
        double aValue = (aBar.getValue() / 1000f);
        a = Math.round(aValue * 1000.0) / 1000.0;
        aBar.addAdjustmentListener(this);

        bBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -2000, 2000); //orientation, start value, width of scroller, min value, max value
        double bValue = (bBar.getValue() / 1000f);
        b = Math.round(bValue * 1000.0) / 1000.0;
        bBar.addAdjustmentListener(this);

        zoomBar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 1, 6000); //orientation, start value, width of scroller, min value, max value
        double zoomValue = (zoomBar.getValue() / 60f);
        zoom = Math.round(zoomValue * 1000.0) / 1000.0;
        zoomBar.addAdjustmentListener(this);

        multiplyBar = new JScrollBar(JScrollBar.HORIZONTAL, 2000, 0, 1, 6000); //orientation, start value, width of scroller, min value, max value
        double multiplyValue = (multiplyBar.getValue() / 1000f);
        multiplier = Math.round(multiplyValue * 1000.0) / 1000.0;
        multiplyBar.addAdjustmentListener(this);

        redLabel = new JLabel(" Red: " + red + "     ");
        greenLabel = new JLabel(" Green: " + green + "     ");
        blueLabel = new JLabel(" Blue: " + blue + "    ");
        zoomLabel = new JLabel(" Zoom: " + zoom + "    ");
        versionLabel = new JLabel(" Version: " + version + "\t\t");
        multiplyLabel = new JLabel(" Multiplier: " + multiplier + "    ");

        DecimalFormat f = new DecimalFormat("#0.000");
        aLabel = new JLabel(" A-Value: " + f.format(a) + "     ");
        bLabel = new JLabel(" B-Value: " + f.format(b) + "     ");

        GridLayout grid = new GridLayout(5, 1); //5 rows, 1 col

        resetPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resetPanel.add(runAButton);
        resetPanel.add(runBButton);
        resetPanel.add(runZoomButton);
        resetPanel.add(runMultiButton);

        labelPanel = new JPanel();
        labelPanel.setLayout(grid);

        sliderPanel = new JPanel();
        sliderPanel.setLayout(grid);

        labelPanel.add(aLabel);
        sliderPanel.add(aBar);

        labelPanel.add(bLabel);
        sliderPanel.add(bBar);

        labelPanel.add(versionLabel);
        sliderPanel.add(versionField);
        /*
        labelPanel.add(redLabel);
        sliderPanel.add(redBar);

        labelPanel.add(greenLabel);
        sliderPanel.add(greenBar);

        labelPanel.add(blueLabel);
        sliderPanel.add(blueBar);*/

        labelPanel.add(zoomLabel);
        sliderPanel.add(zoomBar);

        labelPanel.add(multiplyLabel);
        sliderPanel.add(multiplyBar);

        bigPanel = new JPanel(new BorderLayout());
        bigPanel.add(labelPanel, BorderLayout.WEST);
        bigPanel.add(sliderPanel, BorderLayout.CENTER);
        bigPanel.add(resetPanel, BorderLayout.EAST);
        bigPanel.add(resetButton, BorderLayout.PAGE_START);

        frame.add(bigPanel, BorderLayout.SOUTH);
        frame.add(saveButton, BorderLayout.EAST);

        frame.setVisible(true);
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(new Color(red, green, blue));
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        juliaImage = drawJulia(g);
    }
    public static void main(String[] args) {
	    JuliaSetProgram app = new JuliaSetProgram();
    }
    public void saveImage()
    {
        if(juliaImage!=null)   //juliaImage is globally declared BufferedImage
        {
            FileFilter filter = new FileNameExtensionFilter("*.png","png");
            fileChooser.setFileFilter(filter);
            if(fileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
            {
                File file=fileChooser.getSelectedFile();
                try
                {
                    String st=file.getAbsolutePath();
                    if(st.contains(".png"))
                        st=st.substring(0,st.length()-4);
                    ImageIO.write(juliaImage, "png", new File(st+".png"));
                }catch(Exception e)
                {

                }
            }
        }
    }
    public BufferedImage drawJulia(Graphics g){
        BufferedImage image = new BufferedImage(frame.getWidth() - saveButton.getWidth(), frame.getHeight() - bigPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        int width = frame.getWidth() - saveButton.getWidth();
        int height = frame.getHeight() - bigPanel.getHeight();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                float itr = 300f;
                double zx = 2 * ((i - 0.5 * width) / (0.5 * zoom * width));
                double zy = (j - 0.5 * height) / (0.5 * zoom * height);
                double val = 0;

                while(zx * zx + zy * zy < 6 && itr > 0){
                    val = zx * zx - zy * zy + a;
                    zy = multiplier * zx * zy + b;
                    zx = val;
                    itr--;
                }
                float c;
                if(version == 1) {
                    if (itr > 0)
                        c = Color.HSBtoRGB((300f / itr) % 1, 1, 1);
                    else c = Color.HSBtoRGB(300f / itr, 1, 0);
                }
                else {
                    if (val > 0)
                        c = Color.HSBtoRGB((float)(300f / val) % 1, 1, 1);
                    else c = Color.HSBtoRGB((float) (300f / val), 1, 0);
                }

                image.setRGB(i, j, (int) c);
            }
        }
        g.drawImage(image, 0, 0, null);
        return image;
        //frame width * frame height = 1000 * 600 = 600000; 600000 * 300 = 180,000,000
    }
    @Override
    public void adjustmentValueChanged(AdjustmentEvent event) {
        /*if(event.getSource() == redBar) {
            red = redBar.getValue();
            redLabel.setText(" Red: " + red + "\t\t");
        }
        else if(event.getSource() == greenBar) {
            green = greenBar.getValue();
            greenLabel.setText(" Green: " + green + "\t\t");
        }
        else if(event.getSource() == blueBar) {
            blue = blueBar.getValue();
            blueLabel.setText(" Blue: " + blue + "\t\t");
        }*/
        if(event.getSource() == aBar) {
            double aValue = (aBar.getValue() / 1000f);
            a = Math.round(aValue * 1000.0) / 1000.0;
            DecimalFormat f = new DecimalFormat("#0.000");
            aLabel.setText(" A-Value: " + f.format(a) + "     ");
        }
        else if(event.getSource() == bBar) {
            double bValue = (bBar.getValue() / 1000f);
            b = Math.round(bValue * 1000.0) / 1000.0;
            DecimalFormat f = new DecimalFormat("#0.000");
            bLabel.setText(" B-Value: " + f.format(b) + "     ");
        }
        else if(event.getSource() == zoomBar){
            double zoomValue = (zoomBar.getValue() / 60f);
            zoom = Math.round(zoomValue * 1000.0) / 1000.0;
            DecimalFormat f = new DecimalFormat("#0.000");
            zoomLabel.setText(" Zoom: " + f.format(zoom) + "     ");
        }
        else if(event.getSource() == multiplyBar){
            double multiplyValue = (multiplyBar.getValue() / 1000f);
            multiplier = Math.round(multiplyValue * 1000.0) / 1000.0;
            DecimalFormat f = new DecimalFormat("#0.000");
            multiplyLabel.setText(" Multiplier: " + f.format(multiplier) + "     ");
        }
        versionField.selectAll();
        repaint();
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        if(event.getSource() == versionField) {
            try {
                int original = version;
                version = Integer.parseInt(versionField.getText());
                if(version != 1 && version != 2) {
                    version = original;
                    versionField.setText("Not a valid input. Either write \'1\' or \'2\' in this field.");
                    versionField.selectAll();
                }
                versionLabel.setText(" Version: " + version + "\t\t");
            }catch (Exception e){
                versionField.setText("Not a valid input. Either write \'1\' or \'2\' in this field.");
                versionField.selectAll();
            }
        }
        else if(event.getSource() == saveButton){
            saveImage();
        }
        else if(event.getSource() == resetButton){
            redBar.setValue(20);
            greenBar.setValue(20);
            blueBar.setValue(20);
            zoomBar.setValue(52);
            aBar.setValue(0);
            bBar.setValue(0);
            multiplyBar.setValue(2000);
            versionField.setText("1");
            versionField.postActionEvent();
            versionField.selectAll();
        }
        else if(event.getSource() == runAButton){
            if(!ranA && !stopA) {
                runAButton.setText("Stop A");
                ranA = true;
                runnerA.start();
            }
            else if(ranA && !stopA){
                runAButton.setText("Run A");
                stopA = true;
                runnerA.terminate();
            }
            else if(ranA){
                runAButton.setText("Stop A");
                stopA = false;
                runnerA = null;
                runnerA = new RunnerA();
                runnerA.start();
            }
        }
        else if(event.getSource() == runBButton) {
            if (!ranB && !stopB) {
                runBButton.setText("Stop B");
                ranB = true;
                runnerB.start();
            } else if (ranB && !stopB) {
                runBButton.setText("Run B");
                stopB = true;
                runnerB.terminate();
            } else if (ranB) {
                runBButton.setText("Stop B");
                stopB = false;
                runnerB = null;
                runnerB = new RunnerB();
                runnerB.start();
            }
        }
        else if(event.getSource() == runZoomButton) {
            if (!ranZoom && !stopZoom) {
                runZoomButton.setText("Stop Zoom");
                ranZoom = true;
                runnerZoom.start();
            } else if (ranZoom && !stopZoom) {
                runZoomButton.setText("Run Zoom");
                stopZoom = true;
                runnerZoom.terminate();
            } else if (ranZoom) {
                runZoomButton.setText("Stop Zoom");
                stopZoom = false;
                runnerZoom = null;
                runnerZoom = new RunnerZoom();
                runnerZoom.start();
            }
        }
        else if(event.getSource() == runMultiButton) {
            if (!ranMulti && !stopMulti) {
                runMultiButton.setText("Stop Multiplier");
                ranMulti = true;
                runnerMultiply.start();
            } else if (ranMulti && !stopMulti) {
                runMultiButton.setText("Run Multiplier");
                stopMulti = true;
                runnerMultiply.terminate();
            } else if (ranMulti) {
                runMultiButton.setText("Stop Multiplier");
                stopMulti = false;
                runnerMultiply = null;
                runnerMultiply = new RunnerMultiply();
                runnerMultiply.start();
            }
        }

        versionField.selectAll();
        repaint();
    }
    public class RunnerA extends Thread {
        private final AtomicBoolean running;

        public RunnerA(){
            running = new AtomicBoolean(true);
        }
        public void terminate(){
            running.set(false);
            System.out.println("Terminated A...");
        }
        @Override
        public void run() {
            System.out.println("Starting A... " + running);
            while (running.get()) {
                int num = aBar.getValue();
                if (num == aBar.getMaximum() && !maxA) {
                    maxA = true;
                    aBar.setValue(aBar.getValue() - 20);
                } else if (num == aBar.getMinimum() && maxA) {
                    maxA = false;
                    aBar.setValue(aBar.getValue() + 20);
                } else if (!maxA)
                    aBar.setValue(aBar.getValue() + 20);
                else
                    aBar.setValue(aBar.getValue() - 20);
                try {
                    Thread.sleep(50);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public class RunnerB extends Thread {
        private final AtomicBoolean running;

        public RunnerB(){
            running = new AtomicBoolean(true);
        }
        public void terminate(){
            running.set(false);
            System.out.println("Terminated A...");
        }
        @Override
        public void run() {
            while (running.get()) {
                int num = bBar.getValue();
                if (num == bBar.getMaximum() && !maxB) {
                    maxB = true;
                    bBar.setValue(bBar.getValue() - 20);
                } else if (num == bBar.getMinimum() && maxB) {
                    maxB = false;
                    bBar.setValue(bBar.getValue() + 20);
                } else if (!maxB)
                    bBar.setValue(bBar.getValue() + 20);
                else
                    bBar.setValue(bBar.getValue() - 20);
                try {
                    Thread.sleep(50);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public class RunnerZoom extends Thread {
        private final AtomicBoolean running;

        public RunnerZoom(){
            running = new AtomicBoolean(true);
        }
        public void terminate(){
            running.set(false);
        }
        @Override
        public void run() {
            while (running.get()) {
                int num = zoomBar.getValue();
                if (num == zoomBar.getMaximum() && !maxZoom) {
                    maxZoom = true;
                    zoomBar.setValue(zoomBar.getValue() - 2);
                } else if (num == zoomBar.getMinimum() && maxZoom) {
                    maxZoom = false;
                    zoomBar.setValue(zoomBar.getValue() + 2);
                } else if (!maxZoom)
                    zoomBar.setValue(zoomBar.getValue() + 2);
                else
                    zoomBar.setValue(zoomBar.getValue() - 2);
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public class RunnerMultiply extends Thread {
        private final AtomicBoolean running;

        public RunnerMultiply(){
            running = new AtomicBoolean(true);
        }
        public void terminate(){
            running.set(false);
        }
        @Override
        public void run() {
            while (running.get()) {
                int num = multiplyBar.getValue();
                if (num == multiplyBar.getMaximum() && !maxMulti) {
                    maxMulti = true;
                    multiplyBar.setValue(multiplyBar.getValue() - 2);
                } else if (num == multiplyBar.getMinimum() && maxMulti) {
                    maxMulti = false;
                    multiplyBar.setValue(multiplyBar.getValue() + 2);
                } else if (!maxMulti)
                    multiplyBar.setValue(multiplyBar.getValue() + 2);
                else
                    multiplyBar.setValue(multiplyBar.getValue() - 2);
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
