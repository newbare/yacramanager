package fr.wati.yacramanager.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;
import org.springframework.web.filter.GenericFilterBean;


public class AjaxTimeoutRedirectFilter extends GenericFilterBean
{
 
    private static final Log logger = LogFactory.getLog(AjaxTimeoutRedirectFilter.class);
 
    private ThrowableAnalyzer throwableAnalyzer = new DefaultThrowableAnalyzer();
    private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
 
    private Integer customSessionExpiredErrorCode;
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        try
        {
            chain.doFilter(request, response);
            //logger.debug("Chain processed normally");
        }
        catch (IOException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
            RuntimeException ase = (AuthenticationException) throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, causeChain);
 
            if (ase == null)
            {
                ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, causeChain);
            }
            if (ase != null)
            {
                if (ase instanceof AuthenticationException)
                {
                    throw ase;
                }
                else if (ase instanceof AccessDeniedException)
                {
 
                    if (authenticationTrustResolver.isAnonymous(SecurityContextHolder.getContext().getAuthentication()))
                    {
                        logger.debug("User session expired or not logged in yet");
                        String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
                        String rapidLoginValue= ((HttpServletRequest) request).getHeader("AJAX-LOGIN");
                        boolean rapidLogin=false;
                        if(StringUtils.isNotEmpty(rapidLoginValue)){
                        	rapidLogin=true;
                        }
                        if ("XMLHttpRequest".equals(ajaxHeader) && !rapidLogin)
                        {
                            logger.debug("Ajax call detected, send "+this.customSessionExpiredErrorCode+" error code");
                            HttpServletResponse resp = (HttpServletResponse) response;
                            resp.sendError(HttpStatus.FORBIDDEN.value());
                        }
                        else
                        {
                            logger.debug("Redirect to login page");
                            throw ase;
                        }
                    }
                    else
                    {
                        throw ase;
                    }
                }
            }
 
        }
    }
 
    private static final class DefaultThrowableAnalyzer extends ThrowableAnalyzer
    {
        /**
         * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
         */
        protected void initExtractorMap()
        {
            super.initExtractorMap();
 
            registerExtractor(ServletException.class, new ThrowableCauseExtractor()
            {
                public Throwable extractCause(Throwable throwable)
                {
                    ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
                    return ((ServletException) throwable).getRootCause();
                }
            });
        }
 
    }
 
    public void setCustomSessionExpiredErrorCode(Integer customSessionExpiredErrorCode)
    {
        this.customSessionExpiredErrorCode = customSessionExpiredErrorCode;
    }
}