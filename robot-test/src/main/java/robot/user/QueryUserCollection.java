/*******************************************************************************
 * Licensed Materials - Property of EaseMob
 * 
 * (c) Copyright EaseMob Corporation 2016. 
 *
 * All Rights Reserved.
 *
 *******************************************************************************/
package robot.user;

import robot.GenUtil;

/**
 * @author shawn
 *
 */
public class QueryUserCollection {
    public static void main( String[] args){
        String curlCommandLine = GenUtil.getCurlCmd(GenUtil.POST, "/users");
        System.out.println(curlCommandLine + " -d '{\"start\":0,\"limit\":20}'");
    }
}
