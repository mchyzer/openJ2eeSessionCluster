package org.openJ2eeSessionCluster.beans;

import java.io.Serializable;

/**
 * bean that comes back from the session handler
 * @author mchyzer
 *
 */
@SuppressWarnings("serial")
public class OjscSessionBean implements Serializable {
  
  /**
   * when this session was last persisted.  
   * The time this session was last persisted in millis since 1970
   */
  private long sessionLastPersisted;
  
  
  /**
   * id of this session.  Should be something complex like a secure random uuid
   */
  private String sessionId;

  /**
   * when this session was last persisted.  
   * The time this session was last persisted in millis since 1970
   * @return the time
   */
  public long getSessionLastPersisted() {
    return this.sessionLastPersisted;
  }


  /**
   * when this session was last persisted.  
   * The time this session was last persisted in millis since 1970
   * @param sessionLastPersisted1
   */
  public void setSessionLastPersisted(long sessionLastPersisted1) {
    this.sessionLastPersisted = sessionLastPersisted1;
  }

  /**
   * id of this session.  Should be something complex like a secure random uuid
   * @return id of the session
   */
  public String getSessionId() {
    return this.sessionId;
  }


  /**
   * id of this session.  Should be something complex like a secure random uuid
   * @param sessionId1
   */
  public void setSessionId(String sessionId1) {
    this.sessionId = sessionId1;
  }

  
  
}
