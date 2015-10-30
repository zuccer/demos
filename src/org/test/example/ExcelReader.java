package org.test.example;

import java.io.FileInputStream;     
import java.io.FileNotFoundException;     
import java.io.IOException;     
import java.io.InputStream;     
import java.util.Date;     
import java.util.HashMap;     
import java.util.Map;     
    
import org.apache.poi.hssf.usermodel.HSSFCell;     
import org.apache.poi.hssf.usermodel.HSSFRow;     
import org.apache.poi.hssf.usermodel.HSSFSheet;     
import org.apache.poi.hssf.usermodel.HSSFWorkbook;     
import org.apache.poi.poifs.filesystem.POIFSFileSystem;     
    

public class ExcelReader {     
    private POIFSFileSystem fs;     
    private HSSFWorkbook wb;     
    private HSSFSheet sheet;     
    private HSSFRow row;     
    /**   
     * ��ȡExcel����ͷ������   
     * @param InputStream   
     * @return String ��ͷ���ݵ�����   
     *    
     */    
    public String[] readExcelTitle(InputStream is) {     
        try {     
            fs = new POIFSFileSystem(is);     
            wb = new HSSFWorkbook(fs);     
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
        sheet = wb.getSheetAt(0);     
        row = sheet.getRow(0);     
        //����������     
        int colNum = row.getPhysicalNumberOfCells();     
        String[] title = new String[colNum];     
        for (int i=0; i<colNum; i++) {     
            title[i] = getStringCellValue(row.getCell(i));     
        }     
        return title;     
    }     
         
    /**   
     * ��ȡExcel��������   
     * @param InputStream   
     * @return Map ������Ԫ���������ݵ�Map����   
     */    
    public Map<Integer,String> readExcelContent(InputStream is) {     
        Map<Integer,String> content = new HashMap<Integer,String>();     
        String str = "";     
        try {     
            fs = new POIFSFileSystem(is);     
            wb = new HSSFWorkbook(fs);     
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
        sheet = wb.getSheetAt(0);     
        //�õ�������     
        int rowNum = sheet.getLastRowNum();     
        row = sheet.getRow(0);     
        int colNum = row.getPhysicalNumberOfCells();     
        //��������Ӧ�ôӵڶ��п�ʼ,��һ��Ϊ��ͷ�ı���     
        for (int i = 1; i <= rowNum; i++) {     
            row = sheet.getRow(i);     
            int j = 0;     
            while (j<colNum) {     
        //ÿ����Ԫ�������������"-"�ָ���Ժ���Ҫʱ��String���replace()������ԭ����     
        //Ҳ���Խ�ÿ����Ԫ����������õ�һ��javabean�������У���ʱ��Ҫ�½�һ��javabean     
                str += getStringCellValue(row.getCell(j)).trim() + "\t";     
                j ++;     
            }     
            content.put(i, str);     
            str = "";     
        }     
        return content;     
    }     
         
    /**   
     * ��ȡ��Ԫ����������Ϊ�ַ������͵�����   
     * @param cell Excel��Ԫ��   
     * @return String ��Ԫ����������   
     */    
    private String getStringCellValue(HSSFCell cell) {     
        String strCell = "";     
        switch (cell.getCellType()) {     
        case HSSFCell.CELL_TYPE_STRING:     
            strCell = cell.getStringCellValue();     
            break;     
        case HSSFCell.CELL_TYPE_NUMERIC:     
            strCell = String.valueOf(cell.getNumericCellValue());     
            break;     
        case HSSFCell.CELL_TYPE_BOOLEAN:     
            strCell = String.valueOf(cell.getBooleanCellValue());     
            break;     
        case HSSFCell.CELL_TYPE_BLANK:     
            strCell = "";     
            break;     
        default:     
            strCell = "";     
            break;     
        }     
        if (strCell.equals("") || strCell == null) {     
            return "";     
        }     
        if (cell == null) {     
            return "";     
        }     
        return strCell;     
    }     
         
    /**   
     * ��ȡ��Ԫ����������Ϊ�������͵�����   
     * @param cell Excel��Ԫ��   
     * @return String ��Ԫ����������   
     */    
    private String getDateCellValue(HSSFCell cell) {     
        String result = "";     
        try {     
            int cellType = cell.getCellType();     
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {     
                Date date = cell.getDateCellValue();     
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)      
                + "-" + date.getDate();     
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {     
                String date = getStringCellValue(cell);     
                result = date.replaceAll("[����]", "-").replace("��", "").trim();     
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {     
                result = "";     
            }     
        } catch (Exception e) {     
            System.out.println("���ڸ�ʽ����ȷ!");     
            e.printStackTrace();     
        }     
        return result;     
    }     
         
    public static void main(String[] args) {     
        try {     
            //�Զ�ȡExcel���������     
            InputStream is = new FileInputStream("C://Excel������.xls");     
            ExcelReader excelReader = new ExcelReader();     
            String[] title = excelReader.readExcelTitle(is);
          
            System.out.println("���Excel���ı���:");     
            for (String s : title) {     
                System.out.print(s + "\t");     
            }     
           
            System.out.println();    
            //�Զ�ȡExcel������ݲ���     
            InputStream is2 = new FileInputStream("C://Excel������.xls");     
            Map<Integer,String> map = excelReader.readExcelContent(is2);     
            System.out.println("���Excel��������:");     
            for (int i=1; i<=map.size(); i++) {     
                System.out.println(map.get(i));     
            }     
        } catch (FileNotFoundException e) {     
            System.out.println("δ�ҵ�ָ��·�����ļ�!");     
            e.printStackTrace();     
        }     
    }     
}    