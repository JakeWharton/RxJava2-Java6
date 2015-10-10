package io.reactivex.backport.plugin;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static java.nio.file.FileVisitResult.CONTINUE;

final class BackportingTransformer {
  public static BackportingTransformer from(Path from) {
    return new BackportingTransformer(from);
  }

  private final Path from;

  BackportingTransformer(Path from) {
    this.from = from;
  }

  public void transformInto(Path to) throws IOException {
    Files.walkFileTree(from, new SimpleFileVisitor<Path>() {
      @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
          throws IOException {
        if (file.getFileName().toString().endsWith(".class")) {
          System.out.println("Converting " + file);
          process(file, to);
        } else {
          System.out.println("Skipping " + file);
        }
        return CONTINUE;
      }
    });
  }

  private void process(Path inputFile, Path destination) throws IOException {
    // Create the hierarchy of transformations from last to first.
    ClassWriter writer = new ClassWriter(0);
    ClassVisitor methodRemapper = new BackportingMethodCallRemapper(writer);
    ClassVisitor typeRemapper = new BackportingTypeRemapper(methodRemapper);
    ClassVisitor methodCallStripper = new BackportingMethodCallStripper(typeRemapper);
    ClassVisitor annotationStripper = new BackportingAnnotationStripper(methodCallStripper);
    ClassVisitor methodStripper = new BackportingMethodStripper(annotationStripper);

    byte[] input = Files.readAllBytes(inputFile);
    ClassReader reader = new ClassReader(input);
    reader.accept(methodStripper, ClassReader.EXPAND_FRAMES);

    Path outputFile = destination.resolve(from.relativize(inputFile).toString());
    Files.createDirectories(outputFile.getParent());
    Files.write(outputFile, writer.toByteArray());
  }
}
