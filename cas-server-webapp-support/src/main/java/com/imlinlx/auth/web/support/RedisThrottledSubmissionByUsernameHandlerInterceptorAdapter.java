package com.imlinlx.auth.web.support;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by linlixiang on 2016/10/25.
 */
public class RedisThrottledSubmissionByUsernameHandlerInterceptorAdapter extends AbstractRedisThrottledSubmissionHandlerInterceptorAdapter {
	@Override
	protected String constructKey( final HttpServletRequest request ) {
		final String username = request.getParameter( getUsernameParameter() );

		if( username == null ) {
			return request.getRemoteAddr();
		}

		// return ClientInfoHolder.getClientInfo().getClientIpAddress() + ";" + username.toLowerCase();
		return username;
	}
}
