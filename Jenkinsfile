pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'jdk-21'
    }

    environment {
        DOCKER_REGISTRY_USER = 'meilashinta'
        DOCKER_CRED_ID      = 'dockerhub_cred'
        IMAGE_TAG           = "${BUILD_NUMBER}"
        K8S_MANIFEST_DIR    = 'k8s'
        SKIP_TESTS          = '-DskipTests'
    }

    stages {

        stage('Checkout') {
            steps {
                deleteDir()
                git branch: 'main',
                    url: 'https://github.com/meilashinta/springboot_microservice.git'
            }
        }

        stage('Build Maven JARs') {
            steps {
                echo 'Building JAR files...'

                dir('anggota_service') {
                    bat "mvn clean package %SKIP_TESTS%"
                }
                dir('buku_service') {
                    bat "mvn clean package %SKIP_TESTS%"
                }
                dir('peminjaman_service') {
                    bat "mvn clean package %SKIP_TESTS%"
                }
                dir('pengembalian_service') {
                    bat "mvn clean package %SKIP_TESTS%"
                }
                dir('api-gateway-pustaka') {
                    bat "mvn clean package %SKIP_TESTS%"
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_CRED_ID) {

                        parallel(

                            'Infra Images': {
                                echo 'Building Logstash & Filebeat...'

                                def logstashImg = docker.build(
                                    "${DOCKER_REGISTRY_USER}/logstash:${IMAGE_TAG}",
                                    "-f logstash/Dockerfile ."
                                )
                                logstashImg.push()
                                logstashImg.push("latest")

                                def filebeatImg = docker.build(
                                    "${DOCKER_REGISTRY_USER}/filebeat:${IMAGE_TAG}",
                                    "-f filebeat/Dockerfile ."
                                )
                                filebeatImg.push()
                                filebeatImg.push("latest")
                            },

                            'Anggota Service': {
                                dir('anggota_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/anggota-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },

                            'Buku Service': {
                                dir('buku_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/buku-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },

                            'Peminjaman Service': {
                                dir('peminjaman_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/peminjaman-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },

                            'Pengembalian Service': {
                                dir('pengembalian_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/pengembalian-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },

                            'API Gateway': {
                                dir('api-gateway-pustaka') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/api-gateway-pustaka:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            }
                        )
                    }
                }
            }
        }

        stage('Apply Kubernetes SMTP Secret') {
            steps {
                withCredentials([
                    string(credentialsId: 'smtp-username', variable: 'SMTP_USERNAME'),
                    string(credentialsId: 'smtp-password', variable: 'SMTP_PASSWORD'),
                    string(credentialsId: 'smtp-from', variable: 'SMTP_FROM')
                ]) {
                    bat """
                    kubectl delete secret smtp-secret --ignore-not-found
                    kubectl create secret generic smtp-secret ^
                    --from-literal=SMTP_USERNAME=${SMTP_USERNAME} ^
                    --from-literal=SMTP_PASSWORD=${SMTP_PASSWORD} ^
                    --from-literal=SMTP_FROM=${SMTP_FROM}
                    """
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'

                bat "kubectl apply -f %K8S_MANIFEST_DIR%/00-storage.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/01-eureka.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/02-postgres.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/03-mongo.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/04-rabbitmq.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/05-elasticsearch.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/06-logstash.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/07-kibana.yml"

                bat "kubectl apply -f %K8S_MANIFEST_DIR%/08-anggota-service.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/09-buku-service.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/10-peminjaman-service.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/11-pengembalian-service.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/12-api-gateway.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/13-rabbitmq-email-service.yml"
                bat "kubectl apply -f %K8S_MANIFEST_DIR%/servicemonitor.yaml"

                echo 'Restarting deployments...'
                bat "kubectl rollout restart deployment/anggota-service"
                bat "kubectl rollout restart deployment/buku-service"
                bat "kubectl rollout restart deployment/peminjaman-service"
                bat "kubectl rollout restart deployment/pengembalian-service"
                bat "kubectl rollout restart deployment/api-gateway-pustaka"
                bat "kubectl rollout restart deployment/logstash"
            }
        }
    }

    post {
        success {
            echo '✅ BUILD & DEPLOY BERHASIL!'
        }
        failure {
            echo '❌ BUILD / DEPLOY GAGAL!'
        }
    }
}
