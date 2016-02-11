package robot.ask;
import robot.GenUtil;

public class AskTest 
{
	private	static final String appKey = "huanxin_bH0mwkCXoofn";
	private	static final String appSecret = "PrRV3G0cewyceqJGQCYu";
	
	public static void main( String[] args){
    	String curlCmd = GenUtil.getAskCurlCmd(GenUtil.POST, "/ask.do", appKey, appSecret);
    	System.out.println(curlCmd.substring(0, curlCmd.length() -1) + "?platform=custom&question=%E5%95%A6%E5%95%A6%E5%95%A6&userId=a1f6b8d1447c41e0b2befefffd4fdadd&type=0\"");
    }
}
