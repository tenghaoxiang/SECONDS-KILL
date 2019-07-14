package top.haibaraai.secondsKill.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import top.haibaraai.secondsKill.domain.User;

import java.util.Date;

/**
 * JWT工具类
 */
public class JwtUtils {

    /**
     * 发行者
     */
    private static String SUBJECT = "haibaraai";
    /**
     * 过期时间
     */
    private static final long EXPIRE = 1000 * 60 * 60 * 24;
    /**
     * 秘钥
     */
    private static String SECRET = "haibaraai";

    /**
     * 生成token
     * @param user
     * @return
     */
    public static String geneJsonWebToken(User user) {
        if (user == null) {
            return null;
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("id", user.getId())
                .claim("name", user.getName())
                .claim("headImg", user.getHeadImg())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .signWith(SignatureAlgorithm.HS256, SECRET).compact();
        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null;
        }
    }

}
