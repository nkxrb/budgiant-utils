package org.budgiant.common.wechat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信配置文件
 *
 * @author nkxrb
 * @since 2019/10/16
 */
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatPropertiesConfig {

    /**
     * 小程序appid
     */
    private String wxminiAppid;
    /**
     * 小程序密钥
     **/
    private String wxminiAppSecret;
    /**
     * 微信小程序获取权限请求地址
     **/
    private String wxminiApiAuth = "https://api.weixin.qq.com/sns/jscode2sessio";
    /**
     * 微信小程序获取token请求地址
     **/
    private String wxminiApiToken = "https://api.weixin.qq.com/cgi-bin/toke";
    /**
     * 微信小程序生成有限个小程序码请求地址
     **/
    private String wxminiApiGetwxacode = "https://api.weixin.qq.com/wxa/getwxacod";
    /**
     * 微信小程序生成无限个小程序码请求地址
     **/
    private String wxminiApiGetUnlimited = "https://api.weixin.qq.com/wxa/getwxacodeunlimi";

    public String getWxminiAppid() {
        return wxminiAppid;
    }

    public void setWxminiAppid(String wxminiAppid) {
        this.wxminiAppid = wxminiAppid;
    }

    public String getWxminiAppSecret() {
        return wxminiAppSecret;
    }

    public void setWxminiAppSecret(String wxminiAppSecret) {
        this.wxminiAppSecret = wxminiAppSecret;
    }

    public String getWxminiApiAuth() {
        return wxminiApiAuth;
    }

    public void setWxminiApiAuth(String wxminiApiAuth) {
        this.wxminiApiAuth = wxminiApiAuth;
    }

    public String getWxminiApiToken() {
        return wxminiApiToken;
    }

    public void setWxminiApiToken(String wxminiApiToken) {
        this.wxminiApiToken = wxminiApiToken;
    }

    public String getWxminiApiGetwxacode() {
        return wxminiApiGetwxacode;
    }

    public void setWxminiApiGetwxacode(String wxminiApiGetwxacode) {
        this.wxminiApiGetwxacode = wxminiApiGetwxacode;
    }

    public String getWxminiApiGetUnlimited() {
        return wxminiApiGetUnlimited;
    }

    public void setWxminiApiGetUnlimited(String wxminiApiGetUnlimited) {
        this.wxminiApiGetUnlimited = wxminiApiGetUnlimited;
    }
}
