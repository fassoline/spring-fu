import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

plugins {
	id("org.jetbrains.kotlin.jvm")
	id("io.spring.dependency-management")
	id("org.jetbrains.dokka")
	id("java-library")
}

dependencies {
	api("org.springframework.boot:spring-boot")
	api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation(project(":autoconfigure-adapter"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	compileOnly("org.springframework:spring-webmvc")
	compileOnly("org.springframework:spring-webflux")
	compileOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	compileOnly("org.springframework.data:spring-data-mongodb")
	compileOnly("org.mongodb:mongodb-driver-reactivestreams")
	compileOnly("com.fasterxml.jackson.core:jackson-databind")
	compileOnly("com.samskivert:jmustache")
	compileOnly("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.0.BUILD-SNAPSHOT")

	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testImplementation("org.springframework:spring-test")
	testImplementation("io.projectreactor:reactor-test")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-webflux")
	testImplementation("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.springframework.boot:spring-boot-starter-undertow")
	testImplementation("org.springframework.boot:spring-boot-starter-jetty")
	testImplementation("org.springframework.boot:spring-boot-starter-mustache")
	testImplementation("org.springframework.boot:spring-boot-starter-json")
	testImplementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	testRuntimeOnly("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
	testImplementation("io.mockk:mockk:1.9")
	testImplementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.0.0.BUILD-SNAPSHOT")
}

tasks.withType<DokkaTask> {
	reportUndocumented = false
	outputFormat = "html"
	samples = listOf("src/test/kotlin/org/springframework/fu/kofu/samples")
	externalDocumentationLink {
		url = URL("https://docs.spring.io/spring-framework/docs/5.2.0.BUILD-SNAPSHOT/javadoc-api/")
	}
	externalDocumentationLink {
		url = URL("https://docs.spring.io/spring-framework/docs/5.2.0.BUILD-SNAPSHOT/kdoc-api/spring-framework/")
	}
}

publishing {
	publications {
		create(project.name, MavenPublication::class.java) {
			from(components["java"])
			artifactId = "spring-fu-kofu"
			val sourcesJar by tasks.creating(Jar::class) {
				classifier = "sources"
				from(sourceSets["main"].allSource)
				from(sourceSets["test"].allSource.apply {
					include("org/springframework/boot/kofu/samples/**")
				})
			}
			artifact(sourcesJar)
			val dokkaJar by tasks.creating(Jar::class) {
				dependsOn("dokka")
				classifier = "javadoc"
				from(buildDir.resolve("dokka"))
			}
			artifact(dokkaJar)
		}
	}
}

