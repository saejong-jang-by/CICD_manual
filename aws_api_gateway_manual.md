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
-
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
-
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

API endpoint for calling your send_email Lambda function. 
This endpoint can be called by using an HTTP POST request that has /sendemail as the URL path and an appropriate JSON object in the HTTP request body
(whatever JSON is expected by your send_email Lambda function).
 

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
-
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
 - curl
 - postman
 
# Do an internal test of your /sendemail/{to}/{from} endpoint.
- Click Test (lightning bolt)
- In the Path section, enter values for the {from} and {to} email addresses
- In the Headers text area, enter the following text:
- EmailSubject: My Email Subject
- EmailText: My Email Text
- Click the Test (lightning bolt) button
- Look in the "Logs" field to see the log output for the test request.
- If all went well, an email should have been sent.
- Re-deploy your API
- Select the stage you created earlier
- Click the Deploy buttonlient program, you need to generate a client SDK for your environment of choice, and incorporate it into your client project.  Client SDKs for the following environments can be generated: Java, Android, iOS, Javascript, Ruby