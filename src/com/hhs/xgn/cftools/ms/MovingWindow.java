package com.hhs.xgn.cftools.ms;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.*;

import com.hhs.xgn.cftools.common.FullSubmission;

public class MovingWindow extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6399475809087433073L;

	int id;
	int subId;

	int bci;
	String bcf;

	MovingWindow self = this;

	JLabel user, pid, sta, tc;

	String handle;

	public void setId(int nid) {
		id = nid;
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(this.getGraphicsConfiguration());
		int x = (int) (dim.getWidth() - 300 - 3);
		int y = (int) (dim.getHeight() - screenInsets.bottom - 30 * id - 3);
		this.setBounds(x, y, 300, 30);
	}

	public MovingWindow(int submission, int num, String handle) {

		System.out.println("MovingWindow:" + submission);

		subId = submission;

		this.handle = handle;

		this.setTitle("Moving Window");
		this.setLayout(new GridLayout(1, 4));
		this.setAlwaysOnTop(true);
		getRootPane().setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));

		// JButton close=new JButton("close");
		// close.addActionListener(new ActionListener() {
		//
		// @Override
		// public void actionPerformed(ActionEvent arg0) {
		// self.dispose();
		// MovingStatus.del(id);
		// }
		// });
		// this.add(close);

		user = new JLabel("gwq2017");

		user.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!Desktop.isDesktopSupported()) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
					return;
				}
				Desktop d = Desktop.getDesktop();
				try {
					d.browse(new URI("https://codeforces.com/profile/" + user.getText()));
				} catch (Exception ev) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
				}
			}
		});

		pid = new JLabel("1A");

		pid.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!Desktop.isDesktopSupported()) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
					return;
				}
				Desktop d = Desktop.getDesktop();
				try {
					d.browse(new URI("https://codeforces.com/contest/" + bci + "/problem/" + bcf));
				} catch (Exception ev) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
				}
			}
		});
		sta = new JLabel("WJ");

		sta.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!Desktop.isDesktopSupported()) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
					return;
				}
				Desktop d = Desktop.getDesktop();
				try {
					d.browse(new URI("https://codeforces.com/contest/" + bci + "/submission/" + subId));
				} catch (Exception ev) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
				}
			}
		});

		tc = new JLabel("T1");
		tc.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!Desktop.isDesktopSupported()) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
					return;
				}
				Desktop d = Desktop.getDesktop();
				try {
					d.browse(new URI("https://codeforces.com/contest/" + bci + "/submission/" + subId));
				} catch (Exception ev) {
					JOptionPane.showMessageDialog(null, "Opening a tab is not allowed in your system");
				}
			}
		});

		user.setFont(new Font("Consolas", Font.BOLD, 15));
		pid.setFont(new Font("Consolas", Font.PLAIN, 15));
		sta.setFont(new Font("Consolas", Font.PLAIN, 15));
		tc.setFont(new Font("Consolas", Font.ITALIC, 15));

		this.add(user);
		add(pid);
		add(sta);
		add(tc);

		setId(num);
		this.setUndecorated(true);
		this.setVisible(true);

		Thread t = new Thread() {
			public void run() {
				while (true) {
					update();
					if (sta.getText().equals("WJ") == false && sta.getText().equals("??") == false) {
						break;
					}

					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				dispose();
				MovingStatus.del(id);
			}
		};

		t.start();
	}

	void update() {
		FullSubmission f = MovingStatus.fs.get(subId);
		System.out.println("Getting " + subId + " from " + f);
		if (f == null) {
			user.setText("??");
			pid.setText("??");
			tc.setText("??");
			sta.setText("OOS");
			sta.setToolTipText("Out of sync");
			return;
		}
		user.setText(this.handle);
		pid.setText(f.problem.contestId + f.problem.index);

		bci = f.problem.contestId;
		bcf = f.problem.index;

		tc.setText("T" + (f.passedTestCount + 1));
		StatusCheck(f.verdict);
	}

	void StatusCheck(String v) {
		if (v.equals("OK")) {
			sta.setText("AC");
			sta.setToolTipText("Accepted");
			sta.setForeground(Color.GREEN);
			return;
		}
		if (v.equals("COMPILATION_ERROR")) {
			sta.setText("CE");
			sta.setToolTipText("Compilation Error");
			sta.setForeground(Color.gray);
			return;
		}
		if (v.equals("RUNTIME_ERROR")) {
			sta.setText("RE");
			sta.setToolTipText("Runtime Error");
			sta.setForeground(Color.cyan);
			return;
		}
		if (v.equals("WRONG_ANSWER")) {
			sta.setText("WA");
			sta.setToolTipText("Wrong Answer");
			sta.setForeground(Color.red);
			return;
		}
		if (v.equals("TIME_LIMIT_EXCEEDED")) {
			sta.setText("TLE");
			sta.setToolTipText("Time Limit Exceeded");
			sta.setForeground(Color.blue);
			return;
		}
		if (v.equals("MEMORY_LIMIT_EXCEEDED")) {
			sta.setText("MLE");
			sta.setToolTipText("Memory Limit Exceeded");
			sta.setForeground(Color.orange);
			return;
		}
		if (v.equals("IDLENESS_LIMIT_EXCEEDED")) {
			sta.setText("ILE");
			sta.setToolTipText("Idleness Limit Exceeded");
			sta.setForeground(Color.magenta);
			return;
		}
		if (v.equals("TESTING")) {
			sta.setText("WJ");
			sta.setToolTipText("In queue/Testing");
			sta.setForeground(Color.black);
			return;
		}

		sta.setText("Other");
		sta.setToolTipText(v);

	}
}
