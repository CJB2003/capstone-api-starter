# Capstone 3: Grocery Store E-Commerce API

This is a Spring Boot REST API backend for an online grocery store. The API layer handles all of the logic including the product browsing, category browsing, 
user authentication, user profiles, shopping cart, and the order checkout. 

## Project Description

The starting codebase already had user registration/login, category and product browsing already wired up, 
but it was intentionally given with a few known bugs in the product search/filter logic and the product update logic that needed to be fixed. 
From there, the goal was to fix those bugs and build out several new features that had not been implemented yet: 
a shopping cart, user profile management, and order checkout.

The application follows this Spring Boot architecture:
 
- **Controllers** handle HTTP requests and routing, and enforce security rules using `@PreAuthorize`.
- **Services** contain the business logic.
- **Repositories** extends `JpaRepository` and handle the database operations.
- **Models** are the JPA entities mapped to the MySQL database tables.

## Tech Stack
 
- Java / Spring Boot
- Spring Security (JWT-based authentication, role-based authorization)
- Spring Data JPA / Hibernate
- MySQL
- Insomnia and Swagger UI (manual API testing)

## Features Implemented
 
### Categories
Utilizes all of CRUD for product categories. Reading categories is open to any user; creating, updating, and deleting a category is restricted to users with the ADMIN role only.
 
| Verb | Endpoint | Notes |
|---|---|---|
| GET | `/categories` | List all categories |
| GET | `/categories/{id}` | Get a single category |
| GET | `/categories/{id}/products` | List all products in a category |
| POST | `/categories` | Admin only |
| PUT | `/categories/{id}` | Admin only |
| DELETE | `/categories/{id}` | Admin only |
 
### Products
Product browsing and search, plus admin-only management. Product search supports the following optional query parameters: `cat` (category id), `minPrice`, `maxPrice`, and `subCategory`. These can be combined, for example `/products?cat=1&minPrice=25&maxPrice=100`.
 
| Verb | Endpoint | Notes |
|---|---|---|
| GET | `/products` | Supports filtering by category, price range, and sub-category |
| GET | `/products/{id}` | Get a single product |
| POST | `/products` | Admin only |
| PUT | `/products/{id}` | Admin only |
| DELETE | `/products/{id}` | Admin only |
 
There were two bugs in the original code which I've identified and fixed:
- The product search/filter logic was excluding products that should have matched the given criteria.
- Updating a product's stock value through the PUT endpoint returned a success response without the change actually being saved to the database.
### Shopping Cart
A persistent, per-user shopping cart. Cart contents are tied to the logged-in user's account in the database, so items remain in the cart across logins and sessions. All cart endpoints require an authenticated user.
 
| Verb | Endpoint | Notes |
|---|---|---|
| GET | `/cart` | Returns the current user's cart with full product details and a running total |
| POST | `/cart/products/{id}` | Adds a product to the cart, or increases its quantity by 1 if it is already in the cart |
| PUT | `/cart/products/{id}` | Updates the quantity of a product already in the cart |
| DELETE | `/cart` | Clears all items from the current user's cart |
 
### User Profile
Lets a logged-in user view and update their own profile information. A profile record is automatically created when a user registers.
 
| Verb | Endpoint | Notes |
|---|---|---|
| GET | `/profile` | Returns the current user's profile |
| PUT | `/profile` | Updates the current user's profile |
 
### Checkout / Orders
Converts a user's current shopping cart into a permanent order record. When an order is placed, each line item stores a snapshot of the product's price and any discount at the time of purchase, so past orders remain accurate even if a product's price changes later.
 
| Verb | Endpoint | Notes |
|---|---|---|
| POST | `/orders` | Creates an order from the current cart, creates one order line item per cart item, then clears the cart |
 
Fixed and updated by Chris
