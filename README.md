# aline-batch-mb



# Aline Financial Batch Enrichment and Analytics

## Description

 In our busy schedules, no human has the time to process, check in millions of transactions, and produce insightful metrics from it. This is a highly repetitious task and modern computers excel at repetitive work so we can use Java and Spring Batch to do this work for us. Spring Batch is a highly mature library for consuming large amounts of data, allowing configuration for scalability and reducing boilerplate code, and also breaking work up to be efficient and fast. Consistency of data is paramount in finances, and Spring Batch allows us to make work completed autonomously without having to sacrifice consistency and ensuring that information is preserved as we utilize it.
 
 The following project is meant for taking in large amounts of data and having the ability to enrich data with Faker so as to showcase that our project works with realistic information while allowing us to trace back sources of errors and transactions. This program also excels at analyzing the data given and producing reports of analytics for engineers to process as XML. Currently the project only supports CSV files with plans to also parse S3 buckets and other structured and unstructured data stores to suit client needs.


## Table of Contents
  * [Installation](#installation)
  * [Usage](#usage)
  * [Design](#design)
  * [Credits](#credits)

## Installation
1. Clone the main branch into your desired location

2. Modify the application.properties to point to an enterprise database, or use the built in H2 functionality for proof of concept

3. Change directory to transaction-batch/ and using Maven Build Tools, run `mvn spring-boot:run` in your terminal


## Usage
- Using Postman or any other HTTP Request tool, send an HTTP POST request to the path `batch/load` to start processing data
- The request body has two parameters for specifying parameters of the job to run. dataEnrich and dataAnalyze
- dataEnrich specifies that we want the process to create enriched data from the provided data store and return it as XML
- dataAnalyze specifies that we want the process to run analytics on the provided data store and produce reports as XML
- Once the job is completed, the application will return an HTTP Response of the results of the processing. HTTP 206 for success and HTTP 500 for an error has occurred
 

## Design
![High Level Content](https://user-images.githubusercontent.com/99719821/164756992-4b5adac8-3cd1-42de-92d2-aef07c4fc66f.png)



## Credits

This app uses the following libraries and modules: 

* Spring Boot
* Spring Batch
* MySQL
* JavaFaker
* LuhnAlgorithms
* Lombok
* Swagger
* Micrometer
* XStream
