package kadai4.BackwordChain;

import java.io.FileReader;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import GUI.LogArea;

public class RuleBaseSystem {
	static RuleBase rb;
	static FileManager fm;

	public static void main(String args[]) {
		Scanner scan = new Scanner(System.in);
		//if(args.length != 1){
		//  LogArea.println("Usage: %java RuleBaseSystem [query strings]");
		//  LogArea.println("Example:");
		//  LogArea.println(" \"?x is b\" and \"?x is c\" are queries");
		//  LogArea.println("  %java RuleBaseSystem \"?x is b,?x is c\"");
		//} else {
		fm = new FileManager();
		LogArea.print("Enter data-filename:");
		String datafilename = scan.nextLine();
		LogArea.print("Enter workingmemory-filename:");
		String wmfilename = scan.nextLine();
		ArrayList<Rule> rules = fm.loadRules(datafilename);
		//ArrayList rules = fm.loadRules("AnimalWorld.data");
		ArrayList<String> wm = fm.loadWm(wmfilename);
		//ArrayList wm    = fm.loadWm("AnimalWorldWm.data");
		rb = new RuleBase(rules, wm);

		LogArea.print("add rule? delete rule? add...1/delete...2/No thanks...3 :");
		int j = scan.nextInt();
		switch (j) {
		case 1:
			rb.addRules();
			break;
		case 2:
			rb.deleteRules();
			break;
		case 3:
			break;
		}
		scan = new Scanner(System.in);
		LogArea.print("Enter Search pattern:");
		String pattern = scan.nextLine();

		StringTokenizer st = new StringTokenizer(pattern, ",");
		ArrayList<String> queries = new ArrayList<String>();
		for (int i = 0; i < st.countTokens();) {
			queries.add(st.nextToken());
		}
		rb.backwardChain(queries);
		//}
	}
}

class RuleBase implements Serializable {
	String fileName;
	ArrayList<String> wm;
	ArrayList<Rule> rules;

	RuleBase(ArrayList<Rule> theRules, ArrayList<String> theWm) {
		wm = theWm;
		rules = theRules;
	}

	public void backwardChain(ArrayList<String> hypothesis) {
		LogArea.println("Hypothesis:" + hypothesis);
		ArrayList<String> orgQueries = (ArrayList) hypothesis.clone();
		//HashMap<String,String> binding = new HashMap<String,String>();
		HashMap<String, String> binding = new HashMap<String, String>();
		if (matchingPatterns(hypothesis, binding)) {
			LogArea.println("Yes");
			LogArea.println(binding.toString());
			// 最終的な結果を基のクェリーに代入して表示する
			for (int i = 0; i < orgQueries.size(); i++) {
				String aQuery = (String) orgQueries.get(i);
				LogArea.println("binding: " + binding);
				String anAnswer = instantiate(aQuery, binding);
				LogArea.println("Query: " + aQuery);
				LogArea.println("Answer:" + anAnswer);
			}
		} else {
			LogArea.println("No");
		}
	}

	/**
	* マッチするワーキングメモリのアサーションとルールの後件
	* に対するバインディング情報を返す
	*/
	private boolean matchingPatterns(ArrayList<String> thePatterns, HashMap<String, String> theBinding) {
		String firstPattern;
		if (thePatterns.size() == 1) {
			firstPattern = (String) thePatterns.get(0);
			if (matchingPatternOne(firstPattern, theBinding, 0) != -1) {
				return true;
			} else {
				return false;
			}
		} else {
			firstPattern = (String) thePatterns.get(0);
			thePatterns.remove(0);

			int cPoint = 0;
			while (cPoint < wm.size() + rules.size()) {
				// 元のバインディングを取っておく
				HashMap<String, String> orgBinding = new HashMap<String, String>();
				for (Iterator<String> i = theBinding.keySet().iterator(); i.hasNext();) {
					String key = i.next();
					String value = (String) theBinding.get(key);
					orgBinding.put(key, value);
				}
				int tmpPoint = matchingPatternOne(firstPattern, theBinding, cPoint);
				LogArea.println("tmpPoint: " + tmpPoint);
				if (tmpPoint != -1) {
					LogArea.println("Success:" + firstPattern);
					if (matchingPatterns(thePatterns, theBinding)) {
						//成功
						return true;
					} else {
						//失敗
						//choiceポイントを進める
						cPoint = tmpPoint;
						// 失敗したのでバインディングを戻す
						theBinding.clear();
						for (Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();) {
							String key = i.next();
							String value = orgBinding.get(key);
							theBinding.put(key, value);
						}
					}
				} else {
					// 失敗したのでバインディングを戻す
					theBinding.clear();
					for (Iterator<String> i = orgBinding.keySet().iterator(); i.hasNext();) {
						String key = i.next();
						String value = orgBinding.get(key);
						theBinding.put(key, value);
					}
					return false;
				}
			}
			return false;
			/*
			if(matchingPatternOne(firstPattern,theBinding)){
			return matchingPatterns(thePatterns,theBinding);
			} else {
			return false;
			}
			*/
		}
	}

