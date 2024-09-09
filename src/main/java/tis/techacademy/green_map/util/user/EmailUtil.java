package tis.techacademy.green_map.util.user;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tis.techacademy.green_map.repository.UserRepository;
import tis.techacademy.green_map.util.token.Token;
import tis.techacademy.green_map.util.token.TokenGenerator;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    private final TokenGenerator tokenGenerator;

    private final UserRepository userRepository;

    public void sendSetPasswordEmail(String email) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        Token token = tokenGenerator.generateUUIDToken();

        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setToken(token.getToken());
        user.setTokenCreatedAt(token.getCreatedAt());
        userRepository.save(user);

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText("""
                <div>
                <a href="http://localhost:8080/api/auth/set-password/%s/%s" target="_blank">Click link to change your password</a>
                </div>
                """.formatted(email, token.getToken()), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
                <div>
                  <a href="http://localhost:8080/api/auth/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
                </div>
                """.formatted(email, otp), true);

        javaMailSender.send(mimeMessage);
    }
}