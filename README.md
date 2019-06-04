# KeyStor

Don't store your keys with your data.  Safeguard them with a security architecture solution like KeyStor.

### Why?
We do a lot of security reviews for our clients.  Over the years we've noticed a common problem.

Most data security implementations follow the ancient architecture for protecting sensitive data: 
> Encrypted data is stored in one file and the keys used to encrypt that data sit right next to it on the same hard drive or machine.

In the age of the mainframe computer that made perfect sense.  There was one computer, one hard drive, and you could only access it by physically being in front of it.

Things have changed. **Big time.** 

But, with some of these mainframes still in use, the standards for compliance have kept the bar low.  Having a compliant data security architecture is not enough to avoid joining Home Depot, Target, the DNC and hundreds of other companies that passed compliance audits and still got hacked.

It gets worse.  Common practice for data security is to do what everybody else does.  _We preach it too_:  

>Don't build something new. Follow the proven standard.  

That's great when you are following a strong standard like AES.  Not so great when you are building a next generation cloud computing environment and using a data security architecture designed for systems created before these things were even commercially feasible.

---
In today's world of cloud computing, simply encrypting your data at rest with database encryption is not enough.  

With this architecture, the cryptographic keys and _all_ the data are in the same place at the same time.  

>That's a bit like locking your front door and putting the key under the mat.  

There's a better way. KeyStor's architecture:

* Reduces your attack surface
* Greatly increases the security of sensitive data
* Simplifies any security and compliance audits
* Frees your business logic from being responsible for protecting data

It's based on an architecture principle called _Micro-segmentation_.  

Micro-segmentation strives to isolate the applications and systems that need access to sensitive data as much as possible.

Rather than treating the entire data center (from the entry point of a https certificate to and end point like a VPN connection) as safe, micro-segmentation strives to encrypt data as early as possible and only decrypt when absolutely needed.

With Keystor, companies can create a solution that carries this architecture to the extreme: 

###### Sensitive data is encrypted outside the data center, and if possible, is decrypted as it leaves.

---
In our experience this is possible most of the time, because most of the time the most sensitive data stored in your data center doesn't belong to you. It belongs to your customers.

###### Let's look at an example:  
What is the information most valuable to a hacker?  Social security numbers. Sold as part of an identity package an SSN was worth $30.00 on the black market in 2014. 

But, unless your data center is the IRS, you should **never** have an unencrypted SSN inside your main data center.  You **never** need it because your company doesn't act on that data. 

With our architecture, you can do exactly that by setting up a micro-segment just for your secure data.  We call this secure segment a Keystor. Here are its features:

* It contains your cryptographic keys.  
* Its public facing API only knows how to encrypt data.
* Its private API is only accessible by the business logic that needs it and only knows how to decrypt data as it is being sent to a specific external end points and, if needed, encrypt data as it is passed back from a specific end point.

The micro-segment can be isolated using any type of standard networking controls. Our favorite way to do it is to use a separate virtualized data center in the cloud that can scale up and down as needed.  

If traffic is light and the configuration is kept simple this data center can cost as little as $20.00 per month on AWS. 

When KeyStor is set up properly, the only people in your company with access to sensitive data are the ones that can get into that data center.

##### Ok, that sounds cool, but what exactly is it?

A KeyStor is comprised of two ReSTful services and a keyvault...

