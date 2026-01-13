

````markdown
# 🚀 Deployment Kubernetes

Dokumen ini menguraikan langkah-langkah melakukan deployment **infrastruktur** dan **microservices perpustakaan** ke dalam cluster Kubernetes menggunakan manifest yang terdapat di folder `k8s/`.

---

## 📁 Struktur Folder `k8s/`

Folder `k8s/` berisi file-file konfigurasi Kubernetes yang mengatur komponen-komponen berikut:

| File                           | Fungsi                                                                               |
| ------------------------------ | ------------------------------------------------------------------------------------ |
| `00-storage.yml`               | Persistent Volume dan Persistent Volume Claim (PVC) untuk penyimpanan data permanen  |
| `01-eureka.yml`                | Deployment dan Service Eureka Server sebagai service discovery                       |
| `02-postgres.yml`              | Deployment PostgreSQL sebagai database relasional                                    |
| `03-mongo.yml`                 | Deployment MongoDB sebagai database non-relasional                                   |
| `04-rabbitmq.yml`              | Deployment RabbitMQ sebagai message broker                                           |
| `05-elasticsearch.yml`         | Deployment Elasticsearch untuk penyimpanan log terpusat                              |
| `06-logstash.yml`              | Deployment Logstash untuk pipeline pemrosesan log                                    |
| `07-kibana.yml`                | Deployment Kibana untuk visualisasi log                                              |
| `08-anggota-service.yml`       | Deployment microservice anggota                                                      |
| `09-buku-service.yml`          | Deployment microservice buku                                                         |
| `10-peminjaman-service.yml`    | Deployment microservice peminjaman                                                   |
| `11-pengembalian-service.yml`  | Deployment microservice pengembalian                                                 |
| `12-rabbitmq-email-service.yml`| Deployment consumer email RabbitMQ                                                  |
| `13-api-gateway.yml`           | Deployment API Gateway sebagai pintu masuk client                                   |
| `servicemonitor.yaml`          | ServiceMonitor untuk integrasi dengan Prometheus                                    |
| `values-monitoring.yml`        | Konfigurasi untuk monitoring stack                                                  |

---

## ✅ Persiapan Sebelum Deployment

Pastikan hal-hal berikut sudah terpenuhi sebelum melakukan deployment:

- Docker telah terinstall dan berjalan
- Cluster Kubernetes aktif dan siap digunakan
- `kubectl` sudah dikonfigurasi untuk terhubung ke cluster
- Repository project sudah di-clone dari GitHub:

```bash
git clone https://github.com/furqonaugust17/spring-boot-micro-service.git
cd spring-boot-micro-service
````

---

## ▶️ Proses Deployment

### 1️⃣ Deploy Infrastruktur dan Database

Jalankan perintah berikut secara berurutan untuk membangun infrastruktur utama:

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
kubectl apply -f k8s/12-rabbitmq-email-service.yml
kubectl apply -f k8s/13-api-gateway.yml
```

---

## 🔍 Verifikasi Hasil Deployment

Periksa apakah semua pod berjalan dengan lancar:

```bash
kubectl get pods
```

Kemudian pastikan semua service sudah aktif:

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

> *Catatan:*
> Jika menggunakan Kubernetes lokal, akses layanan dapat dilakukan dengan `kubectl port-forward` atau konfigurasi NodePort sesuai kebutuhan.

---

Dokumen ini diharapkan membantu kamu dalam proses deployment microservices perpustakaan secara terstruktur dan mudah dipahami.

```


