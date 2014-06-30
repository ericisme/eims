package cn.qtone.common.component.frameupload.servlet;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import cn.qtone.common.simplemvc.controller.SimpleController;

/**
 * @author sutk.<br>
 *  附件处理.
 */
public class AttachmentServlet extends SimpleController{

	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = this.getMapWithUser(request);
		
		
		return new ModelAndView("", map);
	}

}
