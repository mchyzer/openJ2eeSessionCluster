package org.openJ2eeSessionCluster.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openJ2eeSessionCluster.beans.OjscJ2eeSessionBean;
import org.openJ2eeSessionCluster.beans.OjscSessionBean;
import org.openJ2eeSessionCluster.beans.OjscSessionStorageBean;
import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.logger.OjscLog;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * servlet filter for open j2ee session cluster
 * @author mchyzer
 *
 */
public class OjscFilter implements Filter {

  /**
   * logger
   */
  private static Log LOG = LogFactory.getLog(OjscFilter.class);
  
  /**
   * thread local for request
   */
  private static ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<HttpServletRequest>();

  /**
   * 
   */
  @Override
  public void destroy() {

  }

  /**
   * do the filter, call the chain
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
    HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

    threadLocalRequest.set(httpServletRequest);
    try {
      
      //are we even doing this thing?
      boolean enable = OjscConfig.retrieveConfig().propertyValueBoolean("ojsc.enable", true);
      
      boolean needToRecover = true;
      
      if (!enable) {

        OjscLog.addItemToLogMap("enabled", enable);
        needToRecover = false;

      }

      OjscJ2eeSessionBean ojscJ2eeSessionBeanPrevious = null;
      OjscSessionStorageBean ojscSessionStorageBeanPrevious = null;
      boolean hasSessionBeanInSession = false;
      
      //see if we need to recover
      String loginId = null;
      try {

        //if enabled
        loginId = OjscUtils.defaultString(retrieveUserIdFromRequest());
        
        {
          String userIdForLog = loginId;
          if (OjscUtils.isBlank(userIdForLog)) {
            
            userIdForLog = "<anonymous>";
            
            OjscLog.addItemToLogMap("userId", userIdForLog);
            
          }
        }
        
        //is there a session?  if no, then forget it
        boolean hasJ2eeSession = httpServletRequest.getSession(false) != null;
        
        OjscLog.addItemToLogMap("hasJ2eeSession", hasJ2eeSession);
      
        OjscSessionBean ojscSessionBean = null;
        if (needToRecover) {
          
          //get the cookie from the browser
          ojscSessionBean = OjscConfig.retrieveConfig().ojscSession().retrieveSessionBean(httpServletRequest);
  
          if (ojscSessionBean == null) {
  
            OjscLog.addItemToLogMap("hasOjscSessionBean", false);
            needToRecover = false;
            
          }
        }
        
        if (needToRecover) {
          
          //we have a bean from browser
          //check the time on it
          long sessionLastPersistedLong = ojscSessionBean.getSessionLastPersisted();
          Date sessionLastPersistedDate = new Date(sessionLastPersistedLong);

          OjscLog.addItemToLogMap("sessionLastPersisted", OjscUtils.dateToString(sessionLastPersistedDate));
          
          Integer requireSessionPersistedInMinutes = OjscConfig.retrieveConfig().propertyValueInt("ojsc.requireSessionPersistedInMinutes");

          if (requireSessionPersistedInMinutes != null && requireSessionPersistedInMinutes > 0) {
            if ((System.currentTimeMillis() - sessionLastPersistedLong) / (1000 * 60) > requireSessionPersistedInMinutes) {
              needToRecover = false;
              OjscLog.addItemToLogMap("sessionLastPersistedTooOld", true);
            }
          }
        }
        
        if (needToRecover && hasJ2eeSession) {
          
          //lets check the one in session, maybe its up to date
          ojscJ2eeSessionBeanPrevious = (OjscJ2eeSessionBean)httpServletRequest
              .getSession(false).getAttribute(OjscJ2eeSessionBean.SESSION_ATTRIBUTE_KEY);

          hasSessionBeanInSession = ojscJ2eeSessionBeanPrevious != null;
          OjscLog.addItemToLogMap("hasSessionBeanInSession", hasSessionBeanInSession);
          
          if (hasSessionBeanInSession) {
            //see if last updated time is correct
            boolean sessionOnServerUpToDate = ojscSessionBean.getSessionLastPersisted() <= ojscJ2eeSessionBeanPrevious.getOjscSessionBean().getSessionLastPersisted();
            OjscLog.addItemToLogMap("sessionOnServerUpToDate", sessionOnServerUpToDate);
            if (sessionOnServerUpToDate) {
              needToRecover = false;
            }
          }
          
        }

        //retrieve from storage
        OjscLog.addItemToLogMap("retrieveFromStorage", needToRecover);

        if (needToRecover) {
          
          long start = System.nanoTime();
          
          ojscSessionStorageBeanPrevious = OjscConfig.retrieveConfig().ojscSessionStorage().retrieveSessionStorageBean(ojscSessionBean.getSessionId());
          
          long took = (System.nanoTime() - start) / 1000000L;

          OjscLog.addItemToLogMap("retrieveFromStorageMillis", took);

          boolean foundSessionStorageBean = ojscSessionStorageBeanPrevious != null;
          
          OjscLog.addItemToLogMap("foundSessionStorageBean", foundSessionStorageBean);
          if (!foundSessionStorageBean) {
            needToRecover = false;
          }
          
        }
        
        //lets see if username matches
        if (needToRecover) {
          
          boolean userNameMatches = OjscUtils.equals(OjscUtils.trimToEmpty(loginId), 
              OjscUtils.trimToEmpty(ojscSessionStorageBeanPrevious.getLoginId()));
            
          OjscLog.addItemToLogMap("userNameMatches", userNameMatches);
          
          needToRecover = userNameMatches;
        }
        
        //lets see if time stamp matches
        if (needToRecover) {
          boolean timestampMatches = ojscSessionBean.getSessionLastPersisted() == ojscSessionStorageBeanPrevious.getOjscSessionBean().getSessionLastPersisted();
          
          OjscLog.addItemToLogMap("timestampMatches", timestampMatches);
          
          needToRecover = timestampMatches;
        }
        
        //dont know why this wouldnt match, but belts and suspenders
        if (needToRecover) {
          boolean sessionIdMatches = OjscUtils.equals(ojscSessionBean.getSessionId(), ojscSessionStorageBeanPrevious.getOjscSessionBean().getSessionId());
          
          OjscLog.addItemToLogMap("sessionIdMatches", sessionIdMatches);
          
          needToRecover = sessionIdMatches;
        }
        
        OjscLog.addItemToLogMap("needToRecover", needToRecover);

        if (needToRecover) {
          //clear out the session stuff?
          HttpSession httpSession = httpServletRequest.getSession();
          {
            @SuppressWarnings("unchecked")
            Enumeration<String> attributeNamesEnumeration = httpSession.getAttributeNames();
            int deleteItemCount = 0;
            while (attributeNamesEnumeration.hasMoreElements()) {
              String attributeName = attributeNamesEnumeration.nextElement();
              httpSession.removeAttribute(attributeName);
              deleteItemCount++;
            }
            OjscLog.addItemToLogMap("deleteItemCount", deleteItemCount);
          }
          {
            int restoreItemCount = 0;
            
            //put all the stuff from the bean
            for (String attributeName : OjscUtils.nonNull(ojscSessionStorageBeanPrevious.getSessionAttributes()).keySet()) {
              httpSession.setAttribute(attributeName, ojscSessionStorageBeanPrevious.getSessionAttributes().get(attributeName));
              restoreItemCount++;
            }
            OjscLog.addItemToLogMap("restoreItemCount", restoreItemCount);
          }
        }
        
      } catch (Throwable e) {
        LOG.error("Non-fatal? error with user retrieving session: " + loginId + ", not restoring session", e);
        OjscLog.addItemToLogMap("restoreException", ExceptionUtils.getStackTrace(e));
        //dont rethrow, its ok
      }

      //ok, now we can store this session to storage
      boolean needToPersist = true;
      boolean needToPersistToSession = true;

      OjscSessionStorageBean ojscSessionStorageBeanNew = new OjscSessionStorageBean();
      OjscSessionBean ojscSessionBean = new OjscSessionBean();
      OjscJ2eeSessionBean ojscJ2eeSessionBeanNew = new OjscJ2eeSessionBean();

      //if there is no session, dont create one
      if (httpServletRequest.getSession(false) == null) {
        needToPersist = false;
        needToPersistToSession = false;
      }

      if (!enable) {
        needToPersist = false;
        needToPersistToSession = false;
      } else {
      
        if (hasSessionBeanInSession && ojscJ2eeSessionBeanPrevious.getCountTooLarge() >= OjscConfig.retrieveConfig().propertyValueInt("ojsc.maxSessionsTooLarge", 5)) {
          OjscLog.addItemToLogMap("maximumNumberOfSessionsTooLarge", true);
          needToPersist = false;
        }
        
        
        ojscSessionStorageBeanNew.setLoginId(loginId);
        ojscSessionStorageBeanNew.setOjscSessionBean(ojscSessionBean);
  
        ojscJ2eeSessionBeanNew.setOjscSessionBean(ojscSessionBean);
  
        if (hasSessionBeanInSession) {
          
          ojscSessionBean.setSessionId(ojscJ2eeSessionBeanPrevious.getOjscSessionBean().getSessionId());
          
          ojscJ2eeSessionBeanNew.setCountTooLarge(ojscJ2eeSessionBeanPrevious.getCountTooLarge());

        } else {

          //we are creating a new one
          ojscJ2eeSessionBeanNew.setCountTooLarge(0);
  
          //if we got this from the server
          if (needToRecover && ojscSessionStorageBeanPrevious != null && ojscSessionStorageBeanPrevious.getOjscSessionBean() != null
              && !OjscUtils.isBlank(ojscSessionStorageBeanPrevious.getOjscSessionBean().getSessionId())) {

            ojscSessionBean.setSessionId(ojscSessionStorageBeanPrevious.getOjscSessionBean().getSessionId());

          } else {

            ojscSessionBean.setSessionId(OjscUtils.uuid());

          }
        }

        OjscLog.addItemToLogMap("sessionId", OjscUtils.abbreviate(ojscSessionBean.getSessionId(), 6));
        OjscLog.addItemToLogMap("countTooLarge", ojscJ2eeSessionBeanPrevious == null ? 0 : ojscJ2eeSessionBeanPrevious.getCountTooLarge());
        
        //now
        ojscSessionBean.setSessionLastPersisted(System.currentTimeMillis());

        OjscLog.addItemToLogMap("sessionLastPersistedLong", ojscSessionBean.getSessionLastPersisted());
      }

      //lets put the bean to the session and the browser.  Note, we have to do this before the next filter, since that one will commit the session
      if (needToPersistToSession) {
        
        //store to browser
        OjscConfig.retrieveConfig().ojscSession().storeSessionBean(httpServletRequest, httpServletResponse, ojscSessionBean);

        //store to session object
        httpServletRequest.getSession(false).setAttribute(OjscJ2eeSessionBean.SESSION_ATTRIBUTE_KEY, ojscJ2eeSessionBeanNew);
        
      }

      
      //run the normal filter chain
      filterChain.doFilter(servletRequest, servletResponse);

      try {
      
        //if we need to store the session to storage
        if (needToPersist) {
  
          //take session attributes, and put in the storage bean
          @SuppressWarnings("unchecked")
          Enumeration<String> attributeNamesEnumeration = httpServletRequest.getSession(false).getAttributeNames();
          int storedItemCount = 0;
          
          while (attributeNamesEnumeration.hasMoreElements()) {
            String attributeName = attributeNamesEnumeration.nextElement();
            ojscSessionStorageBeanNew.getSessionAttributes().put(attributeName, httpServletRequest.getSession(false).getAttribute(attributeName));
            storedItemCount++;
          }
  
          OjscLog.addItemToLogMap("storedItemCount", storedItemCount);

          //lets serialize the bean
          long start = System.nanoTime();
          
          boolean sizeOk = OjscConfig.retrieveConfig().ojscSessionStorage()
              .storeSessionStorageBean(ojscSessionBean.getSessionId(), ojscSessionStorageBeanNew);

          long took = (System.nanoTime() - start) / 1000000L;

          OjscLog.addItemToLogMap("storeToStorageMillis", took);
          

          if (sizeOk) {
            //these have to be consecutive
            ojscJ2eeSessionBeanNew.setCountTooLarge(0);
          } else {
            //increment this count, maybe too many the next time
            ojscJ2eeSessionBeanNew.setCountTooLarge(ojscJ2eeSessionBeanNew.getCountTooLarge() + 1);
          }
          
        }
      } catch (Throwable t) {

        LOG.error("Non-fatal? error with user storing session: " + loginId + ", not storing session", t);
        //dont rethrow, its ok
        OjscLog.addItemToLogMap("storeException", ExceptionUtils.getStackTrace(t));

      }
      
    } finally {
      OjscLog.ojscLogMap();
      threadLocalRequest.remove();
    }
    
  }

  /**
   * retrieve the person actually logged in, do not consider who is acting as someone else
   * @return the loginid
   */
  public static String retrieveUserIdFromRequest() {

    HttpServletRequest httpServletRequest = retrieveHttpServletRequest();
    
    OjscUtils.assertion(httpServletRequest != null,
            "HttpServletRequest is null, is the Servlet mapped in the web.xml?");
    
    Principal principal = httpServletRequest.getUserPrincipal();
    String principalName = null;
    if (principal == null) {
      principalName = httpServletRequest.getRemoteUser();
      if (OjscUtils.isBlank(principalName)) {
        principalName = (String)httpServletRequest.getAttribute("REMOTE_USER");
      }
    } else {
      principalName = principal.getName();
    }

    return principalName;
  }

  
  /**
   * init the filter
   */
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  /**
   * public method to get the http servlet request
   * 
   * @return the http servlet request
   */
  public static HttpServletRequest retrieveHttpServletRequest() {
    return threadLocalRequest.get();
  }

}
