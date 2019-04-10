package cn.smbms.interceptor;

import cn.smbms.pojo.User;
import cn.smbms.tools.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 *
 * @author 张鸿杰
 * Date：2019-03-30
 * Time:15:12
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
    public boolean preHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        if (user == null) {
            response.sendRedirect("error.jsp");
            return false;
        }
          return true ;
    }
}
