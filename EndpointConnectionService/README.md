# Introduction

The Keystor Endpoint Connection Service provides an interface to services in the data center that stores sensitive data so that they can
connect to external services that consume or produce sensitive data.  The ReST API provides a simple way to:

* Call the protected services
* Encrypt data returned from the protected services (e.g. encrypt PCI data as it is returned from Visa)
* Decrypt data which was previously encrypted by the Keystor data center and use it to call a protected service

# Overview

Once this application is running you simply call /proxy and set headers as follows:

                .header("proxy-url", "http://someserver") // some protected server you want to access
                .header("encryption-regex0", "(?<=<ID>).*?(?=</ID>)") // a regular expression used to locate data to encrypt in the response
                .header("encryption-type0", "0") // The type of encryption
                .header("encryption-regex1", "(?<=<CARD>).*?(?=</CARD>)") // a second encryption
                .header("encryption-type1", "0") // The type of the second encryption
                .header("decryption-regex0", "(?<=<ID>).*?(?=</ID>)") // a regular expression used to locate data to decrypt in the request
                .header("decryption-type0", "0") // The type of decryption

Currently the supported encryption types are as follows:
            
            0 = Test Encryption (This is for testing only and surrouds sensitive data with 'encrypted { }'.  It's not actually an encryption.)
            1 = Simple Encrypt
            2 = Simple Tokenize 
            3 = PCI masked card data tokenization.  This does format preserving tokenization of PCI card data so that the BIN and last 4 numbers are preserved.

Support for anything beyond simple Encryption/Decryption with AES depends on integration with Voltage and other products which may or may not be 
part of a particular deployment.

# Running The Application

To test the example application run the following commands.

* To package the example run.

        mvn package


* To run the server run.

        java -jar target/keyvault-endpoingconnectionservice-0.0.1-rc1-SNAPSHOT.jar server example.yml

**Title**
----
  <_Additional information about your API call. Try to use verbs that match both request type (fetching vs modifying) and plurality (one vs multiple)._>

* **URL**

  <_The URL Structure (path only, no root url)_>

* **Method:**
  
  <_The request type_>

  `GET` | `POST` | `DELETE` | `PUT`
  
*  **URL Params**

   <_If URL params exist, specify them in accordance with name mentioned in URL section. Separate into optional and required. Document data constraints._> 

   **Required:**
 
   `id=[integer]`

   **Optional:**
 
   `photo_id=[alphanumeric]`

* **Data Params**

  <_If making a post request, what should the body payload look like? URL Params rules apply here too._>

* **Success Response:**
  
  <_What should the status code be on success and is there any returned data? This is useful when people need to to know what their callbacks should expect!_>

  * **Code:** 200 <br />
    **Content:** `{ id : 12 }`
 
* **Error Response:**

  <_Most endpoints will have many ways they can fail. From unauthorized access, to wrongful parameters etc. All of those should be liste d here. It might seem repetitive, but it helps prevent assumptions from being made where they should be._>

  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "Log in" }`

  OR

  * **Code:** 422 UNPROCESSABLE ENTRY <br />
    **Content:** `{ error : "Email Invalid" }`

* **Sample Call:**

  <_Just a sample call to your endpoint in a runnable format ($.ajax call or a curl request) - this makes life easier and more predictable._> 

* **Notes:**

  <_This is where all uncertainties, commentary, discussion etc. can go. I recommend timestamping and identifying oneself when leaving comments here._> 