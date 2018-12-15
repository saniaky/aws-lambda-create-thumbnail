package com.saniaky.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

import java.io.InputStream;

/**
 * @author Alexander Kohonovsky
 * @since 12/14/18
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class ImageService {


    private final String dstBucket;
    private final AmazonS3 s3Client;

    public ImageService() {
        this.dstBucket = Config.getInstance().getDestBucket();
        this.s3Client = AmazonS3ClientBuilder.defaultClient();
    }

    // Download the image from S3 into a stream
    public InputStream getImageStream(String srcBucket, String srcKey) {
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
        return s3Object.getObjectContent();
    }

    public void save(InputStream input, String key, int size, String contentType) {
        // Set Meta tags
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(size);
        meta.setContentType(contentType);

        System.out.println("Writing to: " + dstBucket + "/" + key);
        s3Client.putObject(dstBucket, key, input, meta);
    }

}
