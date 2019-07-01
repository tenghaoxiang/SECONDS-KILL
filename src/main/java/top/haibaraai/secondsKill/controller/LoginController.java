package top.haibaraai.secondsKill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.haibaraai.secondsKill.config.WechatConfig;
import top.haibaraai.secondsKill.domain.JsonData;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.service.UserService;
import top.haibaraai.secondsKill.util.JwtUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RequestMapping("/login")
@RestController
public class LoginController extends BasicController {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private UserService userService;

    /**
     * 获取微信登录链接
     * @param state 当前页面的url
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("/url")
    public JsonData loginUrl(@RequestParam(value = "state") String state) throws UnsupportedEncodingException {
        String url = String.format(wechatConfig.getLoginQrcodeUrl(), wechatConfig.getLoginAppID(),
                URLEncoder.encode(wechatConfig.getLoginRedirectUrl(), "GBK"), state);
        return success(url);
    }

    /**
     * 微信回调接口
     * @param code
     * @param state
     */
    @RequestMapping("/callback")
    public void loginCallBack(@RequestParam(value = "code") String code,
                              @RequestParam(value = "state") String state,
                              HttpServletResponse response) throws IOException {
        User user = userService.save(code);
        if (user != null) {
            String token = JwtUtils.geneJsonWebToken(user);
            response.sendRedirect(state + "?token=" + token + "&head_img=" + user.getHeadImg() + "&name=" + URLEncoder.encode(user.getName(), "UTF-8"));
        }
    }

}
