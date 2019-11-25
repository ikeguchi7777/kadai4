package kadai4.ForwardChain;

import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import GUI.LogArea;

/**
* RuleBaseSystem
*
*/
public class RuleBaseSystem {
  static RuleBase rb;
  public static void main(String args[]){
    String name;
    ArrayList<String> antecedents;
    String consequent;

    Scanner stdIn = new Scanner(System.in);
    Scanner scan = new Scanner(System.in);
    rb = new RuleBase();
    System.out.print("add rule? delete rule? add...1/delete...2/No thanks...3 : ");
    int j = stdIn.nextInt();

    switch(j){
      case 1:
      System.out.println("--- Add Rule !!! ---");
      System.out.print("Enter RuleName:");
      name = scan.nextLine();
      System.out.print("Enter antecedent:");
      antecedents = new ArrayList<>();
      while(true){
        String a = scan.nextLine();
        if(a.equals("finish")) break;
        antecedents.add(a);
      }
      System.out.print("Enter consequent:");
      consequent = scan.nextLine();
      rb.addRules(name,antecedents,consequent);
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
    stdIn = new Scanner(System.in);
    while(true){
      System.out.print("Enter Search Pattern:");
      String query = stdIn.nextLine();
      if(query.equals("exit")){
        break;
      }
      for(String st:rb.wm.assertions){
        (new Unifier()).unify(st,query);
      }
    }
  }
}

/**
* ワーキングメモリを表すクラス．
*
*
*/
class WorkingMemory {
	ArrayList<String> assertions;
	Scanner scan = new Scanner(System.in);

	WorkingMemory() {
		assertions = new ArrayList<String>();
	}

	/**
	* マッチするアサーションに対するバインディング情報を返す
	* （再帰的）
	*
	* @param     前件を示す ArrayList
	* @return    バインディング情報が入っている ArrayList
	*/
	public ArrayList matchingAssertions(ArrayList<String> theAntecedents) {
		ArrayList bindings = new ArrayList();
		return matchable(theAntecedents, 0, bindings);
	}

	private ArrayList matchable(ArrayList<String> theAntecedents, int n, ArrayList bindings) {
		if (n == theAntecedents.size()) {
			return bindings;
		} else if (n == 0) {
			boolean success = false;
			for (int i = 0; i < assertions.size(); i++) {
				HashMap<String, String> binding = new HashMap<String, String>();
				if ((new Matcher()).matching(
						(String) theAntecedents.get(n),
						(String) assertions.get(i),
						binding)) {
					bindings.add(binding);
					success = true;
				}
			}
			if (success) {
				return matchable(theAntecedents, n + 1, bindings);
			} else {
				return null;
			}
		} else {
			boolean success = false;
			ArrayList newBindings = new ArrayList();
			for (int i = 0; i < bindings.size(); i++) {
				for (int j = 0; j < assertions.size(); j++) {
					if ((new Matcher()).matching(
							(String) theAntecedents.get(n),
							(String) assertions.get(j),
							(HashMap) bindings.get(i))) {
						newBindings.add(bindings.get(i));
						success = true;
					}
				}
			}
			if (success) {
				return matchable(theAntecedents, n + 1, newBindings);
			} else {
				return null;
			}
		}
	}

	/**
	* アサーションをワーキングメモリに加える．
	*
	* @param     アサーションを表す String
	*/
	public void addAssertion(String theAssertion) {
		LogArea.println("ADD:" + theAssertion);
		assertions.add(theAssertion);
	}

	/**
	* 指定されたアサーションがすでに含まれているかどうかを調べる．
	*
	* @param     アサーションを表す String
	* @return    含まれていれば true，含まれていなければ false
	*/
	public boolean contains(String theAssertion) {
		return assertions.contains(theAssertion);
	}

	/**
	* ワーキングメモリの情報をストリングとして返す．
	*
	* @return    ワーキングメモリの情報を表す String
	*/
	public String toString() {
		return assertions.toString();
	}

}

/**
* ルールベースを表すクラス．
*
*
*/
class RuleBase {
  String fileName;
  String dataFilename;
  String pattern;
  FileReader f;
  StreamTokenizer st;
  WorkingMemory wm;
  ArrayList<Rule> rules;
  static FileManager fm;

