package com.netty.util;

import java.awt.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.netty.core.HttpServer;

public class ExcelOperation {

	public static ArrayList<ArrayList<Map<String, String>>> readExcelWithTitle(String filepath) throws Exception {
		String fileType = filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length());
		InputStream is = null;
		Workbook wb = null;
		try {
			is = new FileInputStream(filepath);
			if (fileType.equals("xls")) {
				wb = new HSSFWorkbook(is);
			} else if (fileType.equals("xlsx")) {
				wb = new XSSFWorkbook(is);
			} else {
				throw new Exception("��ȡ�Ĳ���excel�ļ�");
			}
			ArrayList<ArrayList<Map<String, String>>> result = new ArrayList<>();
			int sheetSize = wb.getNumberOfSheets();
			for (int i = 0; i < sheetSize; i++) {// ����sheetҳ
				Sheet sheet = wb.getSheetAt(i);
				ArrayList<Map<String, String>> sheetList = new ArrayList<>();
				ArrayList<String> titles = new ArrayList<>();
				int rowSize = sheet.getLastRowNum() + 1;
				for (int j = 0; j < rowSize; j++) {// ������
					Row row = sheet.getRow(j);
					if (row == null) {// �Թ�����
						continue;
					}
					int cellSize = row.getLastCellNum();// �����ж��ٸ���Ԫ��Ҳ�����ж�����
					if (j == 0) {// ��һ���Ǳ�����
						for (int k = 0; k < cellSize; k++) {
							Cell cell = row.getCell(k);
							titles.add(cell.toString());
						}
					} else {// ��������������
						Map<String, String> rowMap = new HashMap<String, String>();// ��Ӧһ��������
						for (int k = 0; k < titles.size(); k++) {
							Cell cell = row.getCell(k);
							String key = titles.get(k);
							String value = null;
							if (cell != null) {
								value = cell.toString();
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
