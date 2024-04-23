# 📌 ChoreShare

![GitHub repo size](https://img.shields.io/github/repo-size/Axeloooo/ChoreShare?logo=github&color=blue&style=flat-square)
![Github tag](https://img.shields.io/github/v/tag/Axeloooo/ChoreShare?logo=github&color=red&style=flat-square)
![GitHub contributors](https://img.shields.io/github/contributors/Axeloooo/ChoreShare?logo=github&color=yellow&style=flat-square)
![Github issues](https://img.shields.io/github/issues/Axeloooo/ChoreShare?logo=github&style=flat-square)
![Github pull requests](https://img.shields.io/github/issues-pr/Axeloooo/ChoreShare?logo=github&style=flat-square)
![Github license](https://img.shields.io/github/license/Axeloooo/ChoreShare?logo=github&color=orange&style=flat-square)

## 📖 Table of Contents

- [📝 Contributors](#📝-contributors)
- [📜 Description](#📜-description)
- [📚 Tech Stack](#📚-tech-stack)
- [🧩 Architecture Overview](#🧩-architecture-overview)
- [📱 Frontend Documentation](#📱-frontend-documentation)
- [🚀 Backend Documentation](#🚀-backend-documentation)
  - [Database Setup](#database-setup)
  - [Running the Backend](#running-the-backend)

## 📝 Contributors

- Axel Sanchez
- Mariia Podgaietska
- Jayden Mikulcik
- Ryan Lau
- Evan Cherewko
- Alex On
- Rhishik Roy

## 📜 Description

Roommate task board application. Many roommates living together can add and remove tasks to a shared board. This will help organize chores and tasks within a shared space and increase the quality of life within shared living situations.

## 📚 Tech Stack

- Mobile

![Expo](https://img.shields.io/badge/Expo-000020.svg?style=for-the-badge&logo=Expo&logoColor=white)
![React Native](https://img.shields.io/badge/React%20Native-20232A.svg?style=for-the-badge&logo=React&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)
![Redux](https://img.shields.io/badge/Redux-764ABC.svg?style=for-the-badge&logo=Redux&logoColor=white)

- Frontend

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)

- Backend

![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=Spring-Boot&logoColor=white)
![Java](https://img.shields.io/badge/JAVA-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000020?style=for-the-badge&logo=JSON%20web%20tokens)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162.svg?style=for-the-badge&logo=JUnit5&logoColor=white)

- Database

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1.svg?style=for-the-badge&logo=PostgreSQL&logoColor=white)

- DevOps

![GCP](https://img.shields.io/badge/Google%20Cloud-4285F4?style=for-the-badge&logo=Google%20Cloud&logoColor=white)
![Terraform](https://img.shields.io/badge/terraform-%235835CC.svg?style=for-the-badge&logo=terraform&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=Jenkins&logoColor=white)
![Kubernetes](https://img.shields.io/badge/Kubernetes-326CE5?style=for-the-badge&logo=Kubernetes&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white)

## 🧩 Architecture Overview

![ChoreShare Architecture](./images/choreshare_architecture.png)

## 📱 Frontend Documentation

Coming soon...

## 🚀 Backend Documentation

### Running the Backend

1. Open the terminal and clone this repository using HTTPS or SSH (The example below uses SSH).

```bash
git clone git@github.com:Axeloooo/ChoreShare.git
```

2. `cd` into the `ChoreShare` directory.

```bash
cd ChoreShare
```

3. Open a new terminal and from the root directory of the repository, `cd` into the `backend` directory.

```bash
cd backend
```

4. Run the following command to start the backend.

```bash
docker compose up -d
```

5. Open the browser and navigate to `http://localhost:8761` to view the Eureka dashboard.

6. Open the browser and navigate to `http://localhost:5050` to view the PG4Admin dashboard.

## Setup PG4Admin

1. Make sure you followed the steps on [Running the Backend](#running-the-backend) before proceeding.

2. Open the browser and navigate to `http://localhost:5050` to view the PG4Admin dashboard.

3. Login with the following credentials:

- **Username:** `pgadmin4@pgadmin.com`

- **Password:** `admin`

![PG4Admin Login](./images/image1.png)

4. Click on `Add New Server`.

![Add New Server](./images/image2.png)

5. In the `General` tab, enter the following information:

- **Name:** `ChoreShare`

![General Tab](./images/image3.png)

6. In the `Connection` tab, enter the following information:

- **Host name/address:** `postgres`

- **Port:** `5432`

- **Maintenance database:** `postgres`

- **Username:** `postgres`

- **Password:** `root`

- **Save password?:** `Yes`

![Connection Tab](./images/image4.png)
