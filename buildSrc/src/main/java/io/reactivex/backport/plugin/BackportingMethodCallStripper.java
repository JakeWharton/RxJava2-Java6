package io.reactivex.backport.plugin;

import com.google.common.collect.ImmutableMultimap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingMethodCallStripper extends ClassVisitor {
  private static final ImmutableMultimap<String, String> STRIP =
      ImmutableMultimap.<String, String>builder()
          .put("java/lang/IllegalStateException", "addSuppressed")
          .build();

  public BackportingMethodCallStripper(ClassVisitor cv) {
    super(ASM5, cv);
  }

  @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
    if (mv == null) {
      return null;
    }
    return new MethodVisitor(ASM5, mv) {
      @Override
      public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (STRIP.containsEntry(owner, name)) {
          System.out.println("  Stripping method call " + owner + "." + name);
          return;
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
      }
    };
  }
}
