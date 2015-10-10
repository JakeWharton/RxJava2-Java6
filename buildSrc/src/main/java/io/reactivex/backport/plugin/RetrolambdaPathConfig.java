package io.reactivex.backport.plugin;

import java.nio.file.Path;
import java.util.List;
import net.orfjackal.retrolambda.Config;
import org.objectweb.asm.Opcodes;

final class RetrolambdaPathConfig extends Config {
  private final Path inputDir;
  private final Path outputDir;
  private final String classpath;

  RetrolambdaPathConfig(Path inputDir, Path outputDir, String classpath) {
    super(null);
    this.inputDir = inputDir;
    this.outputDir = outputDir;
    this.classpath = classpath;
  }

  @Override public boolean isFullyConfigured() {
    return true;
  }

  @Override public int getBytecodeVersion() {
    return Opcodes.V1_6;
  }

  @Override public String getJavaVersion() {
    return "Java 8";
  }

  @Override public boolean isDefaultMethodsEnabled() {
    return true;
  }

  @Override public Path getInputDir() {
    return inputDir;
  }

  @Override public Path getOutputDir() {
    return outputDir;
  }

  @Override public String getClasspath() {
    return classpath;
  }

  @Override public List<Path> getIncludedFiles() {
    return null;
  }
}
