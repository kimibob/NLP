package com.cmcc.voice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年8月8日 上午11:46:13 zhouqiang created.<br>
 */

public class Test {
	private static final String JSON_OBJ_STR = "[{\"bg\":\"610\",\"ed\":\"7840\",\"nc\":\"1.0\",\"onebest\":\"查询7月22号十点福州仓山区中兴室内小区的无线接通率。\",\"si\":\"0\",\"speaker\":\"0\",\"wordsResultList\":[{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"5\",\"wordEd\":\"66\",\"wordsName\":\"查询\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"66\",\"wordEd\":\"217\",\"wordsName\":\"7月22号\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"0.9999\",\"wordBg\":\"217\",\"wordEd\":\"245\",\"wordsName\":\"十\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"245\",\"wordEd\":\"274\",\"wordsName\":\"点\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"274\",\"wordEd\":\"318\",\"wordsName\":\"福州\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"0.9631\",\"wordBg\":\"318\",\"wordEd\":\"413\",\"wordsName\":\"仓山区\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"413\",\"wordEd\":\"468\",\"wordsName\":\"中兴\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"0.7773\",\"wordBg\":\"468\",\"wordEd\":\"506\",\"wordsName\":\"室内\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"506\",\"wordEd\":\"554\",\"wordsName\":\"小区\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"554\",\"wordEd\":\"583\",\"wordsName\":\"的\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"583\",\"wordEd\":\"636\",\"wordsName\":\"无线\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"1.0000\",\"wordBg\":\"636\",\"wordEd\":\"703\",\"wordsName\":\"接通率\",\"wp\":\"n\"},{\"alternativeList\":[],\"wc\":\"0.0000\",\"wordBg\":\"703\",\"wordEd\":\"703\",\"wordsName\":\"。\",\"wp\":\"p\"},{\"alternativeList\":[],\"wc\":\"0.0000\",\"wordBg\":\"703\",\"wordEd\":\"703\",\"wordsName\":\"\",\"wp\":\"g\"}]}]";
	private static final String JSON_OBJ_STR_2 = "[{'bg':'950','ed':'4710','nc':'1.0','onebest':'查询7月22号十点','si':'0','speaker':'0','wordsResultList':[{'alternativeList':[],'wc':'1.0000','wordBg':'8','wordEd':'107','wordsName':'查询','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'107','wordEd':'277','wordsName':'7月22号','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'277','wordEd':'316','wordsName':'十','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'316','wordEd':'364','wordsName':'点','wp':'n'}]},{'bg':'4730','ed':'9280','nc':'1.0','onebest':'福州仓山区华为室内小区的','si':'1','speaker':'0','wordsResultList':[{'alternativeList':[],'wc':'1.0000','wordBg':'12','wordEd':'80','wordsName':'福州','wp':'n'},{'alternativeList':[],'wc':'0.9990','wordBg':'80','wordEd':'204','wordsName':'仓山区','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'204','wordEd':'280','wordsName':'华为','wp':'n'},{'alternativeList':[],'wc':'0.9261','wordBg':'280','wordEd':'354','wordsName':'室内','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'354','wordEd':'413','wordsName':'小区','wp':'n'},{'alternativeList':[],'wc':'0.9998','wordBg':'413','wordEd':'442','wordsName':'的','wp':'n'}]},{'bg':'9300','ed':'11260','nc':'1.0','onebest':'无线接通率。','si':'2','speaker':'0','wordsResultList':[{'alternativeList':[],'wc':'1.0000','wordBg':'15','wordEd':'88','wordsName':'无线','wp':'n'},{'alternativeList':[],'wc':'1.0000','wordBg':'88','wordEd':'182','wordsName':'接通率','wp':'n'},{'alternativeList':[],'wc':'0.0000','wordBg':'182','wordEd':'182','wordsName':'。','wp':'p'},{'alternativeList':[],'wc':'0.0000','wordBg':'182','wordEd':'182','wordsName':'','wp':'g'}]}]";
	private static final String JSON_OBJ_STR_3 = "{\"code\":200,\"msg\":\"上传成功\",\"data\":{\"task_id\":1744}}";
	public static void main(String[] args) {
//		JSONArray jsonArray = JSON.parseArray(JSON_OBJ_STR);
//		for (int i = 0; i < jsonArray.size(); i++) {
//			System.out.println(jsonArray.getJSONObject(i).getString("onebest"));
//		}
		JSONObject jsonObj = JSON.parseObject(JSON_OBJ_STR_3);
		System.out.println(jsonObj.getJSONObject("data").getString("task_id"));
	}
}
