
# 🚀 Deployment Kubernetes 

Dokumen ini menjelaskan langkah-langkah melakukan **deployment infrastruktur** dan **microservices perpustakaan** ke cluster Kubernetes menggunakan file manifest yang terdapat di folder `k8s/`.

---

## 📁 Struktur Folder `k8s/`

Berdasarkan struktur file, berikut fungsi masing-masing komponen:

| File                           | Fungsi                                                                                   |
| ------------------------------ | ---------------------------------------------------------------------------------------- |
| `00-storage.yml`               | Persistent Volume (PV) dan Persistent Volume Claim (PVC) untuk penyimpanan data          |
| `01-eureka.yml`                | Service Discovery (Eureka Server)                                                        |
| `02-postgres.yml`              | Database Relasional (PostgreSQL)                                                        |
| `03-mongo.yml`                 | Database NoSQL (MongoDB)                                                                |
| `04-rabbitmq.yml`              | Message Broker (RabbitMQ)                                                               |
| `05-elasticsearch.yml`         | Engine Pencarian & Log Storage (Elasticsearch)                                          |
| `06-logstash.yml`              | Log Processing Pipeline (Logstash)                                                      |
| `07-kibana.yml`                | Visualisasi Data & Log (Kibana)                                                        |
| `08-anggota-service.yml`       | Microservice Anggota                                                       |
| `09-buku-service.yml`          | Microservice Buku                                                          |
| `10-peminjaman-service.yml`    | Microservice Peminjaman                                                     |
| `11-pengembalian-service.yml`  | Microservice Pengembalian                                                   |
| `12-api-gateway.yml`           | API Gateway sebagai pintu masuk (Entry Point) client                                   |
| `13-rabbitmq-email-service.yml`| Microservice Consumer Email via RabbitMQ                                               |
| `servicemonitor.yaml`          | Integrasi monitoring dengan Prometheus Operator                                         |
| `values-monitoring.yml`        | Konfigurasi tambahan untuk stack monitoring (Grafana/Prometheus)                        |

---

## ✅ Persiapan Sebelum Deployment

Pastikan hal-hal berikut sudah terpenuhi:

- Cluster Kubernetes (Minikube/Kind/Cloud) sudah berjalan.
- `kubectl` sudah terkonfigurasi dengan benar.
- Docker Desktop/Engine dalam kondisi aktif.
- Clone repository:

```bash
git clone https://github.com/meilashinta/springboot_microservice
cd springboot_microservice
````

---

## ▶️ Proses Deployment

### 1️⃣ Deploy Infrastruktur dan Database

Jalankan perintah berikut **secara berurutan** untuk membangun infrastruktur utama:

```bash
kubectl apply -f k8s/00-storage.yml
kubectl apply -f k8s/01-eureka.yml
kubectl apply -f k8s/02-postgres.yml
kubectl apply -f k8s/03-mongo.yml
kubectl apply -f k8s/04-rabbitmq.yml
kubectl apply -f k8s/05-elasticsearch.yml
kubectl apply -f k8s/06-logstash.yml
kubectl apply -f k8s/07-kibana.yml
```

---

### 2️⃣ Deploy Microservices Perpustakaan

Selanjutnya, deploy microservices yang menangani fungsi-fungsi perpustakaan:

```bash
kubectl apply -f k8s/08-anggota-service.yml
kubectl apply -f k8s/09-buku-service.yml
kubectl apply -f k8s/10-peminjaman-service.yml
kubectl apply -f k8s/11-pengembalian-service.yml
kubectl apply -f k8s/12-api-gateway.yml
kubectl apply -f k8s/13-rabbitmq-email-service.yml
```

---

## 🔍 Verifikasi Hasil Deployment

Periksa apakah semua **pod** berjalan dengan lancar:

```bash
kubectl get pods
```

Periksa semua **service** yang sudah aktif:

```bash
kubectl get svc
```

---

## 🌐 Informasi Akses Layanan

| Service       | Port                                                       |
| ------------- | ---------------------------------------------------------- |
| Eureka Server | 8761                                                       |
| API Gateway   | 9002                                                       |
| Kibana        | NodePort (akses melalui NodePort yang sudah dikonfigurasi) |

