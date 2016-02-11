package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/3f3c154d5c5b43918c0482a291eee543");
    	System.out.println(cmd + " -d '[{\"id\":\"chatCommon\",\"status\":1}]'");
    }
}
