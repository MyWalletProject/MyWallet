package com.mywallet.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.MediaType;


public class WalletUtilities {

	public final static Logger logger =Logger.getLogger(WalletUtilities.class);
	
	
	final static public void  errResp(HttpServletRequest request, HttpServletResponse response, String msg ){
		
		Map<String, Object> responseMessage = new HashMap<String, Object>();
		
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				responseMessage.put("timestamp",new Date());
				responseMessage.put("data", null);
				responseMessage.put("isSuccess",false);
				responseMessage.put("isError",true);
				responseMessage.put("message",msg);

				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
				try {
					String json = new JSONObject(responseMessage).toString();
					response.getWriter().write(json);
				} catch (Exception e) {
					logger.error("exception ",e);
				}
				
	}
	
	  
        public static boolean isPublicAction(String actionName){	
        	
    		Set<String> methods=new HashSet<String>();

    		//public APIs in MyWallet 
    		
    		methods.add("/signup");
    		methods.add("/login");
    		methods.add("/password");
    		methods.add("/swagger-ui.html");
    		methods.add("/configuration/security");
    		methods.add("/configuration/ui");
    		methods.add("/swagger-resources");
    		methods.add("/favicon.ico"); 
    		methods.add("/v2/api-docs");
    		methods.add("/error");
    		methods.add("/webjars/springfox-swagger-ui/css/reset.css");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js");
    		methods.add("/webjars/springfox-swagger-ui/css/typography.css");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js");
    		methods.add("/webjars/springfox-swagger-ui/css/screen.css");
    		methods.add("/webjars/springfox-swagger-ui/lib/backbone-min.js");
    		methods.add("/webjars/springfox-swagger-ui/springfox.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/marked.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/swagger-oauth.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/underscore-min.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js");
    		methods.add("/webjars/springfox-swagger-ui/swagger-ui.js"); 
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js");
    		methods.add("/webjars/springfox-swagger-ui/lib/underscore-min.js");
		
    		
    		return methods.contains(actionName);
    	}	   
      
}
