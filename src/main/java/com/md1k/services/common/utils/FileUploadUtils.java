package com.md1k.services.common.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author vvk
 * @since 2018-08-16
 */
public class FileUploadUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 52428800;
    /**
     * 路径默认格式
     */
    private static String PATH_FORMAT = "{yyyy}/{mm}/{dd}/{uuid}/{time}{rand:6}";

    /**
     * 默认的文件名最大长度
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 200;

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
            "asf", "rm", "rmvb"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // pdf
            "pdf"};

    /**
     * 如果目录不存在，就创建（可递归创建）
     *
     * @param path 路径
     * @return File
     */
    public static File mkdirFileByPath(String path) {
        File file = null;
        String dirPath = "";
        if (path != null && path.lastIndexOf("/") != -1) {
            dirPath = path.substring(0, path.lastIndexOf("/"));
            file = new File(dirPath);
            if (!file.exists()) {
                boolean mkdirs = file.mkdirs();
            }
            file = new File(path);
        }
        return file;
    }

    /**
     * 保存文件
     *
     * @param is   文件流
     * @param path 存储路径
     * @return File（文件对象）
     */
    public static File saveFile(InputStream is, String path) {
        OutputStream os = null;
        byte[] dataBuf = new byte[2048];
        //循环读取输入流文件内容，通过输出流将内容写入新文件
        try {
            File file = mkdirFileByPath(path);
            if (file.exists()) {
                boolean delete = file.delete();
            }
            os = new FileOutputStream(file);
            int rc = 0;
            while ((rc = is.read(dataBuf, 0, 100)) > 0) {
                os.write(dataBuf, 0, rc);
            }
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 上传文件
     *
     * @param request  请求
     * @param rootDir 存储根目录
     * @param pathFormat 文件类型
     * @return List<Map>
     */
    public static List<SaveFileResult> uploadFile(HttpServletRequest request, String rootDir, String pathFormat) {
        if(null == pathFormat){
            pathFormat = PATH_FORMAT;
        }
        List<SaveFileResult> fileList = new ArrayList<>();
        SaveFileResult saveFileResult;
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Iterator<String> ite = multiRequest.getFileNames();
        while (ite.hasNext()) {
            MultipartFile multipartFile = multiRequest.getFile(ite.next());
            saveFileResult = uploadFile(multipartFile, rootDir+pathFormat);
            fileList.add(saveFileResult);
        }
        return fileList;
    }

    /**
     * 上传文件
     *
     * @param files  MultipartFile文件数组
     * @param rootDir 存储根目录
     * @param pathFormat 文件类型
     * @return List<Map>
     */
    public static List<SaveFileResult> uploadFile(MultipartFile[] files, String rootDir, String pathFormat) {
        if(null == pathFormat){
            pathFormat = PATH_FORMAT;
        }
        List<SaveFileResult> fileList = new ArrayList<>();
        SaveFileResult saveFileResult;
        for(MultipartFile file:files){
            saveFileResult = uploadFile(file, rootDir+pathFormat);
            fileList.add(saveFileResult);
        }
        return fileList;
    }

    /**
     * 上传文件
     *
     * @param multipartFile 文件
     * @param absolutePath 文件存储的绝对路径
     * @return Map<String   ,   Object>
     */
    public static SaveFileResult uploadFile(MultipartFile multipartFile, String absolutePath) {
        SaveFileResult saveFileResult = new SaveFileResult();
        InputStream is = null;
        try {
            String savePath = "";
            is = multipartFile.getInputStream();
            String originFileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            String suffix = originFileName.substring( originFileName.lastIndexOf( "." ) ).toLowerCase();
            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = PathFormat.parse(absolutePath, originFileName);
            savePath = savePath + suffix;
            File file = saveFile(is, savePath);
            String size = getFileSize(file);
            saveFileResult.setName(originFileName);
            saveFileResult.setPath(savePath);
            saveFileResult.setContentType(contentType);
            saveFileResult.setSize(size);
            saveFileResult.setSuffix(suffix);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return saveFileResult;
    }

    public static String getFileSize(File file){
        if(null == file){
            return "0";
        }
        String size = "";
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        size = df.format((double) fileLength / 1048576);
        return size+"M";
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (file.exists()) {
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 向文件中写入数据
     *
     * @param file
     * @param text
     */
    public static void writeFile(File file, String text) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制文件
     *
     * @param sourceFilePath
     * @param copyPath
     */
    public static void copyfile(String sourceFilePath, String copyPath) {
        File sourceFile = new File(sourceFilePath);
        File copyFile = new File(copyPath);
        try {
            Files.copy(sourceFile.toPath(), copyFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩单个文件/文件夹
     * @param zipFilePath
     * @param sourceFilePathList
     * @throws Exception
     */
    public static void zip(String zipFilePath,List<String> sourceFilePathList) throws Exception{
        System.out.println("压缩中...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFilePath));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);

        //遍历多个文件/文件夹
        for(String sourceFilePath: sourceFilePathList){
            File sourceFile = new File(sourceFilePath);
            //调用函数
            compress(out,bos,sourceFile,sourceFile.getName());
        }

        bos.close();
        out.close();
        System.out.println("压缩完成");
    }

    /**
     * 压缩单个文件/文件夹
     * @param zipFilePath
     * @param sourceFilePath
     * @throws Exception
     */
    public static void zip(String zipFilePath,String sourceFilePath) throws Exception{
        System.out.println("压缩中...");
        //创建zip输出流
        ZipOutputStream out = new ZipOutputStream( new FileOutputStream(zipFilePath));
        //创建缓冲输出流
        BufferedOutputStream bos = new BufferedOutputStream(out);
        File sourceFile = new File(sourceFilePath);
        //调用函数
        compress(out,bos,sourceFile,sourceFile.getName());
        bos.close();
        out.close();
        System.out.println("压缩完成");
    }

    /**
     * 压缩文件
     * @param out
     * @param bos
     * @param sourceFile
     * @param base
     * @throws Exception
     */
    private static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if (null == flist || flist.length == 0){//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                System.out.println(base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else{//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (File aFlist : flist) {
                    compress(out, bos, aFlist, base + "/" + aFlist.getName());
                }
            }
        } else{//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            FileInputStream fos = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fos);
            int tag;
            System.out.println(base);
            //将源文件写入到zip文件中
            while ((tag = bis.read()) != -1) {
                bos.write(tag);
            }
            bos.flush();//让buffer的数据写到磁盘文件，清空缓冲区，避免缓冲区数据写入下个文件中
            bis.close();
            fos.close();
        }
    }
}
