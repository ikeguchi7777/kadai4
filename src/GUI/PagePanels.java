package GUI;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class SearchPanel extends PagePanelBase {

	public SearchPanel(String name) {
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

