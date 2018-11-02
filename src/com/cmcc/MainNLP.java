package com.cmcc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.cmcc.analysis.jieba.JiebaSegmenter;
import com.cmcc.analysis.jieba.SegToken;
import com.cmcc.analysis.jieba.WordDictionary;
import com.cmcc.analysis.jieba.JiebaSegmenter.SegMode;
import com.cmcc.time.DateUtil;
import com.cmcc.time.TimeNormalizer;
import com.cmcc.time.TimeUnit;
import com.cmcc.util.Const;
import com.cmcc.util.DBUtil;

/**
 *
 * Description: 语义解析类<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年7月24日 上午10:16:46 zhouqiang created.<br>
 */

public class MainNLP {


	private static HashSet<String> kpi_sets = new HashSet<String>();
	private static HashSet<String> city_sets = new HashSet<String>();
	private static HashSet<String> region_sets = new HashSet<String>();
	private static HashSet<String> vendor_sets = new HashSet<String>();
	private static HashSet<String> fz_sets = new HashSet<String>();
	private static HashSet<String> covertype_sets = new HashSet<String>();
	
	private static WordDictionary dictAdd;
	static{
		readText("wydict/kpi.dict", kpi_sets);
		readText("wydict/city_name.dict", city_sets);
		readText("wydict/region_name.dict", region_sets);
		readText("wydict/vendor_name.dict", vendor_sets);
		readText("wydict/591.dict", fz_sets);
		readText("wydict/cover_type.dict", covertype_sets);
		
		//加载自定义词典
		dictAdd = WordDictionary.getInstance();
	    dictAdd.loadUserDict("wydict/kpi.dict");
	    dictAdd.loadUserDict("wydict/city_name.dict");
	    dictAdd.loadUserDict("wydict/region_name.dict");
	    dictAdd.loadUserDict("wydict/vendor_name.dict");
	    dictAdd.loadUserDict("wydict/591.dict");
	    dictAdd.loadUserDict("wydict/cell_name.dict");
	    dictAdd.loadUserDict("wydict/cover_type.dict");
	}
	
