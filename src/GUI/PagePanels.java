package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileFilter;

import kadai4.FileManager;
import kadai4.RuleBase;

class ForwardSearchPanel extends PagePanelBase {

	public ForwardSearchPanel(String name, kadai4.ForwardChain.RuleBaseSystem rbs) {
		super(name);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("クエリで質問してください");
		JTextField text1 = new JTextField(100);
		JButton button = new JButton("検索");
		panel.add(text1);
		panel.add(button);
		add(label, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		ActionListener searchActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rbs.patternSearch(text1.getText());
				text1.setText("");
			}
		};
		text1.addActionListener(searchActionListener);
		button.addActionListener(searchActionListener);
	}

}

class BackwardSearchPanel extends PagePanelBase {

	public BackwardSearchPanel(String name, kadai4.BackwordChain.RuleBaseSystem rbs) {
		super(name);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("クエリで質問してください");
		JTextField text1 = new JTextField(100);
		JButton button = new JButton("検索");
		panel.add(text1);
		panel.add(button);
		add(label, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		ActionListener searchActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rbs.patternSearch(text1.getText());
				text1.setText("");
			}
		};
		text1.addActionListener(searchActionListener);
		button.addActionListener(searchActionListener);
	}

}

class AddPanel extends PagePanelBase {
	public AddPanel(String name, RuleBase rb) {
		super(name);
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		JPanel p1 = new JPanel();
		JTextField dataadress = new JTextField("Dataファイルのアドレスを入力してください。", 50);
		JLabel label1 = new JLabel("Data:");
		JButton button1 = new JButton("参照");
		JButton button2 = new JButton("追加");
		p1.add(label1);
		p1.add(dataadress);
		p1.add(button1);
		p1.add(button2);
		JPanel p2 = new JPanel();
		JLabel label2 = new JLabel("Wm  :");
		JTextField Wmadress = new JTextField("Wmファイルのアドレスを入力してください。", 50);
		JButton button3 = new JButton("参照");
		JButton button4 = new JButton("追加");
		p2.add(label2);
		p2.add(Wmadress);
		p2.add(button3);
		p2.add(button4);
		layout.putConstraint(SpringLayout.NORTH, p1, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, p1, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, p2, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, p2, -10, SpringLayout.EAST, this);
		add(p1);
		add(p2);
		ActionListener listener1 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = System.getProperty("user.dir");
				JFileChooser filechooser = new JFileChooser(path);
				filechooser.setFileFilter(new DataFilter());
				int selected = filechooser.showOpenDialog(p1);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					dataadress.setText(file.getAbsolutePath());
				}
			}
		};
		ActionListener listener2 = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = System.getProperty("user.dir");
				JFileChooser filechooser = new JFileChooser(path);
				filechooser.setFileFilter(new DataFilter());
				int selected = filechooser.showOpenDialog(p2);
				if (selected == JFileChooser.APPROVE_OPTION) {
					File file = filechooser.getSelectedFile();
					Wmadress.setText(file.getAbsolutePath());
				}
			}
		};
		ActionListener AddDataActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rb.addRules(FileManager.loadRules(dataadress.getText()));
			}
		};
		ActionListener AddWmActionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rb.addWorkingMemory(FileManager.loadWm(Wmadress.getText()));
			}
		};
		button1.addActionListener(listener1);
		button2.addActionListener(AddDataActionListener);
		button3.addActionListener(listener2);
		button4.addActionListener(AddWmActionListener);

		JPanel panel = new JPanel();
		JPanel pp1 = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		JLabel label = new JLabel("Rule:");
		JTextField text1 = new JTextField(30);
		pp1.add(label);
		pp1.add(text1);
		JPanel pp2 = new JPanel();
		JLabel label3 = new JLabel("  if:");
		JTextArea textArea = new JTextArea(5, 30);
		pp2.add(label3);
		pp2.add(textArea);
		JPanel pp3 = new JPanel();
		JLabel label4 = new JLabel("then:");
		JTextField text2 = new JTextField(30);
		pp3.add(label4);
		pp3.add(text2);
		panel.add(pp1);
		panel.add(pp2);
		panel.add(pp3);
		JButton button5 = new JButton("追加");
		ActionListener addRuleListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String name = text1.getText();
				text1.setText("");
				String[] t = textArea.getText().split("\n");
				ArrayList<String> rules = new ArrayList<>();
				for (String string : t) {
					rules.add(string);
				}
				textArea.setText("");
				String consequent = text2.getText();
				text2.setText("");
				rb.addRules(name, rules, consequent);
			}
		};
		button5.addActionListener(addRuleListener);
		panel.add(button5);
		layout.putConstraint(SpringLayout.NORTH, panel, 5, SpringLayout.SOUTH, p1);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, panel, 0, SpringLayout.HORIZONTAL_CENTER, this);
		add(panel);
		JPanel p3 = new JPanel();
		JLabel label5 = new JLabel("アサーション:");
		JTextField Wmtxt = new JTextField("アサーションを入力してください。", 50);
		JButton button6 = new JButton("追加");
		ActionListener asActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String as = Wmtxt.getText();
				Wmtxt.setText("");
				rb.addWorkingMemory(as);
			}
		};
		button6.addActionListener(asActionListener);
		p3.add(label5);
		p3.add(Wmtxt);
		p3.add(button6);
		layout.putConstraint(SpringLayout.NORTH, p3, 5, SpringLayout.SOUTH, panel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, p3, 0, SpringLayout.HORIZONTAL_CENTER, this);
		add(p3);
	}
}

class RemovePanel extends PagePanelBase{
	public RemovePanel(String name, RuleBase rb) {
		super(name);
		JPanel p1 = new JPanel();
		JLabel label1 = new JLabel("Rule名:");
		JTextField text1 = new JTextField(30);
		JButton button1 = new JButton("削除");
		p1.add(label1);
		p1.add(text1);
		p1.add(button1);
		JPanel p2 = new JPanel();
		JLabel label2 = new JLabel("アサーション:");
		JTextField text2 = new JTextField(30);
		JButton button2 = new JButton("削除");
		p2.add(label2);
		p2.add(text2);
		p2.add(button2);
		ActionListener ruActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = text1.getText();
				text1.setText("");
				rb.deleteRules(name);
			}
		};
		ActionListener wmActionListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String as = text2.getText();
				text2.setText("");
				rb.deleteAssertion(as);
			}
		};
		text1.addActionListener(ruActionListener);
		button1.addActionListener(ruActionListener);
		text2.addActionListener(wmActionListener);
		button2.addActionListener(wmActionListener);
		add(p1);
		add(p2);
	}
}

/**
 * フィルター
 *
 *
 */
class DataFilter extends FileFilter {

	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String ext = getExtension(f);
		if (ext != null) {
			if (ext.equals("data")) {
				return true;
			} else {
				return false;
			}
		}

		return false;
	}

	public String getDescription() {
		return "DATAファイル";
	}

	/* 拡張子を取り出す */
	private String getExtension(File f) {
		String ext = null;
		String filename = f.getName();
		int dotIndex = filename.lastIndexOf('.');

		if ((dotIndex > 0) && (dotIndex < filename.length() - 1)) {
			ext = filename.substring(dotIndex + 1).toLowerCase();
		}

		return ext;
	}
}
