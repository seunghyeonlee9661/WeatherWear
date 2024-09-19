# WeatherWear
![1 (1)](https://github.com/user-attachments/assets/d7c9202e-c07e-4521-a3a1-86305b9e09e7)

## Table of Contents
1. [📄 Project Introduction](#project-introduction)
2. [🛠️ Tech Stack](#tech-stack)
3. [🗂️ Project Structure](#project-structure)
4. [🌟 Key Features](#key-features)
5. [📑 API Documentation](#api-documentation)
6. [🧪 Testing](#testing)
7. [🚀 Deployment](#deployment)

## 📄 Project Introduction
Welcome to **WeatherWear**! This project is the result of a team effort from the 2024 Sparta Coding Club - Innovation Camp.

**WeatherWear** is an innovative system designed to recommend daily outfits based on the weather. By analyzing weather data from the user's location, it suggests appropriate outfits using factors such as clothing type and color.

**Key Features**:
- 🎯 **Personalized Outfit Recommendations**: Get daily outfit suggestions tailored to today's weather, your wardrobe, similar past outfits, and data from other users.
- 🗣️ **OOTD Sharing**: Register and share your Outfit of the Day (OOTD) with others.
- 🔍 **Search Functionality**: Search for outfits using keywords, weather icons, clothing types, and colors.
- 🚀 **Future Enhancements**: Includes real-time notifications, event-based recommendations, and specific date suggestions.
- ⚙️ **Performance Optimization**: Optimized for stable server performance through load testing.

**Project URL**: [WeatherWear](https://weatherwearclothing.com/)  
**Notion**: [Notion Page](https://www.notion.so/Weather-Wear-9e4122225f5d446489d14b9a028046f3)  
**Project GitHub**: [GitHub Repository](https://github.com/WeatherWearTeam)

## 🛠️ Tech Stack

| Technology                                             | Description                                    | Reason                                                                                         |
|--------------------------------------------------------|------------------------------------------------|------------------------------------------------------------------------------------------------|
| ![Java & Spring Boot](https://img.shields.io/badge/-Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white) | Java is a stable and widely-used object-oriented programming language. Spring Boot is an application framework based on the Spring framework. | Java provides strong performance and platform independence, while Spring Boot offers easy setup and faster development, enhancing productivity compared to the standard Spring framework. |
| ![JPA](https://img.shields.io/badge/-JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white) | Java Persistence API supports mapping between objects and databases. | Allows object-oriented database operations without writing SQL, and works well with implementations like Hibernate to improve code readability and maintainability. |
| ![Gradle](https://img.shields.io/badge/-Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) | A build automation tool supporting flexible build systems and dependency management. | Provides better performance and customization compared to Maven, and supports Groovy or Kotlin DSL for complex build configurations. |
| ![Spring Security](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) | A security framework for Spring applications. | Offers strong authentication and authorization features, supporting modern security standards like OAuth2 and JWT, with flexible configuration options. |
| ![NGINX](https://img.shields.io/badge/-NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white) | A web server and reverse proxy server handling communication between HTTP and HTTPS. | Provides high performance and low memory usage, enhancing web application response times through load balancing and caching. Supports secure communication between HTTP and HTTPS. |
| ![Github](https://img.shields.io/badge/-Github-181717?style=for-the-badge&logo=github&logoColor=white) | Source code version control and collaboration tool. | Facilitates code collaboration and versioning, with features like Pull Requests and Issues for an efficient development process. Provides a user-friendly UI and integration features compared to other platforms. |
| ![Github Actions](https://img.shields.io/badge/-Github%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) | CI/CD pipeline automation tool. | Automatically builds, tests, and deploys code changes, seamlessly integrated with GitHub for convenience. Provides simpler and faster setup compared to other CI/CD tools. |
| ![Postman](https://img.shields.io/badge/-Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) | API development and testing tool. | Allows visual testing of API requests and responses, with automation features for ease of development. Offers a user-friendly interface and powerful functionality compared to other testing tools. |
| ![Redis](https://img.shields.io/badge/-Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) | In-memory database providing fast data access and caching. | Supports high-performance data caching and session management, offering low latency and high throughput. Provides faster data access compared to other databases. |
| ![MySQL](https://img.shields.io/badge/-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) | Open-source relational database management system. | A stable and mature database system supporting relational data models. Guarantees ACID transactions and offers cost efficiency compared to other commercial databases. |
| ![Route 53](https://img.shields.io/badge/-AWS%20Route%2053-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's DNS web service for domain name management and DNS queries. | Provides high availability and fast response times, maintaining consistent performance across global regions. Offers complex domain management features and integrated AWS services. |
| ![ELB](https://img.shields.io/badge/-AWS%20ELB-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's load balancer service. | Enhances application availability and scalability by distributing traffic and reducing server load. Integrates with Auto Scaling for efficient server resource management. |
| ![EC2](https://img.shields.io/badge/-AWS%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's virtual server instance service. | Offers flexible server resource management and high scalability, with a wide range of instance types for various workloads. Provides more features and support compared to other cloud providers. |
| ![S3](https://img.shields.io/badge/-AWS%20S3-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's object storage service. | Provides reliable and scalable data storage with various storage classes and easy data access. Offers cost-effective and high durability compared to other storage services. |
| ![CodeDeploy](https://img.shields.io/badge/-AWS%20CodeDeploy-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's deployment automation service. | Automates application deployment and management, offering various deployment strategies and rollback features for reliable deployments. Reduces manual errors and improves deployment efficiency. |
| ![Amazon RDS](https://img.shields.io/badge/-AWS%20RDS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS's managed relational database service. | Automates backups, patching, and monitoring to reduce database management complexity. Provides improved operational efficiency and stability compared to self-managed databases. |

## 🗂️ Project Structure
<details>
<summary>File Structure</summary>
<pre>
src
 ├── 📂main
 │    ├── 📂java
 │    └── 📂resources
 │         └── 📜application.properties
 ├── 📂board
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂clothes
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂enums
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂global
 │    ├── 📂config
 │    ├── 📂dto
 │    ├── 📂filter
 │    ├── 📂handler
 │    ├── 📂security
 │    └── 📂service
 ├── 📂user
 │    ├── 📂controller
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂enums
 │    ├── 📂repository
 │    ├── 📂service
 │    └── 📂utils
 ├── 📂weather
 │    ├── 📂controller
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂wishlist
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 └── 📜WeatherWearApplication.java
</pre>
</details>

## 🌟 Key Features
- **Personalized Outfit Recommendations**: Get daily outfit suggestions tailored to today's weather, your wardrobe, similar past outfits, and data from other users.
- **OOTD Sharing**: Register and share your Outfit of the Day (OOTD) with others.
- **Search Functionality**: Search for outfits using keywords, weather icons, clothing types, and colors.
- **Future Enhancements**: Includes real-time notifications, event-based recommendations, and specific date suggestions.
- **Performance Optimization**: Optimized for stable server performance through load testing.

## 📑 API Documentation
You can view the API documentation using [Swagger UI](http://localhost:8080/swagger-ui.html).

