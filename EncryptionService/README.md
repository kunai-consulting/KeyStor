# Introduction

See ../README.md for an introduction

# Overview

The Encryption Service is used to do encryption as close to the end user as possible.  Ideally this is outside your data center
so the service should be public facing and should not have access to other services like the Endpoint Connection Service.

* To run the server run (replace the version with the correct one).

        java -jar target/keyvault-encryption-0.0.1-rc5-SNAPSHOT.jar server example.yml

**Encryption Service**
----

 
* **URL**

  encrypt

* **Method:**

  `GET`
    
*  **URL Params**

   **Required:**
   `type=[generic|card_data|ssn]`

* **Data Params**

    **Content:** `[A String that you want encrypted]`

* **Success Response:**  
 
  * **Code:** 200 Success
    **Content:** `[An encrypted string]`
 
* **Error Response:**

  * **Code:** 500 Internal Error <br />
    **Content:** `A very limited error message for security reasons`

* **Sample Call:**

```
curl --data "4012888888881880" http://localhost:8080/encrypt?type=card_data
```
* **Notes:**

  Depending on how and where you deployed the service other responses are possible. As noted elsewhere you should
  only call this service externally using some form of transport level security (e.g. HTTPS).
  
* **URL**

  healthcheck

* **Method:**

  `GET`    

* **Success Response:**
  
  * **Code:** 200 Success
    **Content:** `[Some details which change depending on the keyvault]`
 
* **Error Response:**

  * **Code:** !=200 r <br />
    **Content:** `[Maybe some details about what's not working]`

* **Sample Call:**

```
curl http://localhost:8080/healthcheck
```
* **Notes:**

  Depending on the keyvault the response may be useful, but primarily this just lets you know that the service is up
  and running in the container.  This becomes pretty useful if you want to do autoscaling on AWS with an ELB since
  you typically won't be exposing anything at the root level that has a straightforward response, so the default
  query of root isn't going to work.
  
