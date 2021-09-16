package lambda.email;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import request.EmailRequest;
import response.EmailResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailSender {

  public EmailResult handleRequest(EmailRequest request, Context context) {

    EmailResult emailResult = new EmailResult();
    LambdaLogger logger = context.getLogger();
    logger.log("Entering send_email");

    try {
      AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2).build();

      SendEmailRequest sendEmailRequest = new SendEmailRequest();
      sendEmailRequest.withDestination(
        new Destination().withToAddresses(request.to))
        .withMessage(new Message()
          .withBody(new Body()
            .withHtml(new Content()
              .withCharset("UTF-8").withData(request.htmlBody))
            .withText(new Content()
              .withCharset("UTF-8").withData(request.textBody)))
          .withSubject(new Content()
            .withCharset("UTF-8").withData(request.subject)))
        .withSource(request.from);

      client.sendEmail(sendEmailRequest);

      logger.log("Email sent!");

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      emailResult.message = "success";
      emailResult.timestamp = dtf.format(now);
    } catch (Exception ex) {
      logger.log("The email was not sent. Error message: " + ex.getMessage());
      throw new RuntimeException(ex);
    }
    finally {
      logger.log("Leaving send_email");
    }

    return emailResult;
  }

}
