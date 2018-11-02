package com.cmcc.impl;

import java.io.File;
import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cmcc.inf.VoiceStrategy;

/**
 *
 * Description: 九天语音接口解析类<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver.		Date		Author			Description<br>
 * ------------------------------------------------------<br>
 * 1.0		2018年10月15日 上午9:19:35		zhouqiang		created.<br>
 */

public class JiuTianVoice  implements VoiceStrategy {

	private static String token = "591db7d89f372890d781cfae2537b59b.1536316969";
	private static String task_name = "voice_taskname";
	private static String filePath = "F:\\语音文件\\test.wav";
	private static String url = "http://jiutian.cmri.cn/srsp/v1/speech_recognition?access_token="
			+ token;
	
	// 语音解析等待时长（秒）
	private static int sleepSecond = 5;

	@Override
	public String transformVoice2Text(String audioFilePath) {
		String task_id = "";
		CloseableHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(url);
			FileBody bin = new FileBody(new File(audioFilePath));
			StringBody task_name_sb = new StringBody(task_name, ContentType.create(
                    "text/plain", Consts.UTF_8));
			HttpEntity reqEntity = MultipartEntityBuilder.create()
                    // 相当于<input type="file" name="file"/>
                    .addPart("file", bin)
                    // 相当于<input type="text" name="userName" value=userName>
                    .addPart("task_name", task_name_sb)
                    .build();
			
			httppost.setEntity(reqEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = response.getEntity();
				// 读取返回数据
				String res = EntityUtils.toString(resEntity);
				JSONObject jsonObj = JSON.parseObject(res);
				task_id = jsonObj.getJSONObject("data").getString("task_id");
				// 销毁
				EntityUtils.consume(resEntity);
				System.out.println("【"+task_id+"】任务已提交.....");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} 

		HttpGet httpget = null;
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			uriBuilder.addParameter("task_id", task_id);
	        httpget = new HttpGet(uriBuilder.build());  
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String voice2text = "";
		int loop_num = 20;
		// 循环等待音频处理结果
		while (loop_num > 0) {
			try {
				// 睡眠1min。另外一个方案是让用户尝试多次获取，第一次假设等1分钟，获取成功后break；
				// 失败的话增加到2分钟再获取，获取成功后break；再失败的话加到4分钟；8分钟；……
				Thread.sleep(sleepSecond * 1000);
				
				// 执行get请求.    
	            CloseableHttpResponse response = httpclient.execute(httpget);  

	            int statusCode = response.getStatusLine().getStatusCode();
	            if (statusCode == HttpStatus.SC_OK) {
					System.out.println("waiting... 任务【"+task_id+"】正在处理中.....");
					// 获取响应实体    
		            HttpEntity entity = response.getEntity();  
		            String res_data = EntityUtils.toString(entity);  
		            JSONObject jsonObj = JSON.parseObject(res_data);
		            String code = jsonObj.getString("code");
		            if("200".equals(code)){
		            	voice2text = jsonObj.getJSONObject("data").getString("result");
		            	break;
		            }
					EntityUtils.consume(entity);

	            }
	            loop_num --;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return voice2text;
	}

	public static void main(String[] args) {
		System.out.println(new JiuTianVoice().transformVoice2Text("F:\\语音文件\\test.wav"));
	}
}
