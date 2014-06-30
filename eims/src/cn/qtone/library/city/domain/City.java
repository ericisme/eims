package cn.qtone.library.city.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 城市、镇区实体bean.<br>
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "t_city")
public class City {

	/**
	 * 主键
	 */
	private Integer id;
	/**
	 * 直属的父级城市
	 */
	private Integer parentId;
	/**
	 * 城市名称
	 */
	private String name;
	/**
	 * 城市相对应顶层父级城市的差值.如中山市[0] 石岐区[1]
	 */
	private Integer levelNo;
	/**
	 * 排序号
	 */
	private Integer orderNo;
	/**
	 * 城市面积
	 */
	private Integer areaCode;
	/**
	 * 该表结构从cy52过来,该字段当前系统没用到,先保留.
	 */
	private Integer adminCode;
	/**
	 * 包含父id和自己的id
	 */
	private String path;
	/**
	 * 是否审查通过.该表结构从cy52过来,该字段当前系统没用到,先保留.
	 */
	private Integer checked;
	
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getLevelNo() {
		return levelNo;
	}
	public void setLevelNo(Integer levelNo) {
		this.levelNo = levelNo;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(Integer areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getAdminCode() {
		return adminCode;
	}
	public void setAdminCode(Integer adminCode) {
		this.adminCode = adminCode;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getChecked() {
		return checked;
	}
	public void setChecked(Integer checked) {
		this.checked = checked;
	}
	public City() {
		super();
	}
	public City(Integer id) {
		super();
		this.id = id;
	}
	
}
