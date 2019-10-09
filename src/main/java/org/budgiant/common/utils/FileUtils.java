package org.budgiant.common.utils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件处理
 *
 * @author nkxrb
 * @since 2018-08-16
 */
public class FileUtils {

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 52428800;
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
                boolean b = file.mkdirs();
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
     */
    public static void saveFile(InputStream is, String path) {
        OutputStream os = null;
        byte[] dataBuf = new byte[2048];
        //循环读取输入流文件内容，通过输出流将内容写入新文件
        try {
            File file = mkdirFileByPath(path);
            if (file.exists()) {
                boolean b = file.delete();
            }
            os = new FileOutputStream(file);
            int rc = 0;
            while ((rc = is.read(dataBuf, 0, 100)) > 0) {
                os.write(dataBuf, 0, rc);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param multiRequest 文件请求
     * @param savePath     存储路径(以'/'结尾,支持：/{yyyy}/{mm}/{dd}/{time}{rand:6})
     * @return List
     */
    public static List<String> uploadFile(MultipartHttpServletRequest multiRequest, String savePath) {
        List<String> fileUrlList = new ArrayList<>();
        String url = null;
        Iterator<String> ite = multiRequest.getFileNames();
        while (ite.hasNext()) {
            MultipartFile multipartFile = multiRequest.getFile(ite.next());
            url = dealFile(multipartFile, savePath);
            fileUrlList.add(url);
        }
        return fileUrlList;
    }

    /**
     * 文件处理
     *
     * @param multipartFile 文件对象
     * @param savePath      存储路径(以'/'结尾,支持：/{yyyy}/{mm}/{dd}/{time}{rand:6})
     * @return Map
     */
    public static String dealFile(MultipartFile multipartFile, String savePath) {
        String url = null;
        InputStream is = null;
        try {
            is = multipartFile.getInputStream();
            String originFileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            String suffix = originFileName.substring(originFileName.lastIndexOf(".")).toLowerCase();
            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            String parsePath = PathFormat.parse(savePath, originFileName);
            saveFile(is, parsePath);
            url = parsePath;
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
        return url;
    }

    /**
     * 删除文件
     *
     * @param path 文件绝对路径
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean delete = file.delete();
        }
    }

    /**
     * 向文件中写入数据
     *
     * @param file 文件
     * @param text 要写入的数据
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
     * @param sourceFilePath 源文件路径
     * @param copyPath       复制目的地路径
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
     * 压缩文件/文件夹
     *
     * @param zipFilePath    压缩文件输出路径
     * @param sourceFilePath 源文件路径
     */
    public static void zip(String zipFilePath, String sourceFilePath) {
        //创建zip输出流
        ZipOutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFilePath));
            //创建缓冲输出流
            bos = new BufferedOutputStream(out);
            File sourceFile = new File(sourceFilePath);
            //调用函数
            compress(out, bos, sourceFile, sourceFile.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void compress(ZipOutputStream out, BufferedOutputStream bos, File sourceFile, String base) {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] flist = sourceFile.listFiles();
            if (flist != null && flist.length > 0) {//如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (File aFlist : flist) {
                    compress(out, bos, aFlist, base + "/" + aFlist.getName());
                }
            } else {//如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                try {
                    out.putNextEntry(new ZipEntry(base + "/"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {//如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            FileInputStream fos = null;
            BufferedInputStream bis = null;
            try {
                out.putNextEntry(new ZipEntry(base));
                fos = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fos);
                int tag;
                //将源文件写入到zip文件中
                while ((tag = bis.read()) != -1) {
                    bos.write(tag);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //让buffer的数据写到磁盘文件，清空缓冲区，避免缓冲区数据写入下个文件中
                if (bos != null) {
                    try {
                        bos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
