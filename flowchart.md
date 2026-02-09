

🏠 Room Management System

- User Authentication
   ===================
- Secure login for Admins and Tenants
- Role-based access control 
- Admin Features
  ==============
🛏️ Room Management
- Add / Edit / Remove rooms
- Update room status
👥 Tenant Management
- Add / Remove tenants
- Manage tenant information
💰 Financial Management
- Track payments
- Generate financial reports
- Monitor rent collection
-  Tenant Features
   ===============
🛋️ Room Information
- View room details
- Check payment history
- View transaction history
🛠️ Maintenance
- Raise maintenance requests
- Track request status
- Core Benefits
   ==============
- 📑 Digital record keeping
- 💬 Streamlined communication
- 🔍 Financial transparency

```mermaid

graph TD
    A[Start: Room Management System] --> B[User Authentication]
    B --> C{User Type?}
    C -->|Admin| D[Manage Rooms & Users]
    C -->|Tenant| E[View & Manage Own Room]

    D --> F[Add/Edit Rooms]
    D --> G[Manage Tenants]
    D --> H[Track Payments]
    D --> I[View Reports]

    E --> J[View Room Details]
    E --> K[Make Payments]
    E --> L[Raise Maintenance Requests]

    F --> M[Update Room Status]
    G --> N[Add/Remove Tenants]
    H --> O[Track Rent & Expenses]
    I --> P[Generate Financial Reports]

    J --> Q[View Payment History]
    K --> R[Pay Rent Online]
    L --> S[Track Request Status]

```
