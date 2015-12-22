package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/d948415cb9324801abf39b239dbb2ace");
    	System.out.println(cmd);
    }
}
