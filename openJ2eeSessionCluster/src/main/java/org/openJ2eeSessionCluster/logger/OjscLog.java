package org.openJ2eeSessionCluster.logger;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openJ2eeSessionCluster.filter.OjscFilter;
import org.openJ2eeSessionCluster.util.OjscUtils;

/**
 * main logger, one entry per request
 * @author mchyzer
 *
 */
public class OjscLog {

  /** ojsc log */
  private static final String OJSC_LOG = "ojscLog";

  /** logger */
  private static final Log LOG = LogFactory.getLog(OjscLog.class);

  /**
   * add item to log map
   * @param key
   * @param value
   */
  @SuppressWarnings("unchecked")
  public static void addItemToLogMap(String key, Object value) {
    
    HttpServletRequest httpServletRequest = OjscFilter.retrieveHttpServletRequest();
    if (httpServletRequest == null || !LOG.isDebugEnabled()) {

      //cant or wont log
      return;
    
    }

    Map<String, Object> ojscLog = (Map<String, Object>)httpServletRequest.getAttribute(OJSC_LOG);
    
    if (ojscLog == null) {
      ojscLog = new LinkedHashMap<String, Object>();
      httpServletRequest.setAttribute(OJSC_LOG, ojscLog);
    }
    ojscLog.put(key, value);
  }
  
  /**
   * log something to the log file
   * @param messageMap
   */
  public static void ojscLogMap() {

    HttpServletRequest httpServletRequest = OjscFilter.retrieveHttpServletRequest();

    if (httpServletRequest == null || !LOG.isDebugEnabled()) {

      //cant or wont log
      return;

    }

    @SuppressWarnings("unchecked")
    Map<String, Object> ojscLog = (Map<String, Object>)httpServletRequest.getAttribute(OJSC_LOG);
    
    if (OjscUtils.length(ojscLog) > 0) {
      LOG.debug(OjscUtils.mapToString(ojscLog));
    }
  }

}
