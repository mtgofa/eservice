# الخدمات الإلكترونية (eService)

تطبيق أندرويد — Kotlin + Jetpack Compose + Material 3، واجهة عربية (RTL)، تحديث تلقائي من GitHub Releases.

## الموجود حاليًا
- شاشة تسجيل دخول بالبريد الإلكتروني **أو** اسم المستخدم + كلمة المرور
  (تحقق من المدخلات، إظهار/إخفاء كلمة المرور، "تذكرني"، رسائل الخطأ، حالة التحميل).
- الجلسة محفوظة: التطبيق يفتح على الرئيسية مباشرة بعد أول دخول، مع زر تسجيل خروج.
- شاشة رئيسية فيها بيانات المستخدم ورقم الإصدار وزر "البحث عن تحديث".
- تحديث ذاتي: عند الفتح يقرأ آخر Release من `mtgofa/eservice`، ولو فيه إصدار أحدث يظهر popup
  ينزّل الـ APK ويتحقق من sha256 ثم يفتح المثبّت.
- وضع ليلي تلقائي حسب النظام.

## ربط الـ API
في `gradle.properties`:
```
apiBaseUrl=https://example.com/api
```
التطبيق يرسل `POST {apiBaseUrl}/login` بالـ JSON:
```json
{"login": "user@mail.com أو username", "password": "..."}
```
وينتظر:
```json
{"token": "...", "user": {"name": "...", "email": "...", "username": "..."}}
```
أي رد غير 2xx يُعرض كخطأ (يُعرض حقل `message` لو موجود).

لو `apiBaseUrl` فاضي التطبيق يشتغل **وضع تجريبي**: `admin` أو `admin@eservice.app` وكلمة المرور `123456`.

## الإصدار (Release)
مرة واحدة: `tools/make-keystore.sh` لإنشاء مفتاح التوقيع (`keystore/` و `keystore.properties` خارج git — احتفظ بهم،
بدونهم التطبيقات المثبتة لن تتحدث). ولو هتعمل releases من GitHub ضيف الـ secrets:
`KEYSTORE_BASE64`, `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD`.

- محليًا: `tools/release.sh bump "ملاحظات الإصدار"` — يرفع الإصدار ويبني APK موقّع وينشر Release.
- من GitHub: ارفع `versionCode`/`versionName` في `app/build.gradle.kts` ثم
  `git tag v1.0.1 && git push origin v1.0.1` — أو شغّل workflow **Release** يدويًا من تبويب Actions.

اسم الـ Release لازم يحتوي `(code N)` والملاحظات فيها سطر `sha256: …` — الاتنين بيتعملوا تلقائيًا.

كل push بيشغّل workflow **Build** اللي يبني debug APK (موجود كـ artifact).

## الهيكل
```
app/src/main/java/com/mtgofa/eservice/
  MainActivity.kt      التنقل بين الدخول والرئيسية + نافذة التحديث
  Updater.kt           التحديث الذاتي من GitHub Releases
  data/AuthApi.kt      طلب تسجيل الدخول (أو الوضع التجريبي)
  data/Session.kt      حفظ الجلسة والمستخدم
  ui/                  الثيم والشاشات
tools/release.sh       بناء ونشر Release
```
