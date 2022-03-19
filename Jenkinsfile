pipeline {
    agent any
    tools {
        maven 'MAVEN'
        jdk 'JDK'
    }
    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '5'))
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn -B package -DskipTests=true -Darguments="-Dmaven.javadoc.skip=true"'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/powernukkit-*.jar', fingerprint: true
                }
            }
        }
    }
}
