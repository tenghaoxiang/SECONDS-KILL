package top.haibaraai.secondsKill.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class WechatConfig {

    @Value("${login.AppID}")
    private String loginAppID;

    @Value("${login.AppSecret}")
    private String loginAppSecret;

    @Value("${login.redirect_url}")
    private String loginRedirectUrl;

    @Value("${login.qrcode_url}")
    private String loginQrcodeUrl;

    @Value("${login.access_token_url}")
    private String loginAccessTokenUrl;

    @Value("${login.user_info_url}")
    private String loginUserInfoUrl;

    public String getLoginAppID() {
        return loginAppID;
    }

    public String getLoginAppSecret() {
        return loginAppSecret;
    }

    public String getLoginRedirectUrl() {
        return loginRedirectUrl;
    }

    public String getLoginQrcodeUrl() {
        return loginQrcodeUrl;
    }

    public String getLoginAccessTokenUrl() {
        return loginAccessTokenUrl;
    }

    public String getLoginUserInfoUrl() {
        return loginUserInfoUrl;
    }
}
