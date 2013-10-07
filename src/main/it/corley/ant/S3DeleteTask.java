/**
 *
 */
package it.corley.ant;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

public class S3DeleteTask extends AWSTask
{


    /**
     * Target bucket, to which files should be uploaded.
     */
    private String bucket;

    /**
     * Destination dir on the S3 to which files should be uploaded.
     */
    private String dest;


    private String endPoint = "s3-eu-west-1.amazonaws.com";



    /**
     * Executes the task.
     *
     * @see org.apache.tools.ant.Task#execute()
     */
    public void execute()
    {
        validateConfiguration();
        final AWSCredentials credential = new BasicAWSCredentials(getKey(), getSecret());
        final TransferManager transferManager = new TransferManager(credential);
        log(String.format("Region %s provided", getEndPoint()), Project.MSG_INFO);
        AmazonS3 s3Client = transferManager.getAmazonS3Client();
        s3Client.setEndpoint(getEndPoint());

        ObjectListing objects = s3Client.listObjects(bucket, getDest());
        for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
            log(String.format("Key %s deleting successfully from %s bucket", objectSummary.getKey(), bucket), Project.MSG_INFO);
            s3Client.deleteObject(bucket, objectSummary.getKey());
        }

        log(String.format("Key %s deleted successfully from %s bucket", getDest(), bucket), Project.MSG_INFO);

    }


    private void validateConfiguration()
    {
        if (bucket == null)
        {
            throw new BuildException("Target bucket not given. Cannot upload");
        }
        //        TODO add other properties
    }


    /**
     * ===============================================
     * Getters and setters
     * ===============================================
     */

    public String getDest()
    {
        return dest;
    }

    public void setDest(String dest)
    {
        this.dest = dest;
    }

    public void setBucket(String bucket)
    {
        this.bucket = bucket;
    }


    public String getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }

}
