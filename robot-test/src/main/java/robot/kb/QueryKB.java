package robot.kb;

import robot.GenUtil;

public class QueryKB {
	private static final String uid = "9a46ed8ab4964d918cdac1e432434071";
	private static final String id = "3cb945fa4e8b4ee0a43443c4245a490a";
	public static void main( String[] args){
		String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/kb/custom/" + uid +  "/" + id);
		System.out.println(curlCommandLine);
    }
}
