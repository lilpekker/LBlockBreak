# LBlockBreak

LBlockBreak, Minecraft sunucularında oyuncuların blokları kırarak para kazanmasını sağlayan bir PaperMC eklentisidir. Vault ekonomi sistemi ile entegre çalışır ve VIP seviyelerine göre çarpan özellikleri sunar.

## Özellikler

- ✅ Blok kırarak para kazanma sistemi
- ✅ Vault ekonomi sistemi entegrasyonu
- ✅ VIP seviyelerine göre çarpanlar (1.5x, 2.0x, 3.0x, 4.0x)
- ✅ Yönetim komutları (ekle, kaldır, ayarla, liste, yeniden yükle)
- ✅ Kolay yapılandırılabilir ayarlar
- ✅ Türkçe dil desteği

## Gereksinimler

- **Minecraft:** 1.21.4+
- **Sunucu Yazılımı:** PaperMC
- **Bağımlılıklar:** Vault (ekonomi sistemi için)

## Kurulum

1. Eklenti JAR dosyasını sunucunuzun `plugins` klasörüne atın.
2. Sunucuyu yeniden başlatın.
3. Vault eklentisinin kurulu ve çalışır olduğundan emin olun.
4. Eklenti otomatik olarak yapılandırma dosyasını oluşturacaktır.

## Komutlar

### Ana Komut
- `/bb` - Block break sistemi ana komutu

### Alt Komutlar
- `/bb add <blok> <miktar>` - Belirtilen blok için para miktarı ekle
- `/bb remove <blok>` - Belirtilen bloğu sistemden kaldır
- `/bb set <blok> <miktar>` - Belirtilen blok için para miktarını ayarla
- `/bb list` - Tüm blok ödüllerini listele
- `/bb reload` - Yapılandırma dosyasını yeniden yükle

## İzinler

| İzin | Açıklama | Varsayılan |
|------|----------|------------|
| `blockbreak.admin` | Yönetim komutlarını kullanma | OP |
| `blockbreak.earn` | Blok kırarak para kazanma | true |
| `blockbreak.vip` | VIP 1.5x çarpan | false |
| `blockbreak.vip+` | VIP+ 2.0x çarpan | false |
| `blockbreak.mvip` | MVIP 3.0x çarpan | false |
| `blockbreak.mvip+` | MVIP+ 4.0x çarpan | false |

## Yapılandırma

Eklenti ilk çalıştığında `plugins/LBlockBreak/config.yml` dosyasını otomatik oluşturur. Dosyayı düzenleyerek:

- Blok başına para miktarlarını ayarlayabilir
- VIP çarpanlarını değiştirebilir
- Mesajları özelleştirebilirsiniz

## VIP Çarpanları

- **VIP:** 1.5x çarpan
- **VIP+:** 2.0x çarpan  
- **MVIP:** 3.0x çarpan
- **MVIP+:** 4.0x çarpan

## Derleme

Geliştiriciler için eklentiyi kaynaktan derlemek:

```bash
git clone <repository-url>
cd LBlockBreak
./gradlew build
```

Derlenmiş JAR dosyası `build/libs/` klasöründe bulunacaktır.

## Destek

Sorunlar veya öneriler için lütfen GitHub Issues bölümünü kullanın.

## Lisans

Bu eklenti MIT Lisansı altında dağıtılmaktadır.

## Sürüm Geçmişi

### v1.0
- İlk yayın
- Temel blok kırma para sistemi
- Vault entegrasyonu
- VIP çarpan sistemi
- Yönetim komutları
