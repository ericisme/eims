package cn.qtone.library.borrow.domain;

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

import cn.qtone.common.components.syspurview.core.user.domain.IUser;
import cn.qtone.library.XxptUtil;
import cn.qtone.library.book.domain.Book;

@Entity
@Table(name = "library_borrow")
public class Borrow {
	
	
	private Integer id;
	private Date borrow_time;//借阅时间
	private Date return_time;//归还时间
	private Integer status;//0可借状态；1过期，2丢失；3破损
	private Double money;   //交还罚金
	private String content; //备注
	private Book book;        //book_id;关联借阅的书
	private IUser borrowUser;//user_id；关联借阅用户
	private IUser operaUser; //opera_id;关联操作人
	private Integer book_status;//0库中；1外借
	private Integer limit_day;//可借天数
	
	private String should_return;
	@Transient
	public String getShould_return() {
		return this.should_return =XxptUtil.getReturnDate(borrow_time, limit_day);
	}
	@Transient
	public Integer expiredDay(){
		return (int)XxptUtil.expiredDay(borrow_time)-limit_day;
	}
	public Integer getLimit_day() {
		return limit_day;
	}
	public void setLimit_day(Integer limit_day) {
		this.limit_day = limit_day;
	}
	public Integer getBook_status() {
		return book_status;
	}
	public void setBook_status(Integer book_status) {
		this.book_status = book_status;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getBorrow_time() {
		return borrow_time;
	}
	public void setBorrow_time(Date borrow_time) {
		this.borrow_time = borrow_time;
	}
	public Date getReturn_time() {
		return return_time;
	}
	public void setReturn_time(Date return_time) {
		this.return_time = return_time;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "book_id")
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "user_id")
	public IUser getBorrowUser() {
		return borrowUser;
	}
	public void setBorrowUser(IUser borrowUser) {
		this.borrowUser = borrowUser;
	}
	
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "opera_id")
	public IUser getOperaUser() {
		return operaUser;
	}
	public void setOperaUser(IUser operaUser) {
		this.operaUser = operaUser;
	}

}
