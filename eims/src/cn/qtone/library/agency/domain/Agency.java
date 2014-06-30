package cn.qtone.library.agency.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.qtone.library.city.domain.City;

/**
 * 机构管理 - 机构管理.
 * 
 * @author 贺少辉
 * @version 1.0
 *
 */
@Entity
@Table(name = "library_agency")
public class Agency{

	/**
 	 * 主键
 	 */ 
	private Integer id;

	/**
 	 * 父级机构ID
 	 */ 
	private Integer parent_id;

	/**
 	 * 机构所属的城市
 	 */ 
	private City city;

	/**
 	 * 机构名称
 	 */ 
	private String agency_name;
	
	/**
 	 * 机构简称
 	 */ 
	private String agency_shortname;

	/**
 	 * 机构描述
 	 */ 
	private String agency_desc;
	
	private Integer agency_order;
	
	private Integer agency_type;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}


	public Integer getParent_id(){
		return parent_id;
	}
	
	public void setParent_id(Integer parent_id){
		this.parent_id = parent_id;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "town_id")
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getAgency_name(){
		return agency_name;
	}
	
	public void setAgency_name(String agency_name){
		this.agency_name = agency_name;
	}

	public String getAgency_desc() {
		return agency_desc;
	}

	public void setAgency_desc(String agency_desc) {
		this.agency_desc = agency_desc;
	}

	public Integer getAgency_order() {
		return agency_order;
	}

	public void setAgency_order(Integer agency_order) {
		this.agency_order = agency_order;
	}

	public Integer getAgency_type() {
		return agency_type;
	}

	public void setAgency_type(Integer agency_type) {
		this.agency_type = agency_type;
	}

	public String getAgency_shortname() {
		return agency_shortname;
	}

	public void setAgency_shortname(String agency_shortname) {
		this.agency_shortname = agency_shortname;
	}
}

