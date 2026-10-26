package com.attendify.service;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${attendify.mail.sender-email:}") private String senderEmail;
    @Value("${attendify.mail.sender-name:ATTENDIFY}") private String senderName;
    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }
    public void sendOtp(String to, String name, String otp) {
        try {
            MimeMessage msg = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(msg, "UTF-8");
            h.setFrom(new InternetAddress(senderEmail, senderName));
            h.setTo(to); h.setSubject("ATTENDIFY — Your 6-Digit Login OTP");
            h.setText("<div style='font-family:sans-serif;padding:24px;border:1px solid #e5e7eb;border-radius:12px;'>"
                + "<h2 style='color:#4F46E5;margin:0;'>ATTENDIFY</h2><p>Smart Attendance. Simplified.</p>"
                + "<p>Hello <b>" + name + "</b>, your single-use verification code is:</p>"
                + "<h1 style='letter-spacing:8px;background:#f5f7fb;padding:14px;text-align:center;border-radius:8px;'>" + otp + "</h1>"
                + "<p style='color:#64748B;font-size:12px;'>Valid for 5 minutes. Never share this OTP.</p></div>", true);
            mailSender.send(msg);
        } catch (Exception e) {
            throw new IllegalStateException("Brevo SMTP delivery failed: " + e.getMessage());
        }
    }
}
