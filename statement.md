Problem Statement: 
Engineering hardware labs manage hundreds of high-value items, ranging from basic 8086/8051 trainer kits to advanced ESP32 modules and NVIDIA Jetson Orin Nano boards. Currently, equipment checkouts are typically managed via physical logbooks or static spreadsheets. This manual approach leads to misplaced assets, accountability gaps when items are not returned, and frustration for students who cannot check real-time stock availability before visiting the lab.

Scope of the Project: 
This Java application will digitize the inventory lifecycle. It will maintain a centralized database of all lab equipment, differentiate between available and borrowed stock, and securely log every transaction to ensure total accountability of hardware assets.

Target Users:
Lab Administrators/Faculty: Who need to add new equipment, monitor overall stock levels, and identify exactly who possesses overdue items.

Engineering Students: 
Who need a streamlined way to view available hardware and check out components for their coursework.

High-Level Features: 
Role-based access control, real-time inventory CRUD (Create, Read, Update, Delete) operations, an automated checkout and return workflow, and dynamic stock tracking.