# Serverless Image resizing

This lambda creates a thumbnail for each image file that is uploaded to a bucket.
You need to configure Amazon S3 to invoke this lambda when objects are created.
The Lambda function reads the image object from the source bucket, creates 
a thumbnail image and saves to destination bucket.

### Benefits of Lambda function:
* **No Servers to Manage**  
  AWS Lambda automatically runs your code without requiring you to provision
  or manage servers. Just write the code and upload it to Lambda.
* **Continuous Scaling**  
  AWS Lambda automatically scales your application by running code in
  response to each trigger. Your code runs in parallel and processes 
  each trigger individually, scaling precisely with the size of the workload.
* **Subsecond Metering**  
  With AWS Lambda, you are charged for every 100ms your code executes 
  and the number of times your code is triggered. You don’t pay anything
   when your code isn’t running.

### This lambda uses
Technical stack:
* Gradle 5.0
* Java 8
* [Thumbnailator 0.4.8](https://github.com/coobird/thumbnailator)
* AWS SDK 1.2

AWS Services:
* S3 storage
* AWS Lambda


### Code structure
I splitted the code into 3 parts:
* Handler - no business logic, lambda-specific code
* Controller - business logic
* Service - responsible for interacting with external services (S3) 


## Configure AWS
Here's the overview of what needs to be done to setup working solution:
1. Create the Execution Role (gives your function permission to access AWS resources)
1. Create Buckets for image storage and lambda storage (optional, but recommended)
1. Build and upload Lambda Function
1. Configure Amazon S3 image bucket to Publish Events
1. Test the Lambda Function by uploading test image

### Create lambda role
Open Amazon Console. Go to IAM module.  

Step 1. Create policy.   
Go to Policies -> Create policy 
* Click on JSON tab and paste Lambda role permissions from [below](#lambda-policies).
* Click review
* Put name: "create_thumbnail_policy"
* Click "Review Policy" and save.
* Remember ARN (something like arn:aws:iam::103219112999:policy/create_thumbnail_policy)
 
Step 2. Create Role  
Go to Roles -> Create Role
* Choose Lambda and click "Next: Permissions"
* Add "create_thumbnail_policy"
* Click "Next: Tags" -> "Next: Review"
* Role name: "create_thumbnail_role"

### Create function
Open Amazon Console. Go to Lambda module.
Create function -> Author from scratch:
* Name: CreateThumbnail
* Runtime: Java 8
* Role: Choose an existing
* Existing role: "create_thumbnail_role"

<a name="lambda-policies"/>

## Lambda Role Policies
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "AllowLogsStreaming",
            "Effect": "Allow",
            "Action": [
                "logs:CreateLogGroup",
                "logs:CreateLogStream",
                "logs:PutLogEvents"
            ],
            "Resource": "arn:aws:logs:*:*:*"
        },
        {
            "Sid": "AllowS3Access",
            "Effect": "Allow",
            "Action": "s3:PutObject",
            "Resource": "arn:aws:s3:::__YOUR_BUCKET_NAME_HERE__/*"
        }
    ]
}
```


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
      "Resource":["arn:aws:s3:::__YOUR_BUCKET_NAME_HERE__/*"]
    }
  ]
}
```

## Useful resources:
* [Tutorial: Using AWS Lambda with Amazon S3](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example.html)
* [Resize Images on the Fly with Amazon S3, AWS Lambda, and Amazon API Gateway](https://aws.amazon.com/blogs/compute/resize-images-on-the-fly-with-amazon-s3-aws-lambda-and-amazon-api-gateway/)
* [Bucket Policy Examples](https://docs.aws.amazon.com/AmazonS3/latest/dev/example-bucket-policies.html)
* [Best practices](https://docs.aws.amazon.com/lambda/latest/dg/best-practices.html)
