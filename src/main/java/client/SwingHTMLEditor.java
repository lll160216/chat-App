/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

/**
 *
 * @author ASUS
 */
import java.awt.event.*;  
import javax.swing.*;  
import java.awt.*;  
  
public class SwingHTMLEditor extends JPanel  
implements ActionListener {  
    JTextArea txtArea;  
    JLabel labl;  
    public SwingHTMLEditor() {  
        String htmlText = "<html>\n" +  
            "Create table with using different font styles and colors:\n" +  
            "<h1>C-#corner.com</h1>\n" +  
            "<table border=3 margin=3>\n" +  
            "<tr>\n" +  
            "<td><font color=red>1</font></td>\n" +  
            "<td><font color=blue>2</font></td>\n" +  
            "<td><font color=green>3</font></td>\n" +  
            "</tr>\n" +  
            "<tr>\n" +  
            "<td><font size=-2>4</font></td>\n" +  
            "<td><font size=+2>5</font></td>\n" +  
            "<td><i>6</i></td>\n" +  
            "</tr>\n" +  
            "<tr>\n" +  
            "<td><b>7</b></td>\n" +  
            "<td>8</td>\n" +  
            "<td>9</td>\n" +  
            "</tr>\n" +  
            "</table>";  
        txtArea = new JTextArea(20, 22);  
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));  
        txtArea.setText(htmlText);  
        JScrollPane scrllPn = new JScrollPane(txtArea);  
        JButton lablChngOtpt = new JButton("Click to change");  
        lablChngOtpt.setMnemonic(KeyEvent.VK_C);  
        lablChngOtpt.setAlignmentX(Component.CENTER_ALIGNMENT);  
        lablChngOtpt.addActionListener(this);  
        labl = new JLabel(htmlText) {  
            public Dimension getMaximumSize() {  
                return new Dimension(250, 250);  
            }  
  
            public Dimension getPreferredSize() {  
                return new Dimension(250, 250);  
            }  
  
            public Dimension getMinimumSize() {  
                return new Dimension(250, 250);  
            }  
        };  
  
        labl.setHorizontalAlignment(SwingConstants.CENTER);  
        labl.setVerticalAlignment(SwingConstants.CENTER);  
        JPanel panlLeft = new JPanel();  
        panlLeft.setLayout(new BoxLayout(panlLeft, BoxLayout.PAGE_AXIS));  
        panlLeft.setBorder(BorderFactory.createCompoundBorder(  
            BorderFactory.createTitledBorder(  
                "Write your own HTML and click the button to see the changes"),  
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));  
        panlLeft.add(scrllPn);  
        panlLeft.add(Box.createRigidArea(new Dimension(0, 11)));  
        panlLeft.add(lablChngOtpt);  
        JPanel panlRght = new JPanel();  
        panlRght.setLayout(new BoxLayout(panlRght, BoxLayout.PAGE_AXIS));  
        panlRght.setBorder(BorderFactory.createCompoundBorder(  
            BorderFactory.createTitledBorder("Swing label with HTML"),  
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));  
        panlRght.add(labl);  
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  
        add(panlLeft);  
        add(Box.createRigidArea(new Dimension(11, 0)));  
        add(panlRght);  
    }  
    //Perform changes when user click on the button specified.  
  
    public void actionPerformed(ActionEvent e) {  
        labl.setText(txtArea.getText());  
    }  
  
    private static void showGUI() {  
        //Create and set up the window.  
        JFrame frm = new JFrame("SwingHTMLEditor");  
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        //Add content to the window.  
        frm.add(new SwingHTMLEditor());  
        //Display the window.  
        frm.pack();  
        frm.setVisible(true);  
    }  
  
    public static void main(String[] args) {  
        SwingUtilities.invokeLater(new Runnable() {  
            public void run() {  
                UIManager.put("swing.boldMetal", Boolean.FALSE);  
                showGUI();  
            }  
        });  
    }  
}  