package GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;


class ForwardSearchPanel extends PagePanelBase {

	public ForwardSearchPanel(String name) {
		super(name);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("英文で質問してください");
		JTextField text1 = new JTextField(100);
		JButton button = new JButton("検索");
		panel.add(text1);
		panel.add(button);
		add(label, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		ActionListener searchActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Search(text1.getText());
				text1.setText("");
			}
		};
		text1.addActionListener(searchActionListener);
		button.addActionListener(searchActionListener);
	}

	private void Search(String text) {
		LogArea.println("ここで検索："+text);
	}

}

class BackwardSearchPanel extends PagePanelBase {

	public BackwardSearchPanel(String name) {
		super(name);
		JPanel panel = new JPanel();
		JLabel label = new JLabel("英文で質問してください");
		JTextField text1 = new JTextField(100);
		JButton button = new JButton("検索");
		panel.add(text1);
		panel.add(button);
		add(label, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		ActionListener searchActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Search(text1.getText());
				text1.setText("");
			}
		};
		text1.addActionListener(searchActionListener);
		button.addActionListener(searchActionListener);
	}

	private void Search(String text) {
		LogArea.println("ここで検索："+text);
	}

}

class AddPanel extends PagePanelBase{
	public AddPanel(String name) {
		super(name);
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel p1 = new JPanel();
		JTextField dataadress = new JTextField("Dataファイルのアドレスを入力してください。", 50);
		JLabel label1=new JLabel("Data:");
		JButton button1 = new JButton("参照");
		JButton button2 = new JButton("追加");
		p1.add(label1);
		p1.add(dataadress);
		p1.add(button1);
		p1.add(button2);
		JPanel p2 = new JPanel();
		JLabel label2=new JLabel("Wm  :");
		JTextField Wmadress = new JTextField("Wmファイルのアドレスを入力してください。", 50);
		JButton button3 = new JButton("参照");
		JButton button4 = new JButton("追加");
		p2.add(label2);
		p2.add(Wmadress);
		p2.add(button3);
		p2.add(button4);
		add(p1);
		add(p2);

		JPanel panel = new JPanel();
		JLabel label = new JLabel("ifを追加してください");
		JTextField text1 = new JTextField(100);
		JButton button = new JButton("OK");
		panel.add(text1);
		panel.add(button);
		add(label, BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
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
		ActionListener TextActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text1.setText("");
			}
		};
		button1.addActionListener(listener1);
		button3.addActionListener(listener2);
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
