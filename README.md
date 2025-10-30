# 🏥 Delta Pharmacy Management System - RESTful API

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

A comprehensive Spring Boot RESTful API for managing delta pharmacy operations including user authentication, product management, prescription handling, order processing, payments, and analytics.

![Delta Pharmacy Logo](logo.jpg)

## 📋 Features

### Core Features
- **User Authentication & Authorization** (JWT-based)
- **Product & Inventory Management**
- **Prescription Upload & Approval System**
- **Order Management**
- **Payment Processing** (Mock Integration)
- **Notifications System**
- **Reviews & Ratings**
- **Support Ticket System**
- **Analytics & Reports**
- **Search & Filtering**

### Security
- JWT-based authentication
- Role-based access control (ADMIN, PHARMACIST, CUSTOMER)
- Password encryption with BCrypt
- Stateless session management

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Any IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. Clone the repository
```bash
git clone https://github.com/Shady1997/delta-pharmacy-api.git
cd pharmacy-api
```

2. Build the project
```bash
mvn clean install
```

3. Run the application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8545/pharmacy-api`
- **API Documentation**: http://localhost:8545/pharmacy-api/swagger-ui/index.html

### Default Users

The application comes with pre-configured users:

| Email | Password | Role |
|-------|----------|------|
| admin@pharmacy.com | admin123 | ADMIN |
| pharmacist@pharmacy.com | pharma123 | PHARMACIST |
| customer@example.com | customer123 | CUSTOMER |

## 📚 API Documentation

### Base URL
```
http://localhost:8545/pharmacy-api/api
```

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "fullName": "John Doe",
  "phone": "1234567890",
  "address": "123 Main St"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "user@example.com",
    "fullName": "John Doe",
    "role": "CUSTOMER"
  }
}
```

#### Get Profile
```http
GET /auth/profile
Authorization: Bearer <token>
```

#### Update Profile
```http
PUT /auth/update
Authorization: Bearer <token>
Content-Type: application/json

{
  "fullName": "John Updated",
  "phone": "9876543210",
  "address": "456 New St",
  "currentPassword": "password123",
  "newPassword": "newpassword123"
}
```

### Product Endpoints

#### Get All Products
```http
GET /products
```

#### Get Product by ID
```http
GET /products/{id}
```

#### Create Product (Admin Only)
```http
POST /products
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "name": "Medicine Name",
  "description": "Description",
  "price": 10.99,
  "category": "Pain Relief",
  "brand": "BrandName",
  "imageUrl": "https://example.com/image.jpg",
  "prescriptionRequired": false,
  "stockQuantity": 100,
  "reorderLevel": 20
}
```

#### Update Product (Admin Only)
```http
PUT /products/{id}
Authorization: Bearer <admin-token>
```

#### Delete Product (Admin Only)
```http
DELETE /products/{id}
Authorization: Bearer <admin-token>
```

#### Search Products
```http
GET /search?query=paracetamol
GET /search?filter=prescription_required
```

#### Get Stock Levels
```http
GET /inventory/stock-levels
Authorization: Bearer <token>
```

#### Update Stock
```http
POST /inventory/update-stock
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "productId": 1,
  "quantity": 50,
  "operation": "ADD"
}
```

### Prescription Endpoints

#### Upload Prescription
```http
POST /prescriptions/upload?userId=1&fileName=prescription.pdf&fileType=pdf&doctorName=Dr.Smith&notes=Notes
Authorization: Bearer <token>
```

#### Get User Prescriptions
```http
GET /prescriptions/{userId}
Authorization: Bearer <token>
```

#### Approve Prescription (Pharmacist/Admin Only)
```http
PUT /prescriptions/{id}/approve
Authorization: Bearer <pharmacist-token>
```

#### Reject Prescription (Pharmacist/Admin Only)
```http
PUT /prescriptions/{id}/reject
Authorization: Bearer <pharmacist-token>
Content-Type: application/json

{
  "reason": "Invalid or expired prescription"
}
```

### Order Endpoints

#### Create Order
```http
POST /orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ],
  "shippingAddress": "123 Main St",
  "paymentMethod": "Credit Card",
  "prescriptionId": 1
}
```

#### Get User Orders
```http
GET /orders/{userId}
Authorization: Bearer <token>
```

#### Get All Orders (Admin/Pharmacist)
```http
GET /orders
Authorization: Bearer <admin-token>
```

#### Update Order Status
```http
PUT /orders/{id}/status
Authorization: Bearer <admin-token>
Content-Type: application/json

{
  "status": "CONFIRMED"
}
```

#### Cancel Order
```http
DELETE /orders/{id}
Authorization: Bearer <token>
```

### Payment Endpoints

#### Initiate Payment
```http
POST /payments/initiate
Authorization: Bearer <token>
Content-Type: application/json

{
  "orderId": 1,
  "paymentMethod": "Credit Card",
  "amount": 50.99
}
```

#### Verify Payment
```http
POST /payments/verify
Authorization: Bearer <token>
Content-Type: application/json

{
  "paymentId": 1,
  "transactionId": "TXN-12345"
}
```

#### Get Payment History
```http
GET /payments/history?userId=1
Authorization: Bearer <token>
```

### Notification Endpoints

#### Get User Notifications
```http
GET /notifications/{userId}
Authorization: Bearer <token>
```

#### Get Unread Notifications
```http
GET /notifications/{userId}/unread
Authorization: Bearer <token>
```

#### Mark as Read
```http
PUT /notifications/{id}/read
Authorization: Bearer <token>
```

