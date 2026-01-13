# 📚 Spring Boot Microservices Project  
## Sistem Perpustakaan & Toko

Repository ini berisi implementasi dan dokumentasi teknis **Spring Boot Microservices** menggunakan **Spring Boot 3 (Java 17)** dengan pendekatan **Hybrid Deployment**.

Project ini mencakup dua domain utama:
- **Perpustakaan**
- **Toko**

Seluruh sistem Perpustakaan dideploy di **Kubernetes**, dilengkapi **CI/CD Jenkins Pipeline**, serta **Monitoring & Observability** menggunakan ELK Stack dan Prometheus.

---

## 🧩 Gambaran Umum Sistem

Project ini dirancang sebagai simulasi sistem **enterprise microservices** yang mencakup:

- Deployment microservices di Kubernetes  
- Service discovery menggunakan Eureka  
- API Gateway sebagai entry point  
- Komunikasi asinkron menggunakan RabbitMQ  
- CI/CD otomatis menggunakan Jenkins  
- Monitoring metrics dan centralized logging  

---

## 📂 Dokumentasi Teknis

Seluruh dokumentasi teknis disusun secara terpisah dan dapat diakses melalui folder **`docs/`** berikut:

### 🚀 Deployment Kubernetes (Perpustakaan & Toko)
Berisi konfigurasi Kubernetes dan langkah-langkah deployment seluruh infrastruktur serta microservices domain **Perpustakaan dan Toko**.

👉 [docs/k8s.md](docs/k8s.md)

---

### 📊 Monitoring & Observability
Menjelaskan implementasi **Monitoring dan Logging**, meliputi:
- Prometheus & Grafana (metrics)
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Filebeat sebagai log shipper

👉 [docs/monitoring.md](docs/monitoring.md)

---

### 🔄 CI/CD Jenkins Pipeline
Menjelaskan alur **Jenkins Pipeline**, proses build aplikasi, build Docker image, push ke registry, dan deployment otomatis ke Kubernetes.

👉 [docs/jenkins.md](docs/jenkins.md)

---

## 🎯 Tujuan Pembuatan Project

Dokumentasi dan project ini dibuat untuk:

- Menjelaskan implementasi **arsitektur microservices**
- Memberikan panduan **deployment Kubernetes**
- Menunjukkan penerapan **CI/CD Jenkins**
- Memahami **monitoring dan observability system**
- Menjadi bahan pembelajaran **DevOps & Microservices**

---

## 👩‍💻 Author

**Meilashinta Putri Yuliantoni**    TRPL3D  Mahasiswi Teknologi Rekayasa Perangkat Lunak  Jurusan Teknologi Informasi  Politeknik Negeri Padang
