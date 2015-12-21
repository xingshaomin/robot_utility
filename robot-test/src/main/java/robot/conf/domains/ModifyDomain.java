package robot.conf.domains;
import robot.GenUtil;

public class ModifyDomain 
{
    public static void main( String[] args){
    	String cmd = GenUtil.getCurlCmd(GenUtil.POST, "/conf/domains/851a1e94c28a44b39f8860def309c1e4");
    	System.out.println(cmd);
    }
}
