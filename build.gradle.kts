plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE // 중복된 파일 제외
}

sourceSets {
	main {
		resources {
			srcDirs("src/main/resources") // 문자열로 직접 지정
		}
	}
}

dependencies {
	implementation ("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.28")
	implementation("jakarta.servlet:jakarta.servlet-api:5.0.0")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2") // 최신 버전을 사용
	implementation("org.liquibase:liquibase-core:4.22.0") // 최신 버전으로 변경
	implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final") // 적합한 버전으로 추가
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation ("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") // 버전 확인 후 필요시 수정
	implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.0") // 같은 버전으로 맞추기
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("mysql:mysql-connector-java:8.0.33")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	enabled = false
	useJUnitPlatform()
}
