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
                    -Dsonar.projectKey=`printf $image_name | sed 's|.*/||'` \
                    -Dsonar.projectName=`printf $image_name | sed 's|.*/||'` \
                    -Dsonar.projectVersion=`git rev-parse --short HEAD` \
                    -Dsonar.dependencyCheck.reportPath=$WORKSPACE/dependency-check-report.xml""")
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
            tags.add(env.BRANCH_NAME)
            withDockerRegistry([credentialsId: 'nexus2-docker', url: 'http://$docker_registry']) {
              sh("""docker build --no-cache -t $docker_registry/$image_name:${it} .
                    docker push $docker_registry/$image_name:${it}""")
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
