package cn.qtone.library.book.domain;

import cn.qtone.library.school.domain.School;

public class ErrorBook {
	private Integer line;
	private String book_name;//图书名称*
	private String category_code;//*分类号*
	private String category_name;//分类名*
	private String book_price;//*定价*
	private String book_place;//*存放位置*
	private School school;// shool_id;//*归属单位*
	private String book_isbn;//标准ISBN
	private String book_author;//作者
	private String book_publish;//出版社
	private String book_publish_place;//出版地
	private String book_publish_year;//出版年份
	private String book_storage_time;//入库时间
	private String book_content;//内容简介
	private String book_private;//密级
	private String book_page;//页码
	private String book_format;//开本
	private String book_language;//语种
	private String book_goods;//配送物品
	private String book_publish_time;//版次
	private String book_congshu;//所属丛书
	private String printface;//印刷版面
	private String content;//备注信息
	private String status;//状态
	private String errorReason;//报错原因
	
	public String getErrorReason() {
		return errorReason;
	}
	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
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
	public String getBook_price() {
		return book_price;
	}
	public void setBook_price(String book_price) {
		this.book_price = book_price;
	}
	public String getBook_place() {
		return book_place;
	}
	public void setBook_place(String book_place) {
		this.book_place = book_place;
	}
	public School getSchool() {
		return school;
	}
	public void setSchool(School school) {
		this.school = school;
	}
	public String getBook_isbn() {
		return book_isbn;
	}
	public void setBook_isbn(String book_isbn) {
		this.book_isbn = book_isbn;
	}
	public String getBook_author() {
		return book_author;
	}
	public void setBook_author(String book_author) {
		this.book_author = book_author;
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
	public String getBook_content() {
		return book_content;
	}
	public void setBook_content(String book_content) {
		this.book_content = book_content;
	}
	public String getBook_private() {
		return book_private;
	}
	public void setBook_private(String book_private) {
		this.book_private = book_private;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getLine() {
		return line;
	}
	public void setLine(Integer line) {
		this.line = line;
	}

}
