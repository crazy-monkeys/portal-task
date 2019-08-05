package com.crazy.portal.util.system;

import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * CSV工具类
 * 
 */
public class CSVUtil {
	private static final String CHARSETNAME = "UTF-8";
	private static Logger logger = LoggerFactory.getLogger(CSVUtil.class);
	/**
	 * @param outPath 输出目录
	 * @param fileName 文件名 
	 * @param encoding 编码
	 * @param dataList 数据集合
	 * @return
	 */
	public static void exportCsv(String outPath, String fileName, String encoding, Collection<String> dataList) throws Exception{
		if(StringUtils.isEmpty(outPath) || StringUtils.isEmpty(fileName)){
			return;
		}
		if (dataList == null) {
			return;
		}
		File path = new File(outPath);
		if (!path.exists()) {
			path.mkdir();
		}
		File file = new File(String.format("%s%s", outPath, fileName));
		if(!file.exists()){
			file.createNewFile();
		}
		@Cleanup FileOutputStream out = new FileOutputStream(file);
		@Cleanup OutputStreamWriter osw = new OutputStreamWriter(out, encoding);
		for(String data : dataList) {
			osw.write(data);
			osw.write("\r\n");
		}
	}

	/**
	 * 写入文件头部
	 * @param headMap
	 * @param FileOutputStream
	 * @param splitStr
	 * @param row
	 * @throws IOException
	 */
	private static void writeValueInFile(Map headMap, BufferedWriter FileOutputStream, String splitStr, Map<String,Object> row) throws IOException {
		Iterator propertyIterator = headMap.entrySet().iterator();
		try {
			while (propertyIterator.hasNext()) {
				Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
				if(null == row){
					FileOutputStream.write(propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "");
				}else{
					FileOutputStream.write(row.get(propertyEntry.getKey())!= null ? String.valueOf(row.get(propertyEntry.getKey())) : "");
				}
				if (propertyIterator.hasNext()) {
					FileOutputStream.write(splitStr);
				}
			}
		}catch (IOException ex) {
			logger.error("写入文件头部异常",ex);
			throw ex;
		}
	}

	/**
	 * 根据后缀生成 文件
	 * @param data  数据
	 * @param headMap 头
	 * @param filePath 生成的路径
	 * @param fileName 文件名
	 * @param splitStr 分割符
	 * @throws Exception
	 */
	public static File createFile(List<Map<String,Object>> data, Map headMap, String filePath, String fileName, String splitStr,String suffix) throws IOException {

		//File csvFile = File.createTempFile(fileName, suffix, new File(filePath+File.separator));

		File path = new File(filePath);
		if (!path.exists()) {
			path.mkdirs();
		}
		File file = new File(String.format("%s%s%s", filePath, fileName,suffix));
		if(!file.exists()){
			file.createNewFile();
		}
		try(BufferedWriter FileOutputStream= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CHARSETNAME), 1024)){
            writeValueInFile(headMap, FileOutputStream, splitStr, null);
			FileOutputStream.newLine();
			// 写入文件内容
			for (Iterator iterator = data.iterator(); iterator.hasNext();) {
				Map<String,Object> row = (Map<String,Object>)iterator.next();
                writeValueInFile(headMap, FileOutputStream, splitStr, row);
				if (iterator.hasNext()) {
					FileOutputStream.newLine();
				}
			}
			FileOutputStream.flush();
		}catch (Exception e){
			logger.error("",e);
		}
		return file;
	}
}
