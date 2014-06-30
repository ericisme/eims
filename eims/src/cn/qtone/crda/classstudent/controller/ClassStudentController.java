package cn.qtone.crda.classstudent.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.dao.Page;
import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.crda.classstudent.domain.ClassStudent;
import cn.qtone.crda.classstudent.qvo.ClassStudentQVO;
import cn.qtone.crda.classstudent.qvo.ClassStudentQVOUtil;
import cn.qtone.crda.classstudent.service.ClassStudentService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;
import cn.qtone.library.agency.service.AgencyService;

/**
 * 成人档案袋
 * 
 * @author 王培森
 * @version 1.0
 */
public class ClassStudentController extends SimpleManageController<ClassStudent, ClassStudentService> {

	ClassStudentService classStudentService;
	AgencyService agencyService;
	
	@Override
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Map<String, Object> map = this.getMapWithUser(request);
		map.put("agencySelectHtml", this.agencyService.getAgencyOptionHtmlFromCity(ServletUtil.removeSpace(request,
				"city_id"), 18));
		return new ModelAndView(getIndexPage(), map);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> map = this.getMapWithUser(request);
		ClassStudentQVO qvo = ClassStudentQVOUtil.getClassStudentQVO(request);
		qvo.setStartPage(this.getCurrentPage(request));
		qvo.setPageSize(Page.DEFAULT_PAGE_SIZE);
		Page page = this.classStudentService.pageQueryClassStudent(qvo);
		page.setPaginate(this.getAjaxPage(request, qvo.getStartPage(), page, "classStudentJump"));
		map.put("page", page);
		map.put("isValidCount", this.classStudentService.queryIsValidCount());
		return new ModelAndView(this.getListPage(), map);
	}
	
	@SuppressWarnings("unchecked")
	public ModelAndView validClassStudent(HttpServletRequest request, HttpServletResponse response) {
		
		ClassStudentQVO qvo = ClassStudentQVOUtil.getClassStudentQVO(request);
		this.classStudentService.updateClassStudentEffective(qvo);
		AjaxView view = new AjaxView(true, "匹配成人档案袋学生信息成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	/**
	 * 导出无效数据
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView downloadClassStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ClassStudentQVO qvo = ClassStudentQVOUtil.getClassStudentQVO(request);
		List<ClassStudent> list = this.classStudentService.noPageQueryClassStudent(qvo);
		
		if (list.size() > 0) {
			HSSFWorkbook workbook = this.classStudentService.getClassStudentExport(list,
					request.getSession().getServletContext().getRealPath("/")
							+ "/template/classStudent_export_template.xls");
			try {
				ExcelUtils.download(response, workbook, "无效学生数据表.xls");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			String msg ="<script>alert('根据所选条件,导出条目为0,请重新选择！');window.history.back();</script>";
			ServletOutputStream out=response.getOutputStream();
			out.print(msg);
			out.close();
		}
		return null;
	}
	
	public void setClassStudentService(ClassStudentService classStudentService) {
		this.classStudentService = classStudentService;
	}

	public void setAgencyService(AgencyService agencyService) {
		this.agencyService = agencyService;
	}
}