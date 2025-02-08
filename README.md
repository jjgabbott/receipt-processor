# Receipt Processor
This application is for the fetch-rewards receipt processor challenge. The application is a web service that processes receipt details and applies specific rules to calculate points that should be awarded to a given receipt. These points are then saved to be fetched at a later time.
## Table of Contents
- [Technologies](#technologies)
- [Running the Applicaton](#running-the-application)
- [Usage](#usage)
- [Configuration](#application-configuration)
- [Processing Rules](#processing-rules)
- [Project Architecture](#project-architecture)
## Technologies
- Java 21
- Spring Boot 3.4.2
- Docker
<br/>

## Running the Application
A Docker image (receipt_processor.tar) has been created to run this application. With docker installed, simply navigate to the directory where the image resides and run these commands.

### Obtaining the image
The image can be found here for download https://drive.google.com/file/d/131v8k-d_gooVNZeEou6Fz4fYMYyT20Lk/view?usp=sharing
<br/>
Alternatively you can build the image after cloning this repository by following these [steps](#build-image)

### Load the image
If you chose to download the image, navigate to where the image resides and run this command
```
docker load --input receipt_processor.tar
```

### Start the container
Once the image has been loaded, run this command to start the application
```
docker run -p 8080:8080 receipt_processor
```

This will start the application and expose it locally via port 8080.

<br/>

## Usage

### Endpoint: Process Receipts

* Path: `/receipts/process`
* Method: `POST`
* Payload: Receipt JSON
* Response: JSON containing an id for the receipt.

Description:<br/>
Takes in a JSON receipt and returns a JSON object with an ID </br>
The ID returned is the ID that should be passed into `/receipts/{id}/points` to get the number of points awarded to the receipt <br />

The number of points awarded to a given receipt are determined by these [rules](#processing-rules)

Example Request:
```json
{
  "retailer": "Target",
  "purchaseDate": "2022-01-01",
  "purchaseTime": "13:01",
  "items": [
    {
      "shortDescription": "Mountain Dew 12PK",
      "price": "6.49"
    },{
      "shortDescription": "Emils Cheese Pizza",
      "price": "12.25"
    },{
      "shortDescription": "Knorr Creamy Chicken",
      "price": "1.26"
    },{
      "shortDescription": "Doritos Nacho Cheese",
      "price": "3.35"
    },{
      "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ",
      "price": "12.00"
    }
  ],
  "total": "35.35"
}
```

Example Response:
```json
{"id": "77d0dc0e-bef9-3344-b243-fb68911feee2"}
```
<br />

### Endpoint: Get Points

* Path: `/receipts/{id}/points`
* Method: `GET`
* Response: A JSON object containing the number of points awarded.

Description:<br/>
A simple GET endpoint that looks up the receipt by the ID and returns an object specifying the points awarded. This endpoint will return a 200 status code on a successful fetch or a 404 status code if the ID does not exist.

Example Request:
```
http://localhost:8080/receipts/77d0dc0e-bef9-3344-b243-fb68911feee2/points
```
Example Response:
```json
{ "points": 28 }
```
<br/>

### API specifications can be found here
[api-docs.yaml](./api-docs.yaml)

## Application Configuration
Parameters for the rules used for processing receipts to award points are stored within the [application.properties](./src/main/resources/application.properties) file. This allows easier customization and tuning of the rules should they ever change.
```properties
#name of the application
spring.application.name=receipt-processor

#Used to control the cent amount for awarding points. Zero means the total
#is a round dollar amount with no cents
receipt.processor.centsForPoints=0
#Points awarded for the total having a cents value corresponding to the
#centsForPoints value.
receipt.processor.pointsAwardedCentsForPoints=50

#Used to control what the total can be divisible by for awarding points.
receipt.processor.totalDivisibleBy=0.25
#Points awarded for the total being divisible by the value corresponding
#to the totalDivisibleBy value.
receipt.processor.pointsAwardedTotalDivisibleBy=25

#Used to control the number of items to be grouped by for awarding points.
receipt.processor.itemsGrouping=2
#Points awarded for each grouping of items corresponding to the
#itemsGrouping value
receipt.processor.pointsAwardedItemsGrouping=5

#Used to control the value for calculating what the description
#should be divisible by before awarding points
receipt.processor.descriptionLengthDivisibleBy=3
#Used for calculating the points that should be awarded if the
#description is divisible by the value corresponding to the
#descriotionLengthDivisibleBy value
receipt.processor.pointsAwardedDescriptionLength=0.2

#Points awarded based on the day of month being even or odd
receipt.processor.pointsAwardedForDayOfMonthOdd=6
receipt.processor.pointsAwardedForDayOfMonthEven=0

#Used to control the hours and minutes for the lower and upper thresholds for calculating
#if points should be awarded for purchase time
receipt.processor.timeLowerBoundHours=14
receipt.processor.timeLowerBoundMinutes=0
receipt.processor.timeUpperBoundHours=16
receipt.processor.timeUpperBoundMinutes=0

#Points awarded if the purchase time is within the configured threshold
receipt.processor.pointsAwardedTimeThreshold=10

#API docs configuration
springdoc.api-docs.title=My Application API
springdoc.api-docs.description=This API manages receipts and items for the application.
```
<br/>

## Processing Rules
These rules collectively define how many points should be awarded to a receipt.

* One point for every alphanumeric character in the retailer name.
* 50 points if the total is a round dollar amount with no cents.
* 25 points if the total is a multiple of `0.25`.
* 5 points for every two items on the receipt.
* If the trimmed length of the item description is a multiple of 3, multiply the price by `0.2` and round up to the nearest integer. The result is the number of points earned.
* 6 points if the day in the purchase date is odd.
* 10 points if the time of purchase is after 2:00pm and before 4:00pm.
<br/>

## Project Architecture
```
Configuration # Contains configuration classes used to run the application
  -> OpenAPIConfiguration           # Configuration for customizing the application's API docs
  -> ReceiptProcessorConfiguration  # Configuration for storing values from the application.properties file to be used during receipt processing.

Controller # Defines the entry points into the application
  -> ReceiptProcessorController     # Contains the end points for processing receipts and retrieving saved points

Service # Contains the business logic of the application
  -> ReceiptProcessorService        # Contains an interface and implementation for processing receipts and awarding points based off of a set of rules

Data Access # Contains methods to save/access data
  -> ReceiptProcessorDao            # Contains an interface and implementation for saving and fetching a receipt's points. This is currently stored in memory. 
                                    # A separate implementation of the ReceiptProcessorDao interface could save/fetch this data to/from an external system.

Domain # Contains immutable objects to be used throughout the application
  -> Item                           # A record containing the definition of a receipt's item
  -> Points                         # A record containing the definition of points awarded to a receipt
  -> Receipt                        # A record containing the definition of a receipt
  -> ReceiptId                      # A record containing the definition of a receipt's id

Exception # Contains custom exceptions defined for the application as well as global exception handling
  -> BadRequestException            # Exception to be thrown when an incoming receipt request is malformed
  -> ReceiptNotFoundException       # Exception to be thrown when a receipt is not found within the data store
  -> GlobalExceptionHandler         # Handles custom defined exceptions within the application
  -> ErrorResponse                  # Response returned from the GlobalExceptionHandler after the exception has been handled

Resources # Contains assets to be used within the application
  -> application.properties         # Configurable file to hold all of the values to be used by the application
```

## Build Image
If you aren't comfortable with downloading the image, clone the repository and run these commands to build the image and start the application.
- Navigate to the root of the project where the Dockerfile exists. This should be under `/receipt-processor`
- Run the command 
```
docker build -t receipt_processor .
```
- Start the container and run the application by executing this command 
```
docker run -p 8080:8080 receipt_processor
```
<br/>
This will start the application and expose it locally via port 8080.
