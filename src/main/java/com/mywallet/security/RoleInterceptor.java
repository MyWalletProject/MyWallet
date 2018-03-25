package com.mywallet.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.mywallet.domain.Action;
import com.mywallet.domain.Role;
import com.mywallet.domain.Token;
import com.mywallet.domain.User;
import com.mywallet.services.ActionService;
import com.mywallet.services.TokenService;
import com.mywallet.util.ObjectMap;
import com.mywallet.util.WalletUtilities;

@Component
public class RoleInterceptor implements HandlerInterceptor{

	private static Logger logger = LoggerFactory.getLogger(RoleInterceptor.class);


	@Autowired
	private ActionService actionService;

	@Autowired
	private TokenService tokenService;
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		System.out.println("+++++++++++++++++++++++++++Inside RoleInterceptor +++++++++++++++++++++++++++++++++++ ");
		logger.info("[preHandle][" + request + "]" + "[" + request.getMethod().toString() + "]" + request.getRequestURI() );    

		HandlerMethod roleHandler = (HandlerMethod) handler;

		String handlerMethodName = roleHandler.getMethod().getDeclaringClass().getCanonicalName()+"_"+roleHandler.getMethod().getName();
		System.out.println("final value : "+handlerMethodName);

		if(WalletUtilities.isPublicAction(request.getServletPath())){
			System.out.println("**********isPublicAction***************");
			return true;
		}

		Action actionObj = actionService.findByHandlerMethodName(handlerMethodName);

		if(actionObj==null){

			logger.debug("actionObj is found null : "+actionObj);
			WalletUtilities.errResp(request, response, "Can't perform action due to internal problem : ");
			return false;
		}

		User userObj = tokenService.getUserByTokenString(request.getHeader("mywallet-token"));
		if(userObj==null){
			logger.debug("userObj is found null : "+userObj);

			WalletUtilities.errResp(request, response, "Can't perform action due to internal problem : ");
			return false;
		}

		Role roleObj =userObj.getRole();
		System.out.println(ObjectMap.objectMap(roleObj));
		Integer roleAction=actionService.getActionByActionIdAndRoleId(actionObj.getActionId(), roleObj.getRoleId());
		System.out.println(actionObj.getActionId()+"   <==>  "+ roleObj.getRoleId());

		System.out.println("''''''''''''roleAction'''''''''''''''"+roleAction);
		System.out.println("+++++++++++++++++++++++++++End RoleInterceptor +++++++++++++++++++++++++++++++++++ ");

		if(roleAction.intValue()==0 && roleAction==null){
			WalletUtilities.errResp(request, response, "You don't have enough right to perform these action : ");
			return false;
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.info("[postHandle][" + request + "]");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("this is interceptor, afterCompletion method");		
	}


}
