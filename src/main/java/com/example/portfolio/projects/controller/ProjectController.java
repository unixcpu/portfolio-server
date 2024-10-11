package com.example.portfolio.projects.controller;

import com.example.portfolio.access.model.Role;
import com.example.portfolio.access.service.UserService;
import com.example.portfolio.projects.model.Project;
import com.example.portfolio.access.model.User;
import com.example.portfolio.projects.model.ProjectUser;
import com.example.portfolio.projects.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private UserService userService; // Assuming you have a UserService to get users
	
	@Autowired
	private ResourceLoader resourceLoader;
	// 프로젝트 목록 조회
	@GetMapping("/list")
	public String listProjects(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("projects", projectService.getAllProjects());
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		return "projects/list"; // 수정된 부분
	}
	
	@GetMapping("/new")
	public String createProjectForm(Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		
		// 모든 사용자 가져오기
		List<User> users = userService.getAllUsers(); // 전체 사용자 리스트
		Role userRole = userService.findRoleByUsername(username);
		List<ProjectUser> projectUsers = projectService.getAllProjectUserByUser(user);
		
		List<Project> allProjects = new ArrayList<>();
		// ProjectUser 리스트 순회
		for (ProjectUser projectUser : projectUsers) {
			// 각 ProjectUser에 대한 작업 수행
			Project project = projectUser.getProject(); // ProjectUser로부터 프로젝트 가져오기
			allProjects.add(project);
		}
		
		model.addAttribute("users", users); // 사용자 리스트를 모델에 추가
		model.addAttribute("role", userRole.getName());
		model.addAttribute("user", user);
		model.addAttribute("project", new Project());
		model.addAttribute("allProjects", allProjects);
		return "projects/form"; // 수정된 부분
	}
	
	// 프로젝트 저장
	@PostMapping
	public String saveProject(@ModelAttribute Project project, Model model, Authentication authentication, @RequestParam("attachments") MultipartFile[] files) throws IOException {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		String absolutePath = "src/main/resources/static/uploads/";
		project.setOwner(user);
		for (MultipartFile file : files) {
			if (!file.isEmpty()) { // 파일이 비어있지 않은지 확인
				String fileName = file.getOriginalFilename();
				project.setUpdateFile(fileName);
				String filePath = absolutePath + fileName;
				Path FilePath = Paths.get(filePath);
				Files.createDirectories(FilePath.getParent());
				project.setUpdateFileUrl(filePath);
				
				if (Files.notExists(FilePath)) {
					// 파일 저장
					Files.copy(file.getInputStream(), FilePath);
					System.out.println("파일 저장됨: " + filePath.toString());
				} else {
					System.out.println("파일이 이미 존재합니다: " + filePath.toString());
				}
			}
		}
		
		
		ProjectUser projectUser = new ProjectUser();
		projectUser.setProject(project); // 프로젝트 설정
		projectUser.setUser(user); // 사용자 설정
		
		projectService.saveProject(project, projectUser);
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		
		return "redirect:/projects/list";
	}
	
	// 프로젝트 수정 폼
	@GetMapping("/{id}/edit")
	public String editProjectForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		model.addAttribute("project", projectService.getProjectById(id)
				                              .orElseThrow(() -> new IllegalArgumentException("Invalid project Id:" + id)));
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		return "projects/form"; // 수정된 부분
	}
	
	// 프로젝트 삭제
	@PostMapping("/{id}/delete")
	public String deleteProject(@PathVariable("id") Long id, Model model, Authentication authentication) {
		String username = authentication.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		
		projectService.deleteProject(id);
		Role userRole = userService.findRoleByUsername(username);
		model.addAttribute("role", userRole.getName());
		return "redirect:/projects/list"; // 수정된 부분
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, "startDate", new CustomDateEditor(dateFormat, true));
		binder.registerCustomEditor(Date.class, "dueDate", new CustomDateEditor(dateFormat, true));
	}
	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
		try {
			System.out.println("Requested filename: " + filename);
			Path file = Paths.get("uploads").resolve(filename).normalize();
			System.out.println("File path: " + file.toString());
			Resource resource = new UrlResource(file.toUri());
			if (!resource.exists()) {
				throw new FileNotFoundException("File not found " + filename);
			}
			
			return ResponseEntity.ok()
					       .contentType(MediaType.APPLICATION_OCTET_STREAM)
					       .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
					       .body(resource);
			} catch (MalformedURLException | FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("Error occurred: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
	}
}