### Review Endpoints

#### Submit Review
```http
POST /reviews
Authorization: Bearer <token>
Content-Type: application/json

{
  "productId": 1,
  "rating": 5,
  "comment": "Great product!"
}
```

#### Get Product Reviews
```http
GET /reviews/{productId}
```

### Support Endpoints

#### Create Support Ticket
```http
POST /support/ticket
Authorization: Bearer <token>
Content-Type: application/json

{
  "subject": "Issue with order",
  "description": "Detailed description",
  "priority": "HIGH"
}
```

#### Get Ticket
```http
GET /support/ticket/{id}
Authorization: Bearer <token>
```

#### Chat with Pharmacist
```http
POST /chat
Authorization: Bearer <token>
Content-Type: application/json

{
  "message": "I need help with my prescription"
}
```

### Analytics Endpoints (Admin/Pharmacist Only)

#### Sales Report
```http
GET /reports/sales
Authorization: Bearer <admin-token>
```

#### Inventory Report
```http
GET /reports/inventory
Authorization: Bearer <admin-token>
```

#### Users Report
```http
GET /reports/users
Authorization: Bearer <admin-token>
```

## 🗄️ Database

The application uses H2 in-memory database for development.

### H2 Console Access
```
URL: http://localhost:8545/pharmacy-api/h2-console
JDBC URL: jdbc:h2:mem:pharmacydb
Username: sa
Password: (leave empty)
```

## 📁 Project Structure

```
src/main/java/com/pharmacy/api/
├── model/              # Entity classes
├── repository/         # JPA repositories
├── service/           # Business logic
├── controller/        # REST controllers
├── dto/              # Data transfer objects
├── security/         # Security configuration
├── config/           # Application configuration
└── exception/        # Exception handlers

src/main/resources/
└── application.properties
```

## 🔧 Configuration

Key configurations in `application.properties`:

```properties
# Server
server.port=8545
server.servlet.context-path=/pharmacy-api

# Database
spring.datasource.url=jdbc:h2:mem:pharmacydb

# JWT
jwt.secret=MySecretKeyForJWTTokenGenerationPharmacyAPI2024
jwt.expiration=86400000

# File Upload
spring.servlet.multipart.max-file-size=10MB
```

## 🧪 Testing

Run tests with:
```bash
mvn test
```

