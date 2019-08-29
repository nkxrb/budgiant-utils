package com.md1k.services.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ExcelUtil {

    // 默认上传的地址
//    private static String excelTempPath = InitData.properties.getExcelFileDir();

    /**
     * 报表解析
     *
     * @param file xls文件
     * @return
     */
    public static List<Map<Integer, String>> parseExcelFile(File file) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        // 获取工作簿
        Workbook workBook = null;
        FileInputStream fis = null;
        POIFSFileSystem poifsFileSystem = null;
        try {
            fis = new FileInputStream(file);
            poifsFileSystem = new POIFSFileSystem(fis);
            workBook = WorkbookFactory.create(poifsFileSystem);
            list = parseExcelWorkBook(workBook);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return list;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("关流失败！");
                }
            }
        }
    }

    /**
     * 报表解析
     *
     * @param workBook 工作簿对象
     * @return
     */
    public static List<Map<Integer, String>> parseExcelWorkBook(Workbook workBook) {
        List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
        Map<Integer, String> map = null;
        Sheet sheet = null;
        Row row = null;
        String cellValue = "";
        // 判断工作簿是否有sheet页
        if (workBook.getNumberOfSheets() > 0) {
            // 遍历sheet
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                // 获取sheet
                sheet = workBook.getSheetAt(i);
                // 解析数据
                for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                    map = new HashMap<Integer, String>();
                    row = sheet.getRow(j);
                    if (row == null) {
                        continue;
                    }
                    // 获取数据
                    for (int j2 = 0; j2 <= row.getLastCellNum(); j2++) {
                        // 获取列值
                        cellValue = getColValue(row.getCell(j2));
                        map.put(j2, cellValue);
                    }
                    list.add(map);
                }
            }
        }
        return list;
    }

    /**
     * 获取默认边框样式
     * @param workbook
     * @return
     */
    public static HSSFCellStyle getContentStyle(HSSFWorkbook workbook,short align) {
        // 内容
        HSSFCellStyle fontStyle2 = workbook.createCellStyle();
        HSSFFont font2 = workbook.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 10);// 设置字体大小
        fontStyle2.setFont(font2);
        fontStyle2.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
        fontStyle2.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
        fontStyle2.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
        fontStyle2.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
        fontStyle2.setAlignment(align); // 居中
        return fontStyle2;
    }

    /**
     * 根据excel模板名称获取workbook对象
     * @param tempName
     * @return
     */
    public static HSSFWorkbook getWorkbookByTemp(String tempName){
        InputStream fis = null;
        HSSFWorkbook wb = null;
        File file = new File(tempName);
        try {
            if(!file.exists()){
                file = new File(tempName);
                if(!file.exists()){
                    file.mkdirs();
                }
            }
            fis = new FileInputStream(file);
            wb = new HSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return wb;
    }

    /**
     * 根据workbook对象导出excel
     * @param workbook workbook对象
     * @param fileName 文件名
     * @param retList 数据源集合
     * @param keyNames 数据源中Map集合的key值 (key值对应的value值顺序要列名顺序一致)
     * @param startRow 开始 循环写入数据 的行数(从第几行开始写入数据)
     * @param response 返回请求
     */
    public static void exportByWorkbook(HSSFWorkbook workbook,String fileName,List<Map> retList,String[] keyNames,int startRow,HttpServletResponse response){
        HSSFCellStyle contentStyle = getContentStyle(workbook,XSSFCellStyle.ALIGN_CENTER);
        HSSFCellStyle leftStyle = getContentStyle(workbook,XSSFCellStyle.ALIGN_LEFT);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int xh = 1;
        if(!EditUtil.isEmptyOrNull(retList) && retList.size()>0){
            for(int rowNum=0;rowNum<retList.size();rowNum++){
                if(!EditUtil.isEmptyOrNull(keyNames) && keyNames.length>0){
                    for(int colNum=0;colNum<keyNames.length;colNum++){
                        if("XH".equals(keyNames[colNum])){
                            setCellValue(sheet,startRow+rowNum,colNum,xh++,contentStyle);
                        }else if(keyNames[colNum].endsWith("Name")){
                            setCellValue(sheet,startRow+rowNum,colNum,retList.get(rowNum).get(keyNames[colNum]),leftStyle);
                        }else{
                            setCellValue(sheet,startRow+rowNum,colNum,retList.get(rowNum).get(keyNames[colNum]),contentStyle);
                        }
                    }
                }
            }
        }
        setResponseHeader(response,fileName,workbook);
    }

    /**
     * 根据模板导出报表，可导出多个Sheet页
     *
     * @param ExcelName  导出的Excel文件名
     * @param ModelURl   模板路径 (全路径)
     * @param dataSource 数据源
     * @param response   返回请求
     * @param sheetNames 生成的Sheet页的名称集合
     * @param keyNames   数据源中Map集合的key值 (key值对应的value值顺序要列名顺序一致)
     * @param rowNum     开始 循环写入数据 的行数(从第几行开始写入数据)
     */
    public static void ExcelByModel(String ExcelName, String ModelURl, List<Map<String, String>> dataSource,
                                    HttpServletResponse response, String[] sheetNames, String[] keyNames, int rowNum) throws Exception {

        // 设置导出Excel报表的导出形式
        response.setContentType("application/vnd.ms-excel");
        // 设置导出Excel报表的响应文件名
        String fileName = new String(ExcelName.getBytes("utf-8"), "ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");
        // 创建一个输出流
        OutputStream fileOut = response.getOutputStream();
        // 读取模板文件路径
        File file = new File(ModelURl);
        FileInputStream fins = new FileInputStream(file);
        POIFSFileSystem fs = new POIFSFileSystem(fins);
        // 读取Excel模板
        HSSFWorkbook wb = new HSSFWorkbook(fs);

        // 设置边框样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setFillBackgroundColor(HSSFCellStyle.NO_FILL);
        // 设置边框样式的颜色
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setTopBorderColor(HSSFColor.BLACK.index);

        // style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // 模板页
        HSSFSheet sheetModel = null;
        // 新建的Sheet页
        HSSFSheet newSheet = null;
        // 创建行
        HSSFRow row = null;
        // 创建列
        HSSFCell cell = null;

        // 循环建立Sheet页
        for (int i = 0; i < sheetNames.length; i++) {
            // 读取模板中模板Sheet页中的内容
            sheetModel = wb.getSheetAt(0);
            // 设置新建Sheet的页名
            newSheet = wb.createSheet(sheetNames[i]);
            // 将模板中的内容复制到新建的Sheet页中
            copySheet(sheetModel, newSheet, sheetModel.getFirstRowNum(), sheetModel.getLastRowNum());

            // 获取到新建Sheet页中的第一行为其中的列赋值
            row = newSheet.getRow(0);
            row.getCell(1).setCellValue("这是为表代码赋的值");
            // 注意 合并的单元格也要按照合并前的格数来算
            row.getCell(6).setCellValue("这是为外部代码赋的值");

            // 获取模板中的第二列，并赋值
            row = newSheet.getRow(1);
            row.getCell(1).setCellValue("表名称赋值");
            // 注意 合并的单元格也要按照合并前的格数来算
            row.getCell(6).setCellValue("这是为是否系统表赋的值");

            // 遍历数据源 开始写入数据(因为Excel中是从0开始,所以减一)
            int num = rowNum - 1;
            for (Map<String, String> item : dataSource) {
                // 循环遍历,新建行
                row = newSheet.createRow(num);
                // 判断有多少列数据
                for (int j = 0; j < keyNames.length; j++) {
                    // 设置每列的数据 设置每列的样式 设置每列的值
                    cell = row.createCell(j);
                    cell.setCellStyle(style);
                    cell.setCellValue(item.get(keyNames[j]));
                }
                num++;
            }
            // break 加break可以测试只添加一个Sheet页的情况
        }
        // 写入流
        wb.write(fileOut);
        // 关闭流
        fileOut.close();
    }

    /**
     * 复制同一个workbook下的sheet页
     *
     * @param fromsheet 模板Sheet页
     * @param newSheet  新建Sheet页
     * @param firstrow  模板页的第一行
     * @param lastrow   模板页的最后一行
     */
    public static void copySheet(HSSFSheet fromsheet, HSSFSheet newSheet, int firstrow, int lastrow) {

        // 复制一个单元格样式到新建单元格
        if ((firstrow == -1) || (lastrow == -1) || lastrow < firstrow) {
            return;
        }
        // 复制合并的单元格
        Region region = null;
        for (int i = 0; i < fromsheet.getNumMergedRegions(); i++) {
            region = fromsheet.getMergedRegionAt(i);
            if ((region.getRowFrom() >= firstrow) && (region.getRowTo() <= lastrow)) {
                newSheet.addMergedRegion(region);
            }
        }
        HSSFRow fromRow = null;
        HSSFRow newRow = null;

        HSSFCell newCell = null;
        HSSFCell fromCell = null;
        // 设置列宽
        for (int i = firstrow; i < lastrow; i++) {
            fromRow = fromsheet.getRow(i);
            if (fromRow != null) {
                for (int j = fromRow.getLastCellNum(); j >= fromRow.getFirstCellNum(); j--) {
                    int colnum = fromsheet.getColumnWidth(j);
                    if (colnum > 100) {
                        newSheet.setColumnWidth(j, colnum);
                    }
                    if (colnum == 0) {
                        newSheet.setColumnHidden(j, true);
                    } else {
                        newSheet.setColumnHidden(j, false);
                    }
                }
                break;
            }
        }

        // 复制行并填充数据
        for (int i = 0; i < lastrow; i++) {
            fromRow = fromsheet.getRow(i);
            if (fromRow == null) {
                continue;
            }
            newRow = newSheet.createRow(i - firstrow);
            newRow.setHeight(fromRow.getHeight());
            for (int j = fromRow.getFirstCellNum(); j < fromRow.getPhysicalNumberOfCells(); j++) {
                fromCell = fromRow.getCell(j);
                if (fromCell == null) {
                    continue;
                }

                newCell = newRow.createCell(j);
                newCell.setCellStyle(fromCell.getCellStyle());
                // newCell.getCellStyle().cloneStyleFrom(fromCell.getCellStyle());
                int cType = fromCell.getCellType();
                newCell.setCellType(cType);
                switch (cType) {
                    case HSSFCell.CELL_TYPE_STRING:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(fromCell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        newCell.setCellValue(fromCell.getCellFormula());
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(fromCell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        newCell.setCellValue(fromCell.getErrorCellValue());
                        break;
                    default:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                }
            }
        }
    }

    /**
     * 复制sheet页中的数据及列宽
     *
     * @param fromsheet 模板Sheet页
     * @param newSheet  新建Sheet页
     */
    public static void copySheetData(HSSFSheet fromsheet, HSSFSheet newSheet) {

        int firstrow = fromsheet.getFirstRowNum();
        int lastrow = fromsheet.getLastRowNum();

        // 设置列宽
        for (int i = firstrow; i < lastrow; i++) {
            HSSFRow fromRow = fromsheet.getRow(i);
            if (fromRow != null) {
                for (int j = fromRow.getLastCellNum(); j >= fromRow.getFirstCellNum(); j--) {
                    int colnum = fromsheet.getColumnWidth(j);
                    if (colnum > 100) {
                        newSheet.setColumnWidth(j, colnum);
                    }
                    if (colnum == 0) {
                        newSheet.setColumnHidden(j, true);
                    } else {
                        newSheet.setColumnHidden(j, false);
                    }
                }
                break;
            }
        }

        // 复制行并填充数据
        for (int i = 0; i <= lastrow; i++) {
            HSSFRow fromRow = fromsheet.getRow(i);
            if (fromRow == null) {
                continue;
            }
            HSSFRow newRow = newSheet.getRow(i);
            if (newRow == null) {
                continue;
            }
            HSSFCell fromCell = null;
            HSSFCell newCell = null;
            newRow.setHeight(fromRow.getHeight());
            for (int j = fromRow.getFirstCellNum(); j < fromRow.getPhysicalNumberOfCells(); j++) {
                fromCell = fromRow.getCell(j);
                if (fromCell == null) {
                    continue;
                }
                newCell = newRow.getCell(j);
                if (newCell == null) {
                    continue;
                }
                // newCell.setCellStyle(fromCell.getCellStyle());
                // newCell.getCellStyle().cloneStyleFrom(fromCell.getCellStyle());
                int cType = fromCell.getCellType();
                newCell.setCellType(cType);
                switch (cType) {
                    case HSSFCell.CELL_TYPE_STRING:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        newCell.setCellValue(fromCell.getNumericCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        newCell.setCellValue(fromCell.getCellFormula());
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        newCell.setCellValue(fromCell.getBooleanCellValue());
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                        newCell.setCellValue(fromCell.getErrorCellValue());
                        break;
                    default:
                        newCell.setCellValue(fromCell.getRichStringCellValue());
                        break;
                }
            }
        }
    }

    /**
     * 获取Excel的单元格合并数据
     *
     * @param rows       行合并集合
     * @param columns    列合并集合
     * @param startRow   合并起始行
     * @param startCol   合并起始列
     * @param mergedList 需要合并的行（列）集合
     * @return
     */
    public static List<CellRangeAddress> getMergedRegions(List<Map<String, Object>> rows,
                                                          List<Map<String, Object>> columns, int startRow, int startCol, Integer[] mergedList) {
        List<CellRangeAddress> regionList = new ArrayList<CellRangeAddress>();

        if (EditUtil.isEmptyOrNull(mergedList) || mergedList.length <= 0) {
            return regionList;
        }

        int mergedRowNum = 0;
        int mergedColNum = 0;
        int startMergedRowNum = startRow;
        int startMergedColNum = startCol;
        if (!EditUtil.isEmptyOrNull(rows)) {
            for (Map<String, Object> rowMap : rows) {
                if (!EditUtil.isEmptyOrNull(rowMap.get("mergedRowNum"))) {
                    mergedRowNum = EditUtil.intPByNullToZero(rowMap.get("mergedRowNum").toString());
                }
                if (mergedRowNum != 0) {
                    mergedRowNum = mergedRowNum - 1;
                }
                for (Integer target : mergedList) {
                    CellRangeAddress region = new CellRangeAddress(startMergedRowNum, startMergedRowNum + mergedRowNum,
                            target, target);
                    regionList.add(region);
                }
                startMergedRowNum += mergedRowNum + 1;
            }
        }

        if (!EditUtil.isEmptyOrNull(columns)) {
            for (Map<String, Object> colMap : columns) {
                if (!EditUtil.isEmptyOrNull(colMap.get("mergedColNum"))) {
                    mergedRowNum = EditUtil.intPByNullToZero(colMap.get("mergedColNum").toString());
                }
                if (mergedColNum != 0) {
                    mergedColNum = mergedColNum - 1;
                }
                for (Integer target : mergedList) {
                    CellRangeAddress region = new CellRangeAddress(target, target, startMergedColNum,
                            startMergedColNum + mergedColNum);
                    regionList.add(region);
                }
                startMergedColNum += mergedColNum;
            }
        }

        return regionList;
    }

    /**
     * 向Excel中写入数据并进行单元格的合并
     *
     * @param workbook             Excel对象
     * @param startRow             写数据起始行
     * @param startCol             写数据起始列
     * @param cellRangeAddressList 单元格合并集合
     * @param colArr               加载数据的顺序
     * @param retDataList          数据源集合
     */
    public static void exportExcelMerged(Workbook workbook, int startRow, int startCol,
                                         List<CellRangeAddress> cellRangeAddressList, String[] colArr, List<Map<String, Object>> retDataList) {

        // 设置EXCLE文件属性
        HSSFCellStyle cs = (HSSFCellStyle) workbook.createCellStyle();
        // HSSFCellStyle cs_top = (HSSFCellStyle)workbook.createCellStyle();
        HSSFFont font = (HSSFFont) workbook.createFont();
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("微软雅黑");
        // font.setItalic(true);
        // font.setStrikeout(true);
        font.setFontHeightInPoints((short) 11);// 设置字体大小

        // 指定单元格自动换行,align,border,color
        cs.setWrapText(true);
        cs.setFont(font);
        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs.setFillBackgroundColor(HSSFColor.WHITE.index);
        // cs.setFillBackgroundColor(HSSFCellStyle.NO_FILL);
        // cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // align,border,color
        // cs_top.setFont(font);
        // cs_top.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        // cs_top.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // cs_top.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // cs_top.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // cs_top.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // cs_top.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        // cs_top.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(0);
        Map<String, Object> map = new HashMap<String, Object>();
        // 写入数据到sheet页中
        if (!EditUtil.isEmptyOrNull(retDataList)) {
            for (int i = 0; i < retDataList.size(); i++) {
                map = retDataList.get(i);
                HSSFRow row = sheet.createRow(startRow + i);
                // 数据库字段
                for (int j = 0; j < colArr.length; j++) {
                    String colName = colArr[j];
                    HSSFCell cell = row.createCell(j + startCol);
                    String colValue = EditUtil.objByNullToValue(map.get(colName));
                    if (!EditUtil.objByNullToValue(map.get("qualicationCode")).startsWith("A")
                            && "qualicationStdDesc".equalsIgnoreCase(colName)) {
                        cs.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    } else {
                        cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    }
                    cell.setCellStyle(cs);
                    cell.setCellValue(colValue);
                }
            }
        }

        // 合并单元格
        Map<Integer, Integer> xhMap = new HashMap<Integer, Integer>();
        Integer xh = 1;
        if (!EditUtil.isEmptyOrNull(cellRangeAddressList)) {
            for (int i = 0; i < cellRangeAddressList.size(); i++) {
                CellRangeAddress megBean = cellRangeAddressList.get(i);
                if ("XH".equalsIgnoreCase(colArr[0])) {
                    int firstRow = megBean.getFirstRow();
                    if (!xhMap.containsKey(firstRow)) {
                        xhMap.put(firstRow, firstRow);
                        setCellValue(sheet, firstRow, 0, (xh++) + "");
                    }
                }
                sheet.addMergedRegion(megBean);
            }
        }
    }

    /**
     * 将多个workbook添加到一个压缩流中
     *
     * @param zos
     * @param wbMap
     */
    public static void zipWorkbook(ZipOutputStream zos, Map<String, Workbook> wbMap) {
        if (!EditUtil.isEmptyOrNull(wbMap)) {
            for (String wbName : wbMap.keySet()) {
                // 文件流
                byte[] wbByte = getByteByWorkbook(wbMap.get(wbName));
                // 将每个建议的附件添加到一个压缩流
                ZipEntry ze = new ZipEntry(wbName);
                try {
                    wbName = new String(wbName.getBytes(), "UTF-8");
                    zos.putNextEntry(ze);
                    zos.write(wbByte);
                    zos.closeEntry();
                    wbByte = null;
                } catch (IOException e) {
                    System.err.println("压缩Excel文件出错！");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取workbook的二进制数据
     *
     * @param workbook
     * @return byte[]
     */
    public static byte[] getByteByWorkbook(Workbook workbook) {
        ByteArrayOutputStream zipEntryOut = new ByteArrayOutputStream();
        byte[] b = null;
        try {
            workbook.write(zipEntryOut);
            b = zipEntryOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                zipEntryOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return b;
    }

    /**
     * 根据单元格的类型获取单元格值
     *
     * @param row
     * @return
     */
    public static String getColValue(Row row, int colNum) {
        String cellValue = "";
        if (!EditUtil.isEmptyOrNull(row)) {
            Cell cell = row.getCell(colNum);
            if (!EditUtil.isEmptyOrNull(cell)) {
                cellValue = getColValue(cell);
            }
        }
        return cellValue;
    }

    /**
     * 根据单元格的类型获取单元格值
     *
     * @param cell
     * @return
     */
    public static String getColValue(Cell cell) {
        String colStr = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BLANK:
                    colStr = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    colStr = Boolean.toString(cell.getBooleanCellValue());
                    break;
                // 数值
                case Cell.CELL_TYPE_NUMERIC:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String temp = cell.getStringCellValue();
                    // 判断是否包含小数点，如果不含小数点，则以字符串读取，如果含小数点，则转换为Double类型的字符串
                    if (temp.indexOf(".") > -1) {
                        colStr = String.valueOf(new Double(temp)).trim();
                    } else {
                        colStr = temp.trim();
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    colStr = cell.getStringCellValue().trim();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    colStr = "";
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    if (cell.getStringCellValue() != null) {
                        colStr = cell.getStringCellValue().replaceAll("#N/A", "").trim();
                    }
                    break;
                default:
                    colStr = "";
                    break;
            }
        }
        return colStr;
    }

    public static void setCellValue(Row row, int colNum, Object value, HSSFCellStyle hssfCellStyle) {
        if (!EditUtil.isEmptyOrNull(row)) {
            Cell cell = row.getCell(colNum);
            if (EditUtil.isEmptyOrNull(cell)) {
                cell = row.createCell(colNum);
            }
            cell.setCellStyle(hssfCellStyle);
            cell.setCellValue(EditUtil.objByNullToValue(value));
        }
    }

    public static void setCellValue(Row row, int colNum, Object value) {
        if (!EditUtil.isEmptyOrNull(row)) {
            Cell cell = row.getCell(colNum);
            if (EditUtil.isEmptyOrNull(cell)) {
                cell = row.createCell(colNum);
            }
            cell.setCellValue(EditUtil.objByNullToValue(value));
        }
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, Object value) {
        if (!EditUtil.isEmptyOrNull(sheet)) {
            Row row = sheet.getRow(rowNum);
            if (EditUtil.isEmptyOrNull(row)) {
                row = sheet.createRow(rowNum);
            }
            setCellValue(row, colNum, value);
        }
    }

    public static void setCellValue(HSSFSheet sheet, int rowNum, int colNum, Object value,HSSFCellStyle hssfCellStyle) {
        if (!EditUtil.isEmptyOrNull(sheet)) {
            Row row = sheet.getRow(rowNum);
            if (EditUtil.isEmptyOrNull(row)) {
                row = sheet.createRow(rowNum);
            }
            setCellValue(row,colNum,value,hssfCellStyle);
        }
    }

    public static void setCellValue(Workbook workbook, Object value, int sheetNum, int rowNum, int colNum) {
        if (!EditUtil.isEmptyOrNull(workbook)) {
            HSSFSheet sheet = (HSSFSheet) workbook.getSheetAt(sheetNum);
            if (!EditUtil.isEmptyOrNull(sheet)) {
                setCellValue(sheet, rowNum, colNum, value);
            }
        }
    }

    /**
     * 获取导入成功的条数
     *
     * @param a
     * @return long
     */
    public static long getSuccessCount(int[] a) {
        long count = 0;
        if (!EditUtil.isEmptyOrNull(a)) {
            for (int i : a) {
                count += i;
            }
        }
        return count;
    }

    /**
     * excel删除行
     *
     * @param sheet    sheet
     * @param startRow 起始行
     * @param endRow   结束行
     */
    public static void deleteRow(HSSFSheet sheet, int startRow, int endRow) {
        int lastRowNum = sheet.getLastRowNum();
        // 此方法是将余下的行向上移
        if (lastRowNum >= endRow) {
            sheet.shiftRows(endRow, lastRowNum, (startRow - endRow - 1));
        }
        sheet.removeRow(sheet.getRow(lastRowNum));
    }

    /**
     * 获取导入失败的条数
     *
     * @param a
     * @return long
     */
    public static long getFailCount(int[] a) {
        long count = 0;
        if (!EditUtil.isEmptyOrNull(a)) {
            for (int i : a) {
                if (i == 0) {
                    count += 1;
                }
            }
        }
        return count;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

//    以下是HSSF导出excel方式******************************************************************************************************

    /**
     * HSSF创建导出excel功能
     *
     * @param response
     * @param sheetName sheet名
     * @param fileName  excel文件名
     * @param title     excel标题
     * @param retList   查询出来的数据集
     * @param dateKey   excel数据
     * @param wb
     */
    public static void getHSSFWorkbook(HttpServletResponse response, String sheetName, String fileName, String[] title,
                                       List<Map> retList, Object[] dateKey, HSSFWorkbook wb) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        // 声明列对象
        HSSFCell cell = null;
        // 创建标题
        HSSFCellStyle headStyle = getHeadStyle(wb);// 表头字体样式
        HSSFCellStyle bodyStyle = getBodyStyle(wb);// 内容字体样式
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            // 表头注入样式
            cell.setCellStyle(style);
            cell.setCellStyle(headStyle);
        }

        //数据格式化
        Object[][] values = dataFormat(retList, title, dateKey);
        // 创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                // 将内容按顺序赋给对应的列对象
                if (values[i][j] == null || values[i][j] == "") {
                    values[i][j] = "";
                }
                cell = row.createCell(j);
                cell.setCellValue(values[i][j].toString());
                // 内容注入样式
                cell.setCellStyle(style);
                cell.setCellStyle(bodyStyle);

                // 获得最大列宽
                int width = values.length * 300;

            }
            // 设置统一列宽
            for (int index = 0; index < values.length; index++) {
                Integer width = values.length;
                width = width < 2500 ? 2500 : width + 2500;
                width = width > 10000 ? 10000 + 300 : width + 2500;
                sheet.setColumnWidth(index, width);
            }
        }
        //写入io流
        setResponseHeader(response, fileName, wb);
    }

    //数据格式化
    public static Object[][] dataFormat(List<Map> retList, String[] title, Object[] dateKey) {
        Map<String, Object> date = new HashMap<>();
        Object[][] content = new Object[retList.size()][];
        for (int i = 0; i < retList.size(); i++) {
            content[i] = new Object[title.length];
            date = retList.get(i);
            for (int j = 0; j < dateKey.length; j++) {
                content[i][j] = date.get(dateKey[j]);
            }
        }
        return content;
    }

    // io流
    public static void setResponseHeader(HttpServletResponse response, String fileName, HSSFWorkbook wb) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            // 响应到客户端
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //表头样式设置
    public static HSSFCellStyle getHeadStyle(HSSFWorkbook workbook) {
        // 表头
        HSSFCellStyle fontStyle = workbook.createCellStyle();
        HSSFFont font1 = workbook.createFont();
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font1.setFontName("黑体");
        font1.setFontHeightInPoints((short) 14);// 设置字体大小
        fontStyle.setFont(font1);
        fontStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        fontStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        fontStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        fontStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        fontStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        return fontStyle;
    }

    //内容样式设置
    public static HSSFCellStyle getBodyStyle(HSSFWorkbook workbook) {
        // 内容
        HSSFCellStyle fontStyle2 = workbook.createCellStyle();
        HSSFFont font2 = workbook.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 10);// 设置字体大小
        fontStyle2.setFont(font2);
        fontStyle2.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
        fontStyle2.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
        fontStyle2.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
        fontStyle2.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
        fontStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        return fontStyle2;
    }

}
