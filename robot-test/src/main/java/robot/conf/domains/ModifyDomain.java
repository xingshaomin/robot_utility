package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/71e2ebc1a75a4ee48befc1948aee5402");
    	System.out.println(cmd + " -d '[{\"id\":\"chatCommon\",\"status\":1}]'");
    }
}
