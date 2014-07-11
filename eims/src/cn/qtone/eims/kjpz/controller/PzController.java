package cn.qtone.eims.kjpz.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
import cn.qtone.common.utils.base.DateUtil;
import cn.qtone.common.utils.base.StringUtil;
import cn.qtone.eims.kjpz.domain.Fl;
import cn.qtone.eims.kjpz.domain.Kmgl;
import cn.qtone.eims.kjpz.domain.Pz;
import cn.qtone.eims.kjpz.service.FlService;
import cn.qtone.eims.kjpz.service.KmglService;
import cn.qtone.eims.kjpz.service.PzService;
import cn.qtone.qtoneframework.web.servlet.ServletUtil;

public class PzController extends SimpleManageController<Pz, PzService>{

	private PzService pzService;
	private FlService flService;
	private KmglService kmglService;
	
	/**
	 * 通用的新增对象的界面.
	 */
	public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		Pz pz = new Pz();
		List<Fl> fls = new ArrayList<Fl>();
		Fl fl1 = new Fl();
		Fl fl2 = new Fl();
		fls.add(fl1);
		fls.add(fl2);
		pz.setFlList(fls);
		//map.put(getDomainName(), doNewDomain());
		Integer last_pzh = (Integer)pzService.createCriteria(Pz.class).setProjection(Projections.max("pzh")).uniqueResult();
		if(last_pzh==null){
			last_pzh=0;
		}
		System.out.println("last pzh:"+last_pzh);
		map.put("pz", pz);
		map.put("last_pzh", (last_pzh+1));
		map.put("today", DateUtil.formatDate(new Date(), "yyyy-MM-dd"));
		List<Kmgl> kmgl_list = kmglService.createCriteria(Kmgl.class).add(Expression.eq("zt", 1)).addOrder(Order.asc("kmdh")).list();
		map.put("kmgl_list", kmgl_list);
		return new ModelAndView(getEditPage(), map);
	}
	
	/**
	 * 通用的编辑对象的界面.
	 */
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		Pz pz = doGetDomain(request);
		List<Fl> fls = new ArrayList<Fl>();
		fls = flService.createCriteria(Fl.class).add(Expression.eq("pz.id", pz.getId())).list();
		if(fls.size()==0){
			fls.add(new Fl());
			fls.add(new Fl());
		}
		if(fls.size()==1){
			fls.add(new Fl());
		}
		pz.setFlList(fls);
		map.put(getDomainName(), pz);
		List<Kmgl> kmgl_list = kmglService.createCriteria(Kmgl.class).add(Expression.eq("zt", 1)).addOrder(Order.asc("kmdh")).list();
		map.put("kmgl_list", kmgl_list);
		return new ModelAndView(getEditPage(), map);
	}
	
	/**
	 * 通用的只读显示对象细节的界面.
	 */
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		Pz pz = doGetDomain(request);
		List<Fl> fls = new ArrayList<Fl>();
		fls = flService.createCriteria(Fl.class).add(Expression.eq("pz.id", pz.getId())).list();
		if(fls.size()==0){
			fls.add(new Fl());
			fls.add(new Fl());
			fls.add(new Fl());
			fls.add(new Fl());
		}
		if(fls.size()==1){
			fls.add(new Fl());
			fls.add(new Fl());
			fls.add(new Fl());
		}
		if(fls.size()==2){
			fls.add(new Fl());
			fls.add(new Fl());
		}
		if(fls.size()==3){
			fls.add(new Fl());
		}
		pz.setFlList(fls);
		map.put(getDomainName(), pz);
		List<Kmgl> kmgl_list = kmglService.createCriteria(Kmgl.class).add(Expression.eq("zt", 1)).addOrder(Order.asc("kmdh")).list();
		map.put("kmgl_list", kmgl_list);
		return new ModelAndView(getShowPage(), map);
	}
	
	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Pz pz = (Pz)getCommandObject(request, getDomainClass());
		String[] fl_id_str_array = request.getParameter("fl_id_array").split(",");
		String[] fl_zy_array = request.getParameter("fl_zy_array").split(",");
		String[] fl_kmdh_array = request.getParameter("fl_kmdh_array").split(",");
		String[] fl_jfje_str_array = request.getParameter("fl_jfje_array").split(",");
		String[] fl_dfje_str_array = request.getParameter("fl_dfje_array").split(",");
		//保证pz
		if(isDomainIdBlank(request)){
			getDomainService().save(pz);
		}else{
			getDomainService().saveOrUpdate(pz);
		}		
		//新增/更新fl list
		List<Fl> fls_save_or_update_list = new ArrayList<Fl>();		
		for(int i = 0; i < fl_kmdh_array.length; i++){
			if(!"".equals(StringUtil.trim(fl_kmdh_array[i]))){
				Fl fl = new Fl();
				fl.setId(fl_id_str_array[i].equals("null")?null:Integer.parseInt(fl_id_str_array[i]));
				fl.setRq(pz.getRq());
				fl.setZy(StringUtil.trim(fl_zy_array[i]));
				fl.setKmgl(kmglService.findUniqueBy("kmdh", fl_kmdh_array[i]));
				fl.setJfje(fl_jfje_str_array[i].equals("null")?null:(new BigDecimal(fl_jfje_str_array[i])));
				fl.setDfje(fl_dfje_str_array[i].equals("null")?null:(new BigDecimal(fl_dfje_str_array[i])));
				fl.setLx(1);
				fl.setPz(pz);
				fls_save_or_update_list.add(fl);
			}
		}
		//删除fl list
		List<Integer> fls_id_remove_list = new ArrayList<Integer>();
		if(pz.getId()>0){
			List<Integer> _fl_id = flService.createCriteria(Fl.class).add(Expression.eq("pz.id", pz.getId())).setProjection(Projections.property("id")).list();
			for(Integer fl_id : _fl_id){		
				boolean deleteOrNot = true;
				for(Fl fl_save_or_update : fls_save_or_update_list){
					if(fl_id.equals(fl_save_or_update.getId())){
						deleteOrNot = false;
					}
				}
				if(deleteOrNot){
					fls_id_remove_list.add(fl_id);
				}								
			}
		}
		for(Fl fl : fls_save_or_update_list){
			if(fl.getId()==null){
				flService.save(fl);
			}else{
				flService.saveOrUpdate(fl);		
			}
		}
		for(Integer fl_id : fls_id_remove_list){			
			flService.removeById(fl_id);
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	/**
	 * 通用的删除单个或多个对象.
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] idStrs = request.getParameterValues(idName);
		if (log.isInfoEnabled()) {
			log.info("要删除的对象ID为：" + StringUtil.join(idStrs, ","));
		}
		for(String idStr : idStrs){
			if(idStr.length() > 0){
				getDomainService().removeById((Serializable) ConvertUtils.convert(idStr, idClass));
				List<Integer> _fl_id = flService.createCriteria(Fl.class).add(Expression.eq("pz.id", ConvertUtils.convert(idStr, idClass))).setProjection(Projections.property("id")).list();
				for(Integer fl_id : _fl_id){
					flService.removeById(fl_id);
				}
			}
		}
		AjaxView view = new AjaxView(true, "操作成功！");
		view.setProperty("refresh", true);
		return new ModelAndView(view);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	private boolean isDomainIdBlank(HttpServletRequest request) {
		String idString = request.getParameter(idName);
		if (StringUtils.isBlank(idString)) {
			return true;
		}
		return false;
	}

	public PzService getPzService() {
		return pzService;
	}

	public void setPzService(PzService pzService) {
		this.pzService = pzService;
	}

	public FlService getFlService() {
		return flService;
	}

	public void setFlService(FlService flService) {
		this.flService = flService;
	}

	public KmglService getKmglService() {
		return kmglService;
	}

	public void setKmglService(KmglService kmglService) {
		this.kmglService = kmglService;
	}
	
	
}
