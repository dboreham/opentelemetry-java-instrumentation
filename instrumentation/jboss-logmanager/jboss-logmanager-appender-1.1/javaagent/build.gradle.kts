plugins {
  id("otel.javaagent-instrumentation")
}

muzzle {
  pass {
    group.set("org.jboss.logmanager")
    module.set("jboss-logmanager")
    versions.set("[1.1.0.GA,)")
    assertInverse.set(true)
  }
}

dependencies {
  library("org.jboss.logmanager:jboss-logmanager:1.1.0.GA")

  compileOnly("io.opentelemetry:opentelemetry-api-logs")
  compileOnly(project(":javaagent-bootstrap"))

  // ensure no cross interference
  testInstrumentation(project(":instrumentation:java-util-logging:javaagent"))

  testImplementation("org.awaitility:awaitility")
}

tasks.withType<Test>().configureEach {
  // TODO run tests both with and without experimental log attributes
  jvmArgs("-Dotel.instrumentation.jboss-logmanager.experimental.capture-mdc-attributes=*")
  jvmArgs("-Dotel.instrumentation.jboss-logmanager.experimental-log-attributes=true")
}
