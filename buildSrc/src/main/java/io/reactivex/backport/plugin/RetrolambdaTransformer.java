package io.reactivex.backport.plugin;

import java.nio.file.Path;
import net.orfjackal.retrolambda.Retrolambda;

final class RetrolambdaTransformer {
  public static RetrolambdaTransformer from(Path from, String classpath) {
    return new RetrolambdaTransformer(from, classpath);
  }

  private final Path from;
  private final String classpath;

  private RetrolambdaTransformer(Path from, String classpath) {
    this.from = from;
    this.classpath = classpath;
  }

  public void transformInto(Path to) throws Throwable {
    RetrolambdaPathConfig config = new RetrolambdaPathConfig(from, to, classpath);
    Retrolambda.run(config);
  }
}
