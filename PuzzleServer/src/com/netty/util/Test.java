package com.netty.util;

import java.util.ArrayList;
import java.util.Map;

public class Test {
	public static void main(String[] args) throws Exception {
		ArrayList<ArrayList<Map<String, String>>> result=ExcelOperation.readExcelWithTitle("src/doc/PaperList.xlsx");
		for(int i=0;i<result.size();i++){
			ArrayList<Map<String, String>> sheet=result.get(i);
			for(int j=0;j<sheet.size();j++){
				Map<String , String> rowmap=sheet.get(j);
				for (Map.Entry<String , String > entry : rowmap.entrySet()) {  	  
				    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
				}
			}
		}
	}
	
}