	private int matchingPatternOne(String thePattern, HashMap<String, String> theBinding, int cPoint) {
		if (cPoint < wm.size()) {
			// WME(Working Memory Elements) と Unify してみる．
			for (int i = cPoint; i < wm.size(); i++) {
				if ((new Unifier()).unify(thePattern,
						(String) wm.get(i),
						theBinding)) {
					LogArea.println("Success WM");
					LogArea.println((String) wm.get(i) + " <=> " + thePattern);
					return i + 1;
				}
			}
		}
		if (cPoint < wm.size() + rules.size()) {
			// Ruleと Unify してみる．
			for (int i = cPoint; i < rules.size(); i++) {
				Rule aRule = rename((Rule) rules.get(i));
				// 元のバインディングを取っておく．
				HashMap<String, String> orgBinding = new HashMap<String, String>();
				for (Iterator<String> itr = theBinding.keySet().iterator(); itr.hasNext();) {
					String key = itr.next();
					String value = theBinding.get(key);
					orgBinding.put(key, value);
				}
				if ((new Unifier()).unify(thePattern,
						(String) aRule.getConsequent(),
						theBinding)) {
					LogArea.println("Success RULE");
					LogArea.println("Rule:" + aRule + " <=> " + thePattern);
					// さらにbackwardChaining
					ArrayList<String> newPatterns = aRule.getAntecedents();
					if (matchingPatterns(newPatterns, theBinding)) {
						return wm.size() + i + 1;
					} else {
						// 失敗したら元に戻す．
						theBinding.clear();
						for (Iterator<String> itr = orgBinding.keySet().iterator(); itr.hasNext();) {
							String key = itr.next();
							String value = orgBinding.get(key);
							theBinding.put(key, value);
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	* 与えられたルールの変数をリネームしたルールのコピーを返す．
	* @param   変数をリネームしたいルール
	* @return  変数がリネームされたルールのコピーを返す．
	*/
	int uniqueNum = 0;

	private Rule rename(Rule theRule) {
		Rule newRule = theRule.getRenamedRule(uniqueNum);
		uniqueNum = uniqueNum + 1;
		return newRule;
	}

	private String instantiate(String thePattern, HashMap<String, String> theBindings) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + (String) theBindings.get(tmp);
				LogArea.println("tmp: " + tmp + ", result: " + result);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

	//追加箇所
	void addRules() {
		String name = null;
		ArrayList<String> antecedents = null;
		String consequent = null;

		Scanner scan = new Scanner(System.in);
		LogArea.println("--- Add Rule !!! ---");
		LogArea.print("Enter RuleName:");
		name = scan.nextLine();
		LogArea.print("Enter antecedent:");
		antecedents = new ArrayList<>();
		while (true) {
			String a = scan.nextLine();
			if (a.equals("finish"))
				break;
			antecedents.add(a);
		}
		LogArea.print("Enter consequent:");
		consequent = scan.nextLine();

		rules.add(new Rule(name, antecedents, consequent));
	}

	void deleteRules() {
		Scanner scan = new Scanner(System.in);
		String name = null;

		LogArea.println("--- Delete Rule !!! ---");
		LogArea.print("Enter RuleName:");
		name = scan.nextLine();
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i).getName().equals(name)) {
				rules.remove(i);
				i--;
			}
		}
	}
}

class FileManager {
	FileReader f;
	StreamTokenizer st;

	public ArrayList<Rule> loadRules(String theFileName) {
		ArrayList<Rule> rules = new ArrayList<Rule>();
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_WORD:
					String name = null;
					ArrayList<String> antecedents = null;
					String consequent = null;
					if ("rule".equals(st.sval)) {
						st.nextToken();
						name = st.sval;
						st.nextToken();
						if ("if".equals(st.sval)) {
							antecedents = new ArrayList<String>();
							st.nextToken();
							while (!"then".equals(st.sval)) {
								antecedents.add(st.sval);
								st.nextToken();
							}
							if ("then".equals(st.sval)) {
								st.nextToken();
								consequent = st.sval;
							}
						}
					}
					rules.add(
							new Rule(name, antecedents, consequent));
					break;
				default:
					LogArea.println(((Integer) token).toString());
					break;
				}
			}
		} catch (Exception e) {
			LogArea.println(e.toString());
		}
		return rules;
	}

	public ArrayList<String> loadWm(String theFileName) {
		ArrayList<String> wm = new ArrayList<String>();
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			st.eolIsSignificant(true);
			st.wordChars('\'', '\'');
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				line = new String();
				while (token != StreamTokenizer.TT_EOL) {
					line = line + st.sval + " ";
					token = st.nextToken();
				}
				wm.add(line.trim());
			}
		} catch (Exception e) {
			LogArea.println(e.toString());
		}
		return wm;
	}
}

