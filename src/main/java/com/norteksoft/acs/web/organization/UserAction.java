package com.norteksoft.acs.web.organization;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.norteksoft.acs.base.utils.ExportUserInfo;
import com.norteksoft.acs.base.web.struts2.CRUDActionSupport;
import com.norteksoft.acs.entity.authorization.BranchAuthority;
import com.norteksoft.acs.entity.authorization.BusinessSystem;
import com.norteksoft.acs.entity.authorization.Role;
import com.norteksoft.acs.entity.organization.Department;
import com.norteksoft.acs.entity.organization.User;
import com.norteksoft.acs.entity.organization.UserInfo;
import com.norteksoft.acs.entity.organization.Workgroup;
import com.norteksoft.acs.service.AcsUtils;
import com.norteksoft.acs.service.authority.RecordLockerManager;
import com.norteksoft.acs.service.authorization.BranchAuthorityManager;
import com.norteksoft.acs.service.authorization.RoleManager;
import com.norteksoft.acs.service.organization.CompanyManager;
import com.norteksoft.acs.service.organization.DepartmentManager;
import com.norteksoft.acs.service.organization.ImportUserManager;
import com.norteksoft.acs.service.organization.UserInfoManager;
import com.norteksoft.acs.service.organization.UserManager;
import com.norteksoft.acs.service.sale.SubsciberManager;
import com.norteksoft.acs.web.eunms.AddOrRomoveState;
import com.norteksoft.product.api.ApiFactory;
import com.norteksoft.product.orm.Page;
import com.norteksoft.product.util.CollectionUtils;
import com.norteksoft.product.util.ContextUtils;
import com.norteksoft.product.util.Md5;
import com.norteksoft.product.util.PageUtils;
import com.norteksoft.product.util.SystemUrls;
import com.norteksoft.product.util.freemarker.TagUtil;
import com.norteksoft.product.web.struts2.Struts2Utils;


@SuppressWarnings("deprecation")
@Namespace("/organization")
@ParentPackage("default")
@Results( {
		@Result(name = CRUDActionSupport.RELOAD, location = "user?synchronousLdapMessage=${synchronousLdapMessage}&message=${message}&did=${did}", type = "redirectAction"),
		@Result(name = "deleteList", location = "user!deleteList", type = "redirectAction"),
		@Result(name = "redirect_url", location = "${redirectUrl}", type = "redirect")})
public class UserAction extends CRUDActionSupport<UserInfo> {

	private static final long serialVersionUID = 4814560124772644966L;
	private Page<User> page = new Page<User>(0, true);// 每页5项，自动查询计算总页数.
	private Page<Department> pageUserToDepart = new Page<Department>(20, true);// 每页5项，自动查询计算总页数.
	private Page<Workgroup> pageUserToWork = new Page<Workgroup>(20, true);// 每页5项，自动查询计算总页数.
	private UserInfoManager userInfoManager;
	private UserManager userManager;
	private DepartmentManager departmentManager;
	private List<UserInfo> allUser;
	private List<Department> allDepartment;
	private List<Workgroup> allWorkGroup;
	private List<Long> checkedWorkGroupIds;
	private List<Long> checkedDepartmentIds;
	private Long userInfoIds;
	private User user;
	private UserInfo entity;
	private List<UserInfo> userInfos;
	private Long id;
	private String passWord_CreateTime;
	private String ids;
	private List<Long> workGroupIds;
	private List<Long> departmentIds;
	private String dids;
	private Long userId;
	private List<Role> allRoles;
	private List<Long> roleIds;
	private List<Long> checkedRoleIds;
	private List<Long> passWordOverdueIds;
	private Map<Long,Integer> passwordOverNoticeId;
	private Integer isAddOrRomove;
	private String flag;
	private List<BusinessSystem> systems;
	private CompanyManager companyManager;
	private SubsciberManager subsciberManager;
	private String redirectUrl;
	private String password;
	private String states;
	private String synchronousLdapMessage;
	private String message;
	private String departmentName;
	private String deId;
	private String type;
	private String depIds;
	private String usersId;
	private String mode;
	private String lookId;
	private String look;
	private String looked;
    private String oldDid;
	private String oldType;
	private String olDid;
	private String olType;
	private String passWordChange;
	private String departmId;
	private String departmType;
	private String edit;
	private String edited;;
	
	private String oraginalPassword;
	private Boolean isPasswordChange;
	private String levelpassword;
	
	private File file;
	private String fileName;
	
	private String oneDid;
	private String mainDepartmentName;
	
	private String comy;
	private String fromWorkgroup;
	private String fromChangeMainDepartment;//来自批量更换主职部门
	private Long newMainDepartmentId;
	private String userEmail;
	private String dscId;//正职部门的分支机构id
	private Long branchId;//分支机构id
	private String mainDepartmentSubCompanyId;
	private String validateLoginName;
	private String userLoginName;
	private String chooseDepartmentId;
	private String chooseDepartmentIds;
	private String isCrossBranch;
	private String branId;//分支机构id
	private Long deptId;
	private Long fromBracnhId;
	private Long uusId;
	private Long did;
	private Boolean containBranches=false;//集团公司中是否含有分支机构：true含有分支机构，false不含有分支机构
	private boolean canEditUser = false;//是否可以操作用户
	//复制人员权限
	private String uids;
	private String userIds;
	private String deptIds;
	private String workgroupIds;

	public String getUids() {
		return uids;
	}

