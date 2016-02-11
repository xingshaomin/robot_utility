package robot.user;

import robot.GenUtil;

public class QueryUser {
    private static final String uid = "d5656abeb82d42b7be3dbd2b372d1787";
    public static void main( String[] args){
        String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/user/" + uid);
        System.out.println(curlCommandLine);
    }
}
