// https://www.jenkins.io/doc/book/pipeline/jenkinsfile/

pipeline {
  agent any

  environment {
    GIT_BRANCH='master'
    JAVA_HOME='/Library/Java/JavaVirtualMachines/jdk1.8.0_301.jdk/Contents/Home'
    //  COVERITY_TOOL_HOME = '/Applications/cov-analysis-macosx-2021.06'
  }

  stages {
   stage("checkout") {
      steps {
        // Jenkins cleans the workspace
        cleanWs()
        git branch: "develop",
            url: 'authenticator-android.github.com:SurePassId/Authenticator-Android.git'
      }
    }
    stage('Analyze') {
      steps {
        configFileProvider([configFile(fileId: '63bf33e9-8afc-4409-ba55-89f9ec7744bb', targetLocation: './local.properties')]) {
          //sh '''PATH=/Applications/cov-analysis-macosx-2021.03/bin:${PATH}'''
          sh '''./coverity.sh'''
        }
      }
    }
    stage('Issue Check') {
      steps {
        withCoverityEnvironment(coverityInstanceUrl: 'https://cov.surepassid.com:8443/',
                                createMissingProjectsAndStreams: true,
                                projectName: 'android-authenticator',
                                streamName: 'android-authenticator-surepassid-black',
                                viewName: 'High Impact Outstanding') {
          coverityIssueCheck returnIssueCount: true,
                             markUnstable: true
        }
      }
    }
  }
}