	public void setUids(String uids) {
		this.uids = uids;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(String deptIds) {
		this.deptIds = deptIds;
	}

	public String getWorkgroupIds() {
		return workgroupIds;
	}

	public void setWorkgroupIds(String workgroupIds) {
		this.workgroupIds = workgroupIds;
	}

	@Autowired
	private ImportUserManager importUserManager;
	
	@Autowired
	private BranchAuthorityManager branchAuthorityManager;
	@Autowired
	private AcsUtils acsUtils;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private RecordLockerManager recordLockerManager;
	@Override
	@Action("list")
	public String list() throws Exception{
		return "list";
	}
	
	/**
	 *=================用户管理==================== 用户管理的列表界面 条件是用户信息的dr字段等于0
	 */
	@Action("user")
	public String user() throws Exception {
		containBranches=departmentManager.containBranches();
		if(departmId!=null&&departmType!=null){
			if(departmType.equals("USERSBYDEPARTMENT")
					||departmType.equals("USERSBYBRANCH"))
				departmentId=Long.parseLong(departmId);
		}
		if(did!=null){
			departmentId=did;
		}
		if(departmentId != null){
			return getUserByDepartment();
		}else if(workGroupId != null){
			return getUserByWorkGroup();
		}else if(departmType!=null&&(departmType.equals("NODEPARTMENT")||departmType.equals("NODEPARTMENT_USER")
				||departmType.equals("NOBRANCH")||departmType.equals("BRANCH_NODEPARTMENT_USER"))){
			return getNoDepartmentUsers();
		}else if(departmType!=null&&(departmType.equals("DELETED")||departmType.equals("DELETEDBRANCH"))){
			return deleteList();
		}else if(departmType!=null&&departmType.equals("allDepartment")){
			return getUserByCompanyHasLog();
		}else if(departmType!=null&&departmType.equals("company")){
			return getUserByCompanyHasLog();
		}else{
			flag = "true";
			if(!roleManager.hasAdminRole(ContextUtils.getUserId())){
				List<BranchAuthority> branchAuthoritys = branchAuthorityManager.getBranchByUser(ContextUtils.getUserId());
				if(!branchAuthoritys.isEmpty()){
				  fromBracnhId = branchAuthoritys.get(0).getBranchesId();
				}
			}
			return 	search();
		}
		
	}
	
	public String getUserByCompanyHasLog()throws Exception{
		if(page.getPageSize() <= 1){
			return SUCCESS; 
		}else{
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"),ApiFactory.getBussinessLogService().getI18nLogInfo("acs.viewUserList1"),ContextUtils.getSystemId("acs"));
			page = userInfoManager.queryUsersByCompany(page, companyManager.getCompanyId());
			renderHtml(PageUtils.pageToJson(page));
			return null;
		}
	}

	public void prepareSearch() throws Exception {
		prepareModel();
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String search() throws Exception {
		searchCanEditUser();
		if(page.getPageSize() <= 1){
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"),ApiFactory.getBussinessLogService().getI18nLogInfo("acs.viewUserList1"),ContextUtils.getSystemId("acs"));
			return "user"; 
		}else{
			//如果是系统管理员
			if(roleManager.hasAdminRole(ContextUtils.getUserId())){
			    page = userInfoManager.getSearchUser(page, entity, 0, false);
			}else{
				List<BranchAuthority> branchAuthoritys = branchAuthorityManager.getBranchByUser(ContextUtils.getUserId());
				if(!branchAuthoritys.isEmpty()){
				  page = userInfoManager.queryUsersByBranch(page,branchAuthoritys.get(0).getBranchesId());
				}
			}
			this.renderText(PageUtils.pageToJson(page));
			return null;
		}
	}
	
	@Override
	@Action("user-save")
	public String save() throws Exception {
		boolean logSign=true;//该字段只是为了标识日志信息：true表示新建用户、false表示修改用户
		if(id!=null)logSign=false;
		Long oldDeptId = entity.getUser().getMainDepartmentId();
		if(StringUtils.isNotEmpty(oneDid)){//正职部门id
			  Department d = departmentManager.getDepartmentById(Long.valueOf(oneDid));
			  //如果跨分支机构，先要把user和原来分支机构下部门关系删除，
			  if(!departmentManager.isSameBranch(entity.getUser(),d)){
				  departmentManager.removeDepartmentToUser(entity.getUser(),d);
			  }
			//设置用户的分支机构id
			  if(d!=null){
					if(d.getBranch()){
						entity.getUser().setSubCompanyId(d.getId());
						entity.getUser().setSubCompanyName(StringUtils.isNotEmpty(d.getShortTitle())?d.getShortTitle():d.getName());
					}else{
						if(d.getSubCompanyId()!=null){
							entity.getUser().setSubCompanyId(d.getSubCompanyId());
							Department newbranch=departmentManager.getDepartment(d.getSubCompanyId());
							entity.getUser().setSubCompanyName(StringUtils.isNotEmpty(newbranch.getShortTitle())?newbranch.getShortTitle():newbranch.getName());
						}else{
							entity.getUser().setSubCompanyId(null);
							entity.getUser().setSubCompanyName(ContextUtils.getCompanyName());
						}
					}
				}
			  
		}else{//没有正职部门
			if(StringUtils.isNotEmpty(oldDid)){
				
				Department branch1=departmentManager.getDepartment(Long.valueOf(oldDid));
				if(branch1!=null){
					if(branch1.getBranch()){
						entity.getUser().setSubCompanyId(branch1.getId());
						entity.getUser().setSubCompanyName(StringUtils.isNotEmpty(branch1.getShortTitle())?branch1.getShortTitle():branch1.getName());
					}else{
						if(branch1.getSubCompanyId()!=null){
							entity.getUser().setSubCompanyId(branch1.getSubCompanyId());
							Department newbranch=departmentManager.getDepartment(branch1.getSubCompanyId());
							entity.getUser().setSubCompanyName(StringUtils.isNotEmpty(newbranch.getShortTitle())?newbranch.getShortTitle():newbranch.getName());
						}else{
							entity.getUser().setSubCompanyId(null);
							entity.getUser().setSubCompanyName(ContextUtils.getCompanyName());
						}
					}
				}
				
			}else{//集团公司下的无部门人员
				if(entity.getUser().getSubCompanyId()==null){
					entity.getUser().setSubCompanyName(ContextUtils.getCompanyName());
				}
			}
		}
		if((entity!=null&&entity.getId()==null)||"yes".equals(passWordChange)){
			entity.setPasswordUpdatedTime(new Date()); 
		}
		if(StringUtils.isNotEmpty(oneDid)){
			entity.getUser().setMainDepartmentId(Long.valueOf(oneDid));
		}
		
		userInfoManager.save(entity);
		id = entity.getId();
		
		//新建用户是默认给用户portal普通用户权限
		userInfoManager.giveNewUserPortalCommonRole(entity.getUser());
		
//		//修改ldap密码
//		if(Ldaper.isStartedAboutLdap()){
//			Company company = companyManager.getCompany(ContextUtils.getCompanyId());
//			List<Department> departments = userManager.getDepartmentsByUser(entity.getId());
//			message = Ldaper.modifyUser(entity.getUser(), company.getCode(), departments, false, entity.getUser().getLoginName());
//		}
		
		
		// 处理部门关系，正职或兼职有修改
		Set<Long> addDeptIds = new HashSet<Long>();
		List<Long> delDeptIds = new ArrayList<Long>();
		//dids是新兼职部门id  //deId为原来的兼职部门id
		if(StringUtils.isNotEmpty(oneDid)){
			//正职机构
			addDeptIds.add(Long.valueOf(oneDid));
			if(oldDeptId != null) delDeptIds.add(oldDeptId);
		}else{
			//if(StringUtils.isNotEmpty(oldDid)){//分支机构中无部门人员
			//	addDeptIds.add(Long.valueOf(oldDid));
			//}
			if(oldDeptId != null) delDeptIds.add(oldDeptId);
		}
		if(StringUtils.isEmpty(dids)){// 新兼职部门没有值
			if(StringUtils.isNotEmpty(deId)){ // 而原来的兼职部门有值，删除原来的
				String[] tempDelIds = deId.split("=");
				delDeptIds.addAll(CollectionUtils.changeList(tempDelIds));
			}
		}else{ // 新兼职部门有值
			// 增加的新兼职部门
			String[] tempAddIds = dids.split("=");
			addDeptIds.addAll(CollectionUtils.changeList(tempAddIds));
			if(StringUtils.isNotEmpty(deId)){ // 而原来的兼职部门有值，删除原来的
				String[] tempDelIds = deId.split("=");
				delDeptIds.addAll(CollectionUtils.changeList(tempDelIds));
			}
		}
		if(!delDeptIds.isEmpty()){// 删除的
			userManager.deleteDepartmemtToUser(delDeptIds, entity.getUser().getId());
			deId=null;
			if(addDeptIds.isEmpty()){
				userManager.addNoDepartmentToUser(entity.getId());
			}
		}
		if(!addDeptIds.isEmpty()){ // 增加的
			departmentIds = new ArrayList<Long>();departmentIds.addAll(addDeptIds);
			userManager.addDepartmentToUserDel(entity.getId(), departmentIds, 0);
		}
		
		//分支机构下点击无部门节点新建用户
		if(delDeptIds.isEmpty()&&addDeptIds.isEmpty()&&StringUtils.isNotEmpty(branId)){
			userManager.addBranchUser(entity.getId(),Long.valueOf(branId));
			branId="";
		}else if((!delDeptIds.isEmpty()||!addDeptIds.isEmpty())&&StringUtils.isNotEmpty(branId)){
			userManager.deleteBranchUser(entity.getId(),Long.valueOf(branId));
			branId="";
		}
			
		setUserDeptmentInfo(entity.getUser());
		List<String> messages = new ArrayList<String>();//国际化
		messages.add(entity.getUser().getName());
		if(logSign){
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.addUser",messages),ContextUtils.getSystemId("acs"));
		}else{
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.updateUser",messages),ContextUtils.getSystemId("acs"));
		}
		user=entity.getUser();
		Department department=null;
		if(user.getMainDepartmentId()!=null){
			department=departmentManager.getDepartment(user.getMainDepartmentId());
		}
		 if(department!=null){
		    	if(department.getBranch()){
		    		    mainDepartmentSubCompanyId=department.getId().toString();
		    	}else{
				    if(department.getSubCompanyId()==null){
			    		mainDepartmentSubCompanyId = "null";
			    	}else{
			    		mainDepartmentSubCompanyId = department.getSubCompanyId().toString();
			    	}
		    	}
		  }
	
			return "user-input";
	}
	
	/**
	 * 弹选多个部门树
	 */
	@Action("user-chooseDepartments")
	public String chooseDepartments(){
		
		return "user-departmentTree";
	}
	/**
	 * 弹选单个部门树
	 */
	@Action("user-chooseOneDepartment")
	public String chooseOneDepartment(){
		
		return "user-departmentSingleTree";
	}
	
	
	
	/**
	 * 修改密码方法
	 * 用户界面修改密码
	 */
	@Action("user-modifyPassWord")
	public String modifyPassWord()throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		passWord_CreateTime = sdf.format(new Date());
		return "user-modify-password";
	}
	public void prepareModifyPassWord() throws Exception {
		prepareModel();
	}
	
	public void prepareUpdateUserPassword() throws Exception {
		userId=ContextUtils.getUserId();
		prepareModel();
	}
	
	/**
	 * 各项目中的 修改密码的方法
	 * @return
	 * @throws Exception
	 */
	@Action("user-updateUserPassword")
	public String updateUserPassword()throws Exception {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		passWord_CreateTime = sdf.format(new Date());
		User u=userManager.getUserById(ContextUtils.getUserId());
		oraginalPassword=u.getPassword();
		return "user-update-user-password";
	}
	
	public void prepareSavePassWord() throws Exception {
		prepareModel();
	}
	/**
	 * 修改密码-保存
	 * @return
	 * @throws Exception
	 */
	@Action("user-savePassWord")
    public String savePassWord()throws Exception {
    	if(Md5.toMessageDigest(levelpassword).equals(oraginalPassword)){
    		entity.setPasswordUpdatedTime(new Date());
    		userInfoManager.savePassWord(entity);
//    		Company company = companyManager.getCompany(ContextUtils.getCompanyId());
//    		List<Department> departments = userManager.getDepartmentsByUser(entity.getId());
//    		if(Ldaper.isStartedAboutLdap()){
//    			message = Ldaper.modifyUser(entity.getUser(), company.getCode(), departments, false, entity.getUser().getName());
//    		}
    		isPasswordChange=true;
    	}else{
    		isPasswordChange=false;
    		User myuser = entity.getUser();
    		myuser.setPassword(oraginalPassword);
    		userManager.saveUser(myuser);
    		addActionMessage(Struts2Utils.getText("updatePasswordError"));
    	}
    	ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.allSystem"), 
    			ApiFactory.getBussinessLogService().getI18nLogInfo("acs.updateUserPassword"),ContextUtils.getSystemId("acs"));
		return "user-update-user-password";
	}
    /**
     * 新建用户
     */
	@Action("user-input")
	public String input() throws Exception {
		if(id!=null){
			entity = user.getUserInfo();
			setUserDeptmentInfo(user);
			looked=look;
			edited=edit;
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.updateUser1"),ContextUtils.getSystemId("acs"));
			
			Department department=null;
			if(user.getMainDepartmentId()!=null){
				department=departmentManager.getDepartment(user.getMainDepartmentId());
			}
			 if(department!=null){
			    	if(department.getBranch()){
			    		    mainDepartmentSubCompanyId=department.getId().toString();
			    	}else{
					    if(department.getSubCompanyId()==null){
				    		mainDepartmentSubCompanyId = "null";
				    	}else{
				    		mainDepartmentSubCompanyId = department.getSubCompanyId().toString();
				    	}
			    	}
			  }
		}else{
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.addUser1"),ContextUtils.getSystemId("acs"));
			if(departmentIds.size()>0&&departmentIds.get(0)!=null&&departmentIds.get(0)!=0){
				Department department=departmentManager.getDepartment(departmentIds.get(0));
			    if(department!=null&&!department.getBranch()){
			    	mainDepartmentName = department.getName();
			    }
			    if(department!=null){
			    	if(department.getBranch()){
			    		    mainDepartmentSubCompanyId=department.getId().toString();
			    	}else{
					    if(department.getSubCompanyId()==null){
				    		mainDepartmentSubCompanyId = "null";
				    	}else{
				    		mainDepartmentSubCompanyId = department.getSubCompanyId().toString();
				    	}
			    	}
			    }
			}
		}
		return "user-input";
	}
	
