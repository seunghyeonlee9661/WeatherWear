package com.sparta.WeatherWear.service;


import com.sparta.WeatherWear.dto.UserRequestDTO;
import com.sparta.WeatherWear.entity.User;
import com.sparta.WeatherWear.repository.UserRepository;
import com.sparta.WeatherWear.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class WeatherwearService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* 회원가입 */
    @Transactional
    public ResponseEntity<String> createUser(UserRequestDTO userCreateRequestDTO){
        if (userRepository.findByEmail(userCreateRequestDTO.getEmail()).isPresent()) throw new IllegalArgumentException("중복된 Email 입니다.");
        User user = new User(userCreateRequestDTO,passwordEncoder.encode(userCreateRequestDTO.getPassword()));
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    /* 회원 탈퇴*/
    @Transactional
    public ResponseEntity<String> removeUser(UserDetailsImpl userDetails, String password){
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(password,user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        }
        userRepository.delete(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    /* 강사 수정 */
    @Transactional
    public ResponseEntity<String> updateUser(UserDetailsImpl userDetails, UserRequestDTO userCreateRequestDTO) {
        User user = userDetails.getUser();
        if(!passwordEncoder.matches(userCreateRequestDTO.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 올바르지 않습니다.");
        }
        user.update(userCreateRequestDTO);
        return ResponseEntity.ok().body("회원가입 성공");
    }

}
