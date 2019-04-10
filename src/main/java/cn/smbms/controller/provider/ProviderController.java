package cn.smbms.controller.provider;

import cn.smbms.controller.BaseController;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import com.mysql.jdbc.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-28
 * Time:21:25
 */
@Controller
@RequestMapping(value = "/sys/provide"/*,produces = "text/html;charset=UTF-8"*/)
public class ProviderController extends BaseController {
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
    public String goProviderAdd(@ModelAttribute("provider") Provider provider) {
        return "jsp/provideradd";
    }

    //业务处理

    /**
     *  修改供应商操作
     * @param session
     * @return
     */
    @RequestMapping(value = "/domodify")
    public String domodify(Provider provider, HttpSession session) {
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
     *  REST风格的 modify Provider跳转
     * @param proid
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = {"/modify/{proid}"})
    public String modify(@ModelAttribute("provider") Provider provider2,
                                                            @PathVariable String proid,
                                                            Model model) {
        System.out.println("REST modify provider");
        if(!StringUtils.isNullOrEmpty(proid)){
            Provider provider = providerService.getProviderById(proid);
            model.addAttribute("provider", provider);
        }
            return "jsp/providermodify";
    }
    /**
     *  REST风格的 view Provider跳转
     * @param proid
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = {"/view/{proid}"})
    public String view(@ModelAttribute("provider") Provider provider2,
                                                            @PathVariable String proid,
                                                            Model model) {
        System.out.println("view & modify");
        if(!StringUtils.isNullOrEmpty(proid)){
            Provider provider = providerService.getProviderById(proid);
            model.addAttribute("provider", provider);
        }
            return "jsp/providerview";
    }
    /**
     *  处理增加供应商方法
     * @param session
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/add")
    public String addProvider(Provider provider, HttpSession session) {
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
