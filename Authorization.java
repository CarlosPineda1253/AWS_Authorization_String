import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.*;


public class Authorization {
	
	public void createHeader() throws Exception{
		OkHttpClient client = new OkHttpClient();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HHmmss");
		DateFormat timeFormat = new SimpleDateFormat("HHmmss");
		Date date = new Date();
		Date time = new Date();
		String aws_datetime = dateFormat.format(date) + 'T' + timeFormat.format(time) + 'Z';
		
		String aws_AccessKey = "AKIAJW4DXI*****";
		String aws_SecretKey = "73l7+xIVtlWqP8Ng3vfqFGrI+************";
		String aws_region = "us-west-2";
		
		byte[] aws_signature_bytes = getSignatureKey(aws_SecretKey,dateFormat.format(date),aws_region,"execute-api");
		String aws_signature = aws_signature_bytes.toString();
		
		String aws_authorization = "AWS4-HMAC-SHA256 Credential=" + aws_AccessKey + dateFormat.format(date) + "/" + aws_region + "/execute-api/aws4_request, SignedHeaders=canal;content-type;host;x-amz-date, Signature=" + aws_signature;

		Request request = new Request.Builder()
		  .url("https://8jhdfjs.execute-api.us-west-2.amazonaws.com/test/promotions?title.id=VPIM")
		  .get()
		  .addHeader("canal", "wallet")
		  .addHeader("Content-Type", "application/x-www-form-urlencoded")
		  .addHeader("X-Amz-Date", aws_datetime)
		  .addHeader("Authorization", aws_authorization)
		  .addHeader("Cache-Control", "no-cache")
		  .build();

		Response response = client.newCall(request).execute();
		
	}
	
	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return mac.doFinal(data.getBytes("UTF8"));
	}

	static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
	    byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
	    byte[] kDate = HmacSHA256(dateStamp, kSecret);
	    byte[] kRegion = HmacSHA256(regionName, kDate);
	    byte[] kService = HmacSHA256(serviceName, kRegion);
	    byte[] kSigning = HmacSHA256("aws4_request", kService);
	    return kSigning;
	}
}
