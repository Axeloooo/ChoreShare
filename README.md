# 🛎️ ChoreSync: Roommate Task Coordinator

![GitHub repo size](https://img.shields.io/github/repo-size/2BlackCats/ENSF401_Final_Project?logo=github&color=blue)
![Github tag](https://img.shields.io/github/v/tag/2BlackCats/ENSF401_Final_Project?logo=github&color=red)
![GitHub contributors](https://img.shields.io/github/contributors/2BlackCats/ENSF401_Final_Project?logo=github&color=yellow)
![Github pull requests](https://img.shields.io/github/issues-pr/2BlackCats/ENSF401_Final_Project?logo=github)
![Github license](https://img.shields.io/github/license/2BlackCats/ENSF401_Final_Project?logo=github&color=orange)

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

- Mariia Podgaietska
- Jayden Mikulcik
- Ryan Lau
- Axel Sanchez
- Evan Cherewko
- Alex On
- Rhishik Roy

## 📜 Description

Roommate task board application. Many roommates living together can add and remove tasks to a shared board. This will help organize chores and tasks within a shared space and increase the quality of life within shared living situations.

## 📚 Tech Stack

- Frontend

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![Axios](https://img.shields.io/badge/Axios-5A29E4.svg?style=for-the-badge&logo=Axios&logoColor=white)

- Backend

![SpringBoot](https://img.shields.io/badge/Spring%20Boot-6DB33F.svg?style=for-the-badge&logo=Spring-Boot&logoColor=white)
![Java](https://img.shields.io/badge/JAVA-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)
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

![ChoreSync Architecture](./images/choresync_architecture.png)

## 📱 Frontend Documentation

Coming soon...

## 🚀 Backend Documentation

### Database Setup

1. Open the terminal and clone this repository using HTTPS or SSH (The example below uses SSH).

```bash
git clone git@github.com:2BlackCats/ENSF401_Final_Project.git
```

2. `cd` into the `ENSF401_Final_Project` directory.

```bash
cd ENSF401_Final_Project
```

3. `cd` into the `backend` directory.

```bash
cd backend
```

4. Run the following command to start the PostgreSQL database using Docker.

```bash
docker-compose up postgres pgadmin4 -d
```

5. Open a web browser and navigate to `http://localhost:5050`. Log in to the pgAdmin4 web interface using the following credentials:

- Email: `pgadmin4@pgadmin.com`

- Password: `admin`

![pgAdmin4 Login](./images/image1.png)

6. Right-click on `Add New Server` in the `Quick Links` section.

![Add New Server](./images/image2.png)

7. Enter the following details in the `General` tab:

- Name: `ChoreSync`

![General Tab](./images/image3.png)

8. Click on the `Connection` tab and enter the following details:

- Host name/address: `postgres`

- Port: `5432`

- Username: `postgres`

- Password: `root`

- Save password?: `Yes`

![Connection Tab](./images/image4.png)

9. Click on the `Save` button to save the server configuration.

10. Right-click on the `ChoreSync` server and select `Create > Database...`.

![Create Database](./images/image5.png)

11. Enter the following details in the `General` tab of the `Create - Database` dialog:

- Database: `<service-name-db>`

![Create Database](./images/image6.png)

_Replace `<service-name>` with the name of the database you want to create. For example, `userdb`, `taskdb`, `householddb`, etc._

12. Click on the `Save` button to create the database.

13. If is the first time you are running the application, you will need repeat steps 10 to 12 for each database of each service.

### Running the Backend

1. If you have previously set up the database, you can proceed to run the backend. Otherwise, follow the instructions in the `Database Setup` section.

2. Open a new terminal and from the root directory of the repository, `cd` into the `backend` directory.

```bash
cd backend
```

3. Run the following command to start the backend.

```bash
docker compose up -d
```

4. The backend should now be running.
