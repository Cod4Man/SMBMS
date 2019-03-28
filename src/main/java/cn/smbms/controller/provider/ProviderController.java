package cn.smbms.controller.provider;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-28
 * Time:21:25
 */
@Controller
@RequestMapping(value = "/provide",produces = "text/html;charset=UTF-8")
public class ProviderController {
    @Autowired
    private ProviderService providerService;

    //页面跳转
    /**
     * 跳转至添加供应商页面
     * @param
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "goProviderAdd")
    public String goProviderAdd() {
        return "jsp/provideradd";
    }

    //业务处理

    /**
     *  修改供应商操作
     * @param proContact
     * @param proPhone
     * @param proAddress
     * @param proFax
     * @param proDesc
     * @param id
     * @param session
     * @return
     */
    @RequestMapping(value = "/domodify")
    public String domodify(@RequestParam String  proContact,
                           @RequestParam String  proPhone,
                           @RequestParam String  proCode,
                           @RequestParam String  proAddress,
                           @RequestParam String  proFax,
                           @RequestParam String  proDesc,
                           @RequestParam String  id,
            HttpSession session) {
        Provider provider = new Provider();
        provider.setId(Integer.valueOf(id));
        provider.setProCode(proCode);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        boolean flag = false;
        flag = providerService.modify(provider);
        if(flag){
            return "redirect:query";
        }else{
            return "jsp/providermodify";
        }
    }
    /**
     *  view & modify Provider跳转
     * @param proid
     * @param method
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = {"/view","/modify"})
    public String getProviderById(@RequestParam String proid,
                                    @RequestParam(required = false) String method, Model model) {
        System.out.println("view & modify");
        if(!StringUtils.isNullOrEmpty(proid)){
            Provider provider = providerService.getProviderById(proid);
            model.addAttribute("provider", provider);
        }
        if ("modify".equals(method)) {
            return "jsp/providermodify";
        } else {
            return "jsp/providerview";
        }
    }
    /**
     *  处理增加供应商方法
     * @param proCode
     * @param proName
     * @param proContact
     * @param proPhone
     * @param proAddress
     * @param proFax
     * @param proDesc
     * @param session
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/add")
    public String addProvider(@RequestParam String proCode,
                              @RequestParam String proName,
                              @RequestParam String proContact,
                              @RequestParam String proPhone,
                              @RequestParam String proAddress,
                              @RequestParam String proFax,
                              @RequestParam String proDesc,
                              HttpSession session) {
        Provider provider = new Provider();
        provider.setProCode(proCode);
        provider.setProName(proName);
        provider.setProContact(proContact);
        provider.setProPhone(proPhone);
        provider.setProFax(proFax);
        provider.setProAddress(proAddress);
        provider.setProDesc(proDesc);
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        boolean flag = false;
        flag = providerService.add(provider);
        if(flag){
            return "redirect:query";
        }else{
            return "forward:add";
        }
    }
    /**
     * 查询供应商列表
     * @param queryProName
     * @param queryProCode
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/query")
    public String query(@RequestParam(required = false) String queryProName,
            @RequestParam(required = false) String queryProCode, Model model) {
        if(StringUtils.isNullOrEmpty(queryProName)){
            queryProName = "";
        }
        if(StringUtils.isNullOrEmpty(queryProCode)){
            queryProCode = "";
        }
        List<Provider> providerList = providerService.getProviderList(queryProName,queryProCode);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        return "jsp/providerlist";
    }

    //Ajax回应
    /**
     * Ajax 回应删除供应商
     * @param proid
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/delprovider")
    @ResponseBody
    public String delprovider(@RequestParam String proid) {
        String result = "";
        if(!StringUtils.isNullOrEmpty(proid)){
            int flag = providerService.deleteProviderById(proid);
            if(flag == 0){//删除成功
                result = "true";
            }else if(flag == -1){//删除失败
                result = "false";
            }else if(flag > 0){//该供应商下有订单，不能删除，返回订单数
                result = String.valueOf(flag);
            }
        }else{
            result = "notexit";
        }
        return result;
    }

}
