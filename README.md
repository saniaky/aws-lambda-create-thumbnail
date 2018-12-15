# About CreateThumbnail

This lambda creates a thumbnail for each image file that is uploaded to a bucket.
You need to configure Amazon S3 to invoke this lambda when objects are created.
The Lambda function reads the image object from the source bucket, creates 
a thumbnail image and saves to destination bucket.

This lambda uses:
* Gradle 5.0
* Java 8
* [Thumbnailator 0.4.8](https://github.com/coobird/thumbnailator)
* AWS SDK 1.2

## Configure AWS
1. Create the Execution Role (gives your function permission to access AWS resources)
2. Create Buckets
3. Build and upload Lambda Function to separate S3 bucket
4. Configure Amazon S3 to Publish Events
5. Test the Lambda Function by uploading test image

## S3 Bucket Policies
S3 bucket permissions can be configured in AWS Console. Go to:  
Amazon S3 -> [Select bucket] -> Open "Permissions" tab -> Choose "Bucket Policy".

**Granting Read-Only Permission to an Anonymous User**
```
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Sid":"AllowAnonymousAccess",
      "Effect":"Allow",
      "Principal": "*",
      "Action":["s3:GetObject"],
      "Resource":["arn:aws:s3:::my-bucket/*"]
    }
  ]
}
```

## Useful resources:
* [Tutorial: Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html)
* [Resize Images on the Fly with Amazon S3, AWS Lambda, and Amazon API Gateway](https://aws.amazon.com/blogs/compute/resize-images-on-the-fly-with-amazon-s3-aws-lambda-and-amazon-api-gateway/)
* [Bucket Policy Examples](https://docs.aws.amazon.com/AmazonS3/latest/dev/example-bucket-policies.html)
* [Best practices](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html)
