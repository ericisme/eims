package cn.qtone.eims.kjpz.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.web.servlet.ModelAndView;

import cn.qtone.common.mvc.view.spring.AjaxView;
import cn.qtone.common.simplemvc.controller.SimpleManageController;
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
		pz.addFls(fls);
		//map.put(getDomainName(), doNewDomain());
		map.put("pz", pz);
		List<Kmgl> kmgl_list = kmglService.createCriteria(Kmgl.class).add(Expression.eq("zt", 1)).addOrder(Order.asc("kmdh")).list();
		map.put("kmgl_list", kmgl_list);
		return new ModelAndView(getEditPage(), map);
	}
	
	/**
	 * 通用的编辑对象的界面.
	 */
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		map.put(getDomainName(), doGetDomain(request));
		List<Kmgl> kmgl_list = kmglService.createCriteria(Kmgl.class).add(Expression.eq("zt", 1)).addOrder(Order.asc("kmdh")).list();
		map.put("kmgl_list", kmgl_list);
		return new ModelAndView(getEditPage(), map);
	}
	
	/**
	 * 通用的新增/修改对象的保存.
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Pz pz = (Pz)getCommandObject(request, getDomainClass());
		System.out.println("pz_id:"+pz.getId());
		//request.getParameter("fl_id_array").split(",")
		String[] fl_id_str_array = request.getParameter("fl_id_array").split(",");
		String[] fl_zy_array = request.getParameter("fl_zy_array").split(",");
		String[] fl_kmdh_array = request.getParameter("fl_kmdh_array").split(",");
		String[] fl_jfje_str_array = request.getParameter("fl_jfje_array").split(",");
		String[] fl_dfje_str_array = request.getParameter("fl_dfje_array").split(",");
		System.out.println("request.getParameter(\"fl_id_array\"):"+request.getParameter("fl_id_array"));
		System.out.println("fl_id_str_array:"+fl_id_str_array.length);
		System.out.println("fl_zy_array:"+fl_zy_array.length);		
		System.out.println("fl_kmdh_array:"+fl_kmdh_array.length);		
		System.out.println("fl_jfje_str_array:"+fl_jfje_str_array.length);		
		System.out.println("fl_dfje_str_array:"+fl_dfje_str_array.length);		
		
		List<Fl> fls = new ArrayList<Fl>();
//		if(pz.getId()>0){
//			//List<Fl> _fl = flService.createCriteria(Fl.class).add(Expression.eq("pz.id", pz.getId())).list();
//			List<Fl> _fl =pzService.get(pz.getId()).getFlList();
//			System.out.println("_fl.size():"+_fl.size());	
//			for(Fl fl : _fl){
//				System.out.println("fl_id:"+fl.getId()+" fl_zy:"+fl.getZy());
//			}
//		}
		System.out.println("aaa:");	
		for(int i = 0; i < fl_kmdh_array.length; i++){
			if(!"".equals(StringUtil.trim(fl_kmdh_array[i]))){
				Fl fl = new Fl();
				fl.setId(fl_id_str_array[i].equals("null")?null:Integer.parseInt(fl_id_str_array[i]));
				fl.setZy(StringUtil.trim(fl_zy_array[i]));
				fl.setKmgl(kmglService.findUniqueBy("kmdh", fl_kmdh_array[i]));
				fl.setJfje(fl_jfje_str_array[i].equals("null")?null:(new BigDecimal(fl_jfje_str_array[i])));
				fl.setDfje(fl_dfje_str_array[i].equals("null")?null:(new BigDecimal(fl_dfje_str_array[i])));
				fl.setPz(pz);
				fls.add(fl);
			}
		}
		pz.addFls(fls);
		//Pz pz = (Pz)getCommandObject(request, getDomainClass());
		if(isDomainIdBlank(request)){
			getDomainService().save(pz);
		}else{
			getDomainService().saveOrUpdate(pz);
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
