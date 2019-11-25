package GUI;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;

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