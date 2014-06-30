/**
 * 
 */
package cn.qtone.qtoneframework.web.view;

/**
 * 对一个实体的操作有多个页面，其中新增、修改、阅读三个页面的元素一样， <br>
 * 只是只读、可编辑等状态太同。 此类用于代表一个状态
 * 
 * @author 林子龙
 * 
 */
public class StateStyle {

	private static String DISABLE = "disabled=disabled";
	private static String READONLY = "readonly=readonly";
	private static String DISPLAY = "style=display:none";

	public enum State {
		新增修改, 阅读
	}

	private State state;

	public State getState() {
		return this.state;
	}

	public StateStyle(State state) {
		this.state = state;
	}

	private Boolean isRead() {
		return this.state.equals(State.阅读);
	}

	public String getDisable() {
		if (isRead()) {
			return DISABLE;
		} else {
			return "";
		}
	}

	public String getReadonly() {
		if (isRead()) {
			return READONLY;
		} else {
			return "";
		}
	}

	public String getDisplay() {
		if (isRead()) {
			return DISPLAY;
		} else {
			return "";
		}
	}
}
