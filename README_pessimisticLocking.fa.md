# JDBC CRUD Demo Hibernate


[🇬🇧 English](./README.md)

### 🟦 قفل بدبینانه (Pessimistic Locking) چیست؟
در قفل بدبینانه Hibernate یا دیتابیس فرض می‌کند که احتمال Conflict زیاد است و قبل از اینکه یک رکورد را تغییر بدهی، قفلش می‌کند تا هیچ Session دیگری نتواند آن را تغییر بدهد.
در Oracle این قفل معمولاً با:
    
    SELECT ... FOR UPDATE

انجام می‌شود.

### 🟦 1) FOR UPDATE (قفل بدبینانه معمولی)
وقتی Oracle می‌بیند که Session اول روی رکورد قفل گذاشته، اگر Session دوم بخواهد همان رکورد را بخواند با:

    SELECT ... FOR UPDATE

رفتارش این است:
####  ✔ صبر می‌کند تا Session اول کارش تمام شود.
#### ✔ اگر Session اول commit/rollback کند، آن‌وقت Session دوم ادامه می‌دهد. 
#### ✔ هیچ خطایی نمی‌دهد… فقط منتظر می‌ماند. #

### 🔸 مثال ساده:#

Session 1:

    SELECT * FROM PERSON WHERE ID = 5 FOR UPDATE;

→ رکورد ID=5 را قفل می‌کند و تغییرات را نگه می‌دارد.

Session 2:

    SELECT * FROM PERSON WHERE ID = 5 FOR UPDATE;

→ این Session روی همان رکورد «Block» می‌شود و صبر می‌کند.

پس:

🔵 اگر Session1 → Commit کند → Session2 ادامه می‌دهد

🔴 اگر Session1 → Rollback کند → باز Session2 ادامه می‌دهد

### 🟦 2) FOR UPDATE NOWAIT

این حکم می‌گوید:

❗ اگر رکورد قفل بود → «اصلاً صبر نکن»، سریع خطا بده

رفتار آن:

- اگر رکورد آزاد باشد → قفل می‌شود

- اگر رکورد قفل باشد → ORA-00054: resource busy and acquire with NOWAIT specified

#### 🔸 مثال:

Session 1:
    
    SELECT * FROM PERSON WHERE ID = 5 FOR UPDATE;

Session 2:

    SELECT * FROM PERSON WHERE ID = 5 FOR UPDATE NOWAIT;

→ نتیجه:

❌ خطا:


    ORA-00054: resource busy and acquire with NOWAIT specified

### 🟦 استفاده در Hibernate

#### ✔ FOR UPDATE معمولی:

    Person p = em.find(
    Person.class,
    id,
    LockModeType.PESSIMISTIC_WRITE
    );

اینجا Hibernate این را در Oracle تبدیل می‌کند به:

    SELECT ... FOR UPDATE

### ✔ استفاده از FOR UPDATE NOWAIT در Hibernate

    Map<String, Object> props = new HashMap<>();
    props.put("javax.persistence.lock.timeout", 0);  // NOWAIT

    Person p = em.find(
    Person.class,
    id,
    LockModeType.PESSIMISTIC_WRITE,
    props
    );

تبدیل می‌شود به:

    SELECT ... FOR UPDATE NOWAIT

### 🟦 چه زمانی از کدام استفاده می‌کنیم؟

#### ✔ از FOR UPDATE
برای جایی که کاربر می‌تواند چند ثانیه صبر کند:
- عملیات مالی غیرفوری
- سیستم‌های داخلی
- بیزنس‌هایی که Conflict کم دارند

#### ✔ از FOR UPDATE NOWAIT

برای سیستم‌هایی که باید فوری جواب بدهند:
مثل:
- رزرو بلیت
- رزرو اتاق هتل
- خرید محصول محدود
- ثبت سفارش هم‌زمان
- جلوگیری از double-submission

