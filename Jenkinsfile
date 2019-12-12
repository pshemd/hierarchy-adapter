pipeline {
  agent { label 'docker' }
  options {
    timeout(time: 30, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '5'))
    disableConcurrentBuilds()
    gitLabConnection('External Gitlab')
  }
  environment {
    image_name = "udp-portal-dev.lukoil.com/nexus/ecp-lukoil/hierarchy-adapter"
  }

  stages {
    stage('Build') {
      steps {
        script {
          docker.image('openjdk:11-jdk')
                .inside("--add-host $docker_host_alias") {
            sh 'chmod +x ./gradlew'
            withSonarQubeEnv('SonarQ') {
              sh("""./gradlew build sonarqube \
                    -Dsonar.projectKey=`printf $image_name | sed 's|.*/||'` \
                    -Dsonar.projectName=`printf $image_name | sed 's|.*/||'` \
                    -Dsonar.projectVersion=`git rev-parse --short HEAD`""")
            }
          }
        }
      }
    }
    stage('Build docker image and push') {
      steps {
        script {
          def tags = []
          if (!env.TAG) {
            withDockerRegistry([credentialsId: 'nexus2-docker', url: 'http://$docker_registry']) {
              sh("""docker build --no-cache -t $docker_registry/$image_name:${env.BRANCH_NAME} .
                    docker push $docker_registry/$image_name:${env.BRANCH_NAME}""")
            }
          }
          else {
            tags.addAll((env.TAG).split())
            withDockerRegistry([credentialsId: 'nexus2-docker', url: 'http://$docker_registry']) {
              sh("""docker build --no-cache -t $docker_registry/$image_name:latest .
                    docker push $docker_registry/$image_name:latest""")
              tags.each {
                sh("""docker tag  $docker_registry/$image_name:latest $image_name:${it}
                      docker push $docker_registry/$image_name:${it}""")
              }
            }
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
