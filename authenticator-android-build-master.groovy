// class Constants {
//   static final String BRANCH_MASTER = 'master'

//   static final String BUILD_DEBUG = 'Debug'
//   static final String BUILD_RELEASE = 'Release'

//   static final String TRACK_STAGING = 'staging'
//   static final String TRACK_BETA = 'beta'
//   static final String TRACK_PRODUCTION = 'release'

//   static final String BRAND_SUREPASSID = 'Surepassid'
//   static final String BRAND_MCKESSON = 'Mckesson'

//   static final String VARIANT_BLACK = 'Black'
//   static final String VARIANT_GREEN_PUSH = 'GreenPush'
//   static final String VARIANT_GREEN_EXPRESS = 'GreenExpress'
// }

// def getBuildType() {
//   switch (env.BRANCH_NAME) {
//     case Constants.MASTER_BRANCH:
//       return Constants.RELEASE_BUILD
//     default:
//       return Constants.QA_BUILD
//   }
// }

// def getTrackType() {
//   switch (env.BRANCH_NAME) {
//     case Constants.MASTER_BRANCH:
//       return Constants.RELEASE_TRACK
//     default:
//       return Constants.INTERNAL_TRACK
//   }
// }

// def isDeployCandidate() {
//   return ("${env.BRANCH_NAME}" =~ /(develop|master)/)
// }


pipeline {
  agent any

  environment {
    JAVA_HOME = """${sh(
        returnStdout: true,
        script: '/usr/libexec/java_home -v 1.8.0'
      )}""".trim()
  }

  stages {

     stage("checkout") {
      steps {
        // Jenkins cleans the workspace
        cleanWs()
        // authenticator-android.github.com:SurePassId/Authenticator-Android.git
        git branch: "master",
            credentialsId: 'surepassid-jenkins',
            url: 'authenticator-android.github.com:SurePassId/Authenticator-Android.git'
      }
    }

    stage("build") {
      steps {
        configFileProvider([configFile(fileId: '63bf33e9-8afc-4409-ba55-89f9ec7744bb', targetLocation: './local.properties')]) {
          sh '''./gradlew \
                clean \
                authenticator:bundleSurepassidBlackRelease \
                authenticator:bundleSurepassidGreenPushRelease \
                authenticator:bundleSurepassidGreenExpressRelease'''
          echo "The build stage passed..."
        }
      }
    }

    // stage("test") {
    //   steps {
    //     echo "The test stage passed..."
    //   }
    // }

    stage("Archive App Bundles") {
      steps {
        archiveArtifacts artifacts: 'authenticator/build/outputs/bundle/**/*.aab,authenticator/build/outputs/mapping/**/mapping.txt',
                 fingerprint: true
      }
    }

  }

  // post {
  //   always {
  //   }
  //   success {
  //   }
  //   failure {
  //   }
  //   unstable {
  //   }
  //   changed {
  //   }
  // }
}
