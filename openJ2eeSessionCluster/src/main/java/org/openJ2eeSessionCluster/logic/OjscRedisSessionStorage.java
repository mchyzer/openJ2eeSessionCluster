package org.openJ2eeSessionCluster.logic;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.filter.OjscFilter;
import org.openJ2eeSessionCluster.interfaces.OjscSessionStorageBase;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * store stuff in redis (e.g. aws elasticache)
 * @author mchyzer
 *
 */
public class OjscRedisSessionStorage extends OjscSessionStorageBase {

  
  /**
   * logger
   */
  private static Log LOG = LogFactory.getLog(OjscFilter.class);

  /**
   * lazy load a jedis pool
   */
  private static JedisPool jedisPool = null;
  
  /**
   * call this failsafe method when the app shuts down
   */
  public static void closeJedisPool() {
    if (jedisPool != null) {
      try {
        jedisPool.destroy();
      } catch (Exception e) {
        LOG.error("error closing pool", e);
      }
   }
  }
  
  /**
   * jedis pool
   * @return client
   */
  public static JedisPool jedisPool() {
    
    if (jedisPool != null) {
      return jedisPool;
    }

    synchronized(OjscRedisSessionStorage.class) {
      
      if (jedisPool == null) {
  
        //# redis server
        //ojsc.redis.server =
        //
        //# redis port
        //ojsc.redis.port = 
    
        String serverHost = OjscConfig.retrieveConfig().propertyValueString("ojsc.redis.server");
        int serverPort = OjscConfig.retrieveConfig().propertyValueInt("ojsc.redis.port", 6379);
        jedisPool = new JedisPool(new JedisPoolConfig(), serverHost, serverPort);
      }
      
      return jedisPool;
    }
  }
  
  /**
   * store stuff in s3
   */
  @Override
  public void store(String keyName, String contents) {

    //put prefix on uuid
    keyName = OjscRedisSessionStorage.class.getPackage().getName() + "." + keyName;
    
    Jedis jedis = null;
    
    try {

      JedisPool jedisPool = jedisPool();
      
      jedis = jedisPool.getResource();
      
      // ... do stuff here ... for example
      jedis.set(keyName, contents);
      
      int expireSeconds = OjscConfig.retrieveConfig().propertyValueInt("ojsc.redis.sessionExpireSeconds", 7200);
      if (expireSeconds > 0) {
        //do we need this?
        jedis.expire(keyName, 2 * 60 * 60);
      }
    } finally {
      /// ... it's important to return the Jedis instance to the pool once you've finished using it
      if (null != jedis) {
        jedisPool.returnResource(jedis);
      }
    }
      
  }

  /**
   * retrieve stuff from s3
   */
  @Override
  public String retrieve(String keyName) {
    

    //put prefix on uuid
    keyName = OjscRedisSessionStorage.class.getPackage().getName() + "." + keyName;
    
      
    //this should be set at bucket level, but set here too...
    Calendar expiration = new GregorianCalendar();
    expiration.setTimeInMillis(System.currentTimeMillis());
    expiration.add(Calendar.HOUR, 2);

    Jedis jedis = null;
    
    try {

      JedisPool jedisPool = jedisPool();
      
      jedis = jedisPool.getResource();
      
      // ... do stuff here ... for example
      String contents = jedis.get(keyName);
      
      return contents;
      
    } finally {
      /// ... it's important to return the Jedis instance to the pool once you've finished using it
      if (null != jedis) {
        jedisPool.returnResource(jedis);
      }
    }
    

  }
}
