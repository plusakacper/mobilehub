
# Mobilehub online shop

Smartphone online shop project.

Created using:
- Java 17 
- Spring Boot 3.0  
- MySQL database


## Project setup

To run this project, in the application.properties file you will need to enter the username and password for the database and the path to the folder with the product images:

`spring.datasource.username`

`spring.datasource.password`

`files.directory.path`

Also, you will need to create a MySQL database and perform the operations found in the database.sql file.

To see how the shop works, you can use the login data for the default accounts:
- user: testuser@gmail.com
- admin: mobilehub@gmail.com
The password for both accounts is: "test"
## Features

#### - Products divided into dynamically updating categories
#### - E-mail order confirmation
#### - User roles:
- Guest can view products
- Logged in user can shop and has access to their order page
- Admin has access to the management panel
#### - Shop management functionalities:
- Order and user management: view, delete
- Product management: view, delete, edit and add new products
## Screenshots

**Products list:**

![App Screenshot](https://github.com/plusakacper/mobilehub/blob/screenshots/screenshots/products_list.png)

**Order details:**

![App Screenshot](https://github.com/plusakacper/mobilehub/blob/screenshots/screenshots/order_details.png)

**Manage products:**

![App Screenshot](https://github.com/plusakacper/mobilehub/blob/screenshots/screenshots/manage_products.png)

**Add product:**

![App Screenshot](https://github.com/plusakacper/mobilehub/blob/screenshots/screenshots/add_product.png)

**Edit product:**

![App Screenshot](https://github.com/plusakacper/mobilehub/blob/screenshots/screenshots/edit_product.png)
## Author

- [@plusakacper](https://github.com/plusakacper)

