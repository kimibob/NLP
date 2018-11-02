package com.cmcc.util;

import java.io.File;
import java.io.IOException;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * Description: TODO<br>
 * 
 * Modified log:<br>
 * ------------------------------------------------------<br>
 * Ver. Date Author Description<br>
 * ------------------------------------------------------<br>
 * 1.0 2018年10月12日 下午2:51:18 zhouqiang created.<br>
 */

public class HttpUtil {

	// public static String sendOkhttpRequest(String url, String filePath,
	// String fileName){
	//
	// RequestBody requestBody =new MultipartBody.Builder()
	// .setType(MultipartBody.FORM)
	// .addFormDataPart("file", fileName,
	// RequestBody.create(MediaType.parse("multipart/form-data"), new
	// File(filePath)))
	// .build();
	//
	// OkHttpClient client = new OkHttpClient();
	// Request request = new Request.Builder()
	// .url(url)
	// .post(requestBody)
	// .build();
	//
	// Response response = null;
	// String res = "";
	// try {
	// response = client.newCall(request).execute();
	// res = response.body().string();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// // if (!response.isSuccessful()) {
	// // throw new IOException("Unexpected code " + response);
	// // }
	//
	// return res;
	// }

	public static void sendHttpRequest(String url, String filepath,
			String task_name) {

		CloseableHttpClient httpclient = new DefaultHttpClient();
		try {

			HttpPost httppost = new HttpPost(url);
			FileBody bin = new FileBody(new File(filepath));
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

				System.out.println("服务器正常响应.....");
				HttpEntity resEntity = response.getEntity();
				// httpclient自带的工具类读取返回数据
				String res = EntityUtils.toString(resEntity);
				JSONObject jsonObj = JSON.parseObject(res);
				System.out.println(resEntity.getContent());
				// 销毁
				EntityUtils.consume(resEntity);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void getHttpResponse(String url, String task_id) {
		CloseableHttpClient httpclient = HttpClients.createDefault();  
		
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
    		uriBuilder.addParameter("task_id", task_id);
            HttpGet httpget = new HttpGet(uriBuilder.build());  

            // 执行get请求.    
            CloseableHttpResponse response = httpclient.execute(httpget);  
            try {  
                // 获取响应实体    
                HttpEntity entity = response.getEntity();  
                System.out.println("--------------------------------------");  
                // 打印响应状态    
                System.out.println(response.getStatusLine());  
                if (entity != null) {
                    // 打印响应内容长度    
                    System.out.println("Response content length: " + entity.getContentLength());  
                    // 打印响应内容    
                    System.out.println("Response content: " + EntityUtils.toString(entity));  
                }  
                System.out.println("------------------------------------");  
            } finally {  
                response.close();
            }  
        } catch (Exception e) {
            e.printStackTrace();  
        } finally {  
            // 关闭连接,释放资源    
            try {  
                httpclient.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }
        } 
		
	}

	public static void main(String[] args) {
		String token = "591db7d89f372890d781cfae2537b59b.1536316969";
		String fileName = "voice_filename";
		String filePath = "F:\\语音文件\\test.wav";
		String url = "http://jiutian.cmri.cn/srsp/v1/speech_recognition?access_token="
				+ token;
		//sendHttpRequest(url, filePath, fileName);
		getHttpResponse(url, "1743");
		
	}
}
