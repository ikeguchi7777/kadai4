import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SpringLayout;
import javax.swing.border.EtchedBorder;

public class PageFrameBase extends FrameBase {
	JTabbedPane tabbedpane;

	public PageFrameBase(String title, int x, int y, int width, int height, PagePanelBase[] pages) {
		super(title, x, y, width, height);
		MakePages(pages);
	}

	public PageFrameBase(String title, int width, int height, PagePanelBase[] pages) {
		super(title, width, height);
		MakePages(pages);
	}

	private void MakePages(PagePanelBase[] pages) {
		JPanel p = new JPanel();

		tabbedpane = new JTabbedPane();
		for (PagePanelBase Panel : pages) {
			tabbedpane.add(Panel, Panel.getName());
		}
		//tabbedpane.setSize(1600, 100);

		LogArea logArea = new LogArea(30, 140);
		SpringLayout layout = new SpringLayout();
		p.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, tabbedpane, 5, SpringLayout.NORTH, p);
		layout.putConstraint(SpringLayout.SOUTH, tabbedpane, 305, SpringLayout.NORTH, p);
		layout.putConstraint(SpringLayout.EAST, tabbedpane, -30, SpringLayout.EAST, p);
		layout.putConstraint(SpringLayout.WEST, tabbedpane, 30, SpringLayout.WEST, p);

		layout.putConstraint(SpringLayout.NORTH, logArea, 10, SpringLayout.SOUTH, tabbedpane);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, logArea, 0, SpringLayout.HORIZONTAL_CENTER, p);

		p.add(tabbedpane);
		p.add(logArea);
		add(p, BorderLayout.CENTER);
	}

}

abstract class PagePanelBase extends JPanel {
	private String name;

	public PagePanelBase(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}

class LogArea extends JScrollPane {
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

	public static void addLogLine(String txt) {
		if (instance != null)
			instance.area.append(txt + "\n");
		else
			System.out.println(txt);
	}

	public static void addLog(String txt) {
		if (instance != null)
			instance.area.append(txt);
		else
			System.out.print(txt);
	}
}