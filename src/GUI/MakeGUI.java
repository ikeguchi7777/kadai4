package GUI;
public class MakeGUI {

	public static void main(String[] args) {
		PagePanelBase[] pages= new PagePanelBase[2];
		pages[0] = new SearchPanel("検索");
		pages[1] = new SearchPanel("追加");
		PageFrameBase pageFrame = new PageFrameBase("課題４", 1600, 900, pages);
		pageFrame.setVisible(true);
	}

}