## 📝 Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { }
}
```

Error responses:
```json
{
  "success": false,
  "message": "Error message",
  "data": null
}
```

## 🔐 Security Notes

- JWT tokens expire after 24 hours
- Passwords are encrypted using BCrypt
- Role-based access control is enforced
- CORS is enabled for all origins (configure for production)

## 🚀 Deployment

For production deployment:

1. Update `application.properties` with production database
2. Change JWT secret to a secure random string
3. Configure CORS for specific origins
4. Enable HTTPS
5. Set appropriate file upload limits

## 📄 License

This project is licensed under the MIT License.

## 👥 Support

For issues and questions:
- Create an issue in the repository
- Contact: shadyahmed9719@gmail.com

---

# 📍 Pharmacy API - Artifacts Location Guide

## 🗂️ All Created Artifacts

I've created **9 separate artifacts** containing all the code for your pharmacy API. Here's where to find each component:

---

### **Artifact 1: pom.xml**
📦 **Artifact ID:** `pharmacy_pom_xml`
- Maven configuration file
- All dependencies (Spring Boot, Security, JWT, H2, Lombok, etc.)
- Build configuration

**Location in project:** `pharmacy-api/pom.xml` (root)

---

### **Artifact 2: application.properties**
⚙️ **Artifact ID:** `pharmacy_application_properties`
- Server configuration (port 8545)
- Database configuration (H2)
- JWT settings
- File upload limits
- Logging configuration

**Location in project:** `pharmacy-api/src/main/resources/application.properties`

---

### **Artifact 3: Main Application & Models**
🏗️ **Artifact ID:** `pharmacy_complete_project`
Contains:
- `PharmacyApiApplication.java` (Main class)
- **All 10 Model/Entity classes:**
    - User.java
    - Product.java
    - Prescription.java
    - Order.java
    - OrderItem.java
    - Payment.java
    - Review.java
    - Notification.java
    - SupportTicket.java
- **All 14 DTO classes:**
    - RegisterRequest, LoginRequest, AuthResponse
    - UpdateProfileRequest
    - ProductRequest, StockUpdateRequest
    - OrderRequest, OrderItemRequest, OrderStatusRequest
    - PaymentRequest, PaymentVerificationRequest
    - ReviewRequest
    - SupportTicketRequest
    - ApiResponse

**Location in project:**
- `src/main/java/com/pharmacy/api/PharmacyApiApplication.java`
- `src/main/java/com/pharmacy/api/model/`
- `src/main/java/com/pharmacy/api/dto/`

---

### **Artifact 4: Repositories & Services (Part 1)**
💾 **Artifact ID:** `pharmacy_api_part2`
Contains:
- **All 9 Repository interfaces:**
    - UserRepository
    - ProductRepository
    - PrescriptionRepository
    - OrderRepository
    - OrderItemRepository
    - PaymentRepository
    - ReviewRepository
    - NotificationRepository
    - SupportTicketRepository
- **Service classes (partial):**
    - AuthService
    - ProductService
    - PrescriptionService
    - OrderService (partial)

**Location in project:**
- `src/main/java/com/pharmacy/api/repository/`
- `src/main/java/com/pharmacy/api/service/`

---

### **Artifact 5: Additional Services**
🔧 **Artifact ID:** `pharmacy_api_part3`
Contains:
- **Remaining Service classes:**
    - PaymentService
    - NotificationService
    - ReviewService
    - SupportService
    - AnalyticsService

**Location in project:** `src/main/java/com/pharmacy/api/service/`

---

### **Artifact 6: Security Configuration**
🔒 **Artifact ID:** `pharmacy_api_security`
Contains:
- **Security classes:**
    - JwtTokenProvider
    - JwtAuthenticationFilter
    - CustomUserDetailsService
- **Config classes:**
    - SecurityConfig
    - CorsConfig
- **Exception handling:**
    - GlobalExceptionHandler

**Location in project:**
- `src/main/java/com/pharmacy/api/security/`
- `src/main/java/com/pharmacy/api/config/`
- `src/main/java/com/pharmacy/api/exception/`

---

### **Artifact 7: All Controllers**
🎮 **Artifact ID:** `pharmacy_api_controllers`
Contains all 10 controller classes:
- AuthController
- ProductController
- PrescriptionController
- OrderController
- PaymentController
- NotificationController
- ReviewController
- SupportController
- ChatController
- AnalyticsController

**Location in project:** `src/main/java/com/pharmacy/api/controller/`

---

### **Artifact 8: Data Initialization & README**
🌱 **Artifact ID:** `pharmacy_api_init`
Contains:
- DataInitializer.java (creates sample data)
- Complete README.md with full API documentation

**Location in project:**
- `src/main/java/com/pharmacy/api/config/DataInitializer.java`
- `README.md` (root)

---

### **Artifact 9: Complete Project Structure**
📁 **Artifact ID:** `pharmacy_project_structure`
- Detailed file structure diagram
- Complete class inventory
- Verification checklist
- API endpoint coverage

**This is documentation only** - not source code

---

## 🚀 How to Set Up Your Project

### Step 1: Create Project Structure
```bash
mkdir -p pharmacy-api/src/main/java/com/pharmacy/api
mkdir -p pharmacy-api/src/main/resources
mkdir -p pharmacy-api/src/test/java/com/pharmacy/api
```

### Step 2: Create Package Directories
```bash
cd pharmacy-api/src/main/java/com/pharmacy/api
mkdir model repository service controller dto security config exception
```

### Step 3: Copy Files from Artifacts

**Root directory files:**
1. Copy `pom.xml` from Artifact 1
2. Copy `README.md` from Artifact 8

**src/main/resources:**
3. Copy `application.properties` from Artifact 2

**src/main/java/com/pharmacy/api:**
4. Copy `PharmacyApiApplication.java` from Artifact 3

**model/ directory:**
5. Copy all entity classes from Artifact 3

**dto/ directory:**
6. Copy all DTO classes from Artifact 3

**repository/ directory:**
7. Copy all repository interfaces from Artifact 4

**service/ directory:**
8. Copy service classes from Artifacts 4 & 5

**controller/ directory:**
9. Copy all controller classes from Artifact 7

**security/ directory:**
10. Copy security classes from Artifact 6

**config/ directory:**
11. Copy config classes from Artifacts 6 & 8

**exception/ directory:**
12. Copy GlobalExceptionHandler from Artifact 6

### Step 4: Build and Run
```bash
cd pharmacy-api
mvn clean install
mvn spring-boot:run
```

---

## 📊 Quick Reference Table

| Component | Artifact ID | File Count | Location |
|-----------|-------------|------------|----------|
| Build Config | pharmacy_pom_xml | 1 | Root |
| App Config | pharmacy_application_properties | 1 | resources/ |
| Main + Models | pharmacy_complete_project | 25 | api/, model/, dto/ |
| Repositories | pharmacy_api_part2 | 9 | repository/ |
| Services 1-4 | pharmacy_api_part2 | 4 | service/ |
| Services 5-9 | pharmacy_api_part3 | 5 | service/ |
| Security | pharmacy_api_security | 6 | security/, config/, exception/ |
| Controllers | pharmacy_api_controllers | 10 | controller/ |
| Data Init | pharmacy_api_init | 2 | config/, root |
| Documentation | pharmacy_project_structure | 1 | Reference only |

**Total: 64 files across 9 artifacts**

---

## ✅ Verification Checklist

After setting up, verify you have:

- [ ] `pom.xml` in root
- [ ] `application.properties` in src/main/resources
- [ ] `PharmacyApiApplication.java` in src/main/java/com/pharmacy/api
- [ ] 10 files in model/ directory
- [ ] 14 files in dto/ directory
- [ ] 9 files in repository/ directory
- [ ] 9 files in service/ directory
- [ ] 10 files in controller/ directory
- [ ] 3 files in security/ directory
- [ ] 3 files in config/ directory
- [ ] 1 file in exception/ directory
- [ ] README.md in root

**Total: 73 files (including pom.xml, properties, README)**

---
### How to View diagrams
1. **Standalone Mermaid files:** `.mmd`
    - Can be opened in Mermaid Live Editor
    - URL: https://mermaid.live

2. **Export Options:**
    - **PNG** - Raster image
    - **SVG** - Vector image (scalable)
    - **PDF** - Document format

### **Tools to View/Edit:**

1. **Online:**
    - Mermaid Live Editor: https://mermaid.live
    - GitHub/GitLab (renders automatically in `.md` files)

2. **IDE Plugins:**
    - VS Code: "Mermaid Preview" extension
    - IntelliJ IDEA: Mermaid plugin
    - Obsidian: Native support

3. **Documentation:**
    - Confluence (with Mermaid plugin)
    - Notion (with Mermaid blocks)
    - GitBook (native support)

### **To Save:**
Just copy the Mermaid code from the artifacts and save as:
- `pharmacy-workflow.mmd`
- `pharmacy-sequence.mmd`
- `pharmacy-modules.mmd`
- `pharmacy-usecases.mmd`


## Delta Pharmacy API - Complete Workflow Diagram
```mermaid
   graph TD
    Start([🏁 Start]) --> UserReg[👤 User Registration]
    
    UserReg --> Login[🔐 Login & Get JWT Token]
    Login --> Dashboard{User Role?}
    
    Dashboard -->|Customer| CustomerFlow[👨‍💼 Customer Dashboard]
    Dashboard -->|Pharmacist| PharmacistFlow[💊 Pharmacist Dashboard]
    Dashboard -->|Admin| AdminFlow[⚙️ Admin Dashboard]
    
    %% Customer Flow
    CustomerFlow --> BrowseProducts[🔍 Browse Products]
    BrowseProducts --> SearchFilter[🔎 Search & Filter Medicines]
    SearchFilter --> ViewProduct[📋 View Product Details]
    ViewProduct --> CheckReview[⭐ Check Reviews & Ratings]
    
    CheckReview --> PrescCheck{Prescription<br/>Required?}
    PrescCheck -->|Yes| UploadPresc[📤 Upload Prescription]
    PrescCheck -->|No| AddCart[🛒 Add to Cart]
    
    UploadPresc --> WaitApproval[⏳ Wait for Approval]
    WaitApproval --> CheckStatus[📱 Check Notification]
    CheckStatus --> PrescApproved{Approved?}
    
    PrescApproved -->|Yes| AddCart
    PrescApproved -->|No| ContactSupport[💬 Contact Support]
    
    AddCart --> CreateOrder[📦 Create Order]
    CreateOrder --> StockCheck{Stock<br/>Available?}
    
    StockCheck -->|No| OutOfStock[❌ Out of Stock Notification]
    StockCheck -->|Yes| InitPayment[💳 Initiate Payment]
    
    InitPayment --> PaymentGateway[🏦 Payment Gateway Mock]
    PaymentGateway --> VerifyPayment[✅ Verify Payment]
    VerifyPayment --> PaymentSuccess{Payment<br/>Success?}
    
    PaymentSuccess -->|No| PaymentFailed[❌ Payment Failed]
    PaymentFailed --> RetryPayment{Retry?}
    RetryPayment -->|Yes| InitPayment
    RetryPayment -->|No| CancelOrder[🚫 Cancel Order]
    
    PaymentSuccess -->|Yes| OrderConfirmed[✅ Order Confirmed]
    OrderConfirmed --> UpdateStock[📊 Update Inventory]
    UpdateStock --> OrderNotif[📧 Order Confirmation Email]
    
    OrderNotif --> TrackOrder[📍 Track Order Status]
    TrackOrder --> OrderStatus{Order Status}
    
    OrderStatus -->|Processing| WaitShip[⏳ Wait for Shipment]
    OrderStatus -->|Shipped| InTransit[🚚 In Transit]
    OrderStatus -->|Delivered| OrderComplete[✅ Order Delivered]
    
    OrderComplete --> LeaveReview[⭐ Leave Review & Rating]
    LeaveReview --> ViewHistory[📜 View Order History]
    
    ContactSupport --> CreateTicket[🎫 Create Support Ticket]
    CreateTicket --> ChatBot[🤖 Chat with Pharmacist]
    ChatBot --> TicketResolved[✅ Issue Resolved]
    
    %% Pharmacist Flow
    PharmacistFlow --> ViewPrescriptions[📋 View Pending Prescriptions]
    ViewPrescriptions --> ReviewPresc[🔍 Review Prescription]
    ReviewPresc --> ValidatePresc{Valid<br/>Prescription?}
    
    ValidatePresc -->|Yes| ApprovePresc[✅ Approve Prescription]
    ValidatePresc -->|No| RejectPresc[❌ Reject with Reason]
    
    ApprovePresc --> NotifyCustomer1[📧 Notify Customer]
    RejectPresc --> NotifyCustomer1
    
    NotifyCustomer1 --> ViewOrders[📦 View All Orders]
    ViewOrders --> UpdateOrderStatus[📝 Update Order Status]
    UpdateOrderStatus --> ViewTickets[🎫 View Support Tickets]
    ViewTickets --> RespondTicket[💬 Respond to Tickets]
    RespondTicket --> ViewReports1[📊 View Reports]
    
    %% Admin Flow
    AdminFlow --> ManageProducts[🏥 Manage Products]
    ManageProducts --> AddProduct[➕ Add New Product]
    ManageProducts --> UpdateProduct[✏️ Update Product]
    ManageProducts --> DeleteProduct[🗑️ Delete Product]
    
    AddProduct --> ManageInventory[📦 Manage Inventory]
    UpdateProduct --> ManageInventory
    DeleteProduct --> ManageInventory
    
    ManageInventory --> CheckStockLevels[📊 Check Stock Levels]
    CheckStockLevels --> LowStock{Low Stock<br/>Alert?}
    
    LowStock -->|Yes| ReorderAlert[🔔 Reorder Alert]
    LowStock -->|No| UpdateStockQty[➕➖ Update Stock Quantity]
    
    ReorderAlert --> UpdateStockQty
    UpdateStockQty --> ViewAllOrders[📦 View All Orders]
    
    ViewAllOrders --> ManageOrderStatus[📝 Manage Order Statuses]
    ManageOrderStatus --> ViewPayments[💰 View Payment History]
    ViewPayments --> ViewAnalytics[📈 View Analytics & Reports]
    
    ViewAnalytics --> SalesReport[💵 Sales Report]
    ViewAnalytics --> InventoryReport[📦 Inventory Report]
    ViewAnalytics --> UsersReport[👥 Users Report]
    
    SalesReport --> ManageUsers[👤 Manage Users]
    InventoryReport --> ManageUsers
    UsersReport --> ManageUsers
    
    ManageUsers --> AssignRoles[🎭 Assign Roles]
    AssignRoles --> ViewReports1
    
    ViewReports1 --> End([🏁 End])
    ViewHistory --> End
    TicketResolved --> End
    CancelOrder --> End
    OutOfStock --> End
    
    style Start fill:#4CAF50,stroke:#333,stroke-width:4px,color:#fff
    style End fill:#F44336,stroke:#333,stroke-width:4px,color:#fff
    style Login fill:#2196F3,stroke:#333,stroke-width:2px,color:#fff
    style OrderConfirmed fill:#4CAF50,stroke:#333,stroke-width:2px,color:#fff
    style PaymentFailed fill:#F44336,stroke:#333,stroke-width:2px,color:#fff
    style ApprovePresc fill:#4CAF50,stroke:#333,stroke-width:2px,color:#fff
    style RejectPresc fill:#F44336,stroke:#333,stroke-width:2px,color:#fff
    style CustomerFlow fill:#E3F2FD,stroke:#2196F3,stroke-width:2px
    style PharmacistFlow fill:#F3E5F5,stroke:#9C27B0,stroke-width:2px
    style AdminFlow fill:#FFF3E0,stroke:#FF9800,stroke-width:2px
