package com.cmcc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年10月30日 上午9:04:56 zhouqiang created.<br>
 */

public class OmcNLP {

	public static void main(String[] args) {
//		String msg = "查询基站号990479活动告警，柜号为0，框号为1，槽号为6，并且返回2条结果/记录";
//        System.out.println(getEnodeB(msg));
        
        Pattern pattern = Pattern.compile("(上个月)|(数|多|多少|好几|几|差不多|近|前|后|上|左右)(数|多|多少|好几|几|差不多|近|前|后|上|左右)个月");
        Matcher matcher = pattern.matcher("上上个月");
        while(matcher.find()){
        	System.out.println(matcher.group());
        }
		
        
	}
	
	public static String getCount(String msg){
		
		Pattern pattern = Pattern.compile("(?<=返回)\\d+");
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
		return matcher.group();
	}
	
	public static String getEnodeB(String msg){
		
		Pattern pattern = Pattern.compile("(?<=基站号|基站)\\d+");
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
		return matcher.group();
	}
	
	public static String getCell(String msg){
		
		Pattern pattern = Pattern.compile("(?<=小区号|小区)\\d+");
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
		return matcher.group();
	}


}
