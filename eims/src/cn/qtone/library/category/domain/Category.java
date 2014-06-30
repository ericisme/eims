package cn.qtone.library.category.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "library_category")
public class Category{

	private Integer id;

	private String name;
	
	private String code;

	private Integer parent_id;
	
	private Set<Category> categoryChildren;
	
	public Integer getParent_id() {
		return parent_id;
	}

	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	
	

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	
	@OneToMany(mappedBy = "parent_id" )
	//@OrderBy("id ASC")
	public Set<Category> getCategoryChildren() {
		return categoryChildren;
	}

	public void setCategoryChildren(Set<Category> categoryChildren) {
		this.categoryChildren = categoryChildren;
	}

}

