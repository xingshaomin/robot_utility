package robot.user;

import robot.GenUtil;

public class QueryUser {
    private static final String uid = "5f899b33c4284c2cad60a561ba4ac70f";
    public static void main( String[] args){
        String curlCommandLine = GenUtil.getCurlCmd(GenUtil.GET, "/user/" + uid);
        System.out.println(curlCommandLine);
    }
}
