package io.reactivex.backport.plugin

import org.codehaus.mojo.animal_sniffer.ClassListBuilder
import org.codehaus.mojo.animal_sniffer.SignatureChecker
import org.codehaus.mojo.animal_sniffer.logging.PrintWriterLogger
import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

class AnimalSnifferTask extends DefaultTask {
  @Input File signature
  @InputDirectory File rxJavaDir
  @InputDirectory File runtimeDir
  @Input Configuration transitives

  @TaskAction def run() {
    def logger = new PrintWriterLogger(System.out)

    def plb = new ClassListBuilder(logger);
    plb.process(rxJavaDir)
    plb.process(runtimeDir)
    transitives.files.each {
      plb.process(it)
    }

    def checker = new SignatureChecker(signature.newInputStream(), plb.packages, logger)
    checker.sourcePath = [ rxJavaDir, runtimeDir ]
    checker.checkJars = false
    checker.process(rxJavaDir)
    checker.process(runtimeDir)

    if (checker.signatureBroken) {
      throw new StopExecutionException("Invalidate signatures detected.")
    }
  }
}
