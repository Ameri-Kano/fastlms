package com.zerobase.fastlms.configuration;

import com.zerobase.fastlms.member.entity.LoginHistory;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.repository.LoginHistoryRepository;
import com.zerobase.fastlms.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Configuration
public class UserAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public UserAuthenticationSuccessHandler(LoginHistoryRepository loginHistoryRepository,
                                            MemberRepository memberRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        String userId = request.getParameter("username");
        String userAgent = request.getHeader("user-agent");
        String ip = request.getRemoteAddr();
        LocalDateTime loginDate = LocalDateTime.now();

        LoginHistory loginHistory = LoginHistory.builder()
                .userId(userId)
                .loginDate(loginDate)
                .ip(ip)
                .userAgent(userAgent)
                .build();

        loginHistoryRepository.save(loginHistory);

        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("멤버가 존재하지 않습니다."));
        member.setLastLoginDate(loginDate);
        memberRepository.save(member);

        log.info("Login user_id: " + userId);
        log.info("Login time" + loginDate);
        log.info("Login IP: " + ip);
        log.info("Login User Agent: " + userAgent);
        log.info(loginHistoryRepository
                .findFirstByUserIdOrderByLoginDateDesc(userId)
                .get().getLoginDate().toString());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
