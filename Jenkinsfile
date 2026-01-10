pipeline {
    agent any

    tools {
        maven 'Maven-3'
        jdk 'jdk-21'
    }

    environment {
        DOCKER_REGISTRY_USER = 'meilashinta' 
        
        DOCKER_CRED_ID = 'dockerhub_cred'
        
        IMAGE_TAG = "${BUILD_NUMBER}"
        
        K8S_MANIFEST_DIR = 'k8s' 
        
        SKIP_TESTS = '-DskipTests'
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
                script {
                    echo 'Building JAR files...'
                    dir('anggota_service') { bat "mvn clean package ${SKIP_TESTS}" }
                    dir('buku_service') { bat "mvn clean package ${SKIP_TESTS}" }
                    dir('peminjaman_service') { bat "mvn clean package ${SKIP_TESTS}" }
                    dir('pengembalian_service') { bat "mvn clean package ${SKIP_TESTS}" }
                    dir('api-gateway-pustaka') { bat "mvn clean package ${SKIP_TESTS}" }
                }
            }
        }

        stage('Build & Push Docker Images') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKER_CRED_ID}") {
                        parallel(
                            // --- 1. Infrastructure Images (Logstash & Filebeat Custom) ---
                            'Build Infra Images': {
                                echo 'Building Logstash & Filebeat...'
                                
                                // Build & Push Logstash
                                def logstashImg = docker.build("${DOCKER_REGISTRY_USER}/logstash:${IMAGE_TAG}", "-f logstash/Dockerfile .")
                                logstashImg.push()
                                logstashImg.push("latest")

                                // Build & Push Filebeat (digunakan oleh semua sidecar)
                                def filebeatImg = docker.build("${DOCKER_REGISTRY_USER}/filebeat:${IMAGE_TAG}", "-f filebeat/Dockerfile .")
                                filebeatImg.push()
                                filebeatImg.push("latest")
                            },

                            // --- 2. Microservices Images ---
                            'Build Anggota': {
                                dir('anggota_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/anggota-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Buku': {
                                dir('buku_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/buku-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Peminjaman': {
                                dir('peminjaman_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/peminjaman-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Pengembalian': {
                                dir('pengembalian_service') {
                                    def img = docker.build("${DOCKER_REGISTRY_USER}/pengembalian-service:${IMAGE_TAG}")
                                    img.push()
                                    img.push("latest")
                                }
                            },
                            'Build Gateway': {
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

        stage('Apply Kubernetes Secrets') {
            steps {
                withCredentials([
                    string(credentialsId: 'smtp-username', variable: 'SMTP_USERNAME'),
                    string(credentialsId: 'smtp-password', variable: 'SMTP_PASSWORD'),
                    string(credentialsId: 'smtp-from', variable: 'SMTP_FROM')
                ]) {
                    sh '''
                    kubectl create secret generic smtp-secret \
                    --from-literal=SMTP_USERNAME="$SMTP_USERNAME" \
                    --from-literal=SMTP_PASSWORD="$SMTP_PASSWORD" \
                    --from-literal=SMTP_FROM="$SMTP_FROM" \
                    --dry-run=client -o yaml | kubectl apply -f -
                    '''
                }
            }
        }


        stage('Deploy to Kubernetes') {
            steps {
                script {
                    echo 'Deploying to Kubernetes Cluster...'

                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/00-storage.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/01-eureka.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/02-postgres.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/03-mongo.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/04-rabbitmq.yml"
                    
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/05-elasticsearch.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/06-logstash.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/07-kibana.yml"

                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/08-anggota-service.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/09-buku-service.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/10-peminjaman-service.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/11-pengembalian-service.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/12-api-gateway.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/13-rabbitmq-email-service.yml"
                    bat "kubectl apply -f ${K8S_MANIFEST_DIR}/servicemonitor.yaml"

                    
                    echo 'Rolling out restarts to pick up new images...'
                    bat "kubectl rollout restart deployment/anggota-service"
                    bat "kubectl rollout restart deployment/buku-service"
                    bat "kubectl rollout restart deployment/peminjaman-service"
                    bat "kubectl rollout restart deployment/pengembalian-service"
                    bat "kubectl rollout restart deployment/api-gateway-pustaka"
                    bat "kubectl rollout restart deployment/logstash"
                }
            }
        }
        
        stage('Verify Monitoring') {
            steps {
                echo 'Checking Prometheus targets...'
                bat '''
                sleep 30
                kubectl port-forward svc/monitoring-kube-prometheus-prometheus 9090:9090 &
                sleep 5
                curl -s http://localhost:9090/api/v1/targets | grep anggota-service || exit 1
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Deployment Kubernetes Berhasil! Image baru telah di-push dan cluster telah di-update.'
        }
        failure {
            echo '❌ BUILD/DEPLOY GAGAL!'
        }
    }
}