package com.mywallet.security;

import java.io.IOException;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.mywallet.domain.Token;
import com.mywallet.services.TokenService;
import com.mywallet.util.WalletUtilities;

//@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VerifyTokenFilter implements Filter {

	public final static Logger logger =Logger.getLogger(VerifyTokenFilter.class);

	@Autowired
	private TokenService tokenService;

	public VerifyTokenFilter(){

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("inside VerifyTokenFilter init metod : ");		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		logger.info("inside VerifyTokenFilter doFilter metod : ");	

		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest  req = (HttpServletRequest) request;

		String servletPath = req.getServletPath();
		logger.info("#INFO " + new Date() + " - ServletPath :" + servletPath + ", URL =" + req.getRequestURL());

		boolean flag = false;

		if(WalletUtilities.isPublicAction(servletPath)){
			System.out.println("**********isPublicAction***************");
			flag =true;
		}

		else{

			String	tokenValue = req.getHeader("mywallet-token");
			logger.debug("passed token in header :"+tokenValue);

			if(tokenValue == null) {
				System.out.println("**********Token is empty***************");

				logger.debug("Token is empty");
				flag=false;
			}

			else  {
				System.out.println("**********Token is present ***************");

				logger.info("Trying to authenticate user by Auth-Token method. Token: {}");
				Token tokenObj = tokenService.findByToken(tokenValue);

				logger.info("token object is "+tokenObj);
				if(tokenObj==null){
					logger.error("Token not exist : "+tokenValue);
					WalletUtilities.errResp(req, resp,"token not exist");
				}
				else{
					logger.info("********************token verified succesfully***************");
					chain.doFilter(request, response);
				}
			}
		}	

	}

	@Override
	public void destroy() {
		logger.info("inside VerifyTokenFilter destroy metod : ");		

	}

}
