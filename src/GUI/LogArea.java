package GUI;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.border.EtchedBorder;

public class LogArea extends JScrollPane {
	static LogArea instance;
	JTextArea area;
	JViewport viewport;

	public LogArea(int rows, int columns) {
		super();
		area = new JTextArea(rows, columns);
		EtchedBorder border = new EtchedBorder(EtchedBorder.RAISED);
		area.setEditable(false);
		area.setBorder(border);
		setViewportView(area);
		viewport = getViewport();
		instance = this;
	}

	public static void println(String txt) {
		if (instance != null)
			instance.area.append(txt + "\n");
		else
			System.out.println(txt);
	}

	public static void print(String txt) {
		if (instance != null)
			instance.area.append(txt);
		else
			System.out.print(txt);
	}
}