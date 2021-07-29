pipeline {
  agent any

  environment {
    APP_TRACK_NAME="staging"
    APP_ROLLOUT_PERCENTAGE="100"
    GOOGLE_CREDENTIAL_ID="SurePassID Authenticator"
  }

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
          androidApkUpload trackName: "${APP_TRACK_NAME}",
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: "${APP_ROLLOUT_PERCENTAGE}",
                   googleCredentialsId: "${GOOGLE_CREDENTIAL_ID}",
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
          androidApkUpload trackName: "${APP_TRACK_NAME}",
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: "${APP_ROLLOUT_PERCENTAGE}",
                   googleCredentialsId: "${GOOGLE_CREDENTIAL_ID}",
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
          androidApkUpload trackName: "${APP_TRACK_NAME}",
                   releaseName: '{versionName} ({versionCode})',
                   rolloutPercentage: "${APP_ROLLOUT_PERCENTAGE}",
                   googleCredentialsId: "${GOOGLE_CREDENTIAL_ID}",
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
