package org.openJ2eeSessionCluster.interfaces;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openJ2eeSessionCluster.beans.OjscSessionBean;
import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.logger.OjscLog;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * base class for session storage
 * @author mchyzer
 *
 */
public abstract class OjscSessionBase implements OjscSession {

  /**
   * session pattern regex
   */
  private static Pattern sessionPattern = Pattern.compile("^([a-z0-9A-Z]+)__([0-9]+)$");
  
  /**
   * @see OjscSession#retrieveSessionBean(HttpServletRequest)
   */
  @Override
  public OjscSessionBean retrieveSessionBean(HttpServletRequest request) {
    
    String sessionString = this.retrieveSessionString(request);
    
    if (OjscUtils.isBlank(sessionString)) {
      return null;
    }

    Matcher matcher = sessionPattern.matcher(sessionString);
    
    if (!matcher.matches()) {
      OjscLog.addItemToLogMap("sessionStringDoesntMatch", sessionString);
      return null;
    }
    
    //sessionId__timestamp
    String sessionId = matcher.group(1);
    String timestampString = matcher.group(2);
    long timestampLong = Long.parseLong(timestampString);
    OjscSessionBean ojscSessionBean = new OjscSessionBean();
    ojscSessionBean.setSessionId(sessionId);
    ojscSessionBean.setSessionLastPersisted(timestampLong);
    
//    //unencrypt
//    sessionString = OjscConfig.retrieveConfig().ojscEncryption().decryptForBrowserBase64(sessionString);
//    
//    //get the bean back
//    OjscSessionBean ojscSessionBean = OjscConfig.retrieveConfig().ojscSerialization().unserialize(sessionString, OjscSessionBean.class);
    
    return ojscSessionBean;
      
  }

  /**
   * @see OjscSession#storeSessionBean(HttpServletRequest, HttpServletResponse, OjscSessionBean)
   */
  @Override
  public void storeSessionBean(HttpServletRequest request,
      HttpServletResponse response, OjscSessionBean ojscSessionBean) {
    
    //convert to json
    String beanString = "";
    
    if (ojscSessionBean != null) {
      
      beanString = ojscSessionBean.getSessionId() + "__" + ojscSessionBean.getSessionLastPersisted();
      
//      //serialize to string
//      beanString = OjscConfig.retrieveConfig().ojscSerialization().serialize(ojscSessionBean);
//      
//      //encrypt and base 64
//      beanString = OjscConfig.retrieveConfig().ojscEncryption().encryptForBrowserBase64(beanString);
    }
    
    this.storeSessionString(request, response, beanString);
  }


  
  
}
