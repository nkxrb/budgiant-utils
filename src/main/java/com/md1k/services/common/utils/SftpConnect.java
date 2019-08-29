package com.md1k.services.common.utils;

import com.jcraft.jsch.*;

import java.util.Properties;

/**
 * Sftp 连接对象
 * @author vvk
 * @since 2019/7/22
 */
public class SftpConnect{

    private ChannelSftp sftp;
    /**链接会话**/
    private Session session;
    /**FTP 登录用户名**/
    private String username;
    /**FTP 登录密码**/
    private String password;
    /**私钥**/
    private String privateKey;
    /**FTP 服务器地址IP地址**/
    private String host;
    /**FTP 端口**/
    private Integer port;

    public SftpConnect(String username, String password, String privateKey, String host, Integer port) {
        this.username = username;
        this.password = password;
        this.privateKey = privateKey;
        this.host = host;
        this.port = port;
        this.login();
    }

    public ChannelSftp getSftp() {
        return sftp;
    }

    /**
     * 连接sftp服务器
     *
     * return ChannelSftp sftp 核心对象
     * @throws Exception
     */
    public ChannelSftp login() {
        try {
            JSch jsch = new JSch();
            if (privateKey != null) {
                jsch.addIdentity(privateKey);// 设置私钥
            }
            session = jsch.getSession(username, host, port);
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭连接 server
     */
    public void logout() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }
}
