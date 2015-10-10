package io.reactivex.backport.plugin;

import com.google.common.collect.ImmutableMap;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

import static org.objectweb.asm.Opcodes.ASM5;

final class BackportingTypeRemapper extends RemappingClassAdapter {
  private static final ImmutableMap<String, String> TYPES = ImmutableMap.<String, String>builder()
      .put("java/util/function/BiConsumer", "io/reactivex/functions/BiConsumer")
      .put("java/util/function/BiFunction", "io/reactivex/functions/BiFunction")
      .put("java/util/function/BiPredicate", "io/reactivex/functions/BiPredicate")
      .put("java/util/function/BooleanSupplier", "io/reactivex/functions/BooleanSupplier")
      .put("java/util/function/Consumer", "io/reactivex/functions/Consumer")
      .put("java/util/function/Function", "io/reactivex/functions/Function")
      .put("java/util/function/IntFunction", "io/reactivex/functions/IntFunction")
      .put("java/util/function/LongConsumer", "io/reactivex/functions/LongConsumer")
      .put("java/util/function/Predicate", "io/reactivex/functions/Predicate")
      .put("java/util/function/Supplier", "io/reactivex/functions/Supplier")
      .put("java/util/Optional", "io/reactivex/Optional")
      .put("java/util/Objects", "io/reactivex/internal/java/util/Objects")
      .put("java/util/concurrent/locks/StampedLock", "io/reactivex/internal/java/util/concurrent/locks/StampedLock")
      .build();

  BackportingTypeRemapper(ClassVisitor cv) {
    super(ASM5, cv, new Remapper() {
      @Override public String map(String type) {
        String remap = TYPES.get(type);
        if (remap != null) {
          System.out.println("  Mapping type " + type + " to " + remap);
          return remap;
        }
        return type;
      }
    });
  }
}
