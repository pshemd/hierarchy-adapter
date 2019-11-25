pipeline {
  agent { label 'docker' }
  options {
    timeout(time: 30, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '5'))
    disableConcurrentBuilds()
    gitLabConnection('External Gitlab')
  }
  environment {
    image_name         = "ecp-lukoil/hierarchy-adapter"
    docker_registry    = "nexus2.zyfra.com:8082"
    docker_host_alias  = "nexus2.zyfra.com:192.168.101.190"
  }

  stages {
    stage('Dependency check') {
      steps {
        dependencycheck \
          additionalArguments: '--scan . --out dependency-check-report.xml --format XML', 
          odcInstallation: 'DepCheck5.2.1'
        dependencyCheckPublisher \
          pattern: 'dependency-check-report.xml'
      }
    }
    stage('Build') {
      steps {
        script {
          docker.image('openjdk:11-jdk')
                .inside("--add-host $docker_host_alias") {
            sh 'chmod +x ./gradlew'
            withSonarQubeEnv('SonarQ') {
              sh("""./gradlew build sonarqube \
              -Dsonar.projectKey="hierarchy-adapter" \
              -Dsonar.projectName="hierarchy-adapter" \
              -Dsonar.projectVersion=1.0 \
              -Dsonar.dependencyCheck.reportPath=${WORKSPACE}/dependency-check-report.xml""")
            }
          }
        }
      }
    }
    stage('Deploy') {
      when { anyOf { branch 'master'; buildingTag() } }
      steps {
        script {
          tag = sh(returnStdout: true, script: 'git tag --points-at $(git rev-parse HEAD)').trim()
          if (!tag) {
            tag = "latest";
          }
          sh "docker build --no-cache -t $docker_registry/$image_name:$tag ."
          withDockerRegistry([credentialsId: 'nexus2-docker', url: 'http://$docker_registry']) {
            sh "docker push $docker_registry/$image_name:$tag"
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
