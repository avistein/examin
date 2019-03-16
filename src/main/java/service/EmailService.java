package service;

import com.sendgrid.*;
import javafx.concurrent.Task;
import model.Email;

import java.io.IOException;

/**
 * Service class to send an Email using the SendGrid API.
 *
 * @author Avik Sarkar
 */
public class EmailService {

    /**
     * This method is used to get a task which can be used to send an email over the Internet using SendGrid API.
     * The network connection is being made in separate thread instead of the Main Application thread.
     *
     * @param email The email object containing the Email details.
     * @return A Task which can be which can be used to send an email over the Internet using SendGrid API.
     */
    public Task<Boolean> getSendEmailTask(Email email) {

        Task<Boolean> sendEmailTask = new Task<>() {

            @Override
            protected Boolean call() {

                //the sender email
                com.sendgrid.Email from = new com.sendgrid.Email(email.getSenderEmail());

                //the recipient email
                com.sendgrid.Email to = new com.sendgrid.Email(email.getRecipientEmail());

                //body of the email and content type
                Content content = new Content(email.getContentType(), email.getBody());

                //form the mail using subject and body
                Mail mail = new Mail(from, email.getSubject(), to, content);

                //the API key
                SendGrid sg = new SendGrid("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");

                Request request = new Request();

                try {

                    request.setMethod(Method.POST);
                    request.setEndpoint("mail/send");
                    request.setBody(mail.build());
                    sg.api(request);
                    return true;
                } catch (IOException ex) {

                    ex.printStackTrace();
                    return false;
                }
            }
        };
        return sendEmailTask;
    }
}