	private void setUserDeptmentInfo(User user){
		// 正职部门
		if(user.getMainDepartmentId()!=null){
			Department d = departmentManager.getDepartmentById(user.getMainDepartmentId());
			mainDepartmentName = (d==null?"":d.getName());
		}
		// 兼职部门
		List<Department> departments= userManager.getDepartmentsByUser(user.getId());
		departmentName="";deId="";
		if(departments.size()>0){
			for(Department department:departments){
				
				if(department.getName()==null?false:!department.getId().equals(user.getMainDepartmentId())&&!department.getBranch()){
			        departmentName+=department.getName()+",";
			        deId+=department.getId()+"=";
				}
			}
			if(StringUtils.isNotEmpty(deId)) deId=deId.substring(0, deId.length()-1);
			if(StringUtils.isNotEmpty(departmentName)) departmentName=departmentName.substring(0,departmentName.length()-1);
		}
	}
	public void prepareInputLook() throws Exception {
		prepareModel();
	}
	/**
	 * 显示用户信息
	 * @return
	 * @throws Exception
	 */
	@Action("user-inputLook")
	public String inputLook() throws Exception {
		if(id!=null){
			user=userInfoManager.getUserInfoById(id).getUser();
			if(user.getMainDepartmentId()!=null){
				Department d = departmentManager.getDepartment(user.getMainDepartmentId());
				mainDepartmentName = (d==null?"":d.getName());
			}
			List<Department> departments= userManager.getDepartmentsByUser(user.getId());
			departmentName="";
			if(departments.size()>0){
				for(Department department:departments){
					if(!department.getId().equals(user.getMainDepartmentId())&&!department.getBranch())
			        departmentName+=department.getName()+",";
				}
				if(departmentName.length() > 0) departmentName = departmentName.substring(0,departmentName.length()-1);
			}
			looked=look;
		}
		return "user-input";
	}

	

