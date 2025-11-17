# JDBC CRUD Demo Hibernate


[🇬🇧 English](./README.md)
### 🌿 Logical Delete (Soft Delete) در Hibernate چیست؟

در حالت عادی وقتی یک رکورد را حذف می‌کنیم:

    entityManager.remove(person);


Hibernate یک DELETE واقعی در دیتابیس اجرا می‌کند و ردیف برای همیشه حذف می‌شود.

اما در خیلی از پروژه‌ها حذف واقعی خطرناک است:
- امکان بازیابی وجود ندارد
- برای گزارش‌گیری داده نیاز داریم
- رکوردهای مرتبط (Foreign Key) دچار مشکل می‌شوند
- حذف واقعی ممکن است باعث ناسازگاری شود

به همین دلیل از حذف منطقی (Logical Delete / Soft Delete) استفاده می‌شود.

### 💡 Soft Delete = رکورد را حذف واقعی نمی‌کنیم، فقط غیرفعالش می‌کنیم

معمولاً یک ستون به جدول اضافه می‌کنیم:
    
    is_deleted BOOLEAN DEFAULT false


و به‌جای delete، مقدار آن را true می‌کنیم.

### 🟢 به عبارتی Hibernate کاملاً از Soft Delete پشتیبانی می‌کند
و اینکه Hibernate چند Annotation رسمی دارد:

### 1️⃣ @SQLDelete
وقتی دستور DELETE زدیم، Hibernate به‌جای DELETE واقعی، یک UPDATE اجرا می‌کند.

    @Entity
    @SQLDelete(sql = "UPDATE person SET is_deleted = true WHERE id = ?")
    @Where(clause = "is_deleted = false")
    public class Person {
    @Id
    private Long id;
    
        private String name;
    
        private String city;
    
        private boolean isDeleted = false;
    }


### ✨ اینجا چه اتفاقی می‌افتد؟
#### 🔸 1) هنگام remove()

entityManager.remove(person);

Hibernate این را اجرا می‌کند:

UPDATE person SET is_deleted = true WHERE id = ?

نه DELETE واقعی.

#### 🔸 2) هنگام SELECT
به‌خاطر این خط:

    @Where(clause = "is_deleted = false")

Hibernate فقط رکوردهای حذف‌ نشده را می‌خواند:

    SELECT * FROM person WHERE is_deleted = false

به صورت خودکار.

#### 2️⃣ @Where

#### 3️⃣ @Filter
اگر بخواهیم گاهی حذف‌شده‌ها را هم ببینیم:
- با @Filter می‌توانیم فیلتر را در runtime فعال/غیرفعال کنیم.

ولی برای پروژه‌های معمولی همان @Where کافی است.

### 🟣 چطور استفاده می‌شود؟ مثال کامل
کلاس Person:

    @Entity
    @SQLDelete(sql = "UPDATE person SET is_deleted = true WHERE id = ?")
    @Where(clause = "is_deleted = false")
    public class Person {

        @Id
        @GeneratedValue
        private Long id;

        private String name;

        private String city;

        @Column(name = "is_deleted")
        private boolean deleted = false;

        // getters + setters
    }

#### 🟡 وقتی delete صدا می‌زنی:

    personService.delete(5L);


Hibernate این SQL را اجرا می‌کند:

    UPDATE person SET is_deleted = true WHERE id = 5;

ولی در کد انگار رکورد حذف شده است.

#### 🟢 SELECT ها چطور؟

    List<Person> list = personService.findAll();

Hibernate خودش این را اجرا می‌کند:

    SELECT * FROM person WHERE is_deleted = false;

بدون اینکه خودت شرط بنویسی.

### 🔄 چطور رکورد Soft Delete شده را Restore کنیم؟

این را باید خودمان بنویسیم:

    public void restore(Long id) {
    Person p = em.find(Person.class, id);
    p.setDeleted(false);
    }

### 🔥 مزایای Soft Delete

- اطلاعات هیچ‌وقت حذف واقعی نمی‌شود
- گزارش‌گیری دقیق‌تر
- جلوگیری از مشکل در روابط (foreign keys)
- امکان بازیابی داده
- امنیت بالا
