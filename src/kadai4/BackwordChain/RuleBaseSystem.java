package kadai4.BackwordChain;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import GUI.LogArea;
import kadai4.FileManager;
import kadai4.Rule;
import kadai4.RuleBase;
import kadai4.WorkingMemory;

public class RuleBaseSystem {
	RuleBase rb;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		//if(args.length != 1){
		//  LogArea.println("Usage: %java RuleBaseSystem [query strings]");
		//  LogArea.println("Example:");
		//  LogArea.println(" \"?x is b\" and \"?x is c\" are queries");
		//  LogArea.println("  %java RuleBaseSystem \"?x is b,?x is c\"");
		//} else {

		LogArea.print("Enter data-filename:");
		String datafilename = scan.nextLine();
		LogArea.print("Enter workingmemory-filename:");
		String wmfilename = scan.nextLine();
		ArrayList<Rule> rules = FileManager.loadRules(datafilename);
		//ArrayList rules = fm.loadRules("AnimalWorld.data");
		WorkingMemory wm = new WorkingMemory(FileManager.loadWm(wmfilename));
		//ArrayList wm    = fm.loadWm("AnimalWorldWm.data");
		RuleBaseSystem rbs = new RuleBaseSystem( new RuleBase(rules,wm));
		scan = new Scanner(System.in);
		LogArea.print("Enter Search pattern:");
		String pattern = scan.nextLine();

		rbs.patternSearch(pattern);
		//}
	}

	public RuleBaseSystem(RuleBase rb) {
		this.rb=rb;
	}

	public void patternSearch(String pattern) {
		StringTokenizer st = new StringTokenizer(pattern, ",");
		ArrayList<String> queries = new ArrayList<String>();
		for (int i = 0; i < st.countTokens();) {
			queries.add(st.nextToken());
		}
		rb.backwardChain(queries);
	}
}