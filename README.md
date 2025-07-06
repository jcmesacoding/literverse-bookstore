# 📚 LiterVerse Bookstore

A Java Spring Boot console application that connects to the [Gutendex](https://gutendex.com/) API to fetch and store classic books and their authors in a local PostgreSQL database.

## 🧠 Features

- Search and save books from Gutendex by title
- List stored books with authors and download counts
- List authors by time periods (alive between years)
- Filter books by language (Spanish, French, Italian, Portuguese, etc.)
- View Top 10 downloaded books
- Automatically update missing language info from the API

## 🏗️ Technologies Used

- Java 17+
- Spring Boot 3
- PostgreSQL
- JPA / Hibernate
- RESTTemplate for API calls
- Jackson for JSON parsing

## 🚀 Getting Started

### 1. Clone the repo

```bash
git clone https://github.com/jcmesacoding/literverse-bookstore.git
cd literverse-bookstore
```

### 2. Configure the database

Edit the `src/main/resources/application.properties` file:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

> 💡 You can rename `application-example.properties` to `application.properties` if credentials are excluded from version control.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Or run the main class directly:

```bash
mvn clean package
java -jar target/bookstore-0.0.1-SNAPSHOT.jar
```

### 4. Use the console menu

You'll see an interactive menu in the terminal to:
- Search for books
- List stored books or authors
- Filter by language or year
- View top downloaded titles

## 🖼️ Example Output

```bash
**Welcome to LiterVerse**

***************************************
1- Search Book by Title
2- List Book Registered
3- List Author Registered
4- List Author Alive Per Years
5- List Books by Language
6- Top 10 Books by downloads
0- Exit
***************************************
Select the desired option:
```

## 📌 TODOs

- [ ] Add author deduplication logic
- [ ] Add web interface (Spring MVC or Thymeleaf)
- [ ] Dockerize the project
- [ ] Improve exception handling

## 📜 License

MIT – use it freely, credit appreciated 😊

---

### 👨‍💻 Author

Developed with 💻 by [Juan Carlos Mesa](https://github.com/jcmesacoding)
