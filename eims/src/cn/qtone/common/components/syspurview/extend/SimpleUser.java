package cn.qtone.common.components.syspurview.extend;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 简单的用户对象，主要用于外键相关的关系，暂用。
 * 
 * @author 张但
 *
 */
@Entity
@Table(name = "sys_user")
public class SimpleUser {

	private int userId;
	
	private String realName;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}
}
