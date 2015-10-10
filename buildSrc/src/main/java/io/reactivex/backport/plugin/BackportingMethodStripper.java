package io.reactivex.backport.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingMethodStripper extends ClassVisitor {
  BackportingMethodStripper(ClassVisitor cv) {
    super(ASM5, cv);
  }

  @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {
    if (name.equals("fromFuture") && desc.startsWith("(Ljava/util/concurrent/CompletableFuture;)")) {
      System.out.println("  Stripping method " + name + "(CompletableFuture)");
      return null;
    }
    if (name.equals("fromStream") && desc.startsWith("(Ljava/util/stream/Stream;)")) {
      System.out.println("  Stripping method " + name + "(Stream)");
      return null;
    }
    if (name.equals("stream") && desc.endsWith("Ljava/util/stream/Stream;")) {
      System.out.println("  Stripping method stream()");
      return null;
    }
    if (name.equals("parallelStream") && desc.endsWith("Ljava/util/stream/Stream;")) {
      System.out.println("  Stripping method parallelStream()");
      return null;
    }
    if (name.equals("makeStream") && desc.endsWith("Ljava/util/stream/Stream;")) {
      System.out.println("  Stripping method makeStream()");
      return null;
    }
    if (name.equals("toFuture") && desc.endsWith("Ljava/util/concurrent/CompletableFuture;")) {
      System.out.println("  Stripping method toFuture()");
      return null;
    }
    return super.visitMethod(access, name, desc, signature, exceptions);
  }
}
