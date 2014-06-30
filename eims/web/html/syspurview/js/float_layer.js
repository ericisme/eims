// 页面浮动层JS控制对象

function FloatLayer(){};
FloatLayer.config = { top : 1,id : '',times : 10 };
FloatLayer._oldPos = null;
FloatLayer._tips = null;
FloatLayer.prototype = {
	show : function(/*object*/config) {
		if (config && typeof(config) == 'object') this._setConfig(config);
		if (!FloatLayer.config.id) return;
		FloatLayer._tips = document.getElementById(FloatLayer.config.id);
		if (!FloatLayer._tips) return;
  		setInterval(this.start, FloatLayer.config.times);
	},
	
	start : function() {
		var position = 0;
		if (window.innerHeight) position = window.pageYOffset;
  		else if (document.documentElement && document.documentElement.scrollTop) {
    		position = document.documentElement.scrollTop;
  		} else if (document.body) {
    		position = document.body.scrollTop;
  		}
 		position = position - FloatLayer._tips.offsetTop + FloatLayer.config.top;
  		position = FloatLayer._tips.offsetTop + position/3;
  		if (position < this._oldPos) position = FloatLayer.config.top;
  		if (position != this._oldPos) {
    		FloatLayer._tips.style.top = position + 'px';
    		FloatLayer.config.times = 10;
  		}
  		this._oldPos = position;
	},
	
	_setConfig : function(/*object*/ config) {
		for (var prop in FloatLayer.config) {
			if (config[prop]) FloatLayer.config[prop] = config[prop];
		}
	}
};