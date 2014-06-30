package cn.qtone.library.city.controller;

import cn.qtone.common.simplemvc.controller.SimpleController;
import cn.qtone.library.city.service.CityService;

/**
 * 城市管理控制器类<br>
 *
 * @author 贺少辉
 * @version 1.0
 */
public class CityController extends SimpleController{
 
	CityService cityService;

	public void setCityService(CityService cityService) {
		this.cityService = cityService;
	}
}