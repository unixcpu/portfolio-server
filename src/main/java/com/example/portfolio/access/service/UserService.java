package com.example.portfolio.access.service;

import com.example.portfolio.access.repo.RoleRepository; // 수정된 import
import com.example.portfolio.access.repo.UserRepository;
import com.example.portfolio.access.repo.UserRoleRepository;
import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.model.User;
import com.example.portfolio.access.model.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final RoleRepository roleRepository; // 수정된 필드
	private final PasswordEncoder passwordEncoder;
	private final UserRoleRepository userRoleRepository; // 수정된 필드
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository; // 수정된 필드 초기화
		this.passwordEncoder = passwordEncoder;
		this.userRoleRepository = userRoleRepository;
	}
	
	public Role findRoleByUsername(String username) {
		return userRepository.findRoleByUsername(username);
	}
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public void saveUser(User user) {
		// 널 체크 및 입력값 검증
		Assert.notNull(user, "User must not be null");
		Assert.hasText(user.getUsername(), "Username must not be empty");
		Assert.hasText(user.getPassword(), "Password must not be empty");
		
		// 데이터베이스에 사용자 저장
		userRepository.save(user);
	}
	
	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	// 사용자 등록 메서드
	@Transactional
	public void registerUser(String username, String password) {
		// 사용자 존재 여부 확인
		if (userRepository.existsByUsername(username)) {
			throw new UsernameAlreadyExistsException(username); // 예외 던지기
		}
		
		
		// 새로운 사용자 객체 생성
		User user = new User();
		user.setUsername(username);
		user.setPassword(passwordEncoder.encode(password)); // 비밀번호 암호화
		
		String roleName = "USER"; // 기본 역할
		
		// 역할 조회
		Role role = roleRepository.findByName(roleName)
				            .orElseThrow(() -> new IllegalArgumentException("Invalid role name"));
		
		// 역할을 UserRole로 연결
		UserRole userRole = new UserRole();
		userRole.setUser(user);
		userRole.setRole(role);
		
		user.getUserRoles().add(userRole); // UserRole 추가
		
		// 사용자 저장
		userRepository.save(user);
	}
	
	// 사용자 프로필 업데이트 로직
	public void updateUserProfile(Authentication authentication, String username, String email, String password, MultipartFile profilePic) throws Exception {
		String currentUsername = authentication.getName();
		User currentUser = findByUsername(currentUsername);
		
		// 프로필 사진 저장 로직
		if (profilePic != null && !profilePic.isEmpty()) {
			saveProfilePicture(profilePic, currentUser);
		}
		
		// 사용자 정보 업데이트
		currentUser.setUsername(username);
		currentUser.setEmail(email);
		
		// 비밀번호가 변경되었는지 확인 (기존 해시된 비밀번호와 비교)
		if (password != null && !password.isEmpty()) {
			// 비밀번호가 다르면 인코딩 후 저장
			if (!passwordEncoder.matches(password, currentUser.getPassword())) {
				currentUser.setPassword(encodePassword(password));
			} else {
				System.out.println("비밀번호가 기존과 동일하여 변경하지 않음");
			}
		}
		
		// 변경된 사용자 정보 저장
		saveUser(currentUser);
	}
	
	// 프로필 사진 저장 로직
	public void saveProfilePicture(MultipartFile profilePic, User user) throws Exception {
		String uploadDir = "src/main/resources/static/images/";
		String originalFilename = profilePic.getOriginalFilename();
		//String fileName = originalFilename; // 고유한 파일 이름 생성
		Path filePath = Paths.get(uploadDir, originalFilename).toAbsolutePath();
		
		// 디렉토리가 존재하지 않으면 생성
		Files.createDirectories(filePath.getParent());
		
		// 파일이 존재하는지 확인
		if (Files.notExists(filePath)) {
			// 파일 저장
			//Files.copy(profilePic.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			profilePic.transferTo(filePath.toFile());
			System.out.println("파일 저장됨: " + filePath.toString());
		} else {
			System.out.println("파일이 이미 존재합니다: " + filePath.toString());
		}
		
		// 저장된 파일 경로를 사용자 프로필 이미지 URL로 설정
		user.setProfilePicUrl("/images/" + originalFilename);
		
		// 로그 출력
		System.out.println("프로필 사진 저장됨: " + user.getProfilePicUrl());
	}
	
	// 사용자 ID로 사용자 찾기
	public User findById(Long ownerId) {
		Optional<User> user = userRepository.findById(ownerId); // 사용자 찾기
		return user.orElse(null); // 사용자가 없을 경우 null 반환
	}
}
