import com.shykhov.mg.lib.dependencies.*
import com.shykhov.mg.lib.dependencies.Dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.shykhov.mg.service"
version = "0.0.331"

plugins {
    id("com.shykhov.mg.lib.central-plugin") version "0.1.0"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.1.0"
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.spring") version "2.1.0"
    id("org.gradle.java-library")
    id("org.springframework.boot") version "3.2.4"
    id("org.jetbrains.kotlin.plugin.jpa") version "2.1.0"
}

`добавить гамна` {

    implementation("org.springframework.kafka:spring-kafka:${Versions.spring}")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.23")
    testImplementation("org.springframework.kafka:spring-kafka-test:${Versions.spring}")
    implementation("net.bytebuddy:byte-buddy:1.17.5")
    implementation("net.bytebuddy:byte-buddy-agent:1.17.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

    implementation("org.reflections:reflections:0.10.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.+")
    implementation("org.jgrapht:jgrapht-core:1.5.2")
    implementation("org.jgrapht:jgrapht-ext:1.5.2")
    implementation("io.github.rchowell:dotlin:1.0.2")

    implementation("guru.nidi:graphviz-kotlin:0.18.1")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:${Versions.spring}")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client:${Versions.spring}")
    implementation("org.springframework.boot:spring-boot-starter-security:${Versions.spring}")
    implementation("org.springframework.security:spring-security-rsocket:6.2.3")
    implementation(Dependencies.flyWay)
    implementation(Dependencies.postgreSQL)
    implementation(Dependencies.springStarterData)


    implementation("com.shykhov.mg.lib:kediatRv2:0.0.1")
    implementation("com.shykhov.mg.lib:kRecordable:0.0.1")
    `mg-commons       `("0.1.0")  withScope impl
    `depth-api        `("0.1.0")  withScope impl
    `engine-api       `("0.1.0")  withScope impl
    `candle-api       `("0.1.0")  withScope impl

    implementation("com.fasterxml.uuid:java-uuid-generator:5.1.0")

    implementation("io.hypersistence:hypersistence-utils-hibernate-60:3.7.0")
    implementation( "com.aallam.ulid:ulid-kotlin:1.3.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
    implementation("org.springframework.boot:spring-boot-starter-rsocket:${Versions.spring}")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    implementation(Dependencies.springStarterWebFlux)
    implementation(Dependencies.springStarterActuator)
    implementation(Dependencies.springStarterLogging)
    implementation(Dependencies.springConfigProcessor)
    implementation(Dependencies.springCloudEurekaClient)
    implementation(Dependencies.springCloudAll)
    implementation(Dependencies.springCloudStarterResilience4j)
    implementation(Dependencies.kLogging)
    implementation(Dependencies.janinoLogging)
    implementation(Dependencies.logbackLogging)
    implementation(Dependencies.caffeine)
    implementation(Dependencies.micrometerPrometheus)

    `gson                         ` withScope impl
    `jackson-kotlin               ` withScope impl
    `commons-codec                ` withScope impl
    `jakarta-validation           ` withScope impl
    `kotlin-reflect               ` withScope impl

    `feign-starter    ( ͡° ͜ʖ ͡°)╭∩╮ ` allWithScope impl
    `swagger-ui       ( ͡° ͜ʖ ͡°)╭∩╮ ` allWithScope impl

    `kotest-mockk     ( ͡° ͜ʖ ͡°)╭∩╮ ` allWithScope test
    `spring-test      ( ͡° ͜ʖ ͡°)╭∩╮ ` allWithScope test
    `test-containers  ( ͡° ͜ʖ ͡°)╭∩╮ ` allWithScope test
    testImplementation("com.ninja-squad:springmockk:4.0.2")

}

`куда смотреть` {
    mavenCentral()
    mavenLocal()
    `mg private repo`(project)
}

springBoot.configureBuildInfo()

tasks.bootJar {
    mainClass = "com.shykhov.backtest.BacktestAppKt"
    archiveFileName = "backtest-service.jar"
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
tasks.jar { enabled = false }
tasks.test.configure { useJUnitPlatform() }
//tasks.test { finalizedBy(tasks.jacocoTestReport) }
//tasks.jacocoTestReport { reports {
//    html.required = true}}
//tasks.register("printProjectName") { doLast { println(project.name) } }

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    jvmToolchain(17)
}
