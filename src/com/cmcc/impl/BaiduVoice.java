package com.cmcc.impl;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.util.Util;
import com.cmcc.inf.VoiceStrategy;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2018年10月16日 上午9:46:59		zhouqiang		created.<br>
 */

public class BaiduVoice implements VoiceStrategy {
    //设置APPID/AK/SK
    public static final String APP_ID = "14444256";
    public static final String API_KEY = "G6tyoe5W6GYQGetX4NP3jDzA";
    public static final String SECRET_KEY = "GGY8azI4UUOXhUUg6d6lzN9rT7pzvFoM";

    public static void main(String[] args) {
        new BaiduVoice().transformVoice2Text("F:\\语音文件\\查询8月22号十点福州仓山区无线接通率.wav");

    }

	@Override
	public String transformVoice2Text(String audioFilePath) {
		AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);
		String res = "";
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, Object> para = new HashMap<String, Object>();
        para.put("dev_pid", 1536);
		JSONObject asrRes = client.asr(audioFilePath, "wav", 16000, para);
        try {
			res = asrRes.getString("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
        System.out.println(res);
		return res;
	}
}