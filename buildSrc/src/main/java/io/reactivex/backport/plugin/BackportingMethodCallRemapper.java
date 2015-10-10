package io.reactivex.backport.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingMethodCallRemapper extends ClassVisitor {
  public BackportingMethodCallRemapper(ClassVisitor cv) {
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
        if ("java/util/Collections".equals(owner) && "emptyIterator".equals(name)) {
          owner = "io/reactivex/internal/java/util/Collections";
          System.out.println(
              "  Mapping method java/util/Collections.emptyIterator to io/reactivex/internal/java/util/Collections.emptyIterator");
        }
        if ("java/lang/Long".equals(owner) && "compare".equals(name)) {
          owner = "io/reactivex/internal/java/lang/Long";
          System.out.println(
              "  Mapping method java/lang/Long.compare to io/reactivex/internal/java/lang/Long.compare");
        }
        if ("java/lang/Integer".equals(owner) && "compare".equals(name)) {
          owner = "io/reactivex/internal/java/lang/Integer";
          System.out.println(
              "  Mapping method java/lang/Integer.compare to io/reactivex/internal/java/util/Integer.compare");
        }
        if ("java/lang/Iterable".equals(owner) && "forEach".equals(name)) {
          opcode = Opcodes.INVOKESTATIC;
          owner = "io/reactivex/internal/java/lang/Iterable";
          desc = "(Ljava/lang/Iterable;Lio/reactivex/functions/Consumer;)V";
          System.out.println(
              "  Mapping method java/lang/Iterable.forEach to io/reactivex/internal/java/util/Iterable.forEach");
        }
        if ("java/util/List".equals(owner) && "forEach".equals(name)) {
          opcode = Opcodes.INVOKESTATIC;
          owner = "io/reactivex/internal/java/lang/Iterable";
          desc = "(Ljava/lang/Iterable;Lio/reactivex/functions/Consumer;)V";
          System.out.println(
              "  Mapping method java/util/List.forEach to io/reactivex/internal/java/util/Iterable.forEach");
        }
        if ("java/util/LinkedList".equals(owner) && "forEach".equals(name)) {
          opcode = Opcodes.INVOKESTATIC;
          owner = "io/reactivex/internal/java/lang/Iterable";
          desc = "(Ljava/lang/Iterable;Lio/reactivex/functions/Consumer;)V";
          System.out.println(
              "  Mapping method java/util/LinkedList.forEach to io/reactivex/internal/java/util/Iterable.forEach");
        }
        if ("java/util/concurrent/ScheduledThreadPoolExecutor".equals(owner) && "setRemoveOnCancelPolicy".equals(name)) {
          opcode = Opcodes.INVOKESTATIC;
          owner = "io/reactivex/internal/java/util/concurrent/ScheduledThreadPoolExecutor";
          desc = "(Ljava/util/concurrent/ScheduledThreadPoolExecutor;Z)V";
          System.out.println(
              "  Mapping method java/util/concurrent/ScheduledThreadPoolExecutor.setRemoveOnCancelPolicy to io/reactivex/internal/java/util/concurrent/ScheduledThreadPoolExecutor.setRemoveOnCancelPolicy");
        }
        super.visitMethodInsn(opcode, owner, name, desc, itf);
      }
    };
  }
}
