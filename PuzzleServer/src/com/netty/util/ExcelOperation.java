package com.netty.util;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.util.ZipSecureFile.ThresholdInputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.netty.core.HttpServer;

public class ExcelOperation {
	public static ExcelOperation instance=new ExcelOperation();
	private  ExcelOperation() {
	}
	
	@SuppressWarnings("deprecation")
	public  ArrayList<ArrayList<Map<String, String>>> readExcelWithTitle(String filepath) throws Exception {
		String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
		
		InputStream is = null;
		Workbook wb = null;
		try {
			//is = new FileInputStream(filepath);
			is=this.getClass().getResourceAsStream(filepath);
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
			} else {
				throw new Exception("读取的不是excel文件");
			}
			ArrayList<ArrayList<Map<String, String>>> result = new ArrayList<>();
			int sheetSize = wb.getNumberOfSheets();
			for (int i = 0; i < sheetSize; i++) {// 遍历sheet页
				Sheet sheet = wb.getSheetAt(i);
				ArrayList<Map<String, String>> sheetList = new ArrayList<>();
				ArrayList<String> titles = new ArrayList<>();
				int rowSize = sheet.getLastRowNum() + 1;
				for (int j = 0; j < rowSize; j++) {// 遍历行
					Row row = sheet.getRow(j);
					if (row == null) {// 略过空行
						System.out.println("taog");
						continue;
					}
					int cellSize = row.getLastCellNum();// 行中有多少个单元格，也就是有多少列
					if (j == 0) {// 第一行是标题行
						for (int k = 0; k < cellSize; k++) {
							Cell cell = row.getCell(k);
							titles.add(cell.toString());
						}
					} else {// 其他行是数据行
						Map<String, String> rowMap = new HashMap<String, String>();// 对应一个数据行
						for (int k = 0; k < titles.size(); k++) {
							Cell cell = row.getCell(k);
							String key = titles.get(k);
							String value = null;
							if (cell != null) {
								cell.setCellType (Cell.CELL_TYPE_STRING);
								value = String.valueOf(cell);
							}
							rowMap.put(key, value);
						}
						sheetList.add(rowMap);
					}
				}
				result.add(sheetList);
			}
			return result;
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (wb != null) {
				wb.close();
			}
			if (is != null) {
				is.close();
			}
		}
	}
}
