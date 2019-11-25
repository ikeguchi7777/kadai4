package kadai4;

import java.util.HashMap;
import java.util.StringTokenizer;

public class Matcher {
	StringTokenizer st1;
	StringTokenizer st2;
	HashMap<String, String> vars;

	public Matcher() {
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
