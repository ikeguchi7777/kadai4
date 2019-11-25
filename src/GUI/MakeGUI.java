package GUI;

import kadai4.RuleBase;

public class MakeGUI {

	public static void main(String[] args) {
		RuleBase rb = new RuleBase();
		kadai4.BackwordChain.RuleBaseSystem bw = new kadai4.BackwordChain.RuleBaseSystem(rb);
		kadai4.ForwardChain.RuleBaseSystem fw = new kadai4.ForwardChain.RuleBaseSystem(rb);
		
		PagePanelBase[] pages= new PagePanelBase[4];
		pages[0] = new ForwardSearchPanel("前向き検索",fw);
		pages[1] = new BackwardSearchPanel("後ろ向き検索",bw);
		pages[2] = new AddPanel("追加",rb);
		pages[3] = new RemovePanel("削除", rb);
		PageFrameBase pageFrame = new PageFrameBase("課題４", 1600, 900, pages);
		pageFrame.setVisible(true);
	}

}
