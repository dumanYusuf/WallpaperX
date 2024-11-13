Bu uygulama, Clean Architecture prensiplerine uygun olarak tasarlanmış ve Cloud Firestore veritabanı ile geliştirilmiş bir duvar kağıdı platformudur. Bağımlılık yönetimi Hilt ile sağlanmış olup, Use Case katmanı aracılığıyla Repository ve ViewModel arasında bağlantılar kurulmuştur.

Uygulama açıldığında, ana sayfada API üzerinden çekilen duvar kağıdı resimleri listelenir. Kullanıcı, herhangi bir duvar kağıdına tıklayarak detay sayfasına yönlenebilir ve burada resmi duvar kağıdı olarak ayarlama seçeneklerine erişebilir. Kullanıcı, resmi ana ekran, kilit ekranı veya her iki ekran için de ayarlayabilir. Beğendiği resmi galerisine indirebilir.

Uygulamanın mimarisi şu şekilde yapılandırılmıştır:

Hilt: Bağımlılık yönetimini sağlamak için kullanılır. Hilt, Dependency Injection (DI) sağlayarak kodun test edilebilirliğini ve sürdürülebilirliğini artırır.

Clean Architecture: Uygulama, Clean Architecture prensiplerine uygun olarak katmanlara ayrılmıştır. Bu yapı, veri, iş mantığı ve kullanıcı arayüzü bileşenlerinin bağımsız olarak geliştirilmesini sağlar, böylece kodun okunabilirliğini ve sürdürülebilirliğini artırır.

Use Case: İş mantığını yöneten ve belirli sorumluluklarla işlemleri düzenleyen katmandır. Repository ve ViewModel arasındaki bağlantıyı sağlar, bu da kodun modülerliğini ve bakımını kolaylaştırır. Örneğin, bir resmi telefon ekranına duvar kağıdı olarak ayarlama veya galeriyi indirme işlemleri Use Case katmanında yönetilir.

ViewModel: Kullanıcı arayüzü ile iş mantığı arasındaki köprüyü kurar ve kullanıcı etkileşimlerine yanıt verir. Ana sayfada duvar kağıdı verilerinin yüklenmesi ve detay sayfasına yönlendirme işlemleri ViewModel katmanında yönetilir.

View: Yalnızca kullanıcı arayüzü (UI) kodlarını içerir ve ViewModel’e erişerek UI güncellemelerini gerçekleştirir. Ana sayfada duvar kağıtlarının listelenmesi ve detay sayfasında duvar kağıdı ayarlama seçenekleri gibi işlemler burada sağlanır.

Repository: Veritabanı veya uzak veri kaynaklarıyla etkileşimde bulunur. API'den duvar kağıdı resimlerini alır ve Use Case katmanına iletir. Repository bağımsız olarak test edilebilir olup, iş mantığından ayrıdır.

Bu yapı sayesinde uygulamanın kodları daha düzenli, sürdürülebilir ve test edilebilir hale getirilmiştir. Her fonksiyon ve sınıf, belirli bir işlevi yerine getirecek şekilde tasarlanmış olup, bu yapı uygulamanın bakımı ve geliştirilmesi için büyük avantaj sağlar.