```

## Delta Pharmacy API - Sequence Diagram
```mermaid
sequenceDiagram
    participant C as 👤 Customer
    participant UI as 🖥️ Frontend
    participant API as 🔌 API Gateway
    participant Auth as 🔐 Auth Service
    participant Prod as 🏥 Product Service
    participant Presc as 📋 Prescription Service
    participant Order as 📦 Order Service
    participant Pay as 💳 Payment Service
    participant Notif as 📧 Notification Service
    participant DB as 💾 Database
    
    Note over C,DB: 1️⃣ USER REGISTRATION & LOGIN
    C->>UI: Register Account
    UI->>API: POST /api/auth/register
    API->>Auth: Register User
    Auth->>DB: Save User
    DB-->>Auth: User Saved
    Auth-->>API: JWT Token
    API-->>UI: Auth Response
    UI-->>C: Welcome! You're Registered
    
    C->>UI: Login
    UI->>API: POST /api/auth/login
    API->>Auth: Validate Credentials
    Auth->>DB: Query User
    DB-->>Auth: User Details
    Auth-->>API: JWT Token
    API-->>UI: Token & User Info
    UI-->>C: Login Success
    
    Note over C,DB: 2️⃣ BROWSE & SEARCH PRODUCTS
    C->>UI: Browse Medicines
    UI->>API: GET /api/products
    API->>Prod: Get All Products
    Prod->>DB: Query Products
    DB-->>Prod: Product List
    Prod-->>API: Products with Stock
    API-->>UI: Products JSON
    UI-->>C: Display Products
    
    C->>UI: Search "Paracetamol"
    UI->>API: GET /api/search?query=paracetamol
    API->>Prod: Search Products
    Prod->>DB: Search Query
    DB-->>Prod: Matching Products
    Prod-->>API: Search Results
    API-->>UI: Filtered Products
    UI-->>C: Show Search Results
    
    Note over C,DB: 3️⃣ PRESCRIPTION UPLOAD & APPROVAL
    C->>UI: Select Rx Medicine
    UI-->>C: Prescription Required!
    C->>UI: Upload Prescription
    UI->>API: POST /api/prescriptions/upload
    API->>Presc: Save Prescription
    Presc->>DB: Store Prescription
    DB-->>Presc: Saved
    Presc->>Notif: Create Notification
    Notif->>DB: Save Notification
    Notif-->>Presc: Notification Sent
    Presc-->>API: Prescription Pending
    API-->>UI: Upload Success
    UI-->>C: Prescription Under Review
    
    Note over C,DB: 4️⃣ PHARMACIST REVIEWS PRESCRIPTION
    participant P as 💊 Pharmacist
    P->>UI: Login as Pharmacist
    P->>UI: View Pending Prescriptions
    UI->>API: GET /api/prescriptions/pending
    API->>Presc: Get Pending
    Presc->>DB: Query Pending
    DB-->>Presc: Pending List
    Presc-->>API: Prescriptions
    API-->>UI: Show List
    UI-->>P: Display Prescriptions
    
    P->>UI: Approve Prescription
    UI->>API: PUT /api/prescriptions/{id}/approve
    API->>Presc: Approve Prescription
    Presc->>DB: Update Status
    DB-->>Presc: Updated
    Presc->>Notif: Notify Customer
    Notif->>DB: Save Notification
    Notif-->>C: 📧 Prescription Approved!
    Presc-->>API: Approved
    API-->>UI: Success
    UI-->>P: Prescription Approved
    
    Note over C,DB: 5️⃣ CREATE ORDER
    C->>UI: Add to Cart
    C->>UI: Proceed to Checkout
    UI->>API: POST /api/orders
    API->>Order: Create Order
    Order->>DB: Check Stock
    DB-->>Order: Stock Available
    Order->>DB: Reduce Stock
    Order->>DB: Save Order
    DB-->>Order: Order Created
    Order->>Notif: Order Notification
    Notif-->>C: 📧 Order Created
    Order-->>API: Order Details
    API-->>UI: Order ID #123
    UI-->>C: Order Created Successfully
    
    Note over C,DB: 6️⃣ PAYMENT PROCESSING
    C->>UI: Make Payment
    UI->>API: POST /api/payments/initiate
    API->>Pay: Initiate Payment
    Pay->>DB: Create Payment Record
    DB-->>Pay: Payment Pending
    Pay-->>API: Transaction ID
    API-->>UI: Payment Initiated
    UI-->>C: Redirect to Payment
    
    C->>UI: Complete Payment
    UI->>API: POST /api/payments/verify
    API->>Pay: Verify Payment
    Pay->>Pay: Mock Gateway Check
    Pay->>DB: Update Payment Status
    DB-->>Pay: Payment Completed
    Pay->>Order: Update Order Status
    Order->>DB: Order Confirmed
    Pay->>Notif: Payment Success
    Notif-->>C: 📧 Payment Successful
    Pay-->>API: Payment Verified
    API-->>UI: Payment Success
    UI-->>C: ✅ Order Confirmed!
    
    Note over C,DB: 7️⃣ ORDER TRACKING & DELIVERY
    C->>UI: Track Order
    UI->>API: GET /api/orders/{userId}
    API->>Order: Get User Orders
    Order->>DB: Query Orders
    DB-->>Order: Order List
    Order-->>API: Orders with Status
    API-->>UI: Order History
    UI-->>C: Show Order Status
    
    participant A as ⚙️ Admin
    A->>UI: Login as Admin
    A->>UI: Update Order Status
    UI->>API: PUT /api/orders/{id}/status
    API->>Order: Update Status
    Order->>DB: Update Order
    DB-->>Order: Updated
    Order->>Notif: Status Change
    Notif-->>C: 📧 Order Shipped!
    Order-->>API: Status Updated
    API-->>UI: Success
    UI-->>A: Order Updated
    
    Note over C,DB: 8️⃣ LEAVE REVIEW & SUPPORT
    C->>UI: Leave Review
    UI->>API: POST /api/reviews
    API->>Prod: Save Review
    Prod->>DB: Store Review
    DB-->>Prod: Saved
    Prod-->>API: Review Added
    API-->>UI: Success
    UI-->>C: Thank You!
    
    C->>UI: Need Help?
    UI->>API: POST /api/support/ticket
    API->>Notif: Create Ticket
    Notif->>DB: Save Ticket
    DB-->>Notif: Ticket Created
    Notif-->>API: Ticket #456
    API-->>UI: Ticket Created
    UI-->>C: We'll Contact You Soon
    
    Note over C,DB: 9️⃣ ANALYTICS & REPORTS (ADMIN)
    A->>UI: View Dashboard
    UI->>API: GET /api/reports/sales
    API->>Order: Generate Sales Report
    Order->>DB: Aggregate Sales Data
    DB-->>Order: Sales Stats
    Order-->>API: Sales Report
    
    UI->>API: GET /api/reports/inventory
    API->>Prod: Generate Inventory Report
    Prod->>DB: Query Stock Levels
    DB-->>Prod: Inventory Stats
    Prod-->>API: Inventory Report
    
    UI->>API: GET /api/reports/users
    API->>Auth: Generate Users Report
    Auth->>DB: Query Users
    DB-->>Auth: User Stats
    Auth-->>API: Users Report
    
    API-->>UI: All Reports
    UI-->>A: Display Analytics Dashboard
