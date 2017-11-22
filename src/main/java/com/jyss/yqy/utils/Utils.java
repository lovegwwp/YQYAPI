package com.jyss.yqy.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

	// ///////////
	public static List<Long> stringToLongList(String str, String splitStr) {
		List<Long> list = new ArrayList<Long>();
		if (str == null || str.isEmpty()) {
			return list;
		}
		String[] arr = str.split(splitStr);
		for (String id : arr) {
			list.add(Long.valueOf(id));
		}
		return list;
	}

	public static List<String> stringToStringList(String str, String splitStr) {
		List<String> list = new ArrayList<String>();
		if (str == null || str.isEmpty()) {
			return list;
		}
		String[] arr = str.split(splitStr);
		for (String id : arr) {
			list.add(id);
		}
		return list;
	}

	public static String[] stringtoArray(String str, String splitStr) {
		String[] arr = null;
		if (str != null && !(str.isEmpty())) {
			arr = str.split(splitStr);
		}
		return arr;
	}

	public static boolean saveUpload(MultipartFile file, String filePath) {

		File saveDir = new File(filePath);
		if (!saveDir.getParentFile().exists()) {
			saveDir.getParentFile().mkdirs();
		}
		// 判断文件是否为空
		if (!file.isEmpty()) {
			try {
				// 文件路径转存
				file.transferTo(saveDir);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
