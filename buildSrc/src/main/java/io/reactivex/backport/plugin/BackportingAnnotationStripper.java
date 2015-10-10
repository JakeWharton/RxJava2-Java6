package io.reactivex.backport.plugin;

import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingAnnotationStripper extends ClassVisitor {
  private static final ImmutableSet<String> STRIPPED = ImmutableSet.<String>builder()
      .add("Ljava/lang/FunctionalInterface;")
      .build();

  BackportingAnnotationStripper(ClassVisitor cv) {
    super(ASM5, cv);
  }

  @Override public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    if (STRIPPED.contains(desc)) {
      System.out.println("  Stripping annotation " + desc);
      return null;
    }
    return super.visitAnnotation(desc, visible);
  }
}
