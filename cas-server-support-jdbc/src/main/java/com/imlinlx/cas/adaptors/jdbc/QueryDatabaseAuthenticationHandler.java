package com.imlinlx.cas.adaptors.jdbc;

import org.jasig.cas.adaptors.jdbc.AbstractJdbcUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Created by linlixiang on 2016/10/25.
 */
public class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {
	private String sql;

	@NotNull
	private String table;
	// @NotNull
	private String fieldUsername;
	// @NotNull
	private String fieldPassword;
	// @NotNull
	private String fieldSalt;

	/** {@inheritDoc} */
	@Override
	protected final HandlerResult authenticateUsernamePasswordInternal( final UsernamePasswordCredential credential )
					throws GeneralSecurityException, PreventedException {

		final String username = credential.getUsername();

        //实际使用时,应实现自定义的加密类,默认(this.getPasswordEncoder())是纯文本返回的,也就是不加密
		final String encryptedPassword = this.getPasswordEncoder().encode( credential.getPassword() );
		try {

            // demo
			this.sql = "select password from " + this.table + " where user_name = ?";
			// 在这里可以根据具体情况,修改验证逻辑,比如盐值等
			final String dbPassword = getJdbcTemplate().queryForObject( this.sql, String.class, username );

			if( !dbPassword.equals( encryptedPassword ) ) {
				throw new FailedLoginException( "Password does not match value on record." );
			}
		} catch( final IncorrectResultSizeDataAccessException e ) {
			if( e.getActualSize() == 0 ) {
				throw new AccountNotFoundException( username + " not found with SQL query" );
			} else {
				throw new FailedLoginException( "Multiple records found for " + username );
			}
		} catch( final DataAccessException e ) {
			throw new PreventedException( "SQL exception while executing query for " + username, e );
		}
		return createHandlerResult( credential, new SimplePrincipal( username ), null );
	}

	/**
	 * @param sql The sql to set.
	 */
	public void setSql( final String sql ) {
		this.sql = sql;
	}

	public void setTable( String table ) {
		this.table = table;
	}

	public void setFieldUsername( String fieldUsername ) {
		this.fieldUsername = fieldUsername;
	}

	public void setFieldPassword( String fieldPassword ) {
		this.fieldPassword = fieldPassword;
	}

	public void setFieldSalt( String fieldSalt ) {
		this.fieldSalt = fieldSalt;
	}
}
