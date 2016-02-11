package robot.conf.domains;
import robot.GenUtil;

public class QueryDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.GET, "/conf/domains/3f3c154d5c5b43918c0482a291eee543");
    	System.out.println(cmd);
    }
}
