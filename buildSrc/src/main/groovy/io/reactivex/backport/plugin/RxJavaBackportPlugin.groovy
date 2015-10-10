package io.reactivex.backport.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile

class RxJavaBackportPlugin implements Plugin<Project> {
  @Override void apply(Project project) {
    if (!project.plugins.hasPlugin('java')) {
      throw new IllegalStateException('Project must have "java" plugin applied.')
    }

    project.repositories.mavenCentral()
    project.repositories.maven {
      url 'https://oss.jfrog.org/artifactory/libs-snapshot/'
    }

    // The RxJava 2 dependency without transitive deps.
    def rxJavaDependency = project.configurations.create('rxJava')
    project.dependencies.add('rxJava', 'io.reactivex:rxjava:2.0.0-DP0-SNAPSHOT') {
      transitive = false
    }

    // The transitive dependencies of RxJava 2.
    // TODO figure out how to make this just an RxJava 2 dep but ignoring RxJava itself.
    def transitiveDependencies = project.configurations.create('rxJavaTransitives')
    project.dependencies.add('rxJavaTransitives', 'org.reactivestreams:reactive-streams:1.0.0')


    def animalSnifferSignature = project.configurations.create('animalSnifferSignature')
    project.dependencies.add('animalSnifferSignature',
        'org.codehaus.mojo.signature:java16:1.1@signature')

    def compileTask = project.tasks.getByName('compileJava') as JavaCompile


    // Explode the RxJava jar classes.
    def rxJavaClassesDir = project.file("$project.buildDir/extractedClasses")
    def extractTask = project.tasks.create('extract', Copy) {
      from(project.zipTree(rxJavaDependency.singleFile))
      into(rxJavaClassesDir)
    }
    extractTask.doFirst {
      rxJavaClassesDir.mkdirs()
    }


    // Backport any Java 8 APIs to Java 6.
    def backportedDir = project.file("$project.buildDir/backportedClasses")
    def backportingTask = project.tasks.create('backport', BackportingTask) {
      inputDir = rxJavaClassesDir
      outputDir = backportedDir
    }
    backportingTask.dependsOn(extractTask)


    // Remove classes which are no longer needed after the backport.
    def prunedDir = project.file("$project.buildDir/prunedClasses")
    def pruningTask = project.tasks.create('prune', Copy) {
      from(backportedDir) {
        exclude('**/NbpOnSubscribeStreamSource.class')
        exclude('**/NbpOnSubscribeCompletableFutureSource.class')
        exclude('**/PublisherStreamSource.class')
        exclude('**/PublisherCompletableFutureSource.class')
      }
      into(prunedDir)
    }
    pruningTask.doFirst {
      prunedDir.mkdirs()
    }
    pruningTask.dependsOn(backportingTask)

    // Run Retrolamba to eliminate lambdas and static/default methods on interfaces.
    def retrolambdaDir = project.file("$project.buildDir/retrolambdaClasses")
    def retrolambdaTask = project.tasks.create('retrolambda', RetrolambdaTask) {
      classpath = transitiveDependencies.plus(project.files(backportedDir)).plus(project.files(compileTask.destinationDir)).asPath
      inputDir = prunedDir
      outputDir = retrolambdaDir
    }
    retrolambdaTask.dependsOn(pruningTask, compileTask)



    // Use Animal Sniffer to ensure we aren't referencing any APIs beyond Java 6.
    def animalSnifferTask = project.tasks.create('animalSniffer', AnimalSnifferTask) {
      signature = animalSnifferSignature.singleFile
      rxJavaDir = retrolambdaDir
      runtimeDir = compileTask.destinationDir
      transitives = transitiveDependencies
    }
    animalSnifferTask.dependsOn(retrolambdaTask, compileTask)


    // Add the processed classes to the output jar.
    def jarTask = project.tasks.getByName('jar') as Jar
    // TODO add retrolambdaDir to inputs
    jarTask.dependsOn(animalSnifferTask)
  }
}
