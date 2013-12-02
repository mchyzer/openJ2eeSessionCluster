package org.openJ2eeSessionCluster.logic;

import org.openJ2eeSessionCluster.interfaces.OjscEncryptionBase;


/**
 * default encryption for OJSC
 * @author mchyzer
 *
 */
public class OjscNoEncryption extends OjscEncryptionBase {

  /**
   * @see OjscEncryptionBase#encrypt(byte[])
   */
  @Override
  public byte[] encrypt(byte[] bytes) {
    return bytes;
  }
  
  /**
   * @see OjscEncryptionBase#decrypt(byte[])
   */
  @Override
  public byte[] decrypt(byte[] bytes) {
    return bytes;
  }
  
}
