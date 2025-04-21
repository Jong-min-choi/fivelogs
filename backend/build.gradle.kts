plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.fiveguyes"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
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

dependencies {

	implementation ("org.springframework.boot:spring-boot-starter-mail") // email service
	implementation ("org.springframework.boot:spring-boot-starter-data-redis") // redis
	implementation ("org.springframework.data:spring-data-redis") //redis
	implementation ("com.auth0:java-jwt:4.4.0") // JWT 라이브러리
	implementation ("io.jsonwebtoken:jjwt-api:0.12.6")
	implementation ("io.jsonwebtoken:jjwt-impl:0.12.6")
	implementation ("io.jsonwebtoken:jjwt-jackson:0.12.6")
	implementation ("org.springframework.boot:spring-boot-starter-security") //security
	implementation("org.springframework.boot:spring-boot-starter-actuator") // actuator , health check
	implementation("org.springframework.boot:spring-boot-starter-validation") // 유효성 검사
	implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	// 데이터베이스 관련
	runtimeOnly("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	runtimeOnly("com.mysql:mysql-connector-j")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
