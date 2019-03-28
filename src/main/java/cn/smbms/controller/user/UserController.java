package cn.smbms.controller.user;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-28
 * Time:14:23
 */
@Controller
@RequestMapping(value = "/user", produces = "text/html;charset=UTF-8")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    //页面跳转=====================
    /**
     * 页面跳转至修改密码页面
     * @param
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/goPwdmodify")
    public String goPwdmodify() {
        return "jsp/pwdmodify";
    }

    @RequestMapping(value = "/goUseradd")
    public String goUseradd() {
        return "jsp/useradd";
    }

    //业务========================
    @RequestMapping(value = "/modifyexe")
    public String modifyexe(@RequestParam String uid,
                            @RequestParam String userName,
                            @RequestParam String gender,
                            @RequestParam String birthday,
                            @RequestParam String phone,
                            @RequestParam String address,
                            @RequestParam String userRole,
                                HttpSession session) {
        User user = new User();
        user.setId(Integer.valueOf(uid));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if(userService.modify(user)){
            return "redirect:query";
        }else{
            return "/jsp/usermodify";
        }
    }
    /**
     *  用户的修改查看页面跳转（携带对象）
     * @param userid
     * @param method
     * @param model
     * @return
     */
    @RequestMapping(value = {"/view","/modify"})
    public String getUserById(@RequestParam String userid,@RequestParam(required = false) String method, Model model) {
        System.out.println("view & modify");
        String id = userid;
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            User user = userService.getUserById(id);
            model.addAttribute(user);
        }
        if ("modify".equals(method)) {
            return "jsp/usermodify";
        } else {
            return "jsp/userview";
        }
    }
    /**
     *  添加用户
     * @param userCode
     * @param userName
     * @param userPassword
     * @param gender
     * @param birthday
     * @param phone
     * @param address
     * @param userRole
     * @param session
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/add")
    public String add(@RequestParam String userCode,
                                  @RequestParam String userName,
                                  @RequestParam String userPassword,
                                  @RequestParam String gender,
                                  @RequestParam String birthday,
                                  @RequestParam String phone,
                                  @RequestParam String address,
                                  @RequestParam String userRole,
                                    HttpSession session, Model model) {
        System.out.println("add()================");
        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        if(userService.add(user)){
            return "redirect:query";
        }else{
            return "forward: goUseradd";
        }
    }
    /**
     * 修改密码
     * @param newpassword
     * @param session
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/savepwd")
    public String savepwd(@RequestParam String newpassword,
                          HttpSession session, Model model) {
        System.out.println("修改密码操作");
        Object o = session.getAttribute(Constants.USER_SESSION);
        boolean flag = false;
        if(o != null && !StringUtils.isNullOrEmpty(newpassword)){
            flag = userService.updatePwd(((User)o).getId(),newpassword);
            if(flag){
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码成功,请退出并使用新密码重新登录！");
                session.removeAttribute(Constants.USER_SESSION);//session注销
            }else{
                model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
            }
        }else{
            model.addAttribute(Constants.SYS_MESSAGE, "修改密码失败！");
        }
//        request.getRequestDispatcher("pwdmodify.jsp").forward(request, response);
        return "forward:goPwdmodify";
    }

    /**
     * 查询用户
     * @param queryname
     * @param queryUserRoleS
     * @param pageIndex
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/query")
    public String query(@RequestParam(required = false) String queryname,
                        @RequestParam(required = false, value = "queryUserRole") String queryUserRoleS,
                        @RequestParam(required = false) String pageIndex,
                                                                                         Model model) {
        //查询用户列表
        String queryUserName = queryname;
        String temp = queryUserRoleS;
        int queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;
/**
		 * http://localhost:8090/SMBMS/userlist.do
		 * ----queryUserName --NULL
                * http://localhost:8090/SMBMS/userlist.do?queryname=
		 * --queryUserName ---""*/


        System.out.println("queryUserName servlet--------"+queryUserName);
        System.out.println("queryUserRole servlet--------"+queryUserRole);
        System.out.println("query pageIndex--------- > " + pageIndex);
        if(queryUserName == null){
            queryUserName = "";
        }
        if(temp != null && !temp.equals("")){
            queryUserRole = Integer.parseInt(temp);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
               return "/jsp/error";
            }
        }
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        userList = userService.getUserList(queryUserName,queryUserRole,currentPageNo, pageSize);
        System.out.println(userList+"userList=================>");
        userList.forEach(System.out::println);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "/jsp/userlist";
    }




    //Ajax
    /**
     * Ajax 删除用户数据
     * @param uid
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/deluser")
    @ResponseBody
    public String deluser(@RequestParam String uid) {
        String id = uid;
        Integer delId = 0;
        try{
            delId = Integer.parseInt(id);
        }catch (Exception e) {
            e.printStackTrace();
            delId = 0;
        }
        String result = "";
        if(delId <= 0){
            result = "notexist";
        }else{
            if(userService.deleteUserById(delId)){
                result = "true";
            }else{
                result = "false";
            }
        }
        return result;
    }
    /**
     * Ajax判断用户是否存在
     * @param userCode
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/ucexist")
    @ResponseBody
    public String ucexist(@RequestParam String userCode) {
        //判断用户账号是否可用
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            return  "exist";
        } else {
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                return "exist";
            } else {
                return "notexist";
            }
        }
    }
    /**
     * Ajax返回roleList
     * @param
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/getrolelist")
    @ResponseBody
    public String getrolelist() {
        List<Role> roleList = roleService.getRoleList();
        return JSONArray.toJSONString(roleList);
    }
    /**
     * Ajax验证旧密码
     * @param oldpassword
     * @param session
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/pwdmodify")
    @ResponseBody
    public String pwdmodify(@RequestParam String oldpassword,
                            HttpSession session) {
        Object o = session.getAttribute(Constants.USER_SESSION);
        String result = "";
        if(null == o ){//session过期
            result = "sessionerror";
        }else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
            result = "error";
        }else{
            String sessionPwd = ((User)o).getUserPassword();
            if(oldpassword.equals(sessionPwd)){
                result = "true";
            }else{//旧密码输入不正确
                result = "false";
            }
        }
        return result;
    }
}
