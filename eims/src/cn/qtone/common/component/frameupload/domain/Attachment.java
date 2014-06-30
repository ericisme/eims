package cn.qtone.common.component.frameupload.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sutk.<br>
 *         附件.
 */
@Entity
@Table(name = "attachment")
public class Attachment {
	/**
	 * 主键.
	 */
	private Integer id;
	/**
	 * 附件名称.
	 */
	private String attachName;
	/**
	 * 附件url地址.
	 */
	private String url;
	/**
	 * 附件对应表的ID
	 */
	private Integer specialId;
	/**
	 * 模块ID,每个模块的标识唯一.
	 */
	private String module;
	/**
	 * 上传附件的用户ID.
	 */
	private Integer userId;
	/**
	 * 上传附件的IP.
	 */
	private String ip;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttachName() {
		return attachName;
	}
	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getSpecialId() {
		return specialId;
	}
	public void setSpecialId(Integer specialId) {
		this.specialId = specialId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
