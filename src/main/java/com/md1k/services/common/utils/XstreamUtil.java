package com.md1k.services.common.utils;

import com.thoughtworks.xstream.XStream;

import java.util.HashMap;
import java.util.Map;

//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;

/**
 * 用于xml json javabean之间的相互转换工具类
 *
 * @author vvk
 * @since 2019/5/16
 */
public class XstreamUtil {

    /**
     * javaBean2xml
     *
     * @param obj 对象
     * @return xml字符串
     */
    public static String javaBean2xml(Object obj) {
        XStream xstream = new XStream();//需要xpp3的jar文件
        xstream.alias("xml", obj.getClass());
        String xml = xstream.toXML(obj);
        xml = xml.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");
        return xml;
    }

    /**
     * javaBean2xml(有问题，出现类找不到bug)
     *
     * @param xmlStr xml字符串
     * @return obj 对象
     */
    public static Object xml2javaBean(String xmlStr) {
        xmlStr = xmlStr.replace("__", "_").replace("<![CDATA[", "").replace("]]>", "");
        XStream xstream = new XStream();//需要xpp3的jar文件
        xstream.toXML(new Object());
        return xstream.fromXML(xmlStr);
    }

    /**
     * 解析xml
     *
     * @param xml
     * @return
     */
    public static Map<String, String> xml2map(String xml) {
        Map<String, String> map = new HashMap<>();
//        Document document = null;
//        try {
//            document = DocumentHelper.parseText(xml);
//            Element root = document.getRootElement();
//            List<Element> elementList = root.elements();
//            for (Element e : elementList){
//                map.put(e.getName(), e.getText());
//            }
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
        return map;
    }

}
