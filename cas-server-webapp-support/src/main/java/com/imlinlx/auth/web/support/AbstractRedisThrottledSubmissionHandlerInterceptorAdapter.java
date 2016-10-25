package com.imlinlx.auth.web.support;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.web.support.AbstractThrottledSubmissionHandlerInterceptorAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by linlixiang on 2016/10/25.
 */
public abstract class AbstractRedisThrottledSubmissionHandlerInterceptorAdapter extends AbstractThrottledSubmissionHandlerInterceptorAdapter {

	// inject the actual template
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Override
	protected void recordSubmissionFailure( HttpServletRequest request ) {
		redisTemplate.boundValueOps( constructKey( request ) ).increment( 1 );
	}

	protected abstract String constructKey( HttpServletRequest request );

	@Override
	protected boolean exceedsThreshold( HttpServletRequest request ) {
		String key = constructKey( request );
		boolean forbidden = false;
		if( StringUtils.isEmpty( key ) ) {
			return true;
		}
		BoundValueOperations<String, String> ops = redisTemplate.boundValueOps( key );
		String count = ops.get();

		if( count != null ) {
			forbidden = Integer.parseInt( count ) >= getFailureThreshold();
		}

		// 登录受限后,20分钟 内不允许再登录
		if( forbidden ) {
			ops.expire( 20, TimeUnit.MINUTES );
		}

		return forbidden;
	}

}