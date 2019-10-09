package org.budgiant.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序服务端API工具类
 *
 * @author nkxrb
 * @since 2019/5/22
 */
public class WxMiniUtil {
    /**
     * 小程序appid
     */
    private static String WXMINI_APPID;
    /**
     * 小程序密钥凭证
     */
    private static String WXMINI_APPSECRET;
    /**
     * 微信小程序获取token请求地址
     **/
    private static String WXMINI_API_AUTH = "https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信小程序获取token请求地址
     **/
    private static String WXMINI_API_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
    /**
     * 微信小程序生成有限个小程序码请求地址
     **/
    private static String WXMINI_API_GETWXACODE = "https://api.weixin.qq.com/wxa/getwxacode";
    /**
     * 微信小程序生成无限个小程序码请求地址
     **/
    private static String WXMINI_API_GETUNLIMITED = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";
    /**
     * 临时文件保存路径
     **/
    private static String WXMINI_QRCODE_FILE_PATH = "/tmp/wxqrcodetmp/";

    /**
     * 设置微信小程序的appId
     *
     * @param wxminiAppid 小程序的appId
     */
    public static void setWxminiAppid(String wxminiAppid) {
        WXMINI_APPID = wxminiAppid;
    }

    /**
     * 设置微信小程序密钥
     *
     * @param wxminiAppsecret 小程序密钥
     */
    public static void setWxminiAppsecret(String wxminiAppsecret) {
        WXMINI_APPSECRET = wxminiAppsecret;
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据access_token
     *
     * @return
     */
    public static String getAccessToken() {
        //获取小程序全局唯一后台接口调用凭据access_token
        String tokenJson = HttpUtil.sendGet(WXMINI_API_TOKEN + "?grant_type=client_credential&appid=" + WXMINI_APPID + "&secret=" + WXMINI_APPSECRET);
        JSONObject jsonObject = JSON.parseObject(tokenJson);
        if (!EditUtil.isEmptyOrNull(jsonObject) && !EditUtil.isEmptyOrNull(jsonObject.getString("access_token"))) {
            return jsonObject.getString("access_token");
        } else {
            return null;
        }
    }

    /**
     * 生成带参小程序码(不限制个数)，返回base64格式的字符串，拼接'data:image/png;base64,'即可显示图片
     *
     * @param filePath  小程序码图片保存路径
     * @param paramCode 必填项{scene：二维码携带的打开路径参数}
     * @return
     */
    public static String createUnlimitedQrcode(String accessToken, String filePath, String page, String paramCode) {
        //要携带的参数
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("scene", paramCode);
        params.put("width", 430);
        return postFileBase64(WXMINI_API_GETUNLIMITED + "?access_token=" + accessToken, params, filePath);
    }

    /**
     * 生成带参小程序码，返回base64格式的字符串，拼接'data:image/png;base64,'即可显示图片
     *
     * @param filePath 小程序码图片保存路径
     * @param paramMap 必填项{path：二维码携带的打开路径参数}
     * @return
     */
    public static String createQrcode(String accessToken, String filePath, Map<String, Object> paramMap) {
        return postFileBase64(WXMINI_API_GETWXACODE + "?access_token=" + accessToken, paramMap, filePath);
    }

    /**
     * post请求返回将文件的byte[]转义为base64编码格式的字符串
     *
     * @param targetURL 请求地址
     * @param params    请求参数
     * @param filePath  文件保存路径
     * @return base64字符串（图片）
     */
    private static String postFileBase64(String targetURL, Map<String, Object> params, String filePath) {
        String qrcodeBase64 = "";
        String paramsJson = JSON.toJSONString(params);
        InputStream in = null;
        File file = null;
        try {
            URL targetUrl = new URL(targetURL);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setConnectTimeout(5 * 1000);
            httpConnection.setRequestProperty("Content-Type", "application/json");
            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(paramsJson.getBytes());
            outputStream.flush();

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
            }
            in = httpConnection.getInputStream();
            //新建缓存文件
            file = FileUtils.mkdirFileByPath(WXMINI_QRCODE_FILE_PATH + filePath);
            OutputStream os = new FileOutputStream(WXMINI_QRCODE_FILE_PATH + filePath);
            int length = 0;
            byte[] buff = new byte[1024];
            int index = 0;
            while ((length = in.read(buff)) != -1) {
                os.write(buff, 0, length);
            }
            if (file.length() < 500) {//根据返回字符长度判断是否为正常图片， 异常后，返回错误信息
                StringBuilder output = new StringBuilder();
                BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((new FileInputStream(file))));
                String tempRead;
                while ((tempRead = responseBuffer.readLine()) != null) {
                    output.append(tempRead);
                }
                qrcodeBase64 = output.toString();
            } else {
                //成功后返回qrcodeBase64
                InputStream input = new FileInputStream(file);
                byte[] byt = new byte[input.available()];
                input.read(byt);
                qrcodeBase64 = Base64.encodeBase64String(byt);
            }
            httpConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        return qrcodeBase64;
    }

}
