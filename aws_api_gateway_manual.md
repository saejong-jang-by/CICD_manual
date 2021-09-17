### API Gateway manual

#Steps
- Login to the AWS API Gateway Console (https://us-west-2.console.aws.amazon.com/apigateway)
- Create a new web API (in the same region that your Lambda from the previous exercise is running in)
- Click the Create API button
- Select REST
- Select New API
- Specify API name and description
- Select Regional for Endpoint Type
- Create model definitions for the HTTP request and response bodies your web API will receive and send.
- Select “Models” on the left side
- Create an EmailRequest model
- For “Model name” enter “EmailRequest”
- For “Content type” enter “application/json”
- For “Model description” enter “Email request model”
- For “Model schema” enter the following text. This text is a “JSON schema” that defines the format of an email request JSON object.
{

  "title": "EmailRequest",
  "type": "object",
  "properties": {
    "to": {
      "type": "string",
      "description": "The to email address"
    },
    "from": {
      "type": "string",
      "description": "The from email address"
    },
    "subject": {
      "type": "string",
      "description": "The email's subject"
    },
    "textBody": {
      "type": "string",
      "description": "The plain text email content"
    },
    "htmlBody": {
      "type": "string",
      "description": "The HTML email content"
    }
  }
}

- Click the Create button
 

- Create an EmailResult model
- For “Model name” enter “EmailResult”
- For “Content type” enter “application/json”
- For “Model description” enter “Email result model”
- For “Model schema” enter the following text. This text is a “JSON schema” that defines the format of an email result JSON object.
{

  "title": "EmailResult",
  "type": "object",
  "properties": {
    "message": {
      "type": "string",
      "description": "Operation status message"
    },
    "timestamp": {
      "type": "string",
      "description": "Operation timestamp"
    }
  }
}

- Click the Create button
 

# Next, we will add some resources to your web API. Resources define the URLs that clients will use when calling your web API operations.

- Select “Resources” on the left side.
- Add the /sendemail resource to your web API.
- Select the root resource
- In the Actions menu, select Create Resource
- Configure as proxy resource: No
- Fill in Resource Name: sendemail
- Fill in Resource Path: sendemail
- Enable API Gateway CORS: Yes
- Click the Create Resource button
 

- Add a POST method to the /sendemail resource. (This means that clients will use HTTP POST requests to call the /sendemail endpoint.)
- Select the /sendemail resource
- In the Actions menu, select Create Method
- Select POST as the new method type
- Click the checkmark to create the method
- Integration type: Lambda Function
- Use Lambda Proxy Integration: No
- Lambda Region: accept default
- Lambda Function: select the Lambda function you created in the Lambda/IAM exercise (e.g., send_email)
- Use Default Timeout: Yes
- Click "OK" when asked if you want to give API Gateway permission to call your Lambda function
You have now created a web API endpoint for calling your send_email Lambda function.  This endpoint can be called by using an HTTP POST request that has /sendemail as the URL path and an appropriate JSON object in the HTTP request body (whatever JSON is expected by your send_email Lambda function).
 

# Next, configure the models for the HTTP request and response bodies used by the /sendemail POST method.
- Click on the “Method Request” link.
- Click on “Request Body”.
- Click “Add model”.
- For the model’s “Content type”, enter “application/json”
- For the model’s “Model name”, select “EmailRequest”
- This tells API Gateway that the request body for the /sendmail POST method should have the format defined by the EmailRequest model you created earlier.
- Click the “<- Method Execution” link in the top-left corner to go back to the previous screen.
- Click the “Method Response” link.
- Open the “200” section by clicking the icon to the left of “200”.
- In the “Response Body for 200” section, add a response model with a “Content type” of “application/json” and “Models” value of “EmailResult”.  (If there is already a response model of type “application/json”, you don’t need to create a new one; just modify its “Models” value to be “EmailResult” by clicking the edit icon.)
- This tells API Gateway that the response body for the /sendemail POST method should have the format defined by the EmailResult model you created earlier.
- Click the “<- Method Execution” link in the top-left corner to go back to the previous screen.
 
# Do an internal test of your /sendemail endpoint.
- Click Test (lightning bolt)
- In the "Request Body" field, enter a JSON object containing:
{
        "to": "INSERT A TO EMAIL ADDRESS HERE",
        "from": "INSERT A FROM EMAIL ADDRESS HERE",
        "subject": "Test Message",
        "textBody": "This is a test ...",
        "htmlBody": "This is a test ..."
}
- Click the Test (lightning bolt) button
Look in the "Logs" field to see the log output for the test request.
If all went well, an email should have been sent.
 

# Deploy your API.  This will make it callable from outside the AWS environment by anyone on the Web.
- In the Actions menu, select Deploy API
- Select [New Stage]
- Give your stage a name (e.g., dev) and description
- Click the Deploy button
 

# Do an external test of your /sendemail endpoint.
Using Curl, Postman, or an equivalent tool, call your web API from outside the AWS environment.  Curl is a command-line program that lets you construct and send HTTP requests. Postman is a GUI-based tool that lets you do the same thing.  You may choose whichever you prefer: command-line or GUI
If you want to use Curl, do the following:
Try running the "curl" command in a shell.  If you don't already have it installed, you can download it from https://curl.haxx.se/download.html (Links to an external site.)
Put the request body you want to send into a text file, e.g., data.txt
Run the following curl command to call your web API:
curl -d @data.txt -X POST <WEB-API-URL>
For example,

curl -d @data.txt -X POST https://gqv3z38u0i.execute-api.us-west-2.amazonaws.com/dev/sendemail (Links to an external site.)
If you want to use Postman, do the following:
If you don't have it, you can download Postman here:
https://www.getpostman.com/downloads/ (Links to an external site.)
In Postman, create a request, select POST as the request type, specify your web API's URL, and in the Body tab select "raw" and enter the request JSON object
Click the Send button
 

So far we have been specifying the email parameters in a JSON object contained in the HTTP request body.  Next, you will learn how to send parameters in the URL and HTTP headers instead of the request body.  Specifically, the "to" and "from" email addresses will be specified in the URL, and the subject and body text will be specified in HTTP headers named "EmailSubject" and "EmailText".  Note that because we are not passing parameters in through the HTTP request body, we will not define a “model” for the request body.  Instead, we will define the URL parameters and HTTP headers used to pass in the email parameters.
In the API Gateway console, underneath the /sendemail resource create a sub-resource with the following values:
Configure as proxy resource: No
Resource Name:  to
Resource Path:  {to}
Enable API Gateway CORS: Yes
The curly braces in the path mean that this part of the URL is variable, not fixed.
Underneath the /sendemail/{to} resource create a sub-resource with the following values:
Configure as proxy resource: No
Resource Name:  from
Resource Path:  {from}
Enable API Gateway CORS: Yes
Again, the curly braces in the path mean that this part of the URL is variable, not fixed.
Add a POST method to the /sendemail/{to}/{from} resource.  (This means that clients will use HTTP POST requests to call the /sendemail/{to}/{from} endpoint.)
Select the /sendemail/{to}/{from} resource
In the Actions menu, select Create Method
Select POST as the new method type
Click the checkmark to create the method
Integration type: Lambda Function
Use Lambda Proxy Integration: No
Lambda Region: accept default
Lambda Function: select the Lambda function you created in the Lambda/IAM exercise (e.g., send_email)
Use Default Timeout: Yes
Click "OK" when asked if you want to give API Gateway permission to call your Lambda function
Next, we need to define the HTTP headers that callers should send to the /sendemail/{to}/{from} POST method. We will define a header named “EmailSubject” that will be used to pass in the email’s subject, and a header named “EmailText” that will be used to pass in the email’s body text.
Click the “Method Request” link.
Click on “HTTP Request Headers”.
Click “Add Header” to create a header named “EmailSubject”, and make it required by clicking the Required checkbox.
Click “Add Header” to create a header named “EmailText”, and make it required by clicking the Required checkbox.
This tells API Gateway that calls to the /sendemail/{to}/{from} POST method should contain these two HTTP headers that define the email’s subject and body text.
Next, we need to map the {to} and {from} URL parameters, as well as the "EmailSubject" and "EmailText" HTTP headers to a JSON object that will be sent to the Lambda function.  This will be accomplished by creating a “mapping template”.  Mapping templates describe how to map the parts of an HTTP request (URL parameters, HTTP headers, etc.) to a JSON object that will be passed to the Lambda function.
Click "Integration Request"
Keep the default values for all of the settings
Click on the Mapping Templates section
For "Request body passthrough", select: Never
Click "Add mapping template"
For the Content-Type, enter "application/json" (it looks like it's already there, but it isn't).  This setting means that this mapping template will only be applied to HTTP requests that contain “application/json” in the HTTP Content-Type header.
Enter the following text for the "application/json" template:
{

 "to": "$input.params('to')",
 "from": "$input.params('from')",
 "subject": "$input.params('EmailSubject')",
 "textBody": "$input.params('EmailText')",
 "htmlBody": "$input.params('EmailText')"

}
Click the "Save" button
Go back to the Method configuration screen by clicking “<- Method Execution” in the top-left corner)
 

