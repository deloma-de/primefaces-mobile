# PrimeFaces-Mobile

## Description

PrimeFaces mobile is a module with mobile optimized components inside PrimeFaces till Version 6.2 which then was removed in 7.0. It is based on [jQuery mobile](https://jquerymobile.com/).

It contained some interesting mobile only UI components and renderer of existing versions and is a great framework to create hybrid applications with AJAX history back functionality. Valid use cases are B2B mobile applications used on MDA devices.

## Documention & Showcase

Mobile is covered in the original PrimeFaces PDF [Documentation](https://www.primefaces.org/documentation/) till Version 6.2.

The legacy showcase source can be found at [Github](https://github.com/primefaces/primefaces-showcase-legacy/tree/6_1). 

We recommend **6.1** version / tag since from 6.2 many mobile components already did not worked anymore.

## Components

* content
* field
* footer
* header
* inputslider
* page
* rangeslider
* uiswitch

## Renderekit

There are many mobile optimized renders of existing PrimeFaces component such as  _SelectOneMenu_ ,  _DataTable_  and many more.

## Inofficial fork

This project is an inoffical partial "fork" from legacy PrimeFaces **6.1** mobile components which were removed in PrimeFaces 7.0. 

### Motivation

At [deloma](https://www.deloma.de/Agentur/wp/Logistik-Software), we use this for our ecommerce enterprise application for the driver of a daily delivery tour with google maps navigation, order editing, user receipt, mobile payment (cashcard, creditcard, paypal, apple pay, android pay, cash) and also for stock inventory managing of our merchant clients.

This hybrid app works very stable in production and its not worth the time to refactor with new _responsive_ versions of the _new_ components with later PrimeFaces releases and finding solutions for browser ajax _history back_ support. 

### Migration

This project basically extracts this commit [Remove mobile](https://github.com/primefaces/primefaces/issues/3386).

1. ~~refactor mobile depdendencies from namespaces other then mobile~~
2. ~~remove non-mobile code / resources~~
3. ~~add PrimeFaces 7.0 as dependency~~
4. ~~adjust to PrimeFaces 7.0 changes~~

#### jQuery

PrimeFaces 7.0 uses jQuery 3+ whereas mobile is only compatible to 1.9+ with breaking changes. [Download mobile](https://releases.jquery.com/mobile/)

5. adapt to jQuery 3+
6. mouse jquery.event.props undefined https://github.com/stephband/jquery.event.move/issues/31
7. test with old mobile showcase
8. Reintegrate DataListTemplate / DataTableTemplate ... SwipeEvent

## License

Same as original PrimeFaces:

***
Licensed under the Apache License, Version 2.0 (the "License") [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)