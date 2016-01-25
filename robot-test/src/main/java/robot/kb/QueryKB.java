package robot.kb;

import robot.GenUtil;

public class QueryKB {
	private static final String uid = "38490282ae77429dbe86a9bd2ab75d36";
	private static final String id = "4d49973e77a948999bbf4d72978b0fb0";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/kb/custom/" + uid +  "/" + id);
		System.out.println(curlCommandLine);
    }
}
