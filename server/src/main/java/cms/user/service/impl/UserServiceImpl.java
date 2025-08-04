package cms.user.service.impl;

import cms.user.domain.User;
import cms.user.domain.UserRoleType;
import cms.user.domain.UserSpecification;
import cms.user.dto.*;
import cms.user.repository.UserRepository;
import cms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto, String createdBy, String createdIp) {
        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .avatarUrl(userDto.getAvatarUrl())
                .status(userDto.getStatus())
                .groupId(userDto.getGroupId())
                .createdBy(userRepository.findById(createdBy).orElse(null))
                .createdIp(createdIp)
                .build();

        return convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto, String updatedBy, String updatedIp) {
        User user = userRepository.findById(userDto.getUuid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(userDto.getUsername());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        user.setAvatarUrl(userDto.getAvatarUrl());
        user.setStatus(userDto.getStatus());
        user.setGroupId(userDto.getGroupId());
        user.setUpdatedBy(userRepository.findById(updatedBy).orElse(null));
        user.setUpdatedIp(updatedIp);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public UserDto getUser(String uuid) {
        return userRepository.findById(uuid)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserEnrollmentHistoryDto> getUsers(String username, String name, String phone, String lessonTime,
            String payStatus, String searchKeyword, Pageable pageable) {

        Page<User> userPage;
        boolean useNativeQuery = (lessonTime != null && !lessonTime.trim().isEmpty())
                || (payStatus != null && !payStatus.trim().isEmpty())
                || (searchKeyword != null && !searchKeyword.trim().isEmpty());

        if (useNativeQuery) {
            // 복합 검색: 네이티브 쿼리를 사용하여 "최근" 이력 기반으로 정확히 필터링
            userPage = userRepository.findUsersWithEnrollmentFilters(username, name, phone, lessonTime, payStatus,
                    searchKeyword,
                    pageable);
        } else {
            // 단순 검색: Specification을 사용하여 사용자 기본 정보만으로 필터링
            Specification<User> spec = UserSpecification.search(username, name, phone, searchKeyword);
            userPage = userRepository.findAll(spec, pageable);
        }

        // 임시로 빈 결과 반환 (TODO: 필요시 구현)
        List<UserEnrollmentHistoryDto> dtoList = new ArrayList<>();
        return new PageImpl<>(dtoList, pageable, 0);
    }

    @Override
    @Transactional
    public void changePassword(String uuid, String newPassword, String updatedBy, String updatedIp) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedBy(userRepository.findById(updatedBy).orElse(null));
        user.setUpdatedIp(updatedIp);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDto updateStatus(String uuid, String status, String updatedBy, String updatedIp) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(status);
        user.setUpdatedBy(userRepository.findById(updatedBy).orElse(null));
        user.setUpdatedIp(updatedIp);

        return convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto registerUser(UserRegisterRequest request, String createdBy, String createdIp) {
        UserDto userDto = UserDto.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .name(request.getName())
                .email(request.getEmail())
                .role(request.getRole())
                .avatarUrl(request.getAvatarUrl())
                .status("ACTIVE")
                .groupId(request.getGroupId())
                .build();

        return createUser(userDto, createdBy, createdIp);
    }

    @Override
    @Transactional
    public UserDto registerSiteManager(SiteManagerRegisterRequest request, String createdBy, String createdIp) {
        UserDto userDto = UserDto.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .name(request.getName())
                .email(request.getEmail())
                .role(UserRoleType.ADMIN)
                .avatarUrl(request.getAvatarUrl())
                .status("ACTIVE")
                .groupId(request.getGroupId())
                .build();

        return createUser(userDto, createdBy, createdIp);
    }

    @Override
    public SiteInfo getSiteInfo() {
        // Implementation depends on your requirements
        return new SiteInfo();
    }

    @Override
    @Transactional
    public UserDto updateResetToken(String email, String resetToken, LocalDateTime resetTokenExpiry) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setResetToken(resetToken);
        user.setResetTokenExpiry(resetTokenExpiry);

        return convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updatePasswordWithResetToken(String resetToken, String newPassword) {
        User user = userRepository.findByResetToken(resetToken)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        return convertToDto(userRepository.save(user));
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .groupId(user.getGroupId())
                .resetToken(user.getResetToken())
                .resetTokenExpiry(user.getResetTokenExpiry())
                .createdBy(user.getCreatedBy() != null ? user.getCreatedBy().getUuid() : null)
                .createdIp(user.getCreatedIp())
                .createdAt(user.getCreatedAt())
                .updatedBy(user.getUpdatedBy() != null ? user.getUpdatedBy().getUuid() : null)
                .updatedIp(user.getUpdatedIp())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}