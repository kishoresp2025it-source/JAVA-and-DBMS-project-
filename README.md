# WheelCraft System

Java-based Vehicle Rental and Fleet Management System.

## Technologies
- Java 21
- SQLite
- SQL
- JDBC

## Features
- Vehicle management (CRUD)
- Customer registration
- Vehicle rental and return
- Booking management
- Cash, UPI and Card payment records
- Payment history

## Database
SQLite database with four tables:
Vehicles, Customers, Bookings and Payments.

## How to Run

Compile:
javac -cp ".:lib/sqlite-jdbc.jar" *.java

Run:
java -cp ".:lib/sqlite-jdbc.jar" Main
