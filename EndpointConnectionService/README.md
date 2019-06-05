# Introduction

See https://github.com/kunai-consulting/KeyStor for an introduction

# Overview

The Endpoint Connection Service is used to do decryption and encryption as you are calling external endpoints. This
service should not be visible publicly.  Instead you should be connecting only the applications that need it to the
service with some form of strong two way authenticated transport level secruity (e.g. a VPN)

***Docker***

To run the server with the Docker image (https://hub.docker.com/r/kunai/keystor-encryption) make sure you set the following environment vars exactly the same
for both the encryption service and the endpoint connection service:
* ENCRYPTOR_TYPE: Either 'aes' or 'aks'
* ENCRYPTOR_KEY: This should be the aks key ID if the above is 'aks', or some _randomly_ generated, *carefully* managed string of 16 ASCII chars.
It should be set in production no matter what.


***CLI***

To run the server without Docker run (replace the version with the correct one)...

**Encryption Service**
----

 
* **URL**

  /api/proxy

* **Method:**

  `GET` | `POST` | `DELETE` | `PUT`
    
*  **URL Headers**

   **Required:**
   
   `connection-url: [the full URL of the endpoint you want to connect to`
   
   **Optional:**
   
   `decrypt-regex[n]: [the nth reg exe used to find strings in the request body needing to be decrypted]`
   `decrypt-type[n]: [test|generic|card_data|ssn]`
   `encrypt-regex[n]: [the nth reg exe used to find strings in the response body needing to be ecrypted]`
   `encrypt-type[n]: [test|generic|card_data|ssn]`
   
* **Data Params**

    **Content:** `Your content including any encrypted content that will be decrypted before it gets sent to the endpoint.`

* **Success Response:**  
 
  * **Code:** 200 Success (or what ever the server that the request was proxied to returned)
    **Content:** `[The response data from the proxied endpoint with any encryptions specified in encrypt headers applied]`
 
* **Error Response:**

  * **Code:** 500 Internal Error <br />
    **Content:** `A very limited error message for security reasons`

* **Sample Call:**

```
curl --data "some data and then <CARD>{paste the value returned from the encryptor here}</CARD>" --header "connection-url: http://httpbin.org/post" --header "decryption-regex0: (?<=<CARD>).*?(?=</CARD>)" --header "decryption-type0: card_data" --header "Content-Type: text/plain" --header "Accept: text/plain" http://localhost:80/api/proxy
```
* **Notes:**

  Depending on how and where you deployed the service other responses are possible. As noted elsewhere you should
  only call this service externally using some form of transport level security (e.g. HTTPS).
  
* **URL**

  /api/actuator/health

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
curl http://localhost:80/api/actuator/health
```
* **Notes:**

  Depending on the keyvault the response may be useful, but primarily this just lets you know that the service is up
  and running in the container.  This becomes pretty useful if you want to do autoscaling on AWS with an ELB since
  you typically won't be exposing anything at the root level that has a straightforward response, so the default
  query of root isn't going to work.
  
