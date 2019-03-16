package model;

/**
 * POJO class for Email entity.
 * An instance of this class is used to store email details like sender email address, recipient email address,
 * subject, body and content type.
 *
 * @author Avik Sarkar
 */
public class Email {

    private String senderEmail;
    private String recipientEmail;
    private String subject;
    private String body;
    private String contentType;

    /**
     * Public constructor to initialize the data.
     *
     * @param senderEmail    The sender email with which this.senderEmail will be set.
     * @param recipientEmail The recipient email with which this.recipientEmail will be set.
     * @param subject        The subject with which this.subject will be set.
     * @param body           The body with which this.body will be set.
     * @param contentType    The contentType with which this.contentType will be set.
     */
    public Email(String senderEmail, String recipientEmail, String subject, String body, String contentType) {

        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.body = body;
        this.contentType = contentType;
    }

    /**
     * Getter method to get the senderEmail.
     *
     * @return The senderEmail.
     */
    public String getSenderEmail() {

        return senderEmail;
    }

    /**
     * Setter method to set senderEmail.
     *
     * @param senderEmail The senderEmail to set with.
     */
    public void setSenderEmail(String senderEmail) {

        this.senderEmail = senderEmail;
    }

    /**
     * Getter method to get the recipientEmail.
     *
     * @return The recipientEmail.
     */
    public String getRecipientEmail() {

        return recipientEmail;
    }

    /**
     * Setter method to set recipientEmail.
     *
     * @param recipientEmail The recipientEmail to set with.
     */
    public void setRecipientEmail(String recipientEmail) {

        this.recipientEmail = recipientEmail;
    }

    /**
     * Getter method to get the subject.
     *
     * @return The subject.
     */
    public String getSubject() {

        return subject;
    }

    /**
     * Setter method to set subject.
     *
     * @param subject The subject to set with.
     */
    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Getter method to get the body.
     *
     * @return The body.
     */
    public String getBody() {

        return body;
    }

    /**
     * Setter method to set body.
     *
     * @param body The body to set with.
     */
    public void setBody(String body) {

        this.body = body;
    }

    /**
     * Getter method to get the contentType.
     *
     * @return The contentType.
     */
    public String getContentType() {

        return contentType;
    }

    /**
     * Setter method to set contentType.
     *
     * @param contentType The contentType to set with.
     */
    public void setContentType(String contentType) {

        this.contentType = contentType;
    }
}
