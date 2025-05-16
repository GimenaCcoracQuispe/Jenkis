pipeline {
    agent any

    environment {
        // Cambia el token y el servidor Sonar si tienes otro URL
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_LOGIN = credentials('sonar-token')  // Credencial que debes crear en Jenkins con el token de SonarQube
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'workshop', url: 'https://github.com/GimenaCcoracQuispe/Jenkis.git'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
