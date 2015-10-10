package io.reactivex.backport.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class RetrolambdaTask extends DefaultTask {
  @Input String classpath
  @InputDirectory File inputDir
  @OutputDirectory File outputDir

  @TaskAction def run() {
    RetrolambdaTransformer.from(inputDir.toPath(), classpath).transformInto(outputDir.toPath())
  }
}
