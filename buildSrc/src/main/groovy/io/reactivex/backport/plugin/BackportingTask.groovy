package io.reactivex.backport.plugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class BackportingTask extends DefaultTask {
  @InputDirectory File inputDir
  @OutputDirectory File outputDir

  @TaskAction def run() {
    BackportingTransformer.from(inputDir.toPath()).transformInto(outputDir.toPath())
  }
}
