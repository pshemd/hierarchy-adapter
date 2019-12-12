pipeline {
  agent { label 'docker' }
  options {
    timeout(time: 30, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '5'))
    disableConcurrentBuilds()
    gitLabConnection('GitLab')
  }
  environment {
    image_name = "udp-portal-dev.lukoil.com/nexus/ecp-lukoil/hierarchy-adapter"
  }

  stages {
    stage('Build') {
      steps {
        script {
          docker.image('openjdk:11-jdk')
                .inside() {
            sh 'chmod +x ./gradlew'
            withSonarQubeEnv('SonarQube') {
              sh("""./gradlew build sonarqube \
                    -Dsonar.projectKey=hierarchy-adapter \
                    -Dsonar.projectName=hierarchy-adapter \
                    -Dsonar.projectVersion=1.0""")
            }
          }
        }
      }
    }
    stage('Build docker image and push') {
      steps {
        script {
            withDockerRegistry([credentialsId: '94beadb1-b0d5-4c27-952c-d77616c5288d', url: 'http://udp-portal-dev.lukoil.com/nexus']) {
              sh("""docker build --no-cache -t $image_name:dev .
                    docker push $image_name:dev""")
            }
        }
      }
      post {
        always {
          sh 'docker image prune -f'
        }
      }
    }
  }
  post {
    failure {
      updateGitlabCommitStatus name: 'Jenkins build', state: 'failed'
    }
    success {
      updateGitlabCommitStatus name: 'Jenkins build', state: 'success'
    }
  }
}
