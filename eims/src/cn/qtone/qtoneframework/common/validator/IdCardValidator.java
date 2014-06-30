package cn.qtone.qtoneframework.common.validator;

/**
 * 身份证验证
 * 
 * @author 张但
 * 
 */
public class IdCardValidator {

	/**
	 * 身份证号是否合法
	 * 
	 * @param idCard
	 * @return
	 */
	public static boolean isIdCardLegal(String idCard) {
		if (idCard == null || idCard.length() != 15 && idCard.length() != 18)
			return false;
		try {
			if (idCard.length() == 18) {
				String idCard15 = idCard.substring(0, 6) + idCard.substring(8, 17);
				return trans15To18(idCard15).equalsIgnoreCase(idCard);
			}
			if (idCard.length() == 15) {
				try {
					Long.parseLong(idCard);
					return true;
				} catch (Exception ex) {
					return false;
				}
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	/**
	 * 15位转18位
	 * 
	 * @param idCard15
	 * @return
	 */
	public static String trans15To18(String idCard15) {
		if (idCard15 == null || idCard15.trim().length() != 15) {
			return idCard15;
		}
		String id17 = idCard15.substring(0, 6) + "19" + idCard15.substring(6, 15); // 15为身份证补\'19\'
		char[] code = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' }; // 11个
		int[] factor = { 0, 2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7 }; // 18个;
		int[] idcd = new int[18];
		int i;
		int j;
		int sum;
		int remainder;
		for (i = 1; i < 18; i++) {
			j = 17 - i;
			idcd[i] = Integer.parseInt(id17.substring(j, j + 1));
		}
		sum = 0;
		for (i = 1; i < 18; i++) {
			sum = sum + idcd[i] * factor[i];
		}
		remainder = sum % 11;
		String lastCheckBit = String.valueOf(code[remainder]);
		return id17 + lastCheckBit;
	}

	public static void main(String args[]) {
		System.out.println(isIdCardLegal("445222198407171656"));
		System.out.println(isIdCardLegal("442000731031061"));
	}
}
