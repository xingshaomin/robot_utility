package robot.kb;

import robot.GenUtil;

public class QueryKB {
	private static final String uid = "8207a922984c4ce887a6abcecd123100";
	private static final String id = "1888f04e19094aee8265fdf401332714";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/kb/custom/" + uid +  "/" + id);
		System.out.println(curlCommandLine);
    }
}
