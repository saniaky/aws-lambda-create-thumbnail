package com.saniaky.aws;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.saniaky.aws.Config.PNG_TYPE;

/**
 * @author Alexander Kohonovsky
 * @since 12/14/18
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal"})
public class ImageController {


    private String outputFormat = "image/jpeg";
    private ImageService service = new ImageService();

    public void resizeImage(S3Event s3Event) throws IOException {
        Config config = Config.getInstance();

        for (S3EventNotification.S3EventNotificationRecord eventRecord : s3Event.getRecords()) {
            String srcBucket = getBucketName(eventRecord);
            String srcKey = getKey(eventRecord);
            sanityCheck(srcKey);

            // Read the source image
            InputStream imageInputStream = service.getImageStream(srcBucket, srcKey);

            // Create Thumbnail
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Thumbnails.of(imageInputStream)
                    .size(config.getMaxWidth(), config.getMaxHeight())
                    .outputFormat("jpg")
                    .outputQuality(0.9)
                    //.watermark(Positions.BOTTOM_RIGHT, watermarkImage, 0.5f)
                    .toOutputStream(os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            // Upload to S3 destination bucket
            String destKey = srcKey + "-small.jpg";
            service.save(is, destKey, os.size(), outputFormat);
            System.out.printf("Successfully resized %s/%s and uploaded to %s/%s%n",
                    srcBucket, srcKey, config.getDestBucket(), destKey);
        }
    }

    private void sanityCheck(String srcKey) {
        // Infer the image type.
        Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
        if (!matcher.matches()) {
            throw new RuntimeException("Unable to infer image type for key " + srcKey);
        }

        String imageType = matcher.group(1);
        if (!(Config.JPG_TYPE.equals(imageType)) && !(PNG_TYPE.equals(imageType))) {
            throw new RuntimeException("Skipping non-image " + srcKey);
        }
    }

    private String getBucketName(S3EventNotification.S3EventNotificationRecord eventRecord) {
        return eventRecord.getS3().getBucket().getName();
    }

    private String getKey(S3EventNotification.S3EventNotificationRecord eventRecord) throws UnsupportedEncodingException {
        String srcKey = eventRecord.getS3().getObject().getKey();
        // Object key may have spaces or unicode non-ASCII characters.
        return URLDecoder.decode(srcKey.replace('+', ' '), "UTF-8");
    }

}
