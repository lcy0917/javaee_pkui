package com.pkusoft.admin.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.pkusoft.admin.model.SysLog;
import com.pkusoft.admin.service.SysLogService;
import com.pkusoft.common.constants.AdminFunctionId;
import com.pkusoft.common.constants.AdminUrlRecource;
import com.pkusoft.common.util.LogUtils;
import com.pkusoft.framework.controller.BaseController;
import com.pkusoft.framework.model.GridResult;
import com.pkusoft.framework.model.JsonResult;
import com.pkusoft.framework.model.Pager;

/**
 * 日志页面控制器
 * @author caizh
 */
@Controller
public class SysLogController extends BaseController {
	/** 日志句柄 */
	private static Logger logger = LoggerFactory.getLogger(SysLogController.class);

	/** 日志服务类 */
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 日志列表页面
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_LIST)
	public ModelAndView sysLogList(ModelAndView model) {
		model.setViewName(AdminUrlRecource.SYS_LOG_LIST);
		return model;
	}
	
	/**
	 * 日志列表数据查询
	 * @param sysLog
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = AdminUrlRecource.SYS_LOG_LIST_DATA)
	@ResponseBody
	public GridResult sysLogListData(SysLog sysLog,Pager pager) {
		try {
			List<SysLog> list = sysLogService.getSysLogList(sysLog, pager);
			return new GridResult(true, list, pager.getTotalRecords());
		} catch (Exception e) {
			logger.error("查询列表数据出错", e);
			return new GridResult(false, null);
		}
	}
	
	/**
	 * 日志表单页面
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_FORM)
	public ModelAndView sysLogForm(java.lang.Long logId,ModelAndView model) {
		try {
			if(logId != null){
				SysLog sysLog = sysLogService.get(logId);
				model.addObject("sysLog", sysLog);
			}
			model.setViewName(AdminUrlRecource.SYS_LOG_FORM);
			return model;
		} catch (Exception e) {
			logger.error("访问表单页面出错", e);
			throw new RuntimeException(this.getMessage(e));
		}
	}
	
	/**
	 * 日志高级查询页面
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_ADV_QUERY)
	public ModelAndView sysLogAdvQuery(ModelAndView model) {
		model.setViewName(AdminUrlRecource.SYS_LOG_ADV_QUERY);
		return model;
	}

	/**
	 * 删除日志信息(按指定时间)
	 * 
	 * @param month
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_DELETE)
	@ResponseBody
	public JsonResult sysLogDelete(int month) {
		try {
			Date beginDate = new Date();  //当前日期
			Date endDate = new Date();		//减去指定时间后的日期
			Long dayNum = (endDate.getTime()/(1000*60*60*24))-endDate.getDay()+1;
			endDate.setTime(dayNum*(1000*60*60*24));
			switch (month) {
			case 1:
				break;
			case 3:
				endDate.setMonth(endDate.getMonth()-month);				
				break;
			case 6:
				endDate.setMonth(endDate.getMonth()-month);
				break;
			case 12:
				endDate.setMonth(0);
				break;
			}
			sysLogService.deleteSysLog(beginDate,endDate);
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE, "时间范围条件日志删除成功");
			return new JsonResult(true);
		} catch (Exception e) {
			logger.error("删除信息出错", e);
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE, "时间范围条件日志删除失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}

	/**
	 * 删除日志信息（按日志编号）
	 * @param logId
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_DELETE_BY_IDS)
	@ResponseBody
	public JsonResult deleteSysLogByIds(Long[] logId) {
		try {
			sysLogService.deleteSysLogByIds(logId);
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE, "编号条件删除日志成功");
			return new JsonResult(true);
		} catch (Exception e) {
			logger.error("删除信息出错", e);
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE, "编号条件删除日志失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}	

	/**
	 * 删除全部日志信息
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_LOG_DELETE_All)
	@ResponseBody
	public JsonResult sysLAll() {
		try {
			sysLogService.deleteAllSysLog();
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE_All, "删除全部日志成功");
			return new JsonResult(true);
		} catch (Exception e) {
			logger.error("删除信息出错", e);
			LogUtils.log(AdminFunctionId.SYS_LOG_DELETE_All, "删除全部日志失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}

}