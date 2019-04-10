package cn.smbms.controller.bill;

import cn.smbms.controller.BaseController;
import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.sun.deploy.net.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.security.jca.Providers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-28
 * Time:9:24
 */
@Controller
@RequestMapping(value = "/sys/bill"/*,produces = "text/html;charset=UTF-8"*/)
public class BillController extends BaseController {
    @Autowired
    private BillService billService;
    @Autowired
    private ProviderService providerService;

    //小数点处理工具
    private void totalPriceBigDecimal(String totalPrice) {
        //23.234   45
        BigDecimal totalPriceBigDecimal =
                //设置规则，小数点保留两位，多出部分，ROUND_DOWN 舍弃
                //ROUND_HALF_UP 四舍五入(5入) ROUND_UP 进位
                //ROUND_HALF_DOWN 四舍五入（5不入）
                new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN);
    }

    //页面跳转
    @RequestMapping(value = "/gobilladd")
    public String goBillAdd(@ModelAttribute("bill") Bill bill) {
        return "jsp/billadd";
    }
    @RequestMapping(value = "/goModify/{billid}")
    public String goModify(@ModelAttribute("bill") Bill bill2, @PathVariable String billid, Model model) {
        String id = billid;
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(id);
            model.addAttribute(bill);
        }
        return "jsp/billmodify";
    }
    /**
     * 查询全部订单
     * @param queryProductName
     * @param queryProviderId
     * @param queryIsPayment
     * @param request
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/query")
    public String query(@RequestParam(required = false) String queryProductName,
                                      @RequestParam(required = false) String queryProviderId,
                                      @RequestParam(required = false) String queryIsPayment,
                                      HttpServletRequest request, Model model) {
        int queryProviderIdI = -1;
        int queryIsPaymentI = -1;
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("","");
        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }
        Bill bill = new Bill();
        if(StringUtils.isNullOrEmpty(queryIsPayment)){
            queryIsPaymentI = 0;
            bill.setIsPayment(queryIsPaymentI);
        }else{
            queryIsPaymentI = Integer.parseInt(queryIsPayment);
            bill.setIsPayment(queryIsPaymentI);
        }
        if(StringUtils.isNullOrEmpty(queryProviderId)){
            queryProviderIdI = 0;
            bill.setProviderId(queryProviderIdI);
        }else{
            queryProviderIdI = Integer.parseInt(queryProviderId);
            bill.setProviderId(queryProviderIdI);
        }
        bill.setProductName(queryProductName);
         List<Bill> billList = billService.getBillList(bill);
        model.addAttribute(providerList);
        model.addAttribute(billList);
        model.addAttribute(queryProductName);
        model.addAttribute( "queryProviderId", queryProviderIdI);
        model.addAttribute("queryIsPayment", queryIsPaymentI);
        return "jsp/billlist";
    }

    /**
     * 添加订单
     * @param session
     * @param model
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/billadd")
    public String add(Bill bill, HttpSession session, Model model) {
        bill.setProductCount(new BigDecimal(bill.getProductCount() + "").setScale(2,BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(bill.getTotalPrice() + "").setScale(2,BigDecimal.ROUND_DOWN));
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        flag = billService.add(bill);
        System.out.println("add flag -- > " + flag);
        if(flag){
//            response.sendRedirect(request.getContextPath()+"/jsp/bill.do?method=query");
            return "redirect:query";
        }else{
//            request.getRequestDispatcher("billadd.jsp").forward(request, response);
            return  "redirect:billadd";
        }
    }

    /**
     *  Ajax删除订单
     * @param
     * @return java.lang.String Ajax返回删除结果
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/delbill")
    @ResponseBody
    public String delbill(@RequestParam String billid) {
        System.out.println("删除订单");
        String id = billid;
        String data = "";
        if(!StringUtils.isNullOrEmpty(id)){
            boolean flag = billService.deleteBillById(id);
            if(flag){//删除成功
                data = "true";
            }else{//删除失败
                data = "false";
            }
        }else{
            data = "notexit";
        }
        //直接使用map传参回去试试,不行，ajax无法接收
//        System.out.println("mapjson====" + JSONArray.toJSONString(resultMap));
        return data;

        //把resultMap转换成json对象输出
       /* PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write();
        outPrintWriter.flush();
        outPrintWriter.close();*/
    }

    /** Ajxa获取供应商列表
     *
     * @param
     * @return java.lang.String
     * @author zhj
     * @creed: Talk is cheap,show me the code
     * @date 2019/3/28
     */
    @RequestMapping(value = "/getproviderlist"/*,produces = "text/html;charset=UTF-8"*/)
    @ResponseBody
    public String getproviderlist() {
        System.out.println("getproviderlist ========================= ");
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("","");
        //传回数据
//        model.addAttribute(providerList);
        //把providerList转换成json对象输出
/*        response.setContentType("application/json");
        PrintWriter outPrintWriter = response.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(providerList));
        outPrintWriter.flush();
        outPrintWriter.close();*/
System.out.println("listJson===" + JSONArray.toJSONString(providerList));
        return JSONArray.toJSONString(providerList);
    }

    @RequestMapping(value = "/view/{billid}")
    public String view(@PathVariable String billid, Model model) {
        String id = billid;
        if (!StringUtils.isNullOrEmpty(id)) {
            Bill bill = billService.getBillById(id);
            model.addAttribute(bill);
        }
        return "jsp/billview";
    }
    @RequestMapping(value = "/modify")
    public String modify(Bill bill, Model model, HttpSession session
                         ) {
        System.out.println("modify===============");
        bill.setProductCount(new BigDecimal(bill.getProductCount() + "").setScale(2,BigDecimal.ROUND_DOWN));
        bill.setTotalPrice(new BigDecimal(bill.getTotalPrice() + "").setScale(2,BigDecimal.ROUND_DOWN));
        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        flag = billService.modify(bill);
        if(flag){
//            response.sendRedirect(request.getContextPath()+"/jsp/bill.do?method=query");
            return "redirect:query";
        }else{
            return "redirect:modify";
        }
    }
}
