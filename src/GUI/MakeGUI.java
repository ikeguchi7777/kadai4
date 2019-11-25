package GUI;


public class MakeGUI {

	public static void main(String[] args) {
		kadai4.BackwordChain.RuleBaseSystem bw = new kadai4.BackwordChain.RuleBaseSystem();
		kadai4.ForwardChain.RuleBaseSystem fw = new kadai4.ForwardChain.RuleBaseSystem();
		
		PagePanelBase[] pages= new PagePanelBase[3];
		pages[0] = new ForwardSearchPanel("前向き検索");
		pages[1] = new BackwardSearchPanel("後ろ向き検索");
		pages[2] = new AddPanel("追加");
		PageFrameBase pageFrame = new PageFrameBase("課題４", 1600, 900, pages);
		pageFrame.setVisible(true);
	}

}
