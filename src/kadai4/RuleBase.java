package kadai4;

import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import GUI.LogArea;

/**
* ルールベースを表すクラス．
*
*
*/
public class RuleBase {
	FileReader f;
	StreamTokenizer st;
	public WorkingMemory wm;
	ArrayList<Rule> rules;
	//static FileManager fm;

	public RuleBase() {
	}

	public RuleBase(ArrayList<Rule> theRules, WorkingMemory theWm) {
		wm = theWm;
		rules = theRules;
	}

	public void setWm(WorkingMemory theWm) {
		wm = theWm;
	}

	public void setRules(ArrayList<Rule> theRules) {
		rules = theRules;
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

	/**
	* 前向き推論を行うためのメソッド
	*
	*/
	public void forwardChain() {
		boolean newAssertionCreated;
		// 新しいアサーションが生成されなくなるまで続ける．
		do {
			newAssertionCreated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule aRule = (Rule) rules.get(i);
				LogArea.println("apply rule:" + aRule.getName());
				ArrayList<String> antecedents = aRule.getAntecedents();
				String consequent = aRule.getConsequent();
				//HashMap bindings = wm.matchingAssertions(antecedents);
				ArrayList bindings = wm.matchingAssertions(antecedents);
				if (bindings != null) {
					for (int j = 0; j < bindings.size(); j++) {
						//後件をインスタンシエーション
						String newAssertion = instantiate((String) consequent,
								(HashMap) bindings.get(j));
						//ワーキングメモリーになければ成功
						if (!wm.contains(newAssertion)) {
							LogArea.println("Success: " + newAssertion);
							wm.addAssertion(newAssertion);
							newAssertionCreated = true;
						}
					}
				}
			}
			LogArea.println("Working Memory" + wm);
		} while (newAssertionCreated);
		LogArea.println("No rule produces a new assertion");
		LogArea.println("");
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
			while (cPoint < wm.assertions.size() + rules.size()) {
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

	private int matchingPatternOne(String thePattern, HashMap<String, String> theBinding, int cPoint) {
		if (cPoint < wm.assertions.size()) {
			// WME(Working Memory Elements) と Unify してみる．
			for (int i = cPoint; i < wm.assertions.size(); i++) {
				if ((new Unifier()).unify(thePattern,
						(String) wm.assertions.get(i),
						theBinding)) {
					LogArea.println("Success WM");
					LogArea.println((String) wm.assertions.get(i) + " <=> " + thePattern);
					return i + 1;
				}
			}
		}
		if (cPoint < wm.assertions.size() + rules.size()) {
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
						return wm.assertions.size() + i + 1;
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

	private String instantiate(String thePattern, HashMap theBindings) {
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
	public void addRules(String name, ArrayList<String> antecedents, String consequent) {
		rules.add(new Rule(name, antecedents, consequent));
	}

	public void deleteRules(String name) {
		for (int i = 0; i < rules.size(); i++) {
			if (rules.get(i).getName().equals(name)) {
				rules.remove(i);
				i--;
			}
		}
	}

	private void loadRules(String theFileName) {
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
						//                            if(st.nextToken() == '"'){
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
						//                            }
					}
					// ルールの生成
					rules.add(new Rule(name, antecedents, consequent));
					break;
				default:
					LogArea.println(((Integer)token).toString());
					break;
				}
			}
		} catch (Exception e) {
			LogArea.println(e.toString());
		}
		for (int i = 0; i < rules.size(); i++) {
			LogArea.println(((Rule) rules.get(i)).toString());
		}
	}
}