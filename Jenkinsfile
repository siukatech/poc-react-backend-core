/* Requires the Docker Pipeline plugin */
pipeline {
    agent { docker { image 'alpine/git:2.43.0' } }
    stages {
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}