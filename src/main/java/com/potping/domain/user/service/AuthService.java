package com.potping.domain.user.service;

import com.potping.domain.user.dto.request.SignupRequestDto;
import com.potping.domain.user.entity.User;
import com.potping.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;

    /**
     * 회원가입
     * @param dto 회원가입 요청 데이터 (아이디, 비번, 역할)
     * @return 가입된 사용자의 PK (user_id)
     * @throws IllegalArgumentException 이미 존재하는 아이디일 경우 예외 발생
     */
    @Transactional
    public Long signup(SignupRequestDto dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        User newUser = User.builder()
                .username(dto.username())
                .password(dto.password())
                .role(dto.role())
                .build();

        userRepository.save(newUser);

        return newUser.getId();
    }

    /**
     * 로그인
     * @param username 로그인 아이디
     * @param password 비밀번호
     * @return 로그인에 성공한 User 엔티티
     * @throws IllegalArgumentException 아이디가 없거나 비밀번호가 일치하지 않을 경우 예외 발생
     */
    public User login(String username, String password) {
        // 1. 아이디 존재 여부 확인
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 일치 확인
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }


}