	/**
	 * 
	 * @param text 待解析文本输入
	 * @return HashMap [beginTime, endTime, query_param, query_sql]
	 */
	public HashMap<String, String> translateNLP(String text){

		HashMap<String, String> resultMap = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		
		TimeNormalizer normalizer = new TimeNormalizer("TimeExp.m");
	    normalizer.setPreferFuture(false);
    	/*
    	 * 时间抽取模块
    	 */
		String beginTime = "";
		String endTime = "";
    	normalizer.parse(text);// 抽取时间
		TimeUnit[] unit = normalizer.getTimeUnit();
		//只有一个时间点
		if(unit.length == 1){
			if(!unit[0].Time_Norm.contains("日")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMM");
			}else if(!unit[0].Time_Norm.contains("时")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMMdd");
			}else if(!unit[0].Time_Norm.contains("分")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMMddHH");
			}
		}
		//含有2个时间点，区间时间
		else if(unit.length == 2){
			if(!unit[0].Time_Norm.contains("日")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMM");
				endTime = DateUtil.formatDate(unit[1].getTime(), "yyyyMM");
			}else if(!unit[0].Time_Norm.contains("时")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMMdd");
				endTime = DateUtil.formatDate(unit[1].getTime(), "yyyyMMdd");
			}else if(!unit[0].Time_Norm.contains("分")){
				beginTime = DateUtil.formatDate(unit[0].getTime(), "yyyyMMddHH");
				endTime = DateUtil.formatDate(unit[1].getTime(), "yyyyMMddHH");
			}
		}else{
			throw new RuntimeException("查询无效，未提供查询时间。");
			//return "Invalid query request! unknown time!";
		}
		resultMap.put("begin_time", beginTime);
		resultMap.put("end_time", endTime);
		
//		System.out.println("beginTime=>"+beginTime+" endTime=>"+endTime);
//		for (int i = 0; i < unit.length; i++) {
//			System.out.println("时间文本:" + unit[i].Time_Expression + ",对应时间:"
//					+ DateUtil.formatDateDefault(unit[i].getTime()) 
//					+" "+ unit[i].Time_Norm);
//		}
		
		/*
    	 * 分词模块
    	 */
		StringBuffer city = new StringBuffer("");
		StringBuffer region = new StringBuffer("");
		StringBuffer vendor = new StringBuffer("");
		String location = "";
		String covertype = "";
		String kpi = "";
		
		ArrayList<String> word_list = new ArrayList<String>();
		JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> list = segmenter.process(text, SegMode.SEARCH);

        for (SegToken token : list) {

        	String word = token.word.toUpperCase();
        	word_list.add(word);
        	for (String s : city_sets) {
				if(token.word.indexOf(s)!=-1){
					city.append(",'").append(s).append("'");
					sb.append(s);
				}
			}
        	if("全网".equals(word) || "全省".equals(word)){
        		city.append("'福州','厦门','泉州','漳州','龙岩','三明','南平','莆田','宁德'");
        		sb.append(word);
        	}
        	for (String s : region_sets) {
				if(word.indexOf(s)!=-1){
					region.append(",'").append(s).append("'");
					sb.append(word);
				}
			}
        	if(vendor_sets.contains(word)){
        		vendor.append(",'").append(word).append("'");
        		sb.append(word);
        	}
        	if(fz_sets.contains(word) && !region_sets.contains(word) && !city_sets.contains(word)){
        		location = word;
        		sb.append(word);
        	}
        	if(covertype_sets.contains(word)){
        		covertype = word;
        		sb.append(word);
        	}
        	if(kpi_sets.contains(word)){
        		kpi = word;
        		resultMap.put("kpi", word);
        	}
		}
        
        if(city.toString().startsWith(",")){
        	city.replace(0, 1, "");
        }
        if(region.toString().startsWith(",")){
        	region.replace(0, 1, "");
        }
        if(vendor.toString().startsWith(",")){
        	vendor.replace(0, 1, "");
        }
        resultMap.put("query_param", sb.toString());
        System.out.println(city+";"+region+";"+vendor+";"+covertype+";"+location);
        //yyyyMMddHH 查询7月22日10点海润尊品小区无线接通率
        System.out.println(beginTime);
        String query_sql = "";
        if(beginTime.length() == 10){
        	if("".equals(endTime)){
        		endTime = beginTime;
        	}
        	query_sql = genSQL(kpi,beginTime,endTime,city.toString(),region.toString(),vendor.toString(),covertype,location);
        	
        }else if(beginTime.length() == 8){
        	if("".equals(endTime)){
        		endTime = beginTime;
        	}
    		query_sql = genSQLDD(kpi,beginTime,endTime,city.toString(),region.toString(),vendor.toString(),covertype,location);
        }else if(beginTime.length() == 6){
        	beginTime = beginTime + "01";//月表存放每个小区月粒度的汇总数据，日期以每个月1耗代表当月月份，比如2018年9月则time=20180901
        	if("".equals(endTime)){
        		endTime = beginTime;
        	}
        	query_sql = genSQLMM(kpi,beginTime,endTime,city.toString(),region.toString(),vendor.toString(),covertype,location);
        }
        resultMap.put("query_sql", query_sql);
//        System.out.println("query_sql => "+query_sql);
//        Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//			conn = DBUtil.getConnection();
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery(query_sql);
//			while (rs.next()) {
//				res = text.replace("查询", "").replace("。", "")+": "+rs.getString(1);
//				System.out.println(res);
//				System.out.println("------------------------");
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			DBUtil.close(rs, stmt, conn);
//		}
        
		return resultMap;
	}
	
	public static void main(String[] args) {
		MainNLP nlp = new MainNLP();
 		String[] testCaseArray = {
//				"查询7月22号10点福州仓山区海润尊品室内的中兴小区的RRC连接建立请求次数"
// 				,"查询昨天博仕后家园小区E-RAB建立成功率"
// 				,"查询10月全网的无线接通率指标"
// 				,"查询10月22号十一点福州仓山区华为室内小区的无线接通率"
// 				,"查询上周一至上周五福州和厦门的rrc连接建立请求次数",
 				"上上个月全网的小区级无线掉线率"};

//		TimeNormalizer normalizer = new TimeNormalizer("data/TimeExp.m");
//		normalizer.setPreferFuture(false);
//		String input = "查询上上月全网的无线接通率指标";
//		normalizer.parse(input);// 抽取时间
//		TimeUnit[] unit = normalizer.getTimeUnit();
//		System.out.println(DateUtil.formatDateDefault(unit[0].getTime()) + "-"
//				+ unit[0].getIsAllDayTime());
		// System.out.println(DateUtil.formatDateDefault(unit[1].getTime()) +
		// "-" + unit[1].getIsAllDayTime());
 		
	    for (String testCase : testCaseArray){
	    	System.out.println(nlp.translateNLP(testCase));
	    }
	    
	}
	
