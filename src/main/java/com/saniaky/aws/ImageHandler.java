package com.saniaky.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;

import java.io.IOException;

/**
 * com.saniaky.aws.ImageHandler
 *
 * @author Alexander Kohonovsky
 * @since 12/14/18
 */
@SuppressWarnings("unused")
public class ImageHandler implements RequestHandler<S3Event, String> {

    private ImageController controller = new ImageController();

    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        LambdaLogger logger = context.getLogger();
        try {
            int startTime = context.getRemainingTimeInMillis();
            logger.log("Function name: " + context.getFunctionName());
            logger.log("Max mem allocated: " + context.getMemoryLimitInMB());
            logger.log("Time remaining in milliseconds: " + context.getRemainingTimeInMillis());
            logger.log("CloudWatch log stream name: " + context.getLogStreamName());
            logger.log("CloudWatch log group name: " + context.getLogGroupName());

            controller.resizeImage(s3Event);

            int endTime = context.getRemainingTimeInMillis();
            logger.log("Time took: " + (endTime - startTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}