```

## Delta Pharmacy API - Module Interaction Diagram
```mermaid
graph TB
    subgraph Frontend["🖥️ FRONTEND APPLICATION"]
        WebApp[Web Application]
        MobileApp[Mobile App]
    end
    
    subgraph APIGateway["🔌 API GATEWAY"]
        RestAPI[REST API Endpoints]
        Swagger[Swagger Documentation]
    end
    
    subgraph Security["🔐 SECURITY LAYER"]
        JWT[JWT Token Provider]
        AuthFilter[Authentication Filter]
        UserDetails[User Details Service]
    end
    
    subgraph Controllers["🎮 CONTROLLERS LAYER"]
        AuthCtrl[Auth Controller]
        ProdCtrl[Product Controller]
        PrescCtrl[Prescription Controller]
        OrderCtrl[Order Controller]
        PayCtrl[Payment Controller]
        NotifCtrl[Notification Controller]
        ReviewCtrl[Review Controller]
        SupportCtrl[Support Controller]
        AnalyticsCtrl[Analytics Controller]
    end
    
    subgraph Services["⚙️ BUSINESS LOGIC LAYER"]
        AuthSvc[Auth Service]
        ProdSvc[Product Service]
        PrescSvc[Prescription Service]
        OrderSvc[Order Service]
        PaySvc[Payment Service]
        NotifSvc[Notification Service]
        ReviewSvc[Review Service]
        SupportSvc[Support Service]
        AnalyticsSvc[Analytics Service]
    end
    
    subgraph Repositories["💾 DATA ACCESS LAYER"]
        UserRepo[(User Repository)]
        ProdRepo[(Product Repository)]
        PrescRepo[(Prescription Repository)]
        OrderRepo[(Order Repository)]
        PayRepo[(Payment Repository)]
        NotifRepo[(Notification Repository)]
        ReviewRepo[(Review Repository)]
        TicketRepo[(Support Ticket Repository)]
    end
    
    subgraph Database["🗄️ DATABASE"]
        H2DB[(H2 In-Memory Database)]
    end
    
    subgraph ExternalMock["🔌 MOCK INTEGRATIONS"]
        PaymentGW[Payment Gateway Mock]
        EmailSvc[Email Service Mock]
        SMSSvc[SMS Service Mock]
        PushNotif[Push Notification Mock]
        FileStorage[File Storage Mock]
    end
    
    %% Frontend to API
    WebApp --> RestAPI
    MobileApp --> RestAPI
    
    %% API to Security
    RestAPI --> AuthFilter
    AuthFilter --> JWT
    AuthFilter --> UserDetails
    
    %% Security to Controllers
    JWT --> AuthCtrl
    AuthFilter --> ProdCtrl
    AuthFilter --> PrescCtrl
    AuthFilter --> OrderCtrl
    AuthFilter --> PayCtrl
    AuthFilter --> NotifCtrl
    AuthFilter --> ReviewCtrl
    AuthFilter --> SupportCtrl
    AuthFilter --> AnalyticsCtrl
    
    %% Controllers to Services
    AuthCtrl --> AuthSvc
    ProdCtrl --> ProdSvc
    PrescCtrl --> PrescSvc
    OrderCtrl --> OrderSvc
    PayCtrl --> PaySvc
    NotifCtrl --> NotifSvc
    ReviewCtrl --> ReviewSvc
    SupportCtrl --> SupportSvc
    AnalyticsCtrl --> AnalyticsSvc
    
    %% Services to Services (Cross-Module Communication)
    PrescSvc -.->|Notify User| NotifSvc
    OrderSvc -.->|Check Stock| ProdSvc
    OrderSvc -.->|Validate Prescription| PrescSvc
    OrderSvc -.->|Notify User| NotifSvc
    PaySvc -.->|Update Order| OrderSvc
    PaySvc -.->|Notify User| NotifSvc
    AuthSvc -.->|Get User| UserRepo
    
    %% Services to Repositories
    AuthSvc --> UserRepo
    ProdSvc --> ProdRepo
    PrescSvc --> PrescRepo
    PrescSvc --> UserRepo
    OrderSvc --> OrderRepo
    OrderSvc --> ProdRepo
    PaySvc --> PayRepo
    PaySvc --> OrderRepo
    NotifSvc --> NotifRepo
    ReviewSvc --> ReviewRepo
    ReviewSvc --> ProdRepo
    SupportSvc --> TicketRepo
    AnalyticsSvc --> OrderRepo
    AnalyticsSvc --> ProdRepo
    AnalyticsSvc --> UserRepo
    
    %% Repositories to Database
    UserRepo --> H2DB
    ProdRepo --> H2DB
    PrescRepo --> H2DB
    OrderRepo --> H2DB
    PayRepo --> H2DB
    NotifRepo --> H2DB
    ReviewRepo --> H2DB
    TicketRepo --> H2DB
    
    %% Services to Mock External Services
    PaySvc -.->|Process Payment| PaymentGW
    NotifSvc -.->|Send Email| EmailSvc
    NotifSvc -.->|Send SMS| SMSSvc
    NotifSvc -.->|Push Notification| PushNotif
    PrescSvc -.->|Store File| FileStorage
    
    %% Swagger
    RestAPI --> Swagger
    
    %% Styling
    classDef frontend fill:#E3F2FD,stroke:#2196F3,stroke-width:2px
    classDef security fill:#FFF3E0,stroke:#FF9800,stroke-width:2px
    classDef controller fill:#F3E5F5,stroke:#9C27B0,stroke-width:2px
    classDef service fill:#E8F5E9,stroke:#4CAF50,stroke-width:2px
    classDef repository fill:#FCE4EC,stroke:#E91E63,stroke-width:2px
    classDef database fill:#FFEBEE,stroke:#F44336,stroke-width:3px
    classDef mock fill:#FFF9C4,stroke:#FBC02D,stroke-width:2px
    
    class WebApp,MobileApp frontend
    class JWT,AuthFilter,UserDetails security
    class AuthCtrl,ProdCtrl,PrescCtrl,OrderCtrl,PayCtrl,NotifCtrl,ReviewCtrl,SupportCtrl,AnalyticsCtrl controller
    class AuthSvc,ProdSvc,PrescSvc,OrderSvc,PaySvc,NotifSvc,ReviewSvc,SupportSvc,AnalyticsSvc service
    class UserRepo,ProdRepo,PrescRepo,OrderRepo,PayRepo,NotifRepo,ReviewRepo,TicketRepo repository
    class H2DB database
    class PaymentGW,EmailSvc,SMSSvc,PushNotif,FileStorage mock