  RuleBase(){
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter data-filename:"); // ファイル名の入力
    fileName = scan.nextLine();
    wm = new WorkingMemory();
    fm = new FileManager();

    System.out.print("Enter workingmemory-filename:");
    dataFilename = scan.nextLine();
    ArrayList<String> wms = fm.loadWm(dataFilename); //ワーキングメモリの取り込み
    for(String str : wms){
      wm.addAssertion(str);
    }
    /*    while(true){
    System.out.print("Enter add-assertion:");
    String as = scan.nextLine();
    if(as.equals("exit")){
    break;
  }else{
  wm.addAssertion(as);
}
}*/
rules = new ArrayList<>();
loadRules(fileName);
//scan.close();
}

/**
* 前向き推論を行うためのメソッド
*
*/
public void forwardChain(){
  boolean newAssertionCreated;
  // 新しいアサーションが生成されなくなるまで続ける．
  do {
    newAssertionCreated = false;
    for(int i = 0 ; i < rules.size(); i++){
      Rule aRule = (Rule)rules.get(i);
      System.out.println("apply rule:"+aRule.getName());
      ArrayList<String> antecedents = aRule.getAntecedents();
      String consequent  = aRule.getConsequent();
      //HashMap bindings = wm.matchingAssertions(antecedents);
      ArrayList bindings = wm.matchingAssertions(antecedents);
      if(bindings != null){
        for(int j = 0 ; j < bindings.size() ; j++){
          //後件をインスタンシエーション
          String newAssertion =
          instantiate((String)consequent,
          (HashMap)bindings.get(j));
          //ワーキングメモリーになければ成功
          if(!wm.contains(newAssertion)){
            System.out.println("Success: "+newAssertion);
            wm.addAssertion(newAssertion);
            newAssertionCreated = true;
          }
        }
      }
    }
    System.out.println("Working Memory"+wm);
  } while(newAssertionCreated);
  System.out.println("No rule produces a new assertion");
  System.out.println();
}

private String instantiate(String thePattern, HashMap theBindings){
  String result = new String();
  StringTokenizer st = new StringTokenizer(thePattern);
  for(int i = 0 ; i < st.countTokens();){
    String tmp = st.nextToken();
    if(var(tmp)){
      result = result + " " + (String)theBindings.get(tmp);
    } else {
      result = result + " " + tmp;
    }
  }
  return result.trim();
}

private boolean var(String str1){
  // 先頭が ? なら変数
  return str1.startsWith("?");
}

//追加箇所
void addRules(String name,ArrayList<String> antecedents,String consequent){
  //String name = null;
  //ArrayList<String> antecedents = null;
  //String consequent = null;

  //Scanner scan = new Scanner(System.in);
/*  System.out.print("Enter RuleName:");
  name = scan.nextLine();
  System.out.print("Enter antecedent:");
  antecedents = new ArrayList<>();
  while(true){
    String a = scan.nextLine();
    if(a.equals("finish")) break;
    antecedents.add(a);
  }
  System.out.print("Enter consequent:");
  consequent = scan.nextLine();           */

  rules.add(new Rule(name,antecedents,consequent));
}

void deleteRules(String name){
  for(int i=0; i<rules.size();i++){
    if(rules.get(i).getName().equals(name)){
      rules.remove(i);
      i--;
    }
  }
}








private void loadRules(String theFileName){
  String line;
  try{
    int token;
    f = new FileReader(theFileName);
    st = new StreamTokenizer(f);
    while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
      switch(token){
        case StreamTokenizer.TT_WORD:
        String name = null;
        ArrayList<String> antecedents = null;
        String consequent = null;
        if("rule".equals(st.sval)){
          st.nextToken();
          //                            if(st.nextToken() == '"'){
          name = st.sval;
          st.nextToken();
          if("if".equals(st.sval)){
            antecedents = new ArrayList<String>();
            st.nextToken();
            while(!"then".equals(st.sval)){
              antecedents.add(st.sval);
              st.nextToken();
            }
            if("then".equals(st.sval)){
              st.nextToken();
              consequent = st.sval;
            }
          }
          //                            }
        }
        // ルールの生成
        rules.add(new Rule(name,antecedents,consequent));
        break;
        default:
        System.out.println(token);
        break;
      }
    }
  } catch(Exception e){
    System.out.println(e);
  }
  for(int i = 0 ; i < rules.size() ; i++){
    System.out.println(((Rule)rules.get(i)).toString());
  }
}
}

/**
* ルールを表すクラス．
*
*
*/
class Rule {
	String name;
	ArrayList<String> antecedents;
	String consequent;

