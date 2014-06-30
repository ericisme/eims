package cn.qtone.library.book.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.qtone.library.XxptContants;
import cn.qtone.library.category.domain.Category;
import cn.qtone.library.school.domain.School;

@Entity
@Table(name = "library_book")
public class Book {
	
	
	private Integer id;
	private String book_no;//图书编号//系统生成//条形码
	private String book_name;//图书名称*
	private Category category;//category_id;//分类*
	private String category_code;//*分类号*
	private String category_name;//分类名*
	private Double book_price;//*定价*
	private String book_place;//*存放位置*
	private School school;// shool_id;//*归属单位*
	/**
	 * 2011-6-3
	 */
	private School original_school;// shool_id;//*最原始归属单位*
	/**
	 * 
	 */
	private String book_isbn;//标准ISBN
	private Integer category_no;//种次号//系统生成
	private String book_index;//索书号  //系统生成
	private String book_author;//作者
	private String book_publish;//出版社
	private String book_publish_place;//出版地
	private String book_publish_year;//出版年份
	private String book_storage_time;//入库时间
	private String book_content;//内容简介
	private Integer book_private;//密级
	private String book_page;//页码
	private String book_format;//开本
	private String book_language;//语种
	private String book_goods;//配送物品
	private String book_publish_time;//版次
	private String book_congshu;//所属丛书
	private String printface;//印刷版面
	private String content;//备注信息
	private Integer status;//0在库；1出借；2遗失；3损坏	
	private String readability; //可阅读性：  	好；中；差
	private Integer sjlx;//书籍类型（0：学生图书，1：老师用书，2：期刊杂志）
	private Integer sffq;//是否废弃图书（0：否，1：是）
	
	
	
	public Integer getSffq() {
		return sffq;
	}
	public void setSffq(Integer sffq) {
		this.sffq = sffq;
	}
	public Integer getSjlx() {
		return sjlx;
	}
	public void setSjlx(Integer sjlx) {
		this.sjlx = sjlx;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	//新增
	public String getReadability() {
		return readability;
	}
	public void setReadability(String readability) {
		this.readability = readability;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBook_no() {
		return book_no;
	}
	public void setBook_no(String book_no) {
		this.book_no = book_no;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getBook_isbn() {
		return book_isbn;
	}
	public void setBook_isbn(String book_isbn) {
		this.book_isbn = book_isbn;
	}
	public String getCategory_code() {
		return category_code;
	}
	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public Integer getCategory_no() {
		return category_no;
	}
	public void setCategory_no(Integer category_no) {
		this.category_no = category_no;
	}
	public String getBook_index() {
		return book_index;
	}
	public void setBook_index(String book_index) {
		this.book_index = book_index;
	}
	public String getBook_author() {
		return book_author;
	}
	public void setBook_author(String book_author) {
		this.book_author = book_author;
	}
	public Double getBook_price() {
		return book_price;
	}
	public void setBook_price(Double book_price) {
		this.book_price = book_price;
	}
	public String getBook_publish() {
		return book_publish;
	}
	public void setBook_publish(String book_publish) {
		this.book_publish = book_publish;
	}
	public String getBook_publish_place() {
		return book_publish_place;
	}
	public void setBook_publish_place(String book_publish_place) {
		this.book_publish_place = book_publish_place;
	}
	public String getBook_publish_year() {
		return book_publish_year;
	}
	public void setBook_publish_year(String book_publish_year) {
		this.book_publish_year = book_publish_year;
	}
	public String getBook_storage_time() {
		return book_storage_time;
	}
	public void setBook_storage_time(String book_storage_time) {
		this.book_storage_time = book_storage_time;
	}
	
	
	/*
	 * 导入EXCEL用
	 */
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
	
	private String status_str;
	private String book_private_str;
	
	public void setBook_private_str(String book_private_str) {
		this.book_private_str = book_private_str;
	}
	@Transient
	public String getStatus_str() {
		return status_str;
	}
	
	public void setStatus_str(String status_str) {
		this.status_str = status_str;
	}
	
	@Transient
	public String getBook_private_str() {
		return book_private_str;
	}
	
	public Integer setStatusStrToInt() {
		if(status_str==null || status_str.length()<1) 
			return 0;
		for(int i = 0;i<XxptContants.BOOKSTATUS.size();i++){
			if(status_str.equals(XxptContants.BOOKSTATUS.get(String.valueOf(i))))
				return i;
		}
		return 0;
	} 
	
	public void setBook_storage_time_str(java.util.Date storage_time_str) {
		try {
			this.book_storage_time = sdf.format(storage_time_str);
		} catch (Exception e) {
			
		}
		
	}
	
	public Integer setBook_privateStrToInt() {
		if(book_private_str==null || book_private_str.length()<1) 
			return 0;
		for(int i = 0;i<XxptContants.BOOKPRIVATE.size();i++){
			if(book_private_str.equals(XxptContants.BOOKPRIVATE.get(String.valueOf(i))))
				return i;
		}
		return 0;
	}
	
	
	public String getBook_content() {
		return book_content;
	}
	public void setBook_content(String book_content) {
		this.book_content = book_content;
	}
	public Integer getBook_private() {
		return book_private;
	}
	public void setBook_private(Integer book_private) {
		this.book_private = book_private;
	}
	public String getBook_place() {
		return book_place;
	}
	public void setBook_place(String book_place) {
		this.book_place = book_place;
	}
	public String getBook_page() {
		return book_page;
	}
	public void setBook_page(String book_page) {
		this.book_page = book_page;
	}
	public String getBook_format() {
		return book_format;
	}
	public void setBook_format(String book_format) {
		this.book_format = book_format;
	}
	public String getBook_language() {
		return book_language;
	}
	public void setBook_language(String book_language) {
		this.book_language = book_language;
	}
	public String getBook_goods() {
		return book_goods;
	}
	public void setBook_goods(String book_goods) {
		this.book_goods = book_goods;
	}
	public String getBook_publish_time() {
		return book_publish_time;
	}
	public void setBook_publish_time(String book_publish_time) {
		this.book_publish_time = book_publish_time;
	}
	public String getBook_congshu() {
		return book_congshu;
	}
	public void setBook_congshu(String book_congshu) {
		this.book_congshu = book_congshu;
	}
	public String getPrintface() {
		return printface;
	}
	public void setPrintface(String printface) {
		this.printface = printface;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "category_id")
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "school_id")
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	
	/**
	 * 2011-6-3
	 * @return
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "original_school_id")
	public School getOriginal_school() {
		return original_school;
	}
	public void setOriginal_school(School original_school) {
		this.original_school = original_school;
	}

	
}
