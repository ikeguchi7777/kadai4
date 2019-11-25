package kadai4.ForwardChain;

import java.util.ArrayList;
import java.util.Scanner;

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
	static RuleBase rb;

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
		rb = new RuleBase(rules,wm);
		System.out.print("add rule? delete rule? add...1/delete...2/No thanks...3 : ");
		int j = scan.nextInt();

		switch (j) {
		case 1:
			System.out.println("--- Add Rule !!! ---");
			System.out.print("Enter RuleName:");
			name = scan.nextLine();
			System.out.print("Enter antecedent:");
			antecedents = new ArrayList<>();
			while (true) {
				String a = scan.nextLine();
				if (a.equals("finish"))
					break;
				antecedents.add(a);
			}
			System.out.print("Enter consequent:");
			consequent = scan.nextLine();
			rb.addRules(name, antecedents, consequent);
			break;

		case 2:
			System.out.println("--- Delete Rule !!! ---");
			System.out.print("Enter RuleName:");
			name = scan.nextLine();
			rb.deleteRules(name);
			break;

		case 3:
			break;
		}
		rb.forwardChain();
		while (true) {
			System.out.print("Enter Search Pattern:");
			String query = scan.nextLine();
			if (query.equals("exit")) {
				break;
			}
			for (String st : rb.wm.assertions) {
				if((new Unifier()).unify(st, query))
					LogArea.println(st);
			}
		}
	}
}




