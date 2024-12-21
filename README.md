### The project is not deployed please run it on your local machine it will take 2 mins
## Running the project in your local machine
1. Install IntelliJ IDE
2. Create a database named car_inventory in your mysql workbench
3. Create a table name product in the database
   SQL code for creating table:
   `CREATE TABLE product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    car_name VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    year INT NOT NULL CHECK (year >= 1886),
    price DOUBLE NOT NULL CHECK (price >= 0),
    color VARCHAR(255) NOT NULL,
    fuel_type VARCHAR(20) NOT NULL CHECK (fuel_type IN ('PETROL', 'DIESEL', 'ELECTRIC', 'HYBRID', 'CNG')),
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    image_file_name VARCHAR(255)
); `
4. Run the "ProductInventoryApplication.java" file present in src folder to run the application

## Application functionalities
1. Add, view, edit and delete Cars.
2. Search cars using keywords related to their colour, name, model.
3. Sort cars on the basis of their Price, Name, Posting date, etc (Click on the table head for whichever field you want to sort by)
4. Pagination

## API Documentation
After the springboot application is up and running you can see the swagger api documentation
App link after running: http://localhost:8080   
Swagger api documentation link(Only valid if springboot application is running on the local machine): http://localhost:8080/swagger-ui/index.html