  /**
   * 验证用户注册量
   * @return
   * @throws Exception
   */
	@Action("user-checkUserRegister")
  public String checkUserRegister()throws Exception{
	  Integer maxUser = subsciberManager.getAllowedNumbByCompany(userInfoManager.getCompanyId());
  	  Integer currentUser = userInfoManager.getCompanyIsUsers();
  	  HttpServletRequest request = ServletActionContext.getRequest();
	  String weburl = request.getParameter("weburl");
  	  if(maxUser.intValue()<(currentUser.intValue()+1)){
  		 renderText("1");
  	  }else{
  		renderText(weburl);
  	  }
  	  return null;
  }
  
  
  /**
   * 移除用户
   * @return
   * @throws Exception
   */
	@Action("user-falseDelete")
	public String falseDelete() throws Exception {
		String logSign="";//该字段只是为了标识日志信息：用户1，用户2，...
		
		String[] arr=ids.split(",");
		for(String userId:arr){
			userInfoManager.falseDelete(Long.valueOf(userId),departmentIds);
			
			user=userManager.getUserById(Long.valueOf(userId));
			if(StringUtils.isNotEmpty(logSign)){
				logSign+=",";
			}
			logSign+=user.getName();
		}
		if(departmentIds.get(0)!=null){
		    departmentId=departmentIds.get(0);
		}else{
			if(StringUtils.isEmpty(departmType)){
				departmType = "NODEPARTMENT";
			}
		}
		List<String> messages= new ArrayList<String>();//国际化
		messages.add(logSign);
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.removeUser",messages),ContextUtils.getSystemId("acs"));
		return user();
	}
	
	/**
	 * 删除用户
	 * @return
	 * @throws Exception
	 */
	@Action("user-clearUser")
	public String clearUser() throws Exception {
		String logSign="";//该字段只是为了标识日志信息：用户1，用户2，...
		
		String[] arr=ids.split(",");
		for(String userId:arr){
			userInfoManager.clearUser(Long.valueOf(userId));
			
			user=userManager.getUserById(Long.valueOf(userId));
			if(StringUtils.isNotEmpty(logSign)){
				logSign+=",";
			}
			logSign+=user.getName();
		}
		if(departmentIds.get(0)!=null){
			departmentId=departmentIds.get(0);
		}else{
			if(StringUtils.isEmpty(departmType)){
				departmType = "NODEPARTMENT";
			}
		}
		List<String> messages= new ArrayList<String>();//国际化
		messages.add(logSign);
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.deleteUser",messages),ContextUtils.getSystemId("acs"));
		return list();
	}
	/**
	 * 判断是否是管理员
	 * @return
	 * @throws Exception
	 */
	@Action("user-checkIsAdmin")
	public String checkIsAdmin() throws Exception {
		//User user = userManager.getUserById(id);
		String result = "";
		String[] arr=ids.split(",");
		for(String userId : arr){
			user = userManager.getUserById(Long.valueOf(userId));
			if(roleManager.hasAdminRole(Long.valueOf(userId))){
				result = "yes";
			}
		}
		renderText(result);
		return null;
	}

	/**
	 * 锁定用户
	 */
	public void lock() throws Exception {
		userInfoManager.lock(id);
	}
	public void prepareUserManger() throws Exception {
		prepareModel();
	}
	/**
	 * 启用/禁用
	 * @return
	 * @throws Exception
	 */
	@Action("user-userManger")
	public String userManger()throws Exception{
		return "user-state";
	}
	
