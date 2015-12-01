package robot;

import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class GenUtil {
	
	public static final String DEL = "DELETE";
	public static final String GET = "GET";
	public static final String POST = "POST";
	
	public static final String appKey = "IFzYxUPqUeBV";
	public static final String appSecret = "8ZrZIDmrKNdu2dQikaVh";
	
	public static final String AdminUrl = "https://cloudapi.xiaoi.com";
	public static final String AskUrl = "http://nlp.xiaoi.com";
    
    public static String getCurlCmd(String method, String relativeUrl) {
    	String curlCommandLine = getCurlCmd(method, AdminUrl, relativeUrl, appKey, appSecret);
    	return curlCommandLine;
	}
    
    public static String getAskCurlCmd(String method, String relativeUrl, String appKey, String appSecret) {
    	return getCurlCmd(method, AskUrl, relativeUrl, appKey, appSecret);
    }

	private static String getCurlCmd(String method, String domain, String relativeUrl, String appKey, String appSecret) {
		String realm = "xiaoi.com";
		String nonce = getNonce();
		String HA1 = DigestUtils.shaHex(StringUtils.join(new String[] { appKey, realm, appSecret }, ":"));
		String HA2 = DigestUtils.shaHex(StringUtils.join(new String[] { method, relativeUrl }, ":"));
		String sign = DigestUtils.shaHex(StringUtils.join(new String[] { HA1, nonce, HA2 }, ":"));
		String xAuth = "app_key=\"" + appKey + "\",nonce=\"" + nonce + "\",signature=\"" + sign + "\"";
		String curlCommandLine = "curl -X " + method +" -H 'X-Auth:"+ xAuth + "' -i \"" + domain + relativeUrl + "\"";
		return curlCommandLine;
	}

	public static String getNonce() {
		byte[] b = new byte[20];
		new Random().nextBytes(b);
		String nonce = new String(Hex.encodeHex(b));
		return nonce;
	}
}