/**
* ルールを表すクラス．
*/
class Rule implements Serializable {
	String name;
	ArrayList<String> antecedents;
	String consequent;

	Rule(String theName, ArrayList<String> theAntecedents, String theConsequent) {
		this.name = theName;
		this.antecedents = theAntecedents;
		this.consequent = theConsequent;
	}

	public Rule getRenamedRule(int uniqueNum) {
		ArrayList<String> vars = new ArrayList<String>();
		for (int i = 0; i < antecedents.size(); i++) {
			String antecedent = (String) this.antecedents.get(i);
			vars = getVars(antecedent, vars);
		}
		vars = getVars(this.consequent, vars);
		HashMap<String, String> renamedVarsTable = makeRenamedVarsTable(vars, uniqueNum);

		ArrayList<String> newAntecedents = new ArrayList<String>();
		for (int i = 0; i < antecedents.size(); i++) {
			String newAntecedent = renameVars((String) antecedents.get(i),
					renamedVarsTable);
			newAntecedents.add(newAntecedent);
		}
		String newConsequent = renameVars(consequent,
				renamedVarsTable);

		Rule newRule = new Rule(name, newAntecedents, newConsequent);
		return newRule;
	}

	private ArrayList<String> getVars(String thePattern, ArrayList<String> vars) {
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				vars.add(tmp);
			}
		}
		return vars;
	}

	private HashMap<String, String> makeRenamedVarsTable(ArrayList<String> vars, int uniqueNum) {
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < vars.size(); i++) {
			String newVar = (String) vars.get(i) + uniqueNum;
			result.put((String) vars.get(i), newVar);
		}
		return result;
	}

	private String renameVars(String thePattern,
			HashMap<String, String> renamedVarsTable) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + renamedVarsTable.get(tmp);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str) {
		// 先頭が ? なら変数
		return str.startsWith("?");
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name + " " + antecedents.toString() + "->" + consequent;
	}

	public ArrayList<String> getAntecedents() {
		return antecedents;
	}

	public String getConsequent() {
		return consequent;
	}
}

class Unifier {
	StringTokenizer st1;
	String buffer1[];
	StringTokenizer st2;
	String buffer2[];
	HashMap<String, String> vars;

	Unifier() {
		//vars = new HashMap();
	}

	public boolean unify(String string1, String string2, HashMap<String, String> theBindings) {
		HashMap<String, String> orgBindings = new HashMap<String, String>();
		for (Iterator<String> i = theBindings.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			String value = theBindings.get(key);
			orgBindings.put(key, value);
		}
		this.vars = theBindings;
		if (unify(string1, string2)) {
			return true;
		} else {
			// 失敗したら元に戻す．
			theBindings.clear();
			for (Iterator<String> i = orgBindings.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				String value = orgBindings.get(key);
				theBindings.put(key, value);
			}
			return false;
		}
	}

	public boolean unify(String string1, String string2) {
		// 同じなら成功
		if (string1.equals(string2))
			return true;

		// 各々トークンに分ける
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が異なったら失敗
		if (st1.countTokens() != st2.countTokens())
			return false;

		// 定数同士
		int length = st1.countTokens();
		buffer1 = new String[length];
		buffer2 = new String[length];
		for (int i = 0; i < length; i++) {
			buffer1[i] = st1.nextToken();
			buffer2[i] = st2.nextToken();
		}

		// 初期値としてバインディングが与えられていたら
		if (this.vars.size() != 0) {
			for (Iterator<String> i = vars.keySet().iterator(); i.hasNext();) {
				String key = i.next();
				String value = vars.get(key);
				replaceBuffer(key, value);
			}
		}

		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				return false;
			}
		}

		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
		if (var(token1) && var(token2))
			return varMatching(token1, token2);
		return false;
	}

	boolean varMatching(String vartoken, String token) {
		if (vars.containsKey(vartoken)) {
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {
			replaceBuffer(vartoken, token);
			if (vars.containsValue(vartoken)) {
				replaceBindings(vartoken, token);
			}
			vars.put(vartoken, token);
		}
		return true;
	}

	void replaceBuffer(String preString, String postString) {
		for (int i = 0; i < buffer1.length; i++) {
			if (preString.equals(buffer1[i])) {
				buffer1[i] = postString;
			}
			if (preString.equals(buffer2[i])) {
				buffer2[i] = postString;
			}
		}
	}

	void replaceBindings(String preString, String postString) {
		for (Iterator<String> i = vars.keySet().iterator(); i.hasNext();) {
			String key = i.next();
			if (preString.equals(vars.get(key))) {
				vars.put(key, postString);
			}
		}
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}
}
