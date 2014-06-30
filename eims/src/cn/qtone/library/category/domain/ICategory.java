package cn.qtone.library.category.domain;

import java.util.List;

public class ICategory{

	private Integer id;

	private String name;
	
	private String code;

	private Integer parent_id;
	
	public List<ICategory> categoryChildren;

	public List<ICategory> getCategoryChildren() {
		return categoryChildren;
	}

	public void setCategoryChildren(List<ICategory> categoryChildren) {
		this.categoryChildren = categoryChildren;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}

	public Integer getId(){
		return id;
	}
	
	public void setId(Integer id){
		this.id = id;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	/*@OneToMany(mappedBy = "parent_id" )
	@OrderBy("id ASC")
	public List<Category> getCategoryChilden() {
		return categoryChilden;
	}

	public void setCategoryChilden(List<Category> categoryChilden) {
		this.categoryChilden = categoryChilden;
	}*/

}

