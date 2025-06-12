📇 ContactNest — Cloud-Based Contact Management System

A full-stack web application built using Spring Boot and deployed on AWS, enabling secure, efficient, and interactive contact management in the cloud.


🔧 Tech Stack

Backend: Spring Boot, Spring Data JPA, Hibernate, Spring Security, OAuth2

Frontend: HTML, CSS, JavaScript, SweetAlert2

Database: MySQL (AWS RDS), Hibernate ORM

Cloud & Deployment: AWS Elastic Beanstalk, EC2, RDS, Cloudinary, Docker, PHPMyAdmin

Authentication: OAuth2 (Google, GitHub), Email verification with JavaMail

DevOps: Docker (Multi-container), Docker Compose, Nginx


🚀 Features

✅ User Registration with Email Verification

- Accounts remain disabled until email verification is completed.

- Prevents fake signups and enhances security.

✅ OAuth2 Integration

- Login with Google & GitHub accounts for seamless authentication.

✅ Secure REST API

- Built with Spring Boot RESTful controllers.

- Consumed asynchronously with JavaScript fetch() for real-time DOM updates.

✅ Full Contact Management

- Add, update, delete, view contacts.

- Upload contact images to Cloudinary CDN.

- Search by name, email, phone.

- Server-side pagination for efficient data handling.

✅ Excel Export Functionality

- Export all contacts or filtered search results to Excel.

✅ Robust Validation

- Client-side and server-side validations for consistent, clean data.

✅ Interactive UX

- SweetAlert2 modals for confirmations, alerts, and smooth user interaction.

✅ Security

- Protected routes with Spring Security.

- Encrypted passwords using BCrypt.

✅ Dockerized Deployment

- Multi-container Docker Compose setup (Spring Boot + MySQL + PHPMyAdmin).

- Reduced local deployment time by 90%.

✅ Cloud Deployment

- Deployed on AWS Elastic Beanstalk with fully managed cloud infrastructure.


<!-- 📸 Screenshots
Add your app screenshots here (login page, dashboard, CRUD operations, etc.) -->

🔗 Live Demo
- http://contactnest.ap-southeast-1.elasticbeanstalk.com/home


📂 Clone and Run Locally

- git clone https://github.com/shabhay007/ContactNest.git

- cd ContactNest

- docker-compose up --build

- Ensure Docker and Docker Compose are installed on your system.

💡 Learnings

- Full-stack architecture and design.

- Cloud deployment (AWS Elastic Beanstalk, RDS, EC2, S3).

- REST API development and integration with front-end.

- Auth2 and secure authentication using Spring Security.

- Containerization and DevOps practices using Docker.

- Real-time UI updates with JavaScript and asynchronous APIs.


📞 Contact

- Feel free to connect on LinkedIn or through GitHub for feedback or collaboration opportunities!