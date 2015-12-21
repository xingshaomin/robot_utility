package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/0ecf909c138045a6821138e4f1c1b704");
    	System.out.println(cmd);
    }
}