	//小时表查询
	private static String genSQL(String kpi, String begintime, String endtime, String city,
			String region, String vendor, String covertype, String location) {
        StringBuffer sb = new StringBuffer();
        if("无线接通率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHA06)/sum(ENBHA05)) * (sum(ENBHB06)/sum(ENBHB05)) *100.0, 2),'%') ");
		else if("RRC连接建立成功率".equals(kpi))
	    	sb.append("select concat(round(sum(ENBHA06)*100.0/sum(ENBHA05),2),'%') ");
	    else if("E-RAB建立成功率".equals(kpi)||"ERAB建立成功率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHB06)*100.0/sum(ENBHB05)),2),'%') ");
	    else if("RRC连接建立请求次数".equals(kpi))
	    	sb.append("select sum(ENBHA05) ");
	    else if("RRC连接建立成功次数".equals(kpi))
	    	sb.append("select sum(ENBHA06) ");
	    else if("E-RAB建立请求数".equals(kpi)||"ERAB建立请求数".equals(kpi))
	    	sb.append("select sum(ENBHB05) ");
	    else if("E-RAB建立成功数".equals(kpi)||"ERAB建立成功数".equals(kpi))
	    	sb.append("select sum(ENBHB06) ");
	    else if("小区级无线掉线率".equals(kpi))
	    	sb.append("select concat(to_char((sum(ENBHC07) - sum(ENBHC07_10))*100.0 / (sum(ENBHC02)+sum(ENBHC09_BEF)+sum(ENBHD23)+sum(ENBHA08_N)),'fm99990.00'),'%') ");
	    else
	    	throw new RuntimeException("查询无效，未知的指标名称。");
	    	//return "Invalid query request! unknown kpi!";
        
        sb.append(" from "+Const.TDL_PM_CELL+" where 1=1 ");
        sb.append(" and time>=to_date('").append(begintime).append("','yyyymmddhh24')");
        sb.append(" and time<=to_date('").append(endtime).append("','yyyymmddhh24')");
//      sb.append(" and date_format(time,'%Y%m%d%H')>=").append(begintime);
//		sb.append(" and date_format(time,'%Y%m%d%H')<=").append(endtime);
		if(!"".equals(city)){
			sb.append(" and REGION_NAME in (").append(city).append(")");
		}
		if(!"".equals(region)){
			sb.append(" and CITY_NAME in (").append(region).append(")");
		}
		if(!"".equals(vendor)){
			sb.append(" and PRODUCTOR in (").append(vendor).append(")");
		}
		if(!"".equals(covertype)){
			sb.append(" and COVER_TYPE='").append(covertype).append("'");
		}
		if(!"".equals(location)){
			sb.append(" and ENBAJ02 like '%").append(location).append("%'");
		}
		return sb.toString();
	}
	
