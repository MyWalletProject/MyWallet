package com.mywallet.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 *  SimpleCORSFilter  Enabling Global
 *  CORS Processing   at global level 
    @author Prashank Jauhari
*/

@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCORSFilter implements Filter {

	private static final  Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

	public SimpleCORSFilter() {
	    log.debug("SimpleCORSFilter init");
	}
    /**
     * Creating response for pre-flight request
     * setting access control origins,
     * Access-Control-Allow-Credentials,
     * Access-Control-Allow-Methods,
     * Access-Control-Allow-Headers
     */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		log.debug("inside Cors filter");
	    HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request=(HttpServletRequest) req;
	    response.setHeader("Access-Control-Allow-Origin", "*");
	    response.setHeader("Access-Control-Allow-Credentials", "true");
	    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PATCH");
	    //response.setHeader("Access-Control-Max-Age", "3600");
	    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me,belrium-token");
	    String token=request.getHeader("belrium-token");
	    log.debug("token in the header : "+token);
		if(request.getMethod().equals("OPTIONS")){
			response.setStatus(HttpServletResponse.SC_OK);
			log.debug("Handled Pre-Flight Request Successfully");
			return ;
		}
        log.debug("forwarding request to next filters/servlets");
	    chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) {}

	@Override
	public void destroy() {
	log.debug("destroying cors filter");
	}

}