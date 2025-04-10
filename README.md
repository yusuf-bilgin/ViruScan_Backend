# 🛡️ ViruScan_Backend
Bu servis, kullanıcıların yüklediği dosyaları **PowerShell** aracılığıyla **Windows Defender** ile tarar, sonuçları işler ve veritabanına kaydeder.
Spring Boot ve React kullanarak 3 4 haftada yapmış olduğum fullstack uygulamanın backend bölümüdür. Backend'de bir script dosyası çalıştırılarak powershell'den Windows Defender'a erişim sağlıyorum ve burada istenen dosyayı taratıyorum. Tarama sonucunu, yüklenen dosyanın tarama sonucu, isim, boyut, tür vb. bilgilerini veritabanına kaydediyorum. Frontend'de ise bunu bir nevi otomatize hale getirmeye çalıştım. Kullanıcı kayıt ve giriş işlemlerini yaptıktan sonra virüs taraması için dosya yükleyebilir, yüklenen dosyaların bilgilerini tabloda görüntüleyebilir. Kullanıcının şifre gibi bilgileri SHA-256 ile hashlenerek veritabanına kayıt edilmiştir. 'user'tablosunda mail ve parolası ile eşleşen kullanıcılar 'files' tablosuna erişim sağlayabilir

This is the backend of full-stack application I developed in 3-4 weeks using Spring Boot and React. In the backend, I execute a script file to access Windows Defender through PowerShell, scanning a requested file. The scanning result, along with information such as the uploaded file's name, size, type, and more, is then stored in the database. On the frontend, I attempted to automate this process to some extent.
After users complete registration and login processes, they can upload files for virus scanning. The information about the uploaded files can be viewed in a table. User information such as passwords is hashed with SHA-256 before being stored in the database. Users with matching email and password in the 'user' table can access the 'files' table.

---

## 🔍 Proje Özeti & ⚙️ Özellikler
- 🔐 Kullanıcı kayıt ve giriş (SHA-256 hashleme ile)
- 🛡️ Dosya tarama işlemini Windows Defender üzerinden yürütme
- 🗂️ Tarama sonuçlarının veritabanına kaydı
- 📦 RESTful API mimarisi ile frontend ile veri alışverişi
- 🧩 React frontend ile entegre çalışır (Ayrı repoda)

- Kullanıcılar dosya yüklediğinde backend tarafında PowerShell script’i tetiklenir.
- Script, Windows Defender'ı kullanarak dosya üzerinde virüs taraması yapar.
- Tarama sonucuyla birlikte dosya adı, boyutu, türü gibi bilgiler veritabanına kaydedilir.
- Kullanıcı şifreleri SHA-256 ile hash’lenerek güvenli biçimde saklanır.
- Yalnızca `user` tablosunda kayıtlı ve doğrulanmış kullanıcılar, `files` tablosuna erişebilir.

---

## 🧪 Kullanılan Teknolojiler

- **Spring Boot** (Java)
- **JPA / Hibernate** (Veritabanı erişimi için)
- **H2 veya PostgreSQL**
- **PowerShell** (Windows Defender’a erişim için)
- **Lombok**, **Spring Security**, **ModelMapper**

---

## 🛠️ API Uç Noktaları (Örnekler)

```http
POST /api/auth/register
POST /api/auth/login
POST /api/files/upload
GET  /api/files/user/{id}
