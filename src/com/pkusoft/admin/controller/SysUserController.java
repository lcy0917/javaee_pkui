package com.pkusoft.admin.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pkusoft.admin.model.SysRole;
import com.pkusoft.admin.model.SysRoleUser;
import com.pkusoft.admin.model.SysUser;
import com.pkusoft.admin.service.SysRoleService;
import com.pkusoft.admin.service.SysUserService;
import com.pkusoft.common.constants.AdminFunctionId;
import com.pkusoft.common.constants.AdminUrlRecource;
import com.pkusoft.common.util.LogUtils;
import com.pkusoft.framework.controller.BaseController;
import com.pkusoft.framework.exception.BizException;
import com.pkusoft.framework.model.Criteria;
import com.pkusoft.framework.model.GridResult;
import com.pkusoft.framework.model.JsonResult;
import com.pkusoft.framework.model.Pager;
import com.pkusoft.framework.util.WebUtils;

/**
 * 用户管理控制器
 * 
 */
@Controller
public class SysUserController extends BaseController {

	/** 日志句柄 */
	private static Logger logger = LoggerFactory.getLogger(SysUserController.class);
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	/**
	 * 用户列表页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_LIST)
	public ModelAndView sysUserList(ModelAndView model) {
		model.setViewName(AdminUrlRecource.SYS_USER_LIST);
		return model;
	}

	/**
	 * 用户列表数据
	 * 
	 * @param sysUser
	 * @param pager
	 * @return
	 */
	@RequestMapping(value = AdminUrlRecource.SYS_USER_LIST_DATA)
	@ResponseBody
	public GridResult sysUserListData(String txtQuery) {
		try {
			Criteria<?> criteria = WebUtils.toCriteria(txtQuery);
			List<SysUser> list = sysUserService.getSysUserList(criteria);
			int count = criteria.getPager() == null ? list.size() : criteria.getPager().getTotalRecords();
			return new GridResult(true, list, count);
		} catch (Exception e) {
			logger.error("查询用户列表数据出错", e);
			return new GridResult(false, null);
		}
	}

	/**
	 * 用户管理表单页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_FORM)
	public ModelAndView sysUserForm(Long userId, ModelAndView model) {
		try {
			// 获取提供用户选择的角色列表
			List<SysRole> roleList = sysRoleService.getAllSysRoleList();
			model.addObject("roleList", roleList);
			
			//如果是修改
			if (userId != null) {
				//用户基本信息
				SysUser sysUser = sysUserService.get(userId);
				//用户角色信息
				List<SysRoleUser> checkedRoleList = sysUserService.getSysRoleUserListByUserId(userId);
				
				model.addObject("sysUser", sysUser);
				model.addObject("checkedRoleList", checkedRoleList);
			}
			
			model.setViewName(AdminUrlRecource.SYS_USER_FORM);
			return model;
		} catch (Exception e) {
			logger.error("访问用户管理表单页面出错", e);
			throw new RuntimeException(this.getMessage(e));
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param sysUser
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_SAVE)
	@ResponseBody
	public JsonResult sysUserSave(SysUser sysUser, Long[] roleId) {
		try {
			if(sysUser.getUserId() == null){
				sysUserService.insertSysUser(sysUser, roleId);
				LogUtils.log(AdminFunctionId.SYS_USER_SAVE, "新增用户成功");
			}else{
				sysUserService.updateSysUser(sysUser, roleId);
				LogUtils.log(AdminFunctionId.SYS_USER_SAVE, "修改用户成功");
			}
			
			
			return new JsonResult(true);
		} catch (BizException e) {
			return new JsonResult(false, e.getMessage());
		} catch (Exception e) {
			logger.error("保存用户信息出错", e);
			LogUtils.log(AdminFunctionId.SYS_USER_SAVE, "保存用户信息失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}

	/**
	 * 删除用户
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_DELETE)
	@ResponseBody
	public JsonResult sysUserDelete(Long[] userId) {
		try {
			sysUserService.deleteSysUser(userId);
			LogUtils.log(AdminFunctionId.SYS_USER_DELETE, "删除用户成功");
			return new JsonResult(true);
		} catch (Exception e) {
			logger.error("删除单位信息出错", e);
			LogUtils.log(AdminFunctionId.SYS_USER_DELETE, "删除用户失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}
	
	/**
	 * 高级查询页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_ADV_QUERY)
	public ModelAndView sysUserAdvQuery(ModelAndView model) {
		model.setViewName(AdminUrlRecource.SYS_USER_ADV_QUERY);
		return model;
	}
	
	
	/**
	 * 重置密码
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_RESET_PWD)
	@ResponseBody
	public JsonResult sysUserResetPwd(Long[] userId) {
		try {
			sysUserService.resetPassword(userId);
			LogUtils.log(AdminFunctionId.SYS_USER_RESET_PWD, "重置用户密码成功");
			return new JsonResult(true);
		} catch (Exception e) {
			logger.error("重置密码出错", e);
			LogUtils.log(AdminFunctionId.SYS_USER_RESET_PWD, "重置用户密码失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}
	
	/**
	 * 按角色查用户数据
	 * 
	 * @param roleId
	 * @param roleIdOper
	 * @param userName
	 * @return pager
	 */
	@RequestMapping(value = AdminUrlRecource.SYS_ROLE_USER_LIST_DATA)
	@ResponseBody
	public GridResult getSysUserListByRole(Long roleId,  String roleIdOper, String userName, Pager pager) {
		try {
			List<SysUser> list = sysUserService.getSysUserListByRole(roleId, roleIdOper, userName, pager);
			return new GridResult(true, list, pager.getTotalRecords());
		} catch (Exception e) {
			logger.error("查询列表数据出错", e);
			return new GridResult(false, null);
		}
	}
	
	/**
	 * 用户唯一性验证
	 * 
	 * @param userId
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	@RequestMapping(AdminUrlRecource.SYS_USER_CKECK_INFO)
	@ResponseBody
	public JsonResult sysDeptCheckInfo(Long userId, String fieldName, String fieldValue) {
		try {
			fieldValue = java.net.URLDecoder.decode(fieldValue, "utf-8");
			LogUtils.log(AdminFunctionId.SYS_USER_CKECK_INFO, "用户信息验证成功");
			return new JsonResult(sysUserService.checkUserInfo(userId, fieldName, fieldValue));
		} catch (Exception e) {
			logger.error("用户唯一性验证出错", e);
			LogUtils.log(AdminFunctionId.SYS_USER_CKECK_INFO, "用户信息验证失败");
			return new JsonResult(false, this.getMessage(e));
		}
	}
}
