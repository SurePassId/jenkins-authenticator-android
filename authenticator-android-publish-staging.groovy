pipeline {
  agent any

  stages {
    stage('Copy Archived Build App Bundles') {
      steps {
        copyArtifacts projectName: '01_authenticator-android-build-master'
      }
    }
    // https://wiki.jenkins.io/display/jenkins/google+play+android+publisher+plugin
    // https://plugins.jenkins.io/google-play-android-publisher/
    stage("deploy-black") {
      steps {
        retry(5) {
          // https://plugins.jenkins.io/google-play-android-publisher/
          androidApkUpload trackName: 'staging',
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: '100',
                   googleCredentialsId: 'SurePassID Authenticator',
                   filesPattern: 'authenticator/build/outputs/bundle/surepassidBlackRelease/*.aab',
                   deobfuscationFilesPattern: 'authenticator/build/outputs/mapping/surepassidBlackRelease/mapping.txt',
                   recentChangeList: [
                     [language: 'en-US', text: "Please test the changes from Jenkins build ${env.BUILD_NUMBER}."]
                   ]
        }
      }
    }
    stage("deploy-green-push") {
      steps {
        retry(5) {
          // https://plugins.jenkins.io/google-play-android-publisher/
          androidApkUpload trackName: 'staging',
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: '100',
                   googleCredentialsId: 'SurePassID Authenticator',
                   filesPattern: 'authenticator/build/outputs/bundle/surepassidGreenPushRelease/*.aab',
                   deobfuscationFilesPattern: 'authenticator/build/outputs/mapping/surepassidGreenPushRelease/mapping.txt',
                   recentChangeList: [
                     [language: 'en-US', text: "Please test the changes from Jenkins build ${env.BUILD_NUMBER}."]
                   ]
        }
      }
    }
    stage("deploy-green-express") {
      steps {
        retry(5) {
          // https://plugins.jenkins.io/google-play-android-publisher/
          androidApkUpload trackName: 'staging',
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: '100',
                   googleCredentialsId: 'SurePassID Authenticator',
                   filesPattern: 'authenticator/build/outputs/bundle/surepassidGreenExpressRelease/*.aab',
                   deobfuscationFilesPattern: 'authenticator/build/outputs/mapping/surepassidGreenExpressRelease/mapping.txt',
                   recentChangeList: [
                     [language: 'en-US', text: "Please test the changes from Jenkins build ${env.BUILD_NUMBER}."]
                   ]
        }
      }
    }
  }
}