```

## Delta Pharmacy API - Complete Use Cases
```mermaid
graph LR
    subgraph Actors["👥 ACTORS"]
        Customer([👤 Customer])
        Pharmacist([💊 Pharmacist])
        Admin([⚙️ Admin])
        Guest([👁️ Guest])
    end
    
    subgraph Authentication["🔐 AUTHENTICATION & PROFILE"]
        UC1[Register Account]
        UC2[Login]
        UC3[Logout]
        UC4[View Profile]
        UC5[Update Profile]
        UC6[Change Password]
    end
    
    subgraph ProductManagement["🏥 PRODUCT MANAGEMENT"]
        UC7[Browse Products]
        UC8[Search Products]
        UC9[View Product Details]
        UC10[Filter by Category]
        UC11[Add Product]
        UC12[Update Product]
        UC13[Delete Product]
        UC14[View Reviews]
    end
    
    subgraph InventoryManagement["📦 INVENTORY MANAGEMENT"]
        UC15[View Stock Levels]
        UC16[Update Stock]
        UC17[Low Stock Alerts]
        UC18[Inventory Report]
    end
    
    subgraph PrescriptionManagement["📋 PRESCRIPTION MANAGEMENT"]
        UC19[Upload Prescription]
        UC20[View My Prescriptions]
        UC21[View Pending Prescriptions]
        UC22[Approve Prescription]
        UC23[Reject Prescription]
        UC24[Check Prescription Status]
    end
    
    subgraph OrderManagement["🛒 ORDER MANAGEMENT"]
        UC25[Add to Cart]
        UC26[Create Order]
        UC27[View My Orders]
        UC28[Track Order]
        UC29[Cancel Order]
        UC30[View All Orders]
        UC31[Update Order Status]
    end
    
    subgraph PaymentManagement["💳 PAYMENT MANAGEMENT"]
        UC32[Initiate Payment]
        UC33[Complete Payment]
        UC34[Verify Payment]
        UC35[View Payment History]
        UC36[Payment Failed Retry]
    end
    
    subgraph NotificationSystem["📧 NOTIFICATION SYSTEM"]
        UC37[View Notifications]
        UC38[Mark as Read]
        UC39[Order Notifications]
        UC40[Prescription Notifications]
        UC41[Payment Notifications]
    end
    
    subgraph ReviewSystem["⭐ REVIEW & RATING"]
        UC42[Leave Review]
        UC43[Rate Product]
        UC44[View Product Reviews]
        UC45[Calculate Average Rating]
    end
    
    subgraph SupportSystem["💬 CUSTOMER SUPPORT"]
        UC46[Create Support Ticket]
        UC47[View My Tickets]
        UC48[Chat with Pharmacist]
        UC49[View All Tickets]
        UC50[Respond to Ticket]
        UC51[Close Ticket]
    end
    
    subgraph AnalyticsReporting["📊 ANALYTICS & REPORTING"]
        UC52[Sales Report]
        UC53[Revenue Analysis]
        UC54[Inventory Report]
        UC55[Users Report]
        UC56[Order Statistics]
        UC57[Product Performance]
    end
    
    subgraph UserManagement["👥 USER MANAGEMENT"]
        UC58[View All Users]
        UC59[Assign Roles]
        UC60[Deactivate User]
        UC61[User Activity Log]
    end
    
    %% Guest Interactions
    Guest --> UC7
    Guest --> UC8
    Guest --> UC9
    Guest --> UC10
    Guest --> UC14
    Guest --> UC44
    Guest --> UC1
    Guest --> UC2
    
    %% Customer Interactions
    Customer --> UC2
    Customer --> UC3
    Customer --> UC4
    Customer --> UC5
    Customer --> UC6
    Customer --> UC7
    Customer --> UC8
    Customer --> UC9
    Customer --> UC10
    Customer --> UC14
    Customer --> UC19
    Customer --> UC20
    Customer --> UC24
    Customer --> UC25
    Customer --> UC26
    Customer --> UC27
    Customer --> UC28
    Customer --> UC29
    Customer --> UC32
    Customer --> UC33
    Customer --> UC35
    Customer --> UC36
    Customer --> UC37
    Customer --> UC38
    Customer --> UC42
    Customer --> UC43
    Customer --> UC46
    Customer --> UC47
    Customer --> UC48
    
    %% Pharmacist Interactions
    Pharmacist --> UC2
    Pharmacist --> UC3
    Pharmacist --> UC4
    Pharmacist --> UC5
    Pharmacist --> UC7
    Pharmacist --> UC8
    Pharmacist --> UC9
    Pharmacist --> UC15
    Pharmacist --> UC21
    Pharmacist --> UC22
    Pharmacist --> UC23
    Pharmacist --> UC30
    Pharmacist --> UC31
    Pharmacist --> UC49
    Pharmacist --> UC50
    Pharmacist --> UC51
    Pharmacist --> UC52
    Pharmacist --> UC53
    Pharmacist --> UC54
    
    %% Admin Interactions
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC13
    Admin --> UC14
    Admin --> UC15
    Admin --> UC16
    Admin --> UC17
    Admin --> UC18
    Admin --> UC21
    Admin --> UC22
    Admin --> UC23
    Admin --> UC30
    Admin --> UC31
    Admin --> UC49
    Admin --> UC50
    Admin --> UC51
    Admin --> UC52
    Admin --> UC53
    Admin --> UC54
    Admin --> UC55
    Admin --> UC56
    Admin --> UC57
    Admin --> UC58
    Admin --> UC59
    Admin --> UC60
    Admin --> UC61
    
    %% Styling
    classDef actor fill:#FFE082,stroke:#F57C00,stroke-width:3px
    classDef usecase fill:#B3E5FC,stroke:#0277BD,stroke-width:2px
    classDef critical fill:#FFCDD2,stroke:#C62828,stroke-width:2px
    classDef admin fill:#D1C4E9,stroke:#512DA8,stroke-width:2px
    
    class Customer,Pharmacist,Admin,Guest actor
    class UC1,UC2,UC26,UC32,UC22,UC31 critical
    class UC11,UC12,UC13,UC16,UC59,UC60 admin
```
## 🆘 Need Help?
https://www.linkedin.com/in/shady-ahmed97/

Made with ❤️ using Spring Boot