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
                    def projectName = 'todobackend'
                    def dockerTag = 'latest'
                    def dockerImage = "sakthisiddu1/${projectName}:${dockerTag}"
                    sh "docker build -t ${dockerImage} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhubpwd', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                    sh "docker push sakthisiddu1/todobackend:latest"
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    def deploymentYaml = '''
apiVersion: apps/v1
kind: Deployment
metadata:
  name: todobackend-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: todobackend
  template:
    metadata:
      labels:
        app: todobackend
    spec:
      containers:
      - name: todobackend
        image: sakthisiddu1/todobackend:latest
        ports:
        - containerPort: 9000
'''
                    def serviceYaml = '''
apiVersion: v1
kind: Service
metadata:
  name: todobackend-service
spec:
  selector:
    app: todobackend
  ports:
    - protocol: TCP
      port: 9000
      targetPort: 9000
'''
                    writeFile file: 'deployment.yaml', text: deploymentYaml
                    writeFile file: 'service.yaml', text: serviceYaml
                    sh 'kubectl apply -f deployment.yaml --validate=false'
                    sh 'kubectl apply -f service.yaml --validate=false'
                }
            }
        }
        stage('Port Forward') {
            steps {
                script {
                    sleep 60
                    sh 'kubectl port-forward --address 0.0.0.0 service/todobackend-service 9000:9000'
                }
            }
        }
    }
    post {
        success {
            echo 'Deployment completed successfully!'
        }
    }
}