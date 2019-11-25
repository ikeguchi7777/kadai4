package kadai4.ForwardChain;

import GUI.LogArea;
import kadai4.FileManager;
import kadai4.Rule;
import kadai4.RuleBase;
import kadai4.Unifier;
import kadai4.WorkingMemory;

/**
* RuleBaseSystem
*
*/
public class RuleBaseSystem {

	RuleBase rb;
	public static void main(String args[]) {
		String name;
		ArrayList<String> antecedents;
		String consequent;

		Scanner scan = new Scanner(System.in);
		FileManager fm = new FileManager();
		
		LogArea.print("Enter data-filename:");
		String datafilename = scan.nextLine();
		LogArea.print("Enter workingmemory-filename:");
		String wmfilename = scan.nextLine();
		ArrayList<Rule> rules = fm.loadRules(datafilename);
		//ArrayList rules = fm.loadRules("AnimalWorld.data");
		WorkingMemory wm = new WorkingMemory(fm.loadWm(wmfilename));
		//ArrayList wm    = fm.loadWm("AnimalWorldWm.data");
		RuleBaseSystem rbs = new RuleBaseSystem(new RuleBase(rules,wm));
		
		while (true) {
			System.out.print("Enter Search Pattern:");
			String query = scan.nextLine();
			if (query.equals("exit")) {
				break;
			}
			rbs.patternSearch(query);
		}
	}
	
	public RuleBaseSystem(RuleBase rb) {
		this.rb=rb;
	}
	
	public void patternSearch(String pattern) {
		rb.forwardChain();
		for (String st : rb.forwardWM.assertions) {
			if((new Unifier()).unify(st, pattern))
				LogArea.println(st);
		}
	}
}




