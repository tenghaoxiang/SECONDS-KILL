package top.haibaraai.secondsKill.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.haibaraai.secondsKill.config.WechatConfig;
import top.haibaraai.secondsKill.domain.User;
import top.haibaraai.secondsKill.mapper.UserMapper;
import top.haibaraai.secondsKill.service.UserService;
import top.haibaraai.secondsKill.util.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WechatConfig wechatConfig;

    @Autowired
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User save(String code) {
        String accessTokenUrl = String.format(wechatConfig.getLoginAccessTokenUrl(), wechatConfig.getLoginAppID(),
                wechatConfig.getLoginAppSecret(), code);
        Map<String, Object> map = HttpUtils.doGet(accessTokenUrl);
        /**
         * 接口调用凭证
         */
        String accessToken = (String) map.get("access_token");
        String openId = (String) map.get("openid");
        User dbUser = userMapper.findByWxId(openId);
        if (dbUser != null) {
            return dbUser;
        }
        if (accessToken != null && openId != null) {
            String userInfoUrl = String.format(wechatConfig.getLoginUserInfoUrl(), accessToken, openId);
            Map<String, Object> userInfo = HttpUtils.doGet(userInfoUrl);
            String nickname = (String) userInfo.get("nickname");
            String sex = userInfo.get("sex") + "";
            String province = (String) userInfo.get("province");
            String city = (String) userInfo.get("city");
            String country = (String) userInfo.get("country");
            String headimgurl = (String) userInfo.get("headimgurl");
            /**
             * 解决乱码问题
             */
            try {
                nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
                province = new String(province.getBytes("ISO-8859-1"), "UTF-8");
                city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
                country = new String(country.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("while trans bytes occur: " + e);
            }
            String address = country + "||" + province + "||" + city;
            User user = new User();
            user.setWxId(openId);
            user.setName(nickname);
            user.setSex(sex);
            user.setHeadImg(headimgurl);
            user.setAddress(address);
            user.setCreateTime(new Date());
            userMapper.save(user);
            return user;
        }
        return null;
    }

    @Override
    public User findById(int id) {
        return userMapper.findById(id);
    }
}
