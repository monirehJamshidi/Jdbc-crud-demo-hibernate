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