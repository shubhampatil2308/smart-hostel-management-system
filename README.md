# Smart Hostel Management System

A comprehensive Spring Boot-based hostel management system with role-based access control for Students, Wardens, and Administrators.

## Features

### Student Features
- View assigned room details
- Submit and track complaints
- View attendance records
- Check payment history
- View mess menu

### Warden Features
- Manage student records
- Manage room allocations
- Handle and resolve complaints
- Mark student attendance
- View hostel statistics

### Admin Features
- Manage all users (Students, Wardens, Admins)
- Complete room management
- System-wide complaint management
- Payment tracking
- Mess menu management
- System statistics and reports

## Technology Stack

- **Backend**: Spring Boot 4.0.1
- **Database**: MySQL
- **Security**: Spring Security with BCrypt password encoding
- **Email**: Spring Mail (Gmail SMTP)
- **Frontend**: Thymeleaf templates with modern CSS
- **Java Version**: 21

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+
- Gmail account (for email functionality)

## Setup Instructions

### 1. Database Setup

Create a MySQL database:
```sql
CREATE DATABASE smart_hostel_db;
```

### 2. Configure Database

Update `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Configure Email (Optional but Recommended)

Update email settings in `application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Note**: For Gmail, you need to:
1. Enable 2-Factor Authentication
2. Generate an App Password
3. Use the App Password (not your regular password)

### 4. Run the Application

```bash
mvn spring-boot:run
```

Or build and run:
```bash
mvn clean package
java -jar target/SmartHostel-0.0.1-SNAPSHOT.jar
```

The application will be available at: `http://localhost:8080`

## Default Access

After starting the application, you can register new users through the registration page. The first user should be registered as ADMIN to manage the system.

## Project Structure

```
src/main/java/com/SmartHostel/
├── Config/              # Security and configuration
├── controller/          # REST and MVC controllers
├── model/               # Entity models
├── repository/          # JPA repositories
└── service/             # Business logic services

src/main/resources/
├── templates/           # Thymeleaf HTML templates
├── static/              # CSS, JS, images
└── application.properties
```

## User Roles

1. **STUDENT**: Can view their room, submit complaints, check attendance and payments
2. **WARDEN**: Can manage students, rooms, complaints, and attendance
3. **ADMIN**: Full system access including user management

## Email Notifications

The system sends email notifications for:
- User registration confirmation
- Complaint resolution updates

## Database Schema

The application uses JPA/Hibernate with automatic schema generation (`spring.jpa.hibernate.ddl-auto=update`). The following tables are created automatically:

- `users` - User accounts
- `students` - Student-specific information
- `rooms` - Room details
- `complaints` - Student complaints
- `attendance` - Attendance records
- `payments` - Payment transactions
- `mess_menu` - Daily mess menu

## Security

- Passwords are hashed using BCrypt
- Role-based access control (RBAC)
- Spring Security form-based authentication
- CSRF protection (can be enabled/disabled)

## Development

### Building the Project
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

## Troubleshooting

### Email Not Sending
- Verify Gmail credentials in `application.properties`
- Ensure App Password is used (not regular password)
- Check firewall/network settings

### Database Connection Issues
- Verify MySQL is running
- Check database credentials
- Ensure database exists

### Port Already in Use
Change the port in `application.properties`:
```properties
server.port=8081
```

## License

This project is developed for educational purposes.

## Support

For issues or questions, please check the application logs or contact the development team.