	Rule(String theName, ArrayList<String> theAntecedents, String theConsequent) {
		this.name = theName;
		this.antecedents = theAntecedents;
		this.consequent = theConsequent;
	}

	/**
	* ルールの名前を返す．
	*
	* @return    名前を表す String
	*/
	public String getName() {
		return name;
	}

	/**
	* ルールをString形式で返す
	*
	* @return    ルールを整形したString
	*/
	public String toString() {
		return name + " " + antecedents.toString() + "->" + consequent;
	}

	/**
	* ルールの前件を返す．
	*
	* @return    前件を表す ArrayList
	*/
	public ArrayList<String> getAntecedents() {
		return antecedents;
	}

	/**
	* ルールの後件を返す．
	*
	* @return    後件を表す String
	*/
	public String getConsequent() {
		return consequent;
	}

}

class Matcher {
	StringTokenizer st1;
	StringTokenizer st2;
	HashMap<String, String> vars;

	Matcher() {
		vars = new HashMap<String, String>();
	}

	public boolean matching(String string1, String string2, HashMap<String, String> bindings) {
		this.vars = bindings;
		return matching(string1, string2);
	}

	public boolean matching(String string1, String string2) {
		//LogArea.println(string1);
		//LogArea.println(string2);

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
		for (int i = 0; i < st1.countTokens();) {
			if (!tokenMatching(st1.nextToken(), st2.nextToken())) {
				// トークンが一つでもマッチングに失敗したら失敗
				return false;
			}
		}

		// 最後まで O.K. なら成功
		return true;
	}

	boolean tokenMatching(String token1, String token2) {
		//LogArea.println(token1+"<->"+token2);
		if (token1.equals(token2))
			return true;
		if (var(token1) && !var(token2))
			return varMatching(token1, token2);
		if (!var(token1) && var(token2))
			return varMatching(token2, token1);
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
			vars.put(vartoken, token);
		}
		return true;
	}

	boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}
}

class FileManager {
  FileReader f;
  StreamTokenizer st;
  public ArrayList<Rule> loadRules(String theFileName){
    ArrayList<Rule> rules = new ArrayList<Rule>();
    String line;
    try{
      int token;
      f = new FileReader(theFileName);
      st = new StreamTokenizer(f);
      while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
        switch(token){
          case StreamTokenizer.TT_WORD:
          String name = null;
          ArrayList<String> antecedents = null;
          String consequent = null;
          if("rule".equals(st.sval)){
            st.nextToken();
            name = st.sval;
            st.nextToken();
            if("if".equals(st.sval)){
              antecedents = new ArrayList<String>();
              st.nextToken();
              while(!"then".equals(st.sval)){
                antecedents.add(st.sval);
                st.nextToken();
              }
              if("then".equals(st.sval)){
                st.nextToken();
                consequent = st.sval;
              }
            }
          }
          rules.add(
          new Rule(name,antecedents,consequent));
          break;
          default:
          System.out.println(token);
          break;
        }
      }
    } catch(Exception e){
      System.out.println(e);
    }
    return rules;
  }

  public ArrayList<String> loadWm(String theFileName){
    ArrayList<String> wm = new ArrayList<String>();
    String line;
    try{
      int token;
      f = new FileReader(theFileName);
      st = new StreamTokenizer(f);
      st.eolIsSignificant(true);
      st.wordChars('\'','\'');
      while((token = st.nextToken())!= StreamTokenizer.TT_EOF){
        line = new String();
        while( token != StreamTokenizer.TT_EOL){
          if(st.sval == null)
            line = line + (int)st.nval + " ";
          else
            line = line + st.sval + " ";
          token = st.nextToken();
        }
        wm.add(line.trim());
      }
    } catch(Exception e){
      System.out.println(e);
    }
    return wm;
  }
}

class Unifier {
	StringTokenizer st1;
	String buffer1[];
	StringTokenizer st2;
	String buffer2[];
	HashMap<String, String> vars;

	Unifier() {
		vars = new HashMap<String, String>();
	}

	public boolean unify(String string1, String string2, HashMap<String, String> bindings) {
		this.vars = bindings;
		return unify(string1, string2);
	}

	public boolean unify(String string1, String string2) {
		//LogArea.println(string1);
		//LogArea.println(string2);

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
		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				return false;
			}
		}

		// 最後まで O.K. なら成功
		LogArea.println(vars.toString());
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
		Iterator<String> keys;
		for (keys = vars.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
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
