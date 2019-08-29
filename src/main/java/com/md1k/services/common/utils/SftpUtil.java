package com.md1k.services.common.utils;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.Vector;

/**
 * sftp连接工具类
 *
 * @author vvk
 * @since 2019/7/22
 */
public class SftpUtil {

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input           输入流
     * @throws SftpException
     * @throws Exception
     */
    public static boolean upload(ChannelSftp sftp, String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            System.err.println("directory is not exist");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        return true;
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public static boolean upload(ChannelSftp sftp, String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        return upload(sftp, directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr      要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public void upload(ChannelSftp sftp, String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(sftp, directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public void upload(ChannelSftp sftp, String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(sftp, directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws SftpException
     * @throws FileNotFoundException
     * @throws Exception
     */
    public void download(ChannelSftp sftp, String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException
     * @throws IOException
     * @throws Exception
     */
    public byte[] download(ChannelSftp sftp, String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);

        byte[] fileData = IOUtils.toByteArray(is);

        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public void delete(ChannelSftp sftp, String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(ChannelSftp sftp, String directory) throws SftpException {
        return sftp.ls(directory);
    }
//
//    public static void main(String[] args) throws SftpException, IOException {
//        SftpConnect sftpConnect = new SftpConnect();
//        //byte[] buff = sftp.download("/opt", "start.sh");
//        //System.out.println(Arrays.toString(buff));
//        File file = new File("D:\\upload\\index.html");
//        InputStream is = new FileInputStream(file);
//
//        upload(sftpConnect.getSftp(),"/data/work", "test_sftp_upload.csv", is);
//        sftpConnect.logout();
//    }

}
