package com.hhs.xgn.cftools.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JDialog;

/**
 * @author Administrator 此工具类用法：实例化出对象，调用 void show("标题","内容") 方法. InfoUtil tool
 *         = new InfoUtil(); tool.show("标题","内容")
 */
public class InfoUtil {
    private Point oldP; // 上一次坐标,拖动窗口时用
    private TipWindow tw = null; // 提示框
    private ImageIcon img = null; // 图像组件
    private JLabel imgLabel = null; // 背景图片标签
    private JPanel headPan = null;
    private JPanel feaPan = null;
    private JPanel btnPan = null;
    private JLabel title = null; // 栏目名称
    private JLabel head = null; // 蓝色标题
    private JLabel close = null; // 关闭按钮
    private JTextArea feature = null; // 内容
    private JScrollPane jfeaPan = null;
    private JButton sure = null;
    private String titleT = null;
    private String word = null;
    public String website="";
    // private SimpleDateFormat sdf = new
    // SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void init() {
        // 新建300x220的消息提示框
        tw = new TipWindow(300, 180);
        img = new ImageIcon("assets/bg.jpg");//最好写相对路径
        img=new ImageIcon(img.getImage().getScaledInstance(300, 180, Image.SCALE_DEFAULT));
        
        imgLabel = new JLabel(img);
        // 设置各个面板的布局以及面板中控件的边界
        headPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        feaPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        btnPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        title = new JLabel("Welcome");
        head = new JLabel(titleT);
        close = new JLabel("X");
        feature = new JTextArea(word);
        jfeaPan = new JScrollPane(feature);
        // releaseLabel = new JLabel("发布于  " + time);
        sure = new JButton("View Solution");
        sure.setHorizontalAlignment(SwingConstants.CENTER);
//        sure.setOpaque(true);
//        sure.setBackground(Color.ORANGE);
        // 将各个面板设置为透明，否则看不到背景图片
        ((JPanel) tw.getContentPane()).setOpaque(false);
        headPan.setOpaque(false);
        feaPan.setOpaque(false);
        btnPan.setOpaque(false);

        // 设置JDialog的整个背景图片
        tw.getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        headPan.setPreferredSize(new Dimension(300, 60));

        // 设置提示框的边框,宽度和颜色
        tw.getRootPane().setBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
        title.setPreferredSize(new Dimension(260, 26));
        title.setVerticalTextPosition(JLabel.CENTER);
        title.setHorizontalTextPosition(JLabel.CENTER);
        title.setFont(new Font("Consolas", Font.PLAIN, 12));
        title.setForeground(Color.black);

        close.setFont(new Font("Consolas", Font.BOLD, 15));
        close.setPreferredSize(new Dimension(20, 20));
        close.setVerticalTextPosition(JLabel.CENTER);
        close.setHorizontalTextPosition(JLabel.CENTER);
        close.setCursor(new Cursor(12));
        close.setToolTipText("Close");

        head.setPreferredSize(new Dimension(250, 35));
        head.setVerticalTextPosition(JLabel.CENTER);
        head.setHorizontalTextPosition(JLabel.CENTER);
        head.setFont(new Font("Consolas", Font.PLAIN, 14));
        head.setForeground(Color.black);

        feature.setEditable(false);
        feature.setForeground(Color.BLACK);
        feature.setFont(new Font("Consolas", Font.PLAIN, 14));
        feature.setBackground(new Color(255, 255, 255));
        // 设置文本域自动换行
        feature.setLineWrap(true);

        jfeaPan.setPreferredSize(new Dimension(250, 80));
        jfeaPan.setBorder(null);
        jfeaPan.setBackground(Color.black);

        // releaseLabel.setForeground(Color.DARK_GRAY);
        // releaseLabel.setFont(new Font("宋体", Font.PLAIN, 12));

        // 为了隐藏文本域，加个空的JLabel将他挤到下面去
        JLabel jsp = new JLabel();
        jsp.setPreferredSize(new Dimension(300, 25));

