# Project2OS
*Check the releases*

## Traffic Management in a City of Robots

### Project Overview
This project implements a traffic management system for a simulated city operated by autonomous robots. The goal is to ensure that multiple robots can navigate the grid, following pre-defined routes and traffic rules, without causing collisions or deadlocks. The project demonstrates key concepts from operating systems, particularly in the areas of synchronization, concurrency, and resource management.

### Features
- **Autonomous Robot Navigation:** Robots follow specific routes through intersections and a single-lane bidirectional path to a remote city.
- **Concurrency Management:** Uses locks, condition variables, and semaphores to control access to shared resources, such as intersections and narrow roads.
- **Deadlock Prevention and Recovery:** Implements strategies to prevent deadlocks in critical sections, with mechanisms for recovery if they occur.
- **Simulation Testing:** Multiple test cases to simulate various traffic scenarios, including high congestion and deadlock situations.

### How It Works
- **Robot Driver:** Navigates through the city, follows traffic rules, and waits at intersections when necessary. Robots must avoid deadlocks on a narrow, bidirectional road to a remote location.
- **Traffic Light Controller:** Manages signals at intersections to ensure smooth traffic flow and avoid collisions.
- **Concurrency Control:** Manages shared resources (intersections and narrow roads) using semaphores and locks to avoid conflicts between robots.
- **Deadlock Handling:** The system can both prevent deadlocks and recover from them when detected, ensuring that traffic continues to flow.

### Key Concepts
- **Concurrency:** Robots share resources and need to avoid conflicting accesses.
- **Deadlocks:** Critical sections like intersections or narrow roads are prone to deadlocks, which the system must handle.
- **Synchronization:** Proper synchronization between robots is achieved using concurrency mechanisms from Java.

### Contributing
If you want to contribute to this project:

1. Fork the repository.
2. Create a new branch (git checkout -b feature-branch).
3. Commit your changes (git commit -am 'Add new feature').
4. Push to the branch (git push origin feature-branch).
5. Open a pull request.