![KeyStor Architecture](https://github.com/kunai-consulting/KeyStor/raw/master/KeyStor/Slide1.jpg)

### Encryption Service

The **KeyStor Encryption Service** can be public facing.  It _only_ knows how to encrypt data. It allows your application to encrypt data before passing it in to your main data center.

### Endpoint Connection Service

The **KeyStor Endpoint Connection Service** is  private facing and should only be accessible by the applications in your main data center that need to connect to it.  You can use anything you like to implement this, including all strong forms of authentication or transport level security with secure routing (e.g. a VPN).  This ReSTful service knows how to decrypt data passed to it and send it to whitelisted external endpoints that consume the sensitive data.

### Keyvault

The **keyvault** is the storage for your cryptographic keys.  KeyStor currently supports the following kinds of keyvaults:

* Simple AES encryption with a single key
* Amazon KMS Keys with envelope encryption

Use simple AES encryption if you are looking for a low cost, but safe, solution and don't need the features provided by HP Voltage. AES encryption will return to you a simple AES encrypted cypher containing your data.  For each cryptographic key you use in your back end you can encrypt up to 250 million terabytes without needing to worry about key rotation as long as the security of your secure data center is maintained.

Use KMS Keys if you are going to run KeyStor in AWS.  They make it much easier to manage and secure your keys, have different keys for development and production, etc.  Also, with AWS keys you can enable key rotation.

### Quick Start

Each KeyStor service consists of a Spring Boot based fat JAR file.  You can run the JAR on anything with the Java 8 Runtime Environment installed on it.  

We like to run things in Docker because it makes managing the deployment to production a little easier.  If you are using Docker you can run both services on the same instances in the same cluster. We like to run them on separate ones.  This allows us to keep the public facing Encryption Service separated from the private Endpoint Connection Service with minimum complexity in routing.

---

The next section goes into detail about how to set up a KeyStor.  It involves using Maven, running JAR files or using Docker.  

If it's over your head drop us and e-mail and we can help: [info@kunaiconsulting.com](mailto:info@kunaiconsulting.com)

---

### Building and running the encryptor from the command line

Pull down the source from git

Drop down into the encryption service and use Maven to build it using `package`...

```
cd EncryptionService
mvn package
```

You should now have a fully build fat JAR in `target\keyvault-encryption-0.0.1-rc1-SNAPSHOT.jar`.  You'll need to modify
the configuration file in example.yml and at a minimum change the key used for encryption...

```
cp example.yml encryptor_config.yml
```

open `encryptor_config.yml` in a text editor and change the value in `  key: "ABCDEF0123456789"` to your own key.  Save the file.  At this point you are ready to run with a simple AES keyvault.  To run the encryptor...

```
java -jar target/keyvault-encryption-0.0.1-rc1-SNAPSHOT.jar server encryptor_config.yml
```

You should see some output from your encryptor as it starts up.  If you want to test it you can open a second terminal window and hit your server with a curl command.  Something like this...

```
curl --data "4012888888881880" http://localhost:8080/encrypt?type=card_data
```

Should return an encryption like `45E03DECD3D575708722ACBC6EDE0CFDE27C8A6C04196BC0C04E3A545BC9F1E786E562A3FACCF199CE1F5BB77C67A7E0`
when using the AES encryptor.

Note that for testing and development purposes the example configuration uses an unencrypted HTTP call, but for production you **MUST** use **ONLY HTTPS**

#### Building and running the end point connection service from the command line

Make sure you shut down the encryptor you started in the last section.  If it's in the foreground in your terminal `[Ctrl]-c` will do the trick.

Drop down into the end point connection service and use Maven to build it using `package`...

```
cd ../EndpointConnectionService mvn package
```

You should now have a fully build fat JAR in `target\keyvault-endpointconnection-0.0.1-rc1-SNAPSHOT.jar`.  You'll need to modify the configuration file in example.yml and at a minimum change the key used for encryption...

```
cp example.yml connector_config.yml
```

open `encryptor_config.yml` in a text editor and change the value in `  key: "ABCDEF0123456789"` to your own key.  Save
the file.  At this point you are ready to run with a simple AES keyvault.  To run the encryptor...

```
java -jar target/keyvault-endpointconnection-0.0.1 rc1-SNAPSHOT.jar server connector_config.yml
```

You should see some output from your endpoint connection as it starts up.  If you want to test it you can open a second terminal window and hit your server with a curl command.  Something like this...

```
curl --data "some data and then <CARD>{paste the value returned from the encryptor here}</CARD>" --header "proxy-url: http://httpbin.org/post" --header "decryption-regex0: (?<=<CARD>).*?(?=</CARD>)" --header "decryption-type0: card_data" --header "Content-Type: text/plain" --header "Accept: text/plain" http://localhost:8080/proxy
```

Should return an encryption like...

```
{
  "args": {}, 
  "data": "some data and then <CARD>4012888888881880</CARD>", 
  "files": {}, 
  "form": {}, 
  "headers": {
    "Accept": "text/plain", 
    "Content-Length": "48", 
    "Content-Type": "text/plain", 
    "Host": "httpbin.org", 
    "User-Agent": "curl/7.49.1"
  }, 
  "json": null, 
  "origin": "24.23.163.220", 
  "url": "http://httpbin.org/post"
}
```

when using the AES decryptor.

Note that for testing and development purposes the example configuration uses an unencrypted HTTP call, but for production you *MUST* use *ONLY HTTPS*, and you *MUST* provide routing that restricts access to the Endpoint Connection Service to the applications that are authorized and intended to use it.

#### Docker builds

We like using Docker for deployment because it makes reving and scaling your service so easy.  If you want to build for Docker start by installing docker for your platform then...

https://cloud.docker.com/u/kunai/repository/docker/kunai/keystor-connection
https://cloud.docker.com/u/kunai/repository/docker/kunai/keystor-encryption

#### Deployment

Correct deployment of the services is critical to creating a secure KeyStor.  There are of course many different ways to deploy the two services in KeyStor, but our favorite on AWS is to deploy each service to it's own ECS cluster and configure ELB's to route traffic and manage SSL certificates.  No matter what you do make sure that you:

* Take care to manage the place you deploy your KeyStor with a very high level of security
* Find out what auditors and others will be asking for in terms logging, access control, firewalling, etc. implement that from the start
* **NEVER** expose the connection service publicly, and limit what it can connect to as much as possible
* **NEVER** expose either service over anything but HTTPS

For extra sensitive data you probably want to:

* Don't just run the two services with different routings, run them on different instances
* Run on a single tenancy environment

#### Ya, cool, it's all set up, but how do I use it?

So it's all deployed and now you want to integrate it with your application.  Don't worry it's not hard to do the basics.  There are two services and depending on how your business logic works you'll want to use one or both.  If you have a front end client which collects sensitive data from users you'll want to use the encryption service.  That sensitive data might be collected through a mobile app, or a web app, or an ATM system, or an encrypting PIN pad, etc.

If you don't have a client which collects sensitive data because it's passed to you from an end-point you call, then you only need to use the Endpoint Connection Service.  You'll also want to use the Endpoint Connection Service if you need to decrypt data before passing it to an endpoint that you connect to.  The next two sections document the API for each of these services.  Don't worry it's pretty darn simple.

#### The Encryption Service

First, you're going to want to call the encryptor right when you collect the information you want to encrypt.  If possible you'll want to do it from the browser or app on the client's device.  That will keep your back-end business logic from ever seeing any encrypted data.  You'll make the call using a simple put request like this one...

```
curl -X POST "http://34.222.151.65/api/encrypt?type=generic" -H "accept: */*" -H "Content-Type: application/json" -d "Smith"
```

You're done!  Now you can take what was returned from that call you'll and pass it to your business logic, store it, etc. and not have to worry about it's safety.  You can find the full documentation for the encryption service in the README.md file in the directory for the Encryption Service.

#### The Endpoint Connection Service

The Endpoint Connection Service is a little more complicated than the encryption service.  It uses encryption and decryption done by the service to allow your core business logic to do three things:

* Deliver HTTP requests to the specific endpoints that your secure data center knows how to connect to
* Decrypt sensitive information as it's being passed to the endpoint
* Encrypt sensitive information as it's being returned in a response from the endpoint

In order to accomplish this the Endpoint Connection accepts all types of HTTP requests and uses three different kinds of extra information placed in the header of the request:

* The `proxy-url` header
* The `decrypt-regex{n}` and `decrypt-type{n}` headers
* The `encrypt-regex{n}` and `encrypt-regex{n}` headers

Other than the addition of these headers, and the change in the address the request is initially made to, you can format the contents of your request exactly the same way you would if the code was calling the endpoint directly.
 Let's go over each of the three headers in the above order...

#### The `proxy-url` header

The `proxy-url` header is pretty simple.  In this header you put the URL you want the Endpoint Connection Service to call. So if your code wants to call the whitelisted URL `https://www.foo.com/bar?param=bull` then put that in this header value.

#### The `decrypt-regex{n}` and `decrypt-type{n}` headers

This is where things get a little bit complicated.  The Endpoint Connection Service is going to take the request that you made to it, decrypt all the encrypted data in the request, then make the call to the endpoint.  But it needs to know how to find and decrypt the encrypted information in the body of the request.  That's where the decrypt headers come in.  They use regular expressions to tell the Endpoint Connection Service where to find the encrypted information, and a type header to tell the Endpoint Connection Service what type of encryption to use.  Each pair of headers has an index number at the end so that you can have more than one decryption operation.

Let's break down a simple example so we can see how it works.  Let's look at the test call that we made above...

```
curl --data "some data and then <CARD>{paste the value returned from the encryptor here}</CARD>" --header "proxy-url: http://httpbin.org/post" --header "decryption-regex0: (?<=<CARD>).*?(?=</CARD>)" --header "decryption-type0: card_data" --header "Content-Type: text/plain" --header "Accept: text/plain" http://localhost:8080/proxy
```

In this call we set the `proxy-url` header to `http://httpbin.org/post`  That website will show you the contents of your request in it's response, so it's perfect for testing and playing around with the connection service.  In the body of the request, we have a plain text request with `some data` and then `<CARD></CARD>`. In between the `<CARD>` tags, you pasted the response that you got back from the encryptor so it looks something like...

```
some data and then <CARD>45E03DECD3D575708722ACBC6EDE0CFDE27C8A6C04196BC0C04E3A545BC9F1E786E562A3FACCF199CE1F5BB77C67A7E0</CARD>
```

...but the encyption is one that your keyvault can decrypt.

So the encryptor needs to find the stuff between the `<CARD>` tags, because that's where there's some encrypted data.  To do that we use the regex `(?<=<CARD>).*?(?=</CARD>)` which matches what we want.  This is the first regex operation and there may be more than one so we put this in the `decryption-regex0` header.  If we wanted to make another match it would go in `decryption-regex1`, etc.

We aren't quite done, though, there's one more bit of information we need to give the Endpoint Connection Service so that it can do it's job.  We need to tell it what type of decryption to use.  In this case it's card data so we use type 3 and that goes in `decryption-type0` to be used with the decryption regex with the same index.  If we wanted to make another match the type for that match would go in `decryption-type1`, etc.

#### The `encrypt-regex{n}` and `encrypt-regex{n}` headers

All of this is all well and good, but what if the end point that we connect to returns sensitive information in the response? That's no problem as long as you can construct a regex to find the sensitive information in the response.  The encrypt headers work just like the decrypt headers except instead of decrypting things in the request, they encrypt things in the response before returning the response to the caller in the main data center.

That's it.  You know everything you need now to integrate the code in your main data center with the KeyStor's Endpoint Connection Service. You can find the full documentation for the Endpoint Conection Service in the README.md file in the directory for the Endpoint Connection Service.
