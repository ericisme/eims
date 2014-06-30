package cn.qtone.crda.chargeback.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import cn.qtone.common.simplemvc.dao.HibernateSimpleDao;
import cn.qtone.crda.chargeback.dao.ChargeBackDao;
import cn.qtone.crda.chargeback.dao.UpdateChargeBackFeeStatusContionVO;
import cn.qtone.crda.chargeback.domain.Chargeback;
import cn.qtone.crda.chargeback.qvo.ChargebackQVO;
import cn.qtone.qtoneframework.web.view.poi.ExcelExtractor;
import cn.qtone.qtoneframework.web.view.poi.ExcelUtils;

/**
 * 扣费记录 - 扣费记录
 *
 * @author 贺少辉
 * @version 1.0
 */
public class ChargebackService extends HibernateSimpleDao<Chargeback> {

	ChargeBackDao chargeBackDao;
	
	public void setChargeBackDao(ChargeBackDao chargeBackDao) {
		this.chargeBackDao = chargeBackDao;
	}

	/**
	 * 根据年、月获取扣费记录
	 * @param qvo
	 * @param queryFlag (如果是生成则传入create)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Chargeback>  queryChargebackByQVO(ChargebackQVO qvo){
		Criteria criteria = this.createCriteria(Chargeback.class);
		criteria.add(Restrictions.eq("year",qvo.getYear()));
		criteria.add(Restrictions.eq("month",qvo.getMonth()));
		return criteria.list();
	}
	
	/**
	 * 导出扣费清单，给移动扣费.
	 */
	public HSSFWorkbook getChargebackExport(List<Chargeback> list, String templatePath,String yearMonth) throws FileNotFoundException,
			IOException {
		HSSFWorkbook wb = ExcelUtils.loadWorkbook(templatePath);
		HSSFSheet sheet = wb.getSheetAt(0);
		ExcelUtils.setValue(sheet, 0, 0, yearMonth+"扣费清单");
		int row = 2;
		for (Chargeback chargeback : list) {
			ExcelUtils.setValue(sheet, row, 0, chargeback.getMobile());
			ExcelUtils.setValue(sheet, row, 1, chargeback.getChargeFees()+"");
			row++;
		}
		return wb;
	}
	
	/**
	 * 获取模板中的数据对象.
	 */
	public List<Chargeback> getImportChargeback(String filepath) throws FileNotFoundException, IOException,
			SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		ExcelExtractor<Chargeback> extractor = new ExcelExtractor<Chargeback>();
		extractor.setExcel(filepath);
		extractor.setInitPosition(2, 0);
		extractor.setValueType(Chargeback.class);
		extractor.setErrorMsgColumn(3);
		extractor.setMethodNames(new String[] { "setMobile", "setChargeFees", "setChargeback"});
		List<Chargeback> chargebacks = extractor.getResult();
		return chargebacks;
	}
	
	/**
	 * 修改扣费状态
	 */
	public void updateChargeBackFeeStatus(UpdateChargeBackFeeStatusContionVO vo){
		this.chargeBackDao.updateChargeBackFeeStatus(vo);
	}
	
	/**
	 * 修改订阅表中的扣费状态.
	 */
	public void updateSubscribeChargeBack(){
		this.chargeBackDao.updateSubscribeChargeBack();
	}
	
	/**
	 * 修改订阅表中的扣费次数.<br>
	 */
	public void updateSubscribeChargebackTimes(int status){
		this.chargeBackDao.updateSubscribeChargebackTimes(status);
	}
}