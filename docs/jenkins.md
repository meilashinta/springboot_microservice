


# 🔄 CI/CD Jenkins Pipeline

CI/CD pada project ini menggunakan Jenkins yang diinstal secara native pada Operating System host (bukan container Docker).

Jenkins bertanggung jawab untuk proses otomatisasi berikut:

- Build source code aplikasi  
- Build Docker image  
- Push image ke Docker Registry  
- Deployment ke Kubernetes cluster  

---

## 💻 Instalasi Jenkins

Jenkins diinstall langsung pada host OS.

Pastikan kondisi berikut sudah terpenuhi:

- Java sudah terinstall  
- Jenkins service dalam keadaan berjalan  

Cek status Jenkins dengan perintah:

```bash
sudo systemctl status jenkins
````

Buka Jenkins Dashboard di browser:

```
http://localhost:8080
```

---

## 🔧 Tools yang Harus Tersedia di Host Jenkins

| Tools        | Fungsi                              |
| ------------ | ----------------------------------- |
| Java / Maven | Build aplikasi menggunakan Maven    |
| Docker       | Build dan push Docker image         |
| kubectl      | Deployment dan manajemen Kubernetes |
| Git          | Clone repository dari GitHub        |

---

## ⚙️ Alur Pipeline Jenkins

1. Jenkins menarik source code terbaru dari repository GitHub
2. Build package aplikasi menggunakan Maven
3. Build Docker image dari source code yang sudah di-build
4. Push Docker image ke Docker Registry
5. Terapkan manifest Kubernetes dengan `kubectl apply`
6. Rollout restart deployment untuk memperbarui aplikasi berjalan

---

## 🔐 Credential yang Digunakan di Jenkins

* Docker Registry Credential (untuk push image)
* Git Credential (jika repository bersifat private)
* SMTP Credential (smtp-username, smtp-from, smtp-password untuk notifikasi email)
* DockerHub Credential (jika menggunakan DockerHub)
* Kubernetes Config (~/.kube/config) untuk akses cluster

---

## 🚀 Menjalankan Pipeline

* Buka Jenkins Dashboard
* Pilih project pipeline yang sudah dibuat
* Klik tombol **Build Now**
* Pantau proses melalui console output Jenkins

---

## ✅ Verifikasi Deployment

Setelah pipeline selesai, pastikan deployment berhasil dengan memeriksa status pod dan service:

```bash
kubectl get pods
kubectl get svc
```

Pastikan pod berjalan dengan image terbaru dan service aktif sesuai kebutuhan.


