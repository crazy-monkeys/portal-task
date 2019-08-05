package com.crazy.portal.util.system;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 读取Excel文档
 * @author zhouhf
 * 
 */
public class ExcelReader {
	// 得到文件后，采用POIFSFileSystem 进行解析
	private POIFSFileSystem fs;
	// 创建一个Excel文件
	private HSSFWorkbook wb;
	// 创建一个Excel的Sheet
	private HSSFSheet sheet;
	private HSSFRow row;
	private int startRow;

	public ExcelReader(int startRow) {
		this.startRow = startRow;
	}

	public ExcelReader() {
		this.startRow = 1;
	}

	/**
	 * 读取Excel表格表头的内容
	 * @param is
	 * @return String 表头内容的数组
	 */
	@SuppressWarnings("deprecation")
	public String[] readExcelTitle(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			// HSSFRow.Cells[index]获取的是非空单元格集合索引的单元格，其内容不为空
			// HSSFRow.GetCell（index）获取的是实际创建或存在的单元格，但其内容可以为空
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * @param is
	 * @return Map 包含单元格数据内容的Map对象
	 */
	@SuppressWarnings("deprecation")
	public List<Map<String, String>> readExcelContent(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		
//		sheet.shiftRows(1, rowNum, -1);  
//        Row row1=sheet.getRow(rowNum);  
//        sheet.removeRow(row1);  
		
//		HSSFRow tempRow;
//		while (rowNum > 0) {
//			rowNum--;
//			row = sheet.getRow(rowNum);
//			if (row == null) {
//				sheet.shiftRows(rowNum + 1, sheet.getLastRowNum(), -1);
//			}
//			
//		};
		
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = startRow; i <= rowNum; i++) {
			Map<String, String> map = new HashMap<String, String>();
			row = sheet.getRow(i);
			if (row != null) {
				int j = 0;
				while (j < colNum) {
					if (row.getCell((short) j) == null) {
						map.put(j+"", "");
					} else {
						map.put(j+"", getStringCellValue(row.getCell((short) j)).trim());
					}
					j++;
				}
				list.add(map);
			}
			
		}
		return list;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * @param cell Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			DecimalFormat df = new DecimalFormat("0"); 
			strCell = df.format(cell.getNumericCellValue());
			//strCell = String.valueOf(cell.getNumericCellValue());
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
	 * 获取单元格数据内容为日期类型的数据
	 * @param cell Excel单元格
	 * @return String 单元格数据内容
	 */
	@SuppressWarnings( { "unused", "deprecation" })
	private String getDateCellValue(HSSFCell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "").replace("日", "").trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
				// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	public static void main(String[] args) {

	}
}