Next, configure the models for the HTTP response body used by the /sendemail/{to}/{from} POST method.
Click the “Method Response” link.
Open the “200” section by clicking the icon to the left of “200”.
In the “Response Body for 200” section, add a response model with a “Content type” of “application/json” and “Models” value of “EmailResult”.  (If there is already a response model of type “application/json”, you don’t need to create a new one; just modify its “Models” value to be “EmailResult” by clicking the edit icon.)
This tells API Gateway that the response body for the /sendemail/{to}/{from} POST method should have the format defined by the EmailResult model you created earlier.
Click the “<- Method Execution” link in the top-left corner to go back to the previous screen.
 

# Do an internal test of your /sendemail/{to}/{from} endpoint.
Click Test (lightning bolt)
In the Path section, enter values for the {from} and {to} email addresses
In the Headers text area, enter the following text:
EmailSubject: My Email Subject
EmailText: My Email Text
Click the Test (lightning bolt) button
Look in the "Logs" field to see the log output for the test request.
If all went well, an email should have been sent.
Re-deploy your API
Select the stage you created earlier
Click the Deploy button
Externally Test Your Web API
 Using Curl, Postman, or an equivalent tool, call your web API from outside the AWS environment
If you want to use Curl, do the following:
Run the following curl command to call your web API:
curl -X POST -H "EmailSubject: My Email Subject" -H "EmailText: My Email Text" <WEB-API-URL>/<TO-EMAIL-ADDRESS>/<FROM-EMAIL-ADDRESS>
For example,

curl -X POST -H "EmailSubject: My Email Subject" -H "EmailText: My Email Text" https://gqv3z38u0i.execute-api.us-west-2.amazonaws.com/dev/sendemail/bob@uvnets.com/bob@gmail.com (Links to an external site.)
If you want to use Postman, do the following:
In Postman, create a request, select POST as the request type, specify your web API's URL.
Include the TO and FROM emails addresses in the URL.
In the Headers tab, create the following HTTP headers
KEY: EmailSubject VALUE: My Email Subject

KEY: EmailText  VALUE: My Email Text
Click the Send button
To call your web API from a client program, you need to generate a client SDK for your environment of choice, and incorporate it into your client project.  Client SDKs for the following environments can be generated: Java, Android, iOS, Javascript, Ruby