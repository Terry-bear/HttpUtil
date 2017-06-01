package httputil;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * 对HTTP请求协议做了更改，加快了请求速度
 * @author zhenghua_miao
 *
 */
public class DoNotify {
	private static final String urlbyproperties = ResourceBundle.getBundle("testurl").getString("urlbyproperties");
	private static final String encoding = "UTF-8";
	private static final int timeOut = 2;

	/**
	 * 通知接入渠道
	 */
	public static String doNotify(String notifyUrl,Map<String, String> params) {
		String resCode = null;
		try {
			long beforeCallingTime = System.currentTimeMillis();
			CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(HttpClientUtil.getRequestConfig(timeOut)).build();
//			resCode = HttpClientUtil.doPost(httpClient, notifyUrl, reqContent, "application/json", encoding);
			//application/x-www-form-urlencoded是第三方的content type协议格式,根据不同的格式此处需要进行修改
			resCode = HttpClientUtil.doPost(httpClient, notifyUrl, params, "application/x-www-form-urlencoded", encoding);
			System.out.println("HTTP通知返回, 消耗时长:"+(System.currentTimeMillis() - beforeCallingTime)+" 毫秒");
		}catch (Exception e) {
			System.out.println("HTTP请求失败："+(e.getMessage()));
		}
		return resCode;
	}
	

	public static void main(String[] args) {
		Map<String,String> params = new ConcurrentHashMap<>();
		params.put("芝麻开门","菠萝菠萝蜜");
		doNotify(urlbyproperties,params);
	}
}
