package org.openJ2eeSessionCluster.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openJ2eeSessionCluster.config.OjscConfig;
import org.openJ2eeSessionCluster.interfaces.OjscSessionStorageBase;
import org.openJ2eeSessionCluster.util.OjscUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

/**
 * store stuff in s3
 * @author mchyzer
 *
 */
public class OjscS3SessionStorage extends OjscSessionStorageBase {

  /**
   * amazon s3 client
   * @return client
   */
  private AmazonS3 amazonS3client() {
    
    String accountFileName = OjscConfig.retrieveConfig().propertyValueString("ojsc.s3.accountFileName");
    
    AmazonS3 s3client = null;
    try {
      s3client = new AmazonS3Client(new PropertiesCredentials(new File(
          accountFileName)));
    } catch (IOException ioe) {
      throw new RuntimeException("Cant open account file: " + accountFileName);
    }
    return s3client;
    
  }
  
  /**
   * store stuff in s3
   */
  @Override
  public void store(String keyName, String contents) {

    //put prefix on uuid
    keyName = OjscS3SessionStorage.class.getPackage().getName() + "." + keyName;
    
    String bucketName = OjscConfig.retrieveConfig().propertyValueString("ojsc.s3.bucketName");

    try {
      ObjectMetadata objectMetadata = new ObjectMetadata();
      
      //this should be set at bucket level, but set here too...
      Calendar expiration = new GregorianCalendar();
      expiration.setTimeInMillis(System.currentTimeMillis());
      expiration.add(Calendar.DAY_OF_YEAR, 1);

      objectMetadata.setExpirationTime(expiration.getTime());
      byte[] bytes = OjscUtils.stringGetBytes(contents);
      objectMetadata.setContentLength(bytes.length);
      objectMetadata.setContentType("text/plain");
      
      AmazonS3 s3client = amazonS3client();
      
      s3client.putObject(bucketName, keyName, new ByteArrayInputStream(bytes),
          objectMetadata);
    } catch (Exception e) {
      if (e instanceof AmazonServiceException) {
        AmazonServiceException ase = (AmazonServiceException)e;
        OjscUtils.injectInException(e, "Error Message: " + ase.getMessage() 
            + ", HTTP Status Code: " + ase.getStatusCode()
            + ", AWS Error Code: " + ase.getErrorCode()
            + ", Error Type: " + ase.getErrorType()
            + ", Request ID: " + ase.getRequestId());
      }
      throw new RuntimeException(e);
    }
      
  }

  /**
   * retrieve stuff from s3
   */
  @Override
  public String retrieve(String keyName) {
    
    String bucketName = OjscConfig.retrieveConfig().propertyValueString("ojsc.s3.bucketName");

    try {
            
      AmazonS3 s3client = amazonS3client();

      keyName = OjscS3SessionStorage.class.getPackage().getName() + "." + keyName;

      S3Object s3Object = s3client.getObject(bucketName, keyName);
      
      InputStream inputStream = s3Object.getObjectContent();
      
      StringWriter writer = new StringWriter();
      OjscUtils.copy(inputStream, writer, "UTF-8");
      String theString = writer.toString();
      
      return theString;
    
    } catch (Exception e) {
      if (e instanceof AmazonServiceException) {
        AmazonServiceException ase = (AmazonServiceException)e;
        OjscUtils.injectInException(e, "Error Message: " + ase.getMessage() 
            + ", HTTP Status Code: " + ase.getStatusCode()
            + ", AWS Error Code: " + ase.getErrorCode()
            + ", Error Type: " + ase.getErrorType()
            + ", Request ID: " + ase.getRequestId());
      }
      throw new RuntimeException(e);
    }
    

  }
}