	//月表查询
	private static String genSQLMM(String kpi, String begintime, String endtime, String city,
			String region, String vendor, String covertype, String location) {
		StringBuffer sb = new StringBuffer();
		if("无线接通率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHA06)/sum(ENBHA05)) * (sum(ENBHB06)/sum(ENBHB05)) *100.0, 2),'%') ");
		else if("RRC连接建立成功率".equals(kpi))
	    	sb.append("select concat(round(sum(ENBHA06)*100.0/sum(ENBHA05),2),'%') ");
	    else if("E-RAB建立成功率".equals(kpi)||"ERAB建立成功率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHB06)*100.0/sum(ENBHB05)),2),'%') ");
	    else if("RRC连接建立请求次数".equals(kpi))
	    	sb.append("select sum(ENBHA05) ");
	    else if("RRC连接建立成功次数".equals(kpi))
	    	sb.append("select sum(ENBHA06) ");
	    else if("E-RAB建立请求数".equals(kpi)||"ERAB建立请求数".equals(kpi))
	    	sb.append("select sum(ENBHB05) ");
	    else if("E-RAB建立成功数".equals(kpi)||"ERAB建立成功数".equals(kpi))
	    	sb.append("select sum(ENBHB06) ");
	    else if("小区级无线掉线率".equals(kpi))
	    	sb.append("select concat(to_char((sum(ENBHC07) - sum(ENBHC07_10))*100.0 / (sum(ENBHC02)+sum(ENBHC09_BEF)+sum(ENBHD23)+sum(ENBHA08_N)),'fm99990.00'),'%') ");
	    else
	    	throw new RuntimeException("查询无效，未知的指标名称。");
	    	//return "Invalid query request! unknown kpi!";
        
        sb.append(" from "+Const.TDL_PM_CELL_MONTH+" where 1=1 ");
        sb.append(" and time>=to_date('").append(begintime).append("','yyyymmdd')");
        sb.append(" and time<=to_date('").append(endtime).append("','yyyymmdd')");
//      sb.append(" and date_format(time,'%Y%m')>=").append(begintime);
//		sb.append(" and date_format(time,'%Y%m')<=").append(endtime);
		if(!"".equals(city)){
			sb.append(" and REGION_NAME in (").append(city).append(")");
		}
		if(!"".equals(region)){
			sb.append(" and CITY_NAME in (").append(region).append(")");
		}
		if(!"".equals(vendor)){
			sb.append(" and PRODUCTOR in (").append(vendor).append(")");
		}
		if(!"".equals(covertype)){
			sb.append(" and COVER_TYPE='").append(covertype).append("'");
		}
		if(!"".equals(location)){
			sb.append(" and ENBAJ02 like '%").append(location).append("%'");
		}
		return sb.toString();
	}
	//日表查询
	private static String genSQLDD(String kpi, String begintime, String endtime, String city,
			String region, String vendor, String covertype, String location) {
		StringBuffer sb = new StringBuffer();
		if("无线接通率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHA06)/sum(ENBHA05)) * (sum(ENBHB06)/sum(ENBHB05)) *100.0, 2),'%') ");
		else if("RRC连接建立成功率".equals(kpi))
	    	sb.append("select concat(round(sum(ENBHA06)*100.0/sum(ENBHA05),2),'%') ");
	    else if("E-RAB建立成功率".equals(kpi)||"ERAB建立成功率".equals(kpi))
	    	sb.append("select concat(round((sum(ENBHB06)*100.0/sum(ENBHB05)),2),'%') ");
	    else if("RRC连接建立请求次数".equals(kpi))
	    	sb.append("select sum(ENBHA05) ");
	    else if("RRC连接建立成功次数".equals(kpi))
	    	sb.append("select sum(ENBHA06) ");
	    else if("E-RAB建立请求数".equals(kpi)||"ERAB建立请求数".equals(kpi))
	    	sb.append("select sum(ENBHB05) ");
	    else if("E-RAB建立成功数".equals(kpi)||"ERAB建立成功数".equals(kpi))
	    	sb.append("select sum(ENBHB06) ");
	    else if("小区级无线掉线率".equals(kpi))
	    	sb.append("select concat(to_char((sum(ENBHC07) - sum(ENBHC07_10))*100.0 / (sum(ENBHC02)+sum(ENBHC09_BEF)+sum(ENBHD23)+sum(ENBHA08_N)),'fm99990.00'),'%') ");
	    else
	    	throw new RuntimeException("查询无效，未知的指标名称。");
	    	//return "Invalid query request! unknown kpi!";
		
        sb.append(" from "+Const.TDL_PM_CELL_DAY+" where 1=1 ");
        sb.append(" and time>=to_date('").append(begintime).append("','yyyymmdd')");
        sb.append(" and time<=to_date('").append(endtime).append("','yyyymmdd')");
//      sb.append(" and date_format(time,'%Y%m%d')>=").append(begintime);
//		sb.append(" and date_format(time,'%Y%m%d')<=").append(endtime);
		if(!"".equals(city)){
			sb.append(" and REGION_NAME in (").append(city).append(")");
		}
		if(!"".equals(region)){
			sb.append(" and CITY_NAME in (").append(region).append(")");
		}
		if(!"".equals(vendor)){
			sb.append(" and PRODUCTOR in (").append(vendor).append(")");
		}
		if(!"".equals(covertype)){
			sb.append(" and COVER_TYPE='").append(covertype).append("'");
		}
		if(!"".equals(location)){
			sb.append(" and ENBAJ02 like '%").append(location).append("%'");
		}
		return sb.toString();
	}

	private static void readText(String userDict, HashSet<String> dict_sets) {
		//path 不以'/'开头时默认是从此类所在的包下取资源，以'/'开头则是从ClassPath根下获取。
		InputStream is = MainNLP.class.getClassLoader().getResourceAsStream(userDict);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            //BufferedReader br = Files.newBufferedReader(new Path(userDict), charset);
            String tempString = null;
            while ((tempString = br.readLine())!=null){
            	dict_sets.add(tempString.split("\t")[0]);
            }
            br.close();
            System.out.println(userDict+"load success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}



