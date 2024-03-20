package com.study.library.service;

import com.study.library.jwt.JwtProvider;
import com.study.library.security.PrincipalUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class AccountMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private JwtProvider jwtProvider;

    @Value("${spring.mail.address}")
    private String fromMailAddress;

    @Value("${server.deploy-address}")
    private String serverAddress;

    @Value("${server.port}")
    private String serverPort;

    public boolean sendAuthMail() {
        boolean result = false;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        int userId = principalUser.getUserId();
        String toMailAddress = principalUser.getEmail();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8"); // 파일을 전송한다면 두번째 매개변수 true
            helper.setSubject("도서관리 시스템 사용자 메일 인증"); //메일의 제목
            helper.setFrom(fromMailAddress);
            helper.setTo(toMailAddress);

            String authMailToken = jwtProvider.generateAuthMailToken(userId, toMailAddress);

            StringBuilder mailContent = new StringBuilder();
            mailContent.append("<div>");
            mailContent.append("<h1>계정 활성화 절차</h1>");
            mailContent.append("<h3>해당 계정을 인증하기 위해 아래의 버튼을 클릭해 주세요</h3>");
            mailContent.append("<a href=\"http://" + serverAddress + ":" + serverPort + "/mail/authenticate?authToken=" + authMailToken + "\" style=\"border: 1px solid #dbdbdb; padding: 10px 20px; text-decoration: none; background-color: white; color: #222222;\">인증하기</a>");
            //http://localhost:8080/mail/authenticate?authToken=JWT토큰
            mailContent.append("</div>");

            mimeMessage.setText(mailContent.toString(), "utf-8", "html");

            javaMailSender.send(mimeMessage); // 메일 전송
            result = true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return result;
    }
    public Map<String, String> authenticate(String token) {
        Claims claims = null;
        Map<String, String> resultMap = null;

        // ExpiredJwtException => 토큰 유효기간 만료
        // MalformedJwtException => 위조, 변조
        // SignatureException => 형식, 길이 오류
        // IllegalArgumentException => null 또는 빈값

        try {
            claims = jwtProvider.getClaims(token);
        } catch (IllegalArgumentException | SignatureException e) {
            resultMap = Map.of("message", "잘못된 요청입니다.");
        } catch (MalformedJwtException e) {
            resultMap = Map.of("message", "인증 메일을 다시 요청하세요.");
        } catch (ExpiredJwtException e) {
            resultMap = Map.of("message", "인증 시간이 만료된 요청입니다.");
        } catch (JwtException e) {
            resultMap = Map.of("message", "인증 메일을 다시 요청 하세요.");
        }

        return resultMap;
    }
}
