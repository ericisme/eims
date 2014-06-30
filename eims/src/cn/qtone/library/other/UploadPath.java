package cn.qtone.library.other;

import java.io.File;

import cn.qtone.common.utils.base.DateUtil;

public class UploadPath {
	/**
	 * 附件根目录
	 */
	private static final String BASE_UPLOAD_FOLDER = File.separator + "upload";

	private int dept_id;
	
	
	/**
	 * 取得附件相对路径
	 * 
	 * @param module
	 *            模块
	 * @return 根目录 + 模块 + 年月
	 */
	public String getUploadPath() {
		return BASE_UPLOAD_FOLDER + File.separator + this.getDept_id() + File.separator + DateUtil.getYear()
				+ File.separator + DateUtil.getMonth();
	}

	public static void main(String[] args) {
		System.out.println(new UploadPath().getUploadPath());
	}

	public int getDept_id() {
		return dept_id;
	}

	public void setDept_id(int dept_id) {
		this.dept_id = dept_id;
	}
}
