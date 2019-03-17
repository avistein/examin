package service;

import com.sendgrid.*;
import javafx.concurrent.Task;
import model.Email;

import java.io.IOException;
import java.nio.file.Paths;

import static util.ConstantsUtil.*;

/**
 * Service class to send an Email using the SendGrid API.
 *
 * @author Avik Sarkar
 */
public class EmailService {

    /*--------------------------------------------Declaration of variables--------------------------------------------*/

    private FileHandlingService fileHandlingService;

    /*----------------------------------------End of Declaration of variables-----------------------------------------*/

    /**
     * Public default constructor to initialize data
     */
    public EmailService() {

        fileHandlingService = new FileHandlingService();
    }

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

                SendGrid sg;

                //if the email.properties exists in the system then get the api key from there
                if (Paths.get(USER_HOME, ROOT_DIR, CONFIG_DIR, "email.properties").toFile().exists()) {

                    //the API key
                    String apiKey = fileHandlingService.loadPropertiesValuesFromPropertiesFile
                            ("email.properties", "sendGridApiKey").get("sendGridApiKey");


                    sg = new SendGrid(apiKey);
                }

                //api key doesn't exist in the system , so email sending failed
                else {

                    return false;
                }

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