        sure.setPreferredSize(new Dimension(60, 30));
        // 设置标签鼠标手形
        sure.setCursor(new Cursor(12));
        
        // headPan.add(title);
        headPan.add(head);
        headPan.add(close);

        feaPan.add(jsp);
        feaPan.add(jfeaPan);
        // feaPan.add(releaseLabel);

        btnPan.add(sure);

        tw.add(headPan, BorderLayout.NORTH);
        tw.add(feaPan, BorderLayout.CENTER);
        tw.add(btnPan, BorderLayout.SOUTH);
    }

    public void handle() {
        // 为更新按钮增加相应的事件
        sure.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                // JOptionPane.showMessageDialog(tw, "谢谢，再见");
            	if(website!="" && Desktop.isDesktopSupported()){
            		Desktop d=Desktop.getDesktop();
            		try {
						d.browse(URI.create(website));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
            	}
                tw.close();
            }

            public void mouseEntered(MouseEvent e) {
                sure.setBorder(BorderFactory.createLineBorder(Color.gray));
            }

            public void mouseExited(MouseEvent e) {
                sure.setBorder(null);
            }
        });
        
        // 增加鼠标拖动事件
        title.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
                Point newP = new Point(e.getXOnScreen(), e.getYOnScreen());
                int x = tw.getX() + (newP.x - oldP.x);
                int y = tw.getY() + (newP.y - oldP.y);
                tw.setLocation(x, y);
                oldP = newP;
            }
        });
        // 鼠标按下时初始坐标,供拖动时计算用
        title.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                oldP = new Point(e.getXOnScreen(), e.getYOnScreen());
            }
        });
        // 右上角关闭按钮事件
        close.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                tw.close();
            }

            public void mouseEntered(MouseEvent e) {
                close.setBorder(BorderFactory.createLineBorder(Color.gray));
            }

            public void mouseExited(MouseEvent e) {
                close.setBorder(null);
            }
        });
    }

    public void show(String titleT,String word){
    	this.titleT = titleT;
        this.word = word;
        this.website="";
        
        // time = sdf.format(new Date());
        init();
        handle();
        tw.setAlwaysOnTop(true);
        tw.setUndecorated(true);
        tw.setResizable(false);
        sure.setText("OK");
        tw.setVisible(true);
        tw.run();
    }
    public void show(String titleT, String word,String website) {
        this.titleT = titleT;
        this.word = word;
        this.website=website;
        
        // time = sdf.format(new Date());
        init();
        handle();
        tw.setAlwaysOnTop(true);
        tw.setUndecorated(true);
        tw.setResizable(false);
        sure.setText("View");
        tw.setVisible(true);
        tw.run();
    }
    
    public void close() {
        tw.close();
    }
}

class TipWindow extends JDialog {
    private static final long serialVersionUID = 8541659783234673950L;
    private static Dimension dim;
    private int x, y;
    private int width, height;
    private static Insets screenInsets;

    public TipWindow(int width, int height) {
        this.width = width;
        this.height = height;
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(
                this.getGraphicsConfiguration());
        x = (int) (dim.getWidth() - width - 3);
        y = (int) (dim.getHeight() - screenInsets.bottom - 3);
        initComponents();
    }

    public void run() {
        for (int i = 0; i <= height; i += 10) {
            try {
                this.setLocation(x, y - i);
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
        // 此处代码用来实现让消息提示框5秒后自动消失
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        close();
    }

    private void initComponents() {
        this.setSize(width, height);
        this.setLocation(x, y);
        this.setBackground(Color.black);
    }

    public void close() {
        x = this.getX();
        y = this.getY();
        int ybottom = (int) dim.getHeight() - screenInsets.bottom;
        for (int i = 0; i <= ybottom - y; i += 10) {
            try {
                setLocation(x, y + i);
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
        }
        dispose();
    }

}