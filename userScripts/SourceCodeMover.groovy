def user = '/Users/htaylor'
def projectLocation = '/apps/GrailsBankingDemo/'
def sourceDir = '/Users/htaylor/Downloads/' // ${user} won't work here because Ant will keep appending ${user}.
def basePackage = '/com/genrocket/bank'

def tdlDir = "${user}${projectLocation}src/groovy${basePackage}/testDataLoader/"
def tdsDir = "${user}${projectLocation}grails-app/services${basePackage}/testData/"
def serviceDir = "${user}${projectLocation}grails-app/services${basePackage}/"
def testsDir = "${user}${projectLocation}test/integration${basePackage}/"
def scenarioDir = "${user}${projectLocation}test/scenarios/"

println 'clear'.execute().text
println "cd ${sourceDir}".execute().text

class FileFilter implements FilenameFilter {
  public boolean accept(File f, String filename) {
    return filename.endsWith("grs")
  }
}

List<String> scenarios = new File(sourceDir).list(new FileFilter())

scenarios.each { scenario ->
  println "genrocket -r ${scenario}".execute().text
}

new AntBuilder().move(todir: tdlDir) {
  fileset(dir: sourceDir) {
    include(name: "**/*TestDataLoader.groovy")
  }
}

new AntBuilder().move(todir: tdsDir) {
  fileset(dir: sourceDir) {
    include(name: "**/*TestDataService.groovy")
  }
}

new AntBuilder().move(todir: serviceDir) {
  fileset(dir: sourceDir) {
    include(name: "**/*Service.groovy")
  }
}

new AntBuilder().move(todir: testsDir) {
  fileset(dir: sourceDir) {
    include(name: "**/*ServiceIntegrationSpec.groovy")
  }
}

new AntBuilder().move(todir: scenarioDir) {
  fileset(dir: sourceDir) {
    include(name: "**/*Scenario.grs")
  }
}