

# 📊 Monitoring & Observability

Sistem monitoring pada project ini menggabungkan beberapa teknologi utama untuk memastikan aplikasi berjalan optimal dan mudah dipantau:

- ✅ **Prometheus** → Pengumpulan metrics aplikasi dan infrastruktur  
- ✅ **Grafana** → Visualisasi metrics dalam bentuk dashboard interaktif  
- ✅ **ELK Stack** → Manajemen dan visualisasi log terpusat  
- ✅ **Filebeat** → Pengirim log dari microservices ke Logstash  

Monitoring ini sangat penting untuk memantau:

- Kesehatan dan status service secara real-time  
- Penggunaan resource seperti CPU dan memori  
- Performa aplikasi secara menyeluruh  
- Logging terpusat untuk troubleshooting dan analisis  

---

## 🧩 Komponen Monitoring

### 📈 Metrics Stack

| Komponen             | Fungsi                             |
|----------------------|----------------------------------|
| Prometheus           | Mengumpulkan metrics dari service |
| Grafana              | Visualisasi data metrics          |
| Spring Boot Actuator | Mengekspos endpoint metrics       |
| ServiceMonitor       | Integrasi monitoring di Kubernetes |

Setiap microservice Spring Boot menyediakan endpoint metrics di:

```

/actuator/prometheus

````

Konfigurasi di `application.properties`:

```properties
management.endpoints.web.exposure.include=health,info,prometheus
````

---

### 📜 Logging Stack (ELK)

| Komponen      | Fungsi                                      |
| ------------- | ------------------------------------------- |
| Elasticsearch | Penyimpanan dan indexing log                |
| Logstash      | Pipeline pemrosesan dan filtering log       |
| Kibana        | Visualisasi log dan dashboard               |
| Filebeat      | Pengirim log dari microservices ke Logstash |

---

## 🔄 Alur Metrics

1. Microservices mengekspos metrics di endpoint `/actuator/prometheus`
2. Prometheus secara berkala melakukan scraping metrics tersebut
3. Metrics disimpan dan dikelola di Prometheus
4. Grafana mengambil data dari Prometheus untuk ditampilkan pada dashboard

---

## 🔄 Alur Logging

1. Microservices menghasilkan log aktivitas
2. Filebeat membaca file log dan mengirimkannya ke Logstash
3. Logstash memproses dan mengirim log ke Elasticsearch
4. Kibana menampilkan log tersebut dalam dashboard yang mudah dianalisis

---

## 🚀 Deployment Monitoring di Kubernetes

Untuk menjalankan monitoring stack, deploy konfigurasi berikut:

| File                    | Fungsi                          |
| ----------------------- | ------------------------------- |
| `values-monitoring.yml` | Konfigurasi lengkap monitoring  |
| `servicemonitor.yaml`   | ServiceMonitor untuk Prometheus |

Jalankan perintah berikut:

```bash
kubectl apply -f k8s/values-monitoring.yml
kubectl apply -f k8s/servicemonitor.yaml
```

---

## 🌐 Akses Dashboard Monitoring

### 🔎 Prometheus

Untuk mengetahui alamat service Prometheus:

```bash
kubectl get svc prometheus
```

Gunakan metode NodePort atau `kubectl port-forward` untuk mengakses.

---

### 📊 Grafana

Lihat alamat service Grafana:

```bash
kubectl get svc grafana
```

Login default:

* Username: `admin`
* Password: `admin`

---

### 📑 Kibana

Periksa alamat service Kibana:

```bash
kubectl get svc kibana
```

---

## 🎯 Manfaat Monitoring

* Memantau penggunaan CPU, memori, dan request aplikasi secara realtime
* Mendeteksi jika ada service yang down atau bermasalah
* Menganalisis bottleneck dan performa aplikasi
* Observasi traffic dan perilaku sistem secara menyeluruh

