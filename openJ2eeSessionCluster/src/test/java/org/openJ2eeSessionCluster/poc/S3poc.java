package org.openJ2eeSessionCluster.poc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.openJ2eeSessionCluster.util.OjscUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3poc {

  /**
   * convert string to bytes
   * @param bytes
   * @return the string
   */
  public static byte[] stringGetBytes(String string) {
    try {
      return string.getBytes("UTF-8");
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw new RuntimeException("Cant convert string to bytes", unsupportedEncodingException);
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
//    String bucketName = "edu.upenn.isc.mchyzer.testBucket";
    String bucketName = "edu.upenn.isc.mobile.prod.sessions";
    
    String keyName = "123456";

    AmazonS3 s3client = null;
    try {
      
      //s3client = new AmazonS3Client(new PropertiesCredentials(new File(
      //    "R:\\personal\\accounts\\aws_testuser.properties")));
      
      s3client = new AmazonS3Client(new PropertiesCredentials(new File(
          "R:\\aws\\aws_fast_mobile_prod.properties")));
       
      
      List<Bucket> buckets = s3client.listBuckets();
      
      if (buckets != null) {
        
        for (Bucket bucket : buckets) {
        
          System.out.println(bucket.getName());
          
        }
      
      }

      ObjectListing objectListing = s3client.listObjects(bucketName);
      
      System.out.println("\n#### Keys:\n");
      
      List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
      
      if (objectSummaries != null) {
        for (S3ObjectSummary s3ObjectSummary : objectSummaries) {
          System.out.println(s3ObjectSummary.getKey());
        }
      }
      
      System.out.println("\n#### Uploading a new object to S3 from a file\n");
      ObjectMetadata objectMetadata = new ObjectMetadata();

      Calendar expiration = new GregorianCalendar();
      expiration.setTimeInMillis(System.currentTimeMillis());
      expiration.add(Calendar.DAY_OF_YEAR, 1);

      objectMetadata.setExpirationTime(expiration.getTime());
      byte[] bytes = stringGetBytes("Some string to put to AWS, new string");
      objectMetadata.setContentLength(bytes.length);
      objectMetadata.setContentType("text/plain");

      s3client.putObject(bucketName, keyName, new ByteArrayInputStream(bytes),
          objectMetadata);
      
      
      S3Object s3Object = s3client.getObject(bucketName, keyName);
      
      InputStream inputStream = s3Object.getObjectContent();
      
      StringWriter writer = new StringWriter();
      OjscUtils.copy(inputStream, writer, "UTF-8");
      String theString = writer.toString();
      System.out.println(theString);

    } catch (IOException ioe) {
      ioe.printStackTrace();
    } catch (AmazonServiceException ase) {
      System.out.println("Caught an AmazonServiceException, which " +
          "means your request made it " +
          "to Amazon S3, but was rejected with an error response" +
          " for some reason.");
      System.out.println("Error Message:    " + ase.getMessage());
      System.out.println("HTTP Status Code: " + ase.getStatusCode());
      System.out.println("AWS Error Code:   " + ase.getErrorCode());
      System.out.println("Error Type:       " + ase.getErrorType());
      System.out.println("Request ID:       " + ase.getRequestId());
      ase.printStackTrace();
    } catch (AmazonClientException ace) {
      System.out.println("Caught an AmazonClientException, which " +
          "means the client encountered " +
          "an internal error while trying to " +
          "communicate with S3, " +
          "such as not being able to access the network.");
      System.out.println("Error Message: " + ace.getMessage());
      ace.printStackTrace();
    }

  }

}
