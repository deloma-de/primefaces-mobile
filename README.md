# PrimeFaces-Mobile

## Description

This project contains legacy mobile optimized components of [PrimeFaces](https://www.primefaces.org/) till Version 6.2 which then were removed in 7.0. Client side is based on [jQuery mobile](https://jquerymobile.com/) [(demo)](https://demos.jquerymobile.com/1.5.0-rc1/).

It contained some interesting mobile only UI components and renderer of existing versions and is a great framework to create hybrid applications with AJAX history back functionality. Valid use cases are B2B mobile applications used on MDA devices.

## Download JAR

* [7.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-7.0.jar) - compatible to PrimeFaces-7.0

## Showcase

* [Showcase](https://admin.deloma.de/primefaces-mobile-showcase/)

Repository can be found at [Showcase Repo](https://github.com/djmj/primefaces-mobile-showcase)

## Documention

Mobile is covered in the original PrimeFaces PDF [Documentation](https://www.primefaces.org/documentation/) till Version 6.2.

### Components

* content
* field
* footer
* header
* inputslider
* page
* rangeslider
* uiswitch _deprecated in favor of InputSwitch_

There are many mobile optimized renders of existing PrimeFaces component such as  _SelectOneMenu_ ,  _DataTable_  and many more.

## Changes

### 6.1 -> 7.0

- /java
    - adapted Java classes to PrimeFaces 7.0 changes
    - migrated Renderer classes to new DOM / CSS of jquery 1.5
- /resources
    - replaced jquery mobile with version 1.5.0-rc1
    - migrated widgets to new DOM / CSS

**Components**

- Button: added new icon position options:  _float-left_  and  _float-right_  that replace old options  _left, right_  
    
**DOM / CSS changes**

- icon as child span
- _ui-li-divider_  now _ui-listview-item-divider_
- _ui-li-static_  now  _ui-listview-item-static_
- _ui-tab_  now  _ui-tabs-tab_
- _ui-btn_  now  _ui-button_
- _ui-btn-icon-notext_  now  _ui-button-icon-only_


### History

This project basically extracts this commit [Remove mobile](https://github.com/primefaces/primefaces/issues/3386).

### jQuery

PrimeFaces 7.0 uses jQuery 3+ whereas mobile version below 7.0 were only compatible up to 1.9 which leads to breaking changes. [Download mobile](https://releases.jquery.com/mobile/).

### Build from source

Following plugin is required to build it from source using Maven.

[maven-jsf-plugin-1.3.3-SNAPSHOT.jar](https://cdn.deloma.de/others/libraries/primefaces/maven-jsf-plugin-1.3.3-SNAPSHOT.jar)

## Inofficial fork

This project is an inoffical partial "fork" from legacy PrimeFaces **6.1** extracting mobile components which were removed in PrimeFaces 7.0. 

### Motivation

At [deloma](https://www.deloma.de/Agentur/wp/Logistik-Software), we use this for our ecommerce enterprise application for the driver of a daily delivery tour with google maps navigation, order editing, user receipt, mobile payment (cashcard, creditcard, paypal, apple pay, android pay, cash) and also for stock inventory managing of our merchant clients.

This hybrid app works very stable in production and its not worth the time to refactor with new _responsive_ versions of the _new_ components with later PrimeFaces releases and finding solutions for browser ajax _history back_ support.

## License

***
Licensed under the Apache License, Version 2.0 (the "License") [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)