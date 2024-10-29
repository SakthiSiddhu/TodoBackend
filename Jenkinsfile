pipeline {
    agent any

    tools {
        maven 'Maven_3.9.9'
    }

    environment {
        KUBECONFIG = '/home/ec2-user/.kube/config'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SakthiSiddhu/TodoBackend'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def dockerTag = "latest"
                    def projectName = "todobackend"
                    sh "docker build -t sakthisiddu1/${projectName}:${dockerTag} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhubpwd', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    def dockerTag = "latest"
                    def projectName = "todobackend"
                    sh "docker push sakthisiddu1/${projectName}:${dockerTag}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def projectName = "todobackend"
                    def dockerTag = "latest"
                    def deploymentYaml = """
                    apiVersion: apps/v1
                    kind: Deployment
                    metadata:
                      name: ${projectName}
                    spec:
                      replicas: 1
                      selector:
                        matchLabels:
                          app: ${projectName}
                      template:
                        metadata:
                          labels:
                            app: ${projectName}
                        spec:
                          containers:
                          - name: ${projectName}
                            image: sakthisiddu1/${projectName}:${dockerTag}
                            ports:
                            - containerPort: 9000
                    """
                    def serviceYaml = """
                    apiVersion: v1
                    kind: Service
                    metadata:
                      name: ${projectName}
                    spec:
                      selector:
                        app: ${projectName}
                      ports:
                        - protocol: TCP
                          port: 9000
                          targetPort: 9000
                    """
                    writeFile file: 'deployment.yaml', text: deploymentYaml
                    writeFile file: 'service.yaml', text: serviceYaml
                    sh 'kubectl apply -f deployment.yaml'
                    sh 'kubectl apply -f service.yaml'
                }
            }
        }

        stage('Wait for Deployment') {
            steps {
                sleep 60
            }
        }

        stage('Port Forward') {
            steps {
                script {
                    def projectName = "todobackend"
                    sh "kubectl port-forward --address 0.0.0.0 service/${projectName} 9000:9000"
                }
            }
        }
    }
}