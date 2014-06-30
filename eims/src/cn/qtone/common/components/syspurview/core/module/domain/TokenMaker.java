package cn.qtone.common.components.syspurview.core.module.domain;

import java.util.Random;

import cn.qtone.common.utils.base.StringUtil;

/**
 * 模块的token生成对象.
 * 
 * @author 马必强
 *
 */
public class TokenMaker
{
	public final static int TOKEN_LEN = 6; // 每一个token的长度
	
	private final static char[] chars = {'a' ,'b', 'c', 'd', 'e', 'f', 'g', 'h',
		'i','j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	/**
	 * 获取指定个数的token字符串.
	 * @param pToken 指定的父菜单的TOKEN
	 * @param nums
	 * @return
	 */
	public final static String[] getTokens(String pToken, int nums)
	{
		String[] result = new String[nums];
		StringBuilder buf = new StringBuilder();
		Random rand = new Random();
		for (int i = 0; i < nums; i ++) {
			buf.delete(0, buf.length());
			for (int j = 0; j < TOKEN_LEN; j ++) {
				buf.append(chars[rand.nextInt(chars.length)]);
			}
			result[i] = pToken + buf.toString();
		}
		return result;
	}
	
	/**
	 * 获取指定模块的父菜单的唯一token .
	 * @param mod
	 * @return
	 */
	public final static String getParentToken(Module mod)
	{
		if (mod == null) return "";
		return getParentToken(mod.getMToken());
	}
	
	/**
	 * 获取指定token的父菜单模块的token
	 * @param token
	 * @return
	 */
	public final static String getParentToken(String token)
	{
		String tmp = StringUtil.trim(token).intern();
		if (tmp == "" || tmp.length() == TOKEN_LEN) return "";
		int depth = tmp.length() / TOKEN_LEN;
		int index = (depth - 1) * TOKEN_LEN;
		return tmp.substring(0, index);
	}
	
	/**
	 * 获取当前模块其本身的token，不包括父token.
	 * @param mod
	 * @return
	 */
	public final static String getSelfToken(Module mod)
	{
		if (mod.getMToken().length() == TOKEN_LEN) return mod.getMToken();
		int depth = mod.getMToken().length() / TOKEN_LEN;
		int index = (depth - 1) * TOKEN_LEN;
		return mod.getMToken().substring(index);
	}
}