	@Action("user-saveUserState")
	public String saveUserState()throws Exception{
		if(StringUtils.isEmpty(states)){
			return user();
		}else{
			boolean logSign=true;//该字段只是为了标识日志信息：true表示启用、false表示禁用
			entity = userInfoManager.getUserInfoById(id);
			
			String[] stateStr = states.split(",");
			for (int i=0;i<stateStr.length;i++) {
				if(stateStr[i].equals("accountUnLock"))//用户密码过期解锁
					userInfoManager.unblock(id);
				if(stateStr[i].equals("accountLock"))//用户密码过期不解锁
					lock();
				if(stateStr[i].equals("forbidden"))//禁用
					userInfoManager.forbidden(id);
					logSign=false;
				if(stateStr[i].equals("invocation")){//启用
					userInfoManager.invocation(id);
					logSign=true;
				}
			}
			List<String> messages= new ArrayList<String>();//国际化
			messages.add(entity.getUser().getName());
			if(logSign){
				ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
						ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement",messages),ContextUtils.getSystemId("acs"));
			}else{
				ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
						ApiFactory.getBussinessLogService().getI18nLogInfo("acs.changeUserStatusUn",messages),ContextUtils.getSystemId("acs"));
			}
			
			return user();
		}
		
	}

	/**
	 * ================已删除用户管理==============
	 * 
	 */
	@Override
	@Action("user-delete")
	public String delete() throws Exception {
		String userNames = userInfoManager.delete(ids);
		if(StringUtils.isNotEmpty(userNames)){
			List<String> messages = new ArrayList<String>();
			messages.add(userNames);
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.deleteUserManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.deleteUserThorough",messages),ContextUtils.getSystemId("acs"));
		}
		return deleteList();
	}

	/**
	 * 已删除用户的列表界面
	 */
	@Action("user-deleteList")
	public String deleteList() throws Exception {
		deleteListCanEditUser();
		containBranches=departmentManager.containBranches();
		if(page.getPageSize() <= 1){
			return "user-delete"; 
		}else{
			if(departmType.equals("DELETEDBRANCH")){
				userInfoManager.getBranchDeletedUser(page, entity, 0, true,branchId);
			}else{
			    userInfoManager.getCompanyDeleteUser(page, entity, 0, true);
			}
			renderHtml(PageUtils.pageToJson(page));
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.deleteUserManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.deletedUserList"),ContextUtils.getSystemId("acs"));
			return null;
		}
	}
	private void deleteListCanEditUser(){
		canEditUser = roleManager.hasSystemAdminRole(ContextUtils.getUserId());
		if(canEditUser)return;
		if(departmType!=null&&departmType.equals("DELETEDBRANCH")){
			hasBranchAdminRole(branchId);
		}
	}
	
	private void hasBranchAdminRole(Long branchId){
		canEditUser = branchAuthorityManager.hasBranchAdminRole(branchId, ContextUtils.getUserId());
		if(canEditUser)return;
		List<Long> branchIds = new ArrayList<Long>();
		List<BranchAuthority> branches=branchAuthorityManager.getBranchByUser(ContextUtils.getUserId());
		for(BranchAuthority branch:branches){
			Department d=departmentManager.getDepartment(branch.getBranchesId());
			branchIds.add(d.getId());
			userManager.getSubBranchs(d.getId(),branchIds);
		}
		if(branchIds.contains(branchId)){
			canEditUser=true;
		}
	}
	
	/**
	 * 无部门人员列表
	 */
	@Action("user-getNoDepartmentUsers")
	public String getNoDepartmentUsers() throws Exception{
		getNoDepartmentUsersCanEditUser();
		containBranches=departmentManager.containBranches();
		if(page.getPageSize() <= 1){
			return "user"; 
		}else{
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.viewUserList1"),ContextUtils.getSystemId("acs"));
			if(departmType.equals("NOBRANCH")||departmType.equals("BRANCH_NODEPARTMENT_USER")){
				userInfoManager.getNoBranchUsers(page,branchId);
			}else{
			    userInfoManager.getNoDepartmentUsers(page);
			}
			renderHtml(PageUtils.pageToJson(page));
			return null;
		}
	}
	private void getNoDepartmentUsersCanEditUser(){
		canEditUser = roleManager.hasSystemAdminRole(ContextUtils.getUserId());
		if(canEditUser)return;
		if(departmType.equals("NOBRANCH")||departmType.equals("BRANCH_NODEPARTMENT_USER")){
			hasBranchAdminRole(branchId);
		}
	}
	
	  /**
     * 跳转到己删除用户添加部门页面
     */
    public void prepareToDepartmentToUsersDel() throws Exception {
    	
    	//entity =userInfoManager.getUserInfoById(userId);
    	userInfos=new ArrayList<UserInfo>();
    	String[] arr=ids.split(",");
    	for(int i=0;i<arr.length;i++){
    		User user=userManager.getUserById(Long.valueOf(arr[i]));
    		userInfos.add(user.getUserInfo());
    	}
	} 
    /**
     * 己删除用户添加部门
     * @return
     * @throws Exception
     */
    @Action("user-toDepartmentToUsersDel")
    public String toDepartmentToUsersDel()throws Exception{
    	pageUserToDepart = userManager.getDepartmentList(pageUserToDepart);
    	//checkedDepartmentIds = userManager.getCheckedDepartmentIds(userId);
    	 isAddOrRomove=AddOrRomoveState.ADD.code;
    	return "user-deleted-department-list";
    }

	/**
	 * 给用户(己删除)分配部门
	 */
    @Action("user-saveDepartmentToUserDel")
	public String saveDepartmentToUserDel() throws Exception{
		String[] arr=ids.split(",");
		String logUserNames ="";
		String logDepartmentName = "";
		for(int i=0;i<arr.length;i++){
			User user = userManager.getUserById(Long.valueOf(arr[i]));
			Department department=departmentManager.getDepartment(departmentId);
			if(!userInfoManager.isDepartmentHasSameLoginNameUser(user,department)){
				logUserNames+=user.getName()+",";
				user.getUserInfo().setDr(0);
				user.getUserInfo().setDeleted(false);
				user.setDeleted(false);
				
				logDepartmentName=department.getName();
				if(department != null){
					user.setMainDepartmentId(departmentId);
				}
				//设置用户的分支机构id
				if(department.getBranch()){
				    user.setSubCompanyId(department.getId());
				}else{
					user.setSubCompanyId(department.getSubCompanyId());
				}
				userInfoManager.save(user.getUserInfo());
				userManager.saveUser(user);
				List<Long> dIds = new ArrayList<Long>();
				dIds.add(departmentId);
				userManager.addDepartmentToUserDel(user.getUserInfo().getId(), dIds,0);
		   }
		}
		if(StringUtils.isNotEmpty(logUserNames)&&StringUtils.isNotEmpty(logDepartmentName)){
			List<String> messages = new ArrayList<String>();//国际化
			messages.add(logUserNames.substring(0, logUserNames.length()-1));
			messages.add(logDepartmentName);
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.assgnDeleteduser",messages),ContextUtils.getSystemId("acs"));
		}
		return deleteList();
	}
	
	/**
	 * 批量更换用户的主职部门
	 */
	@Action("user-batchChangeUserMainDepartment")
	public String batchChangeUserMainDepartment() throws Exception{
		userManager.batchChangeMainDepartment(ids,newMainDepartmentId);
		
		String logSign="";//该字段只是为了标识日志信息：用户1，用户2，...
		if(StringUtils.isNotEmpty(ids)){
			String[] userids=ids.split(",");
			Department department = departmentManager.getDepartment(newMainDepartmentId);
			for(String userid:userids){
				user=userManager.getUserById(Long.valueOf(userid));
				if(StringUtils.isNotEmpty(logSign)){
					logSign+=",";
				}
				logSign+=user.getName();
			}
			List<String> messages = new ArrayList<String>();//国际化
			messages.add(logSign);
			messages.add(department.getName());
			ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
					ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement",messages),ContextUtils.getSystemId("acs"));
		}
		return user();
	}

	/**
	 * 登录验证
	 * @return
	 * @throws Exception
	 */
	@Action("user-checkLoginPassword")
	public String checkLoginPassword() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String orgPassword = request.getParameter("orgPassword");
		String istrue = userInfoManager.checkLoginPassword(orgPassword);
		if("true".equals(istrue)){
			renderText("");
			return null;
		}
		   renderText(getText("user.rulesNotMatch")+"。"+getText("securitSet.rule")+" "+istrue);
		   return null;
	}
	

	public String checkOldPassword() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String oldPassword = request.getParameter("oldPassword");
		Long id = Long.valueOf(request.getParameter("id"));
		User user = userManager.getUserById(id);
		if(oldPassword==null || "".equals(oldPassword.trim())){
			this.renderText("false");
		}else if(oldPassword.equals(user.getPassword())){
		//}else if(PasswordEncoder.encode(oldPassword).equals(user.getPassword())){
			this.renderText("true");
		}else{
			this.renderText("false");
		}
		return null;
	}
	
	public String updatePassword() throws Exception{
		User user = userManager.getUserById(id);
		String oldPassword = Md5.toMessageDigest(oraginalPassword);
		if(StringUtils.isNotBlank(oraginalPassword) && oldPassword.equals(user.getPassword())){
			user.getUserInfo().setPasswordUpdatedTime(new Date());
			user.setPassword(password);
			user.setResetPassword(true);
			userManager.saveUser(user);
			renderText("");
		}else{
			renderText("old_pwd_error");
		}
		return null;
	}
	
	public  void overdueblock()throws Exception{
		userInfoManager.overdueblock(id);
	}
	/**
	 * 显示导入用户信息界面
	 * @return
	 * @throws Exception
	 */
	@Action("user-showImportUser")
	public String showImportUser() throws Exception{
		return "user-import-user";
	}
	/**
	 * 导入用户信息
	 * @return
	 * @throws Exception
	 */
	@Action("user-importUser")
	public String importUser() throws Exception{
		String result = "";
		try {
			result = ApiFactory.getDataImporterService().importData(file, fileName,importUserManager);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		renderText(result);
		return null;
	}
	//导出
	@Action("user-exportUser")
	public String exportUser() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		response.reset();
		String fileName1="用户信息.xls";
		response.setContentType("application/x-download");
		String agent = request.getHeader("User-Agent");
		boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
		if (isMSIE) {
		    fileName1 = URLEncoder.encode(fileName1, "UTF-8");
		} else {
		    fileName1 = new String(fileName1.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName1);
		response.setContentType("application/x-download");
		User user=userManager.getUserById(ContextUtils.getUserId());
		
		if(roleManager.hasSystemAdminRole(user)){//系统管理员
			List<Department> depts = departmentManager.getAllDepartment();
			ExportUserInfo.exportUser(response.getOutputStream(), depts, false);
		}else if(roleManager.hasBranchAdminRole(user)){//分支机构管理员
			List<Department> depts = new ArrayList<Department>();
			List<BranchAuthority> branches=branchAuthorityManager.getBranchByUser(ContextUtils.getUserId());
			for(BranchAuthority branch:branches){
				Department d=departmentManager.getDepartment(branch.getBranchesId());
				depts.add(d);
				userManager.getSubDepartment(d.getId(),depts);
			}
			ExportUserInfo.exportUser(response.getOutputStream(), depts, true);
		}
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.exportUser"),ContextUtils.getSystemId("acs"));
		return null;
	}
	
	/**
	 * 更新用户缓存
	 * @return
	 * @throws Exception
	 */
	@Action("update-user-cache")
	public String updateUserCache() throws Exception{
		userManager.updateUsers();
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.updateUserCache"), 
				ContextUtils.getSystemId("acs"));
		return null;
	}
	/**
	 * 用户解锁
	 * @return
	 * @throws Exception
	 */
	@Action("user-unlockUser")
	public String unlockUser() throws Exception{
		String logSign="";//该字段只是为了标识日志信息：用户1，用户2，...
		if(StringUtils.isNotEmpty(ids)){
			String[] userids=ids.split(",");
			for(String userid:userids){
				user=userManager.getUserById(Long.valueOf(userid));
				if(StringUtils.isNotEmpty(logSign)){
					logSign+=",";
				}
				logSign+=user.getName();
			}
		}
		
		this.renderText(userManager.unlockUser(ids));
		List<String> messages = new ArrayList<String>();//国际化
		messages.add(logSign);
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userUnlock",messages),ContextUtils.getSystemId("acs"));
		return null;
	}
	
	/**
	 * 数据解锁
	 * @return
	 * @throws Exception
	 */
	@Action("user-unlockData")
	public String unlockData() throws Exception{
		String logSign="";//该字段只是为了标识日志信息：用户1，用户2，...
		String[] userids=ids.split(",");
		List<Long> userIdList=new ArrayList<Long>();
		for(String userid:userids){
			user=userManager.getUserById(Long.valueOf(userid));
			if(StringUtils.isNotEmpty(logSign)){
				logSign+=",";
			}
			logSign+=user.getName();
			userIdList.add(Long.valueOf(userid));
		}
		recordLockerManager.releaseLockedDataBy(userIdList);
		this.renderText("ok");
		List<String> messages = new ArrayList<String>();//国际化
		messages.add(logSign);
		ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
				ApiFactory.getBussinessLogService().getI18nLogInfo("acs.dataUnlock",messages),ContextUtils.getSystemId("acs"));
		return null;
	}
	
	/**
	 * 验证用户邮箱不能重复
	 * @return
	 */
	@Action("user-validateEamil")
	public String validateEamil()throws Exception{
		renderText(userManager.validateEmail(id,dscId,validateLoginName,fromBracnhId,branId));
		return null;
	}
	/**
	 * 验证用户名在新的分支机构内是否重复
	 * @return
	 * @throws Exception
	 */
	@Action("user-validateLoginNameRepeat")
	public String validateLoginNameRepeat()throws Exception{
		  renderText(userManager.validateLoginNameRepeat(userLoginName,Long.valueOf(chooseDepartmentId),isCrossBranch));
	  	  return null;
	}
	
	//校验用户登录名重复
	@Action("user-validateLoginNameRepeatByDepartIds")
	public String validateLoginNameRepeatByDepartIds()throws Exception{
		  renderText(userManager.validateLoginNameRepeatByDepartIds(userLoginName,chooseDepartmentIds,uusId));
	  	  return null;
	}
	//=====================复制人员权限========================
	@Action("user-copyRoleToUser")
	public String copyRoleToUser(){
		userManager.copyRoleToUser(uids,userIds);
		renderText(Struts2Utils.getText("copySuccess"));
		return null;
	}
	/**
	 * 复制权限给部门
	 * @return
	 */
	@Action("user-copyRoleToDepartment")
	public String copyRoleToDepartment(){
		userManager.copyRoleToDepartment(uids,deptIds);
		renderText(Struts2Utils.getText("copySuccess"));
		return null;
	}
	/**
	 * 复制权限给工作组
	 * @return
	 */
	@Action("user-copyRoleToWorkgroup")
	public String copyRoleToWorkgroup(){
		userManager.copyRoleToWorkgroup(uids,workgroupIds);
		renderText(Struts2Utils.getText("copySuccess"));
		return null;
	}
	
	@Override
	protected void prepareModel() throws Exception {
		if (id != null) {
			entity = userInfoManager.getUserInfoById(id);
		}else if(userId != null){
			user = userManager.getUserById(userId);
			entity = user.getUserInfo();
			id = entity.getId();
		}else {
			entity = new UserInfo();
			entity.setUser(new User());
		}
	}

	public UserInfo getModel() {
		return entity;
	}
	
	public User getUser() {
		return user;
	}
	
	public Long getUserInfoIds() {
		return userInfoIds;
	}

	public UserInfo getEntity() {
		return entity;
	}

	public List<Long> getCheckedWorkGroupIds() {
		return checkedWorkGroupIds;
	}

	public List<Long> getCheckedDepartmentIds() {
		return checkedDepartmentIds;
	}

	public Integer getIsAddOrRomove() {
		return isAddOrRomove;
	}

	public void setIsAddOrRomove(Integer isAddOrRomove) {
		this.isAddOrRomove = isAddOrRomove;
	}

	@Required
	public void setUserInfoManager(UserInfoManager userInfoManager) {
		this.userInfoManager = userInfoManager;
	}
	
	@Required
	public void setDepartmentManager(DepartmentManager departmentManager) {
		this.departmentManager = departmentManager;
	}
	@Required
	public void setCompanyManager(CompanyManager companyManager) {
		this.companyManager = companyManager;
	}
	public UserManager getUserManager() {
		return userManager;
	}

	@Required
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCheckedWorkGroupIds(List<Long> checkedWorkGroupIds) {
		this.checkedWorkGroupIds = checkedWorkGroupIds;
	}

	public void setCheckedDepartmentIds(List<Long> checkedDepartmentIds) {
		this.checkedDepartmentIds = checkedDepartmentIds;
	}

	public List<UserInfo> getAllUser() {
		return allUser;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Page<User> getPage() {
		return page;
	}

	public void setPage(Page<User> page) {
		this.page = page;
	}

	
	public Page<Workgroup> getPageUserToWork() {
		return pageUserToWork;
	}

	public void setPageUserToWork(Page<Workgroup> pageUserToWork) {
		this.pageUserToWork = pageUserToWork;
	}


	public List<Department> getAllDepartment() {
		return allDepartment;
	}

	public void setAllDepartment(List<Department> allDepartment) {
		this.allDepartment = allDepartment;
	}

	public List<Long> getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(List<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}

	public void setUserInfoIds(Long userInfoIds) {
		this.userInfoIds = userInfoIds;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}
  
	public List<Workgroup> getAllWorkGroup() {
		return allWorkGroup;
	}

	public void setWorkGroupIds(List<Long> workGroupIds) {
		this.workGroupIds = workGroupIds;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public List<Long> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Long> roleIds) {
		this.roleIds = roleIds;
	}

	public List<Long> getCheckedRoleIds() {
		return checkedRoleIds;
	}

	public void setCheckedRoleIds(List<Long> checkedRoleIds) {
		this.checkedRoleIds = checkedRoleIds;
	}

	public void setEntity(UserInfo entity) {
		this.entity = entity;
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public Page<Department> getPageUserToDepart() {
		return pageUserToDepart;
	}

	public void setPageUserToDepart(Page<Department> pageUserToDepart) {
		this.pageUserToDepart = pageUserToDepart;
	}

	/**
	 * 查看部门下的用户
	 * @return
	 * @throws Exception
	 */
	@Action("user-getUserByDepartment")
	public String getUserByDepartment() throws Exception{
		containBranches=departmentManager.containBranches();
		if(departmentId != null){
			getUserByDepartmentDepartmentIdNoNullCanEditUser();
			if(page.getPageSize() <= 1){
				return "user"; 
			}else{
				ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
						ApiFactory.getBussinessLogService().getI18nLogInfo("acs.viewUserList1"),ContextUtils.getSystemId("acs"));
				Department department = departmentManager.getDepartmentById(departmentId);
				if(department.getBranch()){//分支机构
					userInfoManager.queryUsersByBranch(page, departmentId);
				}else if((!department.getBranch())
						&&(!department.getId().equals(department.getSubCompanyId()))
						&&department.getSubCompanyId()!=null){//分支机构下部门
					userInfoManager.queryUsersByBranchDepartment(page, departmentId);
				}else{
				    userInfoManager.queryUsersByDepartment(page, departmentId);
				}
				renderHtml(PageUtils.pageToJson(page));
				return null;
			}
			
		}else{
			search();
		}
		return SUCCESS;
	}
	
	private void getUserByDepartmentDepartmentIdNoNullCanEditUser(){
		Department department = departmentManager.getDepartmentById(departmentId);
		canEditUser = roleManager.hasSystemAdminRole(ContextUtils.getUserId());
		if(canEditUser) return;
		if(department.getBranch()){//分支机构
			hasBranchAdminRole(departmentId);
		}else if((!department.getBranch())
				&&(!department.getId().equals(department.getSubCompanyId()))
				&&department.getSubCompanyId()!=null){//分支机构下部门
			hasBranchAdminRole(department.getSubCompanyId());
		}
	}
	private void searchCanEditUser(){
		canEditUser = roleManager.hasSystemAdminRole(ContextUtils.getUserId());
		if(canEditUser)return;
		List<BranchAuthority> branchAuthoritys = branchAuthorityManager.getBranchByUser(ContextUtils.getUserId());
		if(!branchAuthoritys.isEmpty()){
			hasBranchAdminRole(branchAuthoritys.get(0).getBranchesId());
		}
		User u = userManager.getUserById(ContextUtils.getUserId()); 
		boolean securityAuditAdminable = (roleManager.hasSecurityAdminRole(u)||roleManager.hasAuditAdminRole(u));
		if(securityAuditAdminable)canEditUser = false;//如果兼职安全管理员但不兼职系统管理员则不能修改用户
	}
	
	@Action("user-getUserByCompany")
	public String getUserByCompany()throws Exception{
		canEditUser = roleManager.hasSystemAdminRole(ContextUtils.getUserId());
		containBranches=departmentManager.containBranches();
		if(page.getPageSize() <= 1){
			return "user"; 
		}else{
			page = userInfoManager.queryUsersByCompany(page, companyManager.getCompanyId());
			renderHtml(PageUtils.pageToJson(page));
			return null;
		}
	}
	
	/**
     * 验证更换正职部门
     * @return
     * @throws Exception
     */
    @Action("user-changeMainDepartment")
    public String changeMainDepartment()throws Exception{
    	String result="ok";
    	String repeatLoginName="";
    	int i=0;
		for(String str:ids.split(",")){
			User u=userManager.getUserById(Long.valueOf(str));
			User newUser=userManager.getUserInBranchDepartment(u.getLoginName(), ContextUtils.getCompanyId(), branchId);
			User deleteUser=userManager.getDeleteUserInBranchDepartment(u.getLoginName(), ContextUtils.getCompanyId(), branchId);
			if((newUser != null&&!u.getId().equals(newUser.getId()))||(deleteUser!=null&&!u.getId().equals(deleteUser.getId()))){
				if(StringUtils.isNotEmpty(repeatLoginName)){
					repeatLoginName+=",";
				}
				repeatLoginName+=u.getLoginName();
			}
			i++;
		}
    	if(StringUtils.isNotEmpty(repeatLoginName)){
    		result="选择的部门所属分支机构中已有登录名为："+repeatLoginName+"。请重新选择！";
    	}else{
    		for(String str:ids.split(",")){
        		User u=userManager.getUserById(Long.valueOf(str));
        		if(!userManager.sameBranches(branchId, u)){
        			result="no";
        			break;
        		}
        	}
    	}
    	this.renderText(result);
    	return null;
    }
	/**
     * 验证已删除添加部门
     * @return
     * @throws Exception
     */
    @Action("user-validateDeletedUserAdd")
    public String validateDeletedUserAdd(){
    	this.renderText(userInfoManager.validateDeletedUserAdd(ids,branchId,deptId));
    	return null;
    }
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@Action("user-getUserByWorkGroup")
	public String getUserByWorkGroup() throws Exception{
		look = "look";
		fromWorkgroup = "fromWorkgroup";
		if(page.getPageSize() <= 1){
			return SUCCESS; 
		}else{
			if(workGroupId != null){
				ApiFactory.getBussinessLogService().log(ApiFactory.getBussinessLogService().getI18nLogInfo("acs.userManagement"), 
						ApiFactory.getBussinessLogService().getI18nLogInfo("acs.viewUserList1"),ContextUtils.getSystemId("acs"));
				page = userInfoManager.queryUsersByWorkGroup(page, workGroupId);
			}
			renderHtml(PageUtils.pageToJson(page));
			return null;
		}
	}
	public String updateUsersPassword() throws Exception{
		//国家化
		HttpServletRequest request = Struts2Utils.getRequest();
		String userIdStr = request.getParameter("id");
		String language = "zh_CN";
		if(StringUtils.isNotEmpty(userIdStr)){
			language = ApiFactory.getPortalService().getUserLanguageById(Long.parseLong(userIdStr));
		}
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("updatePassword", Struts2Utils.getText("updatePassword",language));
		root.put("passwordError", Struts2Utils.getText("passwordError",language));
		root.put("passwordUpdateSuccess", Struts2Utils.getText("passwordUpdateSuccess",language));
		root.put("updatePasswordS", Struts2Utils.getText("updatePasswordS",language));
		root.put("passwordInfo", Struts2Utils.getText("passwordInfo",language));
		root.put("passwordOverdueUpdate", Struts2Utils.getText("passwordOverdueUpdate",language));
		root.put("userName", Struts2Utils.getText("userName",language));
		root.put("originalPassword", Struts2Utils.getText("originalPassword",language));
		root.put("newPassword", Struts2Utils.getText("newPassword",language));
		root.put("repeatPassword", Struts2Utils.getText("repeatPassword",language));
		root.put("passwordOutThree", Struts2Utils.getText("passwordOutThree",language));
		root.put("enterNewPassword", Struts2Utils.getText("user.enterNewPassword",language));
		root.put("passwordTwiceNotSame", Struts2Utils.getText("user.passwordTwiceNotSame",language));
		root.put("skip", Struts2Utils.getText("skip",language));
		root.put("clear", Struts2Utils.getText("clear",language));
		root.put("ftlSubmit", Struts2Utils.getText("ftlSubmit",language));
		root.put("overdue", request.getParameter("overdue"));
		root.put("resetPassword", request.getParameter("resetPassword"));
		root.put("name", request.getParameter("name"));
		root.put("url", request.getParameter("url"));
		root.put("id", request.getParameter("id"));
		root.put("resourceCtx", request.getParameter("resourceCtx"));
		root.put("base", SystemUrls.getSystemUrl("imatrix"));
		String html =TagUtil.getContent(root, "update-password.ftl");
		
		//将信息内容输出到JSP页面
		HttpServletResponse response = Struts2Utils.getResponse();
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(html);
		return null;
	}
	
	private Long workGroupId;
	private Long departmentId;

	public Long getWorkGroupId() {
		return workGroupId;
	}

	public void setWorkGroupId(Long workGroupId) {
		this.workGroupId = workGroupId;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getPassWord_CreateTime() {
		return passWord_CreateTime;
	}

	public void setPassWord_CreateTime(String passWord_CreateTime) {
		this.passWord_CreateTime = passWord_CreateTime;
	}

	public List<Long> getPassWordOverdueIds() {
		return passWordOverdueIds;
	}

	public void setPassWordOverdueIds(List<Long> passWordOverdueIds) {
		this.passWordOverdueIds = passWordOverdueIds;
	}

	public Map<Long, Integer> getPasswordOverNoticeId() {
		return passwordOverNoticeId;
	}

	public void setPasswordOverNoticeId(Map<Long, Integer> passwordOverNoticeId) {
		this.passwordOverNoticeId = passwordOverNoticeId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public List<BusinessSystem> getSystems() {
		return systems;
	}
	
	public void setSystems(List<BusinessSystem> systems) {
		this.systems = systems;
	}
	
	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SubsciberManager getSubsciberManager() {
		return subsciberManager;
	}
	@Required
	public void setSubsciberManager(SubsciberManager subsciberManager) {
		this.subsciberManager = subsciberManager;
	}

	
	public String getStates() {
		return states;
	}

	public void setStates(String states) {
		this.states = states;
	}

	public Long getCompanyId() {
		return ContextUtils.getCompanyId();
	}

	public String getSynchronousLdapMessage() {
		return synchronousLdapMessage;
	}

	public void setSynchronousLdapMessage(String synchronousLdapMessage) {
		this.synchronousLdapMessage = synchronousLdapMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<UserInfo> getUserInfos() {
		return userInfos;
	}

	public void setUserInfos(List<UserInfo> userInfos) {
		this.userInfos = userInfos;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDids() {
		return dids;
	}

	public void setDids(String dids) {
		this.dids = dids;
	}


	public String getDeId() {
		return deId;
	}

	public void setDeId(String deId) {
		this.deId = deId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDepIds() {
		return depIds;
	}

	public void setDepIds(String depIds) {
		this.depIds = depIds;
	}

	public String getUsersId() {
		return usersId;
	}


	public String getLookId() {
		return lookId;
	}

	public void setLookId(String lookId) {
		this.lookId = lookId;
	}

	public String getLook() {
		return look;
	}

	public void setLook(String look) {
		this.look = look;
	}

	public void setUsersId(String usersId) {
		this.usersId = usersId;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getLooked() {
		return looked;
	}

	public void setLooked(String looked) {
		this.looked = looked;
	}

	public String getOldDid() {
		return oldDid;
	}

	public void setOldDid(String oldDid) {
		this.oldDid = oldDid;
	}

	public String getOldType() {
		return oldType;
	}

	public void setOldType(String oldType) {
		this.oldType = oldType;
	}

	public String getOlDid() {
		return olDid;
	}

	public void setOlDid(String olDid) {
		this.olDid = olDid;
	}

	public String getOlType() {
		return olType;
	}

	public void setOlType(String olType) {
		this.olType = olType;
	}

	public String getPassWordChange() {
		return passWordChange;
	}

	public void setPassWordChange(String passWordChange) {
		this.passWordChange = passWordChange;
	}

	public String getDepartmId() {
		return departmId;
	}

	public void setDepartmId(String departmId) {
		this.departmId = departmId;
	}

	public String getDepartmType() {
		return departmType;
	}

	public void setDepartmType(String departmType) {
		this.departmType = departmType;
	}

	public String getEdit() {
		return edit;
	}

	public void setEdit(String edit) {
		this.edit = edit;
	}

	public String getEdited() {
		return edited;
	}

	public void setEdited(String edited) {
		this.edited = edited;
	}
	public void setOraginalPassword(String oraginalPassword) {
		this.oraginalPassword = oraginalPassword;
	}
	public String getOraginalPassword() {
		return oraginalPassword;
	}
	public Boolean getIsPasswordChange() {
		return isPasswordChange;
	}
	public void setLevelpassword(String levelpassword) {
		this.levelpassword = levelpassword;
	}
	public void setFile(File file) {
		this.file = file;
	}
	
	public void setFileFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOneDid() {
		return oneDid;
	}

	public void setOneDid(String oneDid) {
		this.oneDid = oneDid;
	}

	public String getMainDepartmentName() {
		return mainDepartmentName;
	}

	public void setMainDepartmentName(String mainDepartmentName) {
		this.mainDepartmentName = mainDepartmentName;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getComy() {
		return comy;
	}

	public void setComy(String comy) {
		this.comy = comy;
	}

	public String getFromWorkgroup() {
		return fromWorkgroup;
	}

	public void setFromWorkgroup(String fromWorkgroup) {
		this.fromWorkgroup = fromWorkgroup;
	}

	public String getFromChangeMainDepartment() {
		return fromChangeMainDepartment;
	}

	public void setFromChangeMainDepartment(String fromChangeMainDepartment) {
		this.fromChangeMainDepartment = fromChangeMainDepartment;
	}

	public Long getNewMainDepartmentId() {
		return newMainDepartmentId;
	}

	public void setNewMainDepartmentId(Long newMainDepartmentId) {
		this.newMainDepartmentId = newMainDepartmentId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Long getBranchId() {
		return branchId;
	}

	public void setBranchId(Long branchId) {
		this.branchId = branchId;
	}

	public String getMainDepartmentSubCompanyId() {
		return mainDepartmentSubCompanyId;
	}

	public void setMainDepartmentSubCompanyId(String mainDepartmentSubCompanyId) {
		this.mainDepartmentSubCompanyId = mainDepartmentSubCompanyId;
	}

	public String getDscId() {
		return dscId;
	}
	public void setDscId(String dscId) {
		this.dscId = dscId;
	}

	public String getValidateLoginName() {
		return validateLoginName;
	}

	public void setValidateLoginName(String validateLoginName) {
		this.validateLoginName = validateLoginName;
	}

	public String getUserLoginName() {
		return userLoginName;
	}

	public void setUserLoginName(String userLoginName) {
		this.userLoginName = userLoginName;
	}

	public String getChooseDepartmentId() {
		return chooseDepartmentId;
	}

	public void setChooseDepartmentId(String chooseDepartmentId) {
		this.chooseDepartmentId = chooseDepartmentId;
	}

	public String getIsCrossBranch() {
		return isCrossBranch;
	}

	public void setIsCrossBranch(String isCrossBranch) {
		this.isCrossBranch = isCrossBranch;
	}

	public String getBranId() {
		return branId;
	}

	public void setBranId(String branId) {
		this.branId = branId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getFromBracnhId() {
		return fromBracnhId;
	}

	public void setFromBracnhId(Long fromBracnhId) {
		this.fromBracnhId = fromBracnhId;
	}

	public String getChooseDepartmentIds() {
		return chooseDepartmentIds;
	}

	public void setChooseDepartmentIds(String chooseDepartmentIds) {
		this.chooseDepartmentIds = chooseDepartmentIds;
	}

	public Long getUusId() {
		return uusId;
	}

	public void setUusId(Long uusId) {
		this.uusId = uusId;
	}

	public boolean isCanEditUser() {
		return canEditUser;
	}

	public Long getDid() {
		return did;
	}

	public void setDid(Long did) {
		this.did = did;
	}
	
	public Boolean getContainBranches() {
		return containBranches;
	}
}
