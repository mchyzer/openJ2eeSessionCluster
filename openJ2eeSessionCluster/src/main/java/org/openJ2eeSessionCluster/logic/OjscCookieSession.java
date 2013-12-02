/**
 * 
 */
package org.openJ2eeSessionCluster.logic;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openJ2eeSessionCluster.beans.OjscSessionBean;
import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.interfaces.OjscSession;
import org.openJ2eeSessionCluster.interfaces.OjscSessionBase;
import org.openJ2eeSessionCluster.util.OjscUtils;


/**
 * store session in a cookie
 * @author mchyzer
 *
 */
public class OjscCookieSession extends OjscSessionBase {

  /**
   * add a cookie (or change a cookie?) to the response for the context
   * @param request
   * @param response
   * @param name
   * @param value
   */
  public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name,
      String value) {
    if (value == null) {
      value = "";
    }

    StringBuilder result = new StringBuilder();
    
    
    if (name.contains(";") || value.contains(";") || name.contains("=") || value.contains("=")) {
      throw new RuntimeException("Name or value cant contain semi or equals: " + name + ", " + value);
    }

    result.append(name).append("=").append(value);
    
    if (!OjscUtils.isBlank(request.getContextPath())) {
      
      String contextPath = request.getContextPath();
      
      result.append("; Path=").append(contextPath);
      
    }
    
    //Set-Cookie: SSID=Ap4P….GTEq; Domain=.foo.com; Path=/; Expires=Wed, 13 Jan 2021 22:23:01 GMT; Secure; HttpOnly
    
    if (OjscConfig.retrieveConfig().propertyValueBoolean("ojsc.secureCookieFlag", true)) {
      result.append("; Secure");
      
    }
    
    result.append("; HttpOnly");
    
    response.addHeader("Set-Cookie", result.toString());
    
  }

  /**
   * get a cookie from the request by name
   * @param request
   * @param name
   * @return the cookie object
   */
  public static String retrieveCookieValue(HttpServletRequest request, String name) {
    Cookie cookies[] = request.getCookies();

    // Return null if there are no cookies or the name is invalid.
    if (cookies == null || name == null || name.length() == 0) {
      return null;
    }
    // Otherwise, we do a linear scan for the cookie.
    for (int i = 0; i < cookies.length; i++) {
      // If the current cookie name matches the one we're looking for,
      // we've
      // found a matching cookie.
      if (cookies[i].getName().equals(name)) {
        return cookies[i].getValue();
      }
    }
    return null;
  }

  /**
   * name of the cookie
   */
  private static final String COOKIE_NAME = "OJSC_COOKIE";

  /**
   * @see OjscSession#retrieveSessionString(HttpServletRequest)
   */
  @Override
  public String retrieveSessionString(HttpServletRequest request) {

    String cookieValue = retrieveCookieValue(request, COOKIE_NAME);
    if (OjscUtils.isBlank(cookieValue)) {
      return null;
    }

    //first, url unencode
    cookieValue = OjscUtils.urlDecode(cookieValue);
    
    return cookieValue;
  }

  /**
   * @see OjscSession#storeSessionString(HttpServletRequest, HttpServletResponse, OjscSessionBean)
   */
  @Override
  public void storeSessionString(HttpServletRequest request,
      HttpServletResponse response, String string) {
    
    //url encode
    string = OjscUtils.urlEncode(string);
    
    //set the cookie
    addCookie(request, response, COOKIE_NAME, string);
    
  }

}
