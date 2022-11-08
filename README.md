# TestResultApi

TestResult API is a secure spring boot based application to generate, store and secure provide test results to end
users.
*TestPerfect is a test center management software for corona tests*

This application handle the public communication / interface for the end user to
authenticate and get the test result. But also ensure, that this api has a seperated databases to ensure
the security, integrity and availability of the data.

# How it works?

The TestCenterAPI (called TCA) is communicating with the TestResultApi (TRA) to submit test results. The TRA store the
test results in a database and provide a public api to get the test results based on a secure generated accessToken and
the birthdate of
the patient. TCA are only submit test results once, they can not be modified or deleted to ensure the integrity of the data.

Patient can now use the NextJs server side rendering portal to access the test results by entering the accessToken and
the birthdate.
If both are correct, the patient will get the test result. If the patient request the information for the first time,
the TRA will generate
a verificationHash that are send over Webhooks back to the TCA. This is needed to complete the German logging
requirements.

So summary:

* TCA submit test results to TRA
* TRA store test results in database and response with accessToken
* Patient use accessToken and birthdate to get test results
* TRA generate verificationHash and send it to TCA via Webhook
* Action is completed, and can repeat during the next 24 hours

# Multi Tenant

For a test center software, it is important to have a multi tenant solution. The TRA is designed to be multi tenant.
The TCA need to submit a special header to the TRA to identify the tenant.
`X-Tenant: <tenantId>`
If no header is provided, the TRA will reject the request.

# Authentication

The TRA is required a m2m authentication to verify the TCA. For this case client side authentication according to
RFC6749 is used.
The clientId and clientSecret is exchanged via Keycloak.

# Secure storage
We never store personal information of the patient in plain format in this database or any logs. 

First we generate a sha256 hash from the birthdate and encrypt this hash with a AES256 encryption, the secret to decrypt this information are included in the JWT token (RFC 7519) as payload.
So we are only able to decrypt the birthdate hash if the user provide us the accessToken.
The same methode is used for all other personal user information.

We follow the principle user has the own control of the data at any time.


# Patient access

To ensure, that no bruteforce or automated attacks are possible, the patient access is limited to 5 requests per 10
minutes.
Failing requests are logged and blocked after 5 failed requests for 10 minutes.
We use Cloudflare recaptcha to ensure, that the requester is a human.
