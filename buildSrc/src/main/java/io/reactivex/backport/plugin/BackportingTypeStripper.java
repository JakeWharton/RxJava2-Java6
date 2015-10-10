package io.reactivex.backport.plugin;

import com.google.common.collect.ImmutableSet;
import org.objectweb.asm.ClassVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingTypeStripper extends ClassVisitor {
  private static final ImmutableSet<String> STRIPPED = ImmutableSet.<String>builder()
      .add("io/reactivex/internal/operators/PublisherCompletableFutureSource")
      .add("io/reactivex/internal/operators/PublisherStreamSource")
      .build();

  BackportingTypeStripper(ClassVisitor cv) {
    super(ASM5, cv);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    if (STRIPPED.contains(name)) {
      System.out.println("  Stripping type " + name);
      return;
    }
    super.visit(version, access, name, signature, superName, interfaces);
  }
}

