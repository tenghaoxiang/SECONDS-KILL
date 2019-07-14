package top.haibaraai.secondsKill.Intercepter;

import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.util.JwtUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginIntercepter implements HandlerInterceptor {

    private static final Gson gson = new Gson();

    /**
     * 进入controller之前进行拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            token = request.getParameter("token");
        }
        if (token != null && !token.equals("undefined")) {
            Claims claims = JwtUtils.checkJWT(token);
            Integer userId = (Integer) claims.get("id");
            String name = (String) claims.get("name");
            request.setAttribute("user_id", userId);
            request.setAttribute("name", name);
            return true;
        }
        sendJsonMessage(response, new JsonData(-1, null, "请登录!"));
        return false;
    }

    /**
     * 相应数据给前端
     * @param response
     * @param object
     * @throws IOException
     */
    public static void sendJsonMessage(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print(gson.toJson(object));
        writer.close();
        response.flushBuffer();
    }

}
