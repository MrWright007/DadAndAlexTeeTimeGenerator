package com.alextest;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    public static void email(String teeTimeTime, String teeTimeCourse, String teeTimeDate ) {

        // Recipient's email ID needs to be mentioned.
        String to = "gregandalexwright@googlegroups.com";

        // Sender's email ID needs to be mentioned
        String from = "alexwright923@gmail.com";

        // Assuming you are sending email from through gmails smtp
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("alexwright923@gmail.com", "spiderman.23");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("New Tee Time Booked for "+teeTimeTime+" on "+teeTimeDate+" for the "+teeTimeCourse+" course at Bradshaw!");

            // Now set the actual message
            // Send the actual HTML message.
            message.setContent(
                    "<!-- #######  THIS IS A COMMENT - Visible only in the source editor #########-->\n" +
                            "<h2>New Tee Time Created by Alex's Awesome Tee-Time Generator!</h2>\n" +
                            "<p style=\"font-size: 1.5em;\">Alex and Greg have secured a Tee Time for 2 at the time:&nbsp;<strong style=\"background-color: #317399; padding: 0 5px; color: #fff;\">"+teeTimeTime+"</strong>&nbsp;at Bradshaw Farms on the <strong>"+teeTimeCourse+"&nbsp;</strong>course on the date&nbsp;<strong>"+teeTimeDate+"</strong>.</p>\n" +
                            "<p style=\"font-size: 1.5em;\">Thank you for being a great Dad! <img src=\"https://html5-editor.net/images/smiley.png\" alt=\"smiley\" /></p>\n" +
                            "<table class=\"editorDemoTable\" style=\"background-color: #b7e9f7;\">\n" +
                            "<tbody>\n" +
                            "<tr>\n" +
                            "<td><strong>Time</strong></td>\n" +
                            "<td><strong>Course</strong></td>\n" +
                            "<td><strong>Date</strong></td>\n" +
                            "</tr>\n" +
                            "<tr>\n" +
                            "<td>"+teeTimeTime+"</td>\n" +
                            "<td>"+teeTimeCourse+"</td>\n" +
                            "<td>"+teeTimeDate+"</td>\n" +
                            "</tr>\n" +
                            "</tbody>\n" +
                            "</table>\n" +
                            "<p>&nbsp;</p>\n" +
                            "<p><em>This email brought to you by Alex Wright Industries.</em></p>",
                    "text/html");

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}