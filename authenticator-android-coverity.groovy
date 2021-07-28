// https://www.jenkins.io/doc/book/pipeline/jenkinsfile/

pipeline {
  agent any

  environment {
    GIT_BRANCH='develop'
    JAVA_HOME='/Library/Java/JavaVirtualMachines/jdk1.8.0_301.jdk/Contents/Home'

    // COV_AUTH_KEY_PATH = "${HOME}/coverity/auth-key.txt"
    COV_URL = 'https://cov.surepassid.com:8443/'
    COV_PROJECT = 'android-authenticator-surepassid'
    COV_STREAM = 'android-authenticator-surepassid-develop'
    COV_VIEW = 'High Impact Outstanding'
  }

  stages {
   stage("checkout") {
      steps {
        // Jenkins cleans the workspace
        cleanWs()
        git branch: "${GIT_BRANCH}",
            url: 'authenticator-android.github.com:SurePassId/Authenticator-Android.git'
      }
    }
    stage('Analyze') {
      steps {
        configFileProvider([configFile(fileId: '63bf33e9-8afc-4409-ba55-89f9ec7744bb', targetLocation: './local.properties')]) {
          sh '''./coverity.sh'''
        }
      }
    }
    stage('Issue Check') {
      steps {
        withCoverityEnvironment(coverityInstanceUrl: "${COV_URL}",
                                projectName: "${COV_PROJECT}",
                                streamName: "${COV_STREAM}",
                                viewName: "${COV_VIEW}",
                                authKeyPath: "${HOME}/coverity/auth-key.txt") {
          coverityIssueCheck returnIssueCount: true,
                             markUnstable: true
        }
      }
    }
  }
}
