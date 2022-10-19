# PrimeFaces-Mobile

## Description

This project contains legacy mobile optimized components of [PrimeFaces](https://www.primefaces.org/) which were only supported till Version 6.2. Client side is based on [jQuery mobile](https://jquerymobile.com/) [(demo)](https://demos.jquerymobile.com/1.5.0-rc1/).

It contained some interesting mobile only UI components and renderer of existing versions and is a great framework to create hybrid applications with AJAX history back functionality like B2B mobile applications used on MDA devices.

## Download JAR

|Mobile Version|PrimeFaces Core|
|[12.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-12.0.jar)|12.0|
|:---:|:---:|
|[11.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-11.0.jar)|11.0|
|[10.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-10.0.jar)|10.0|
|[8.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-8.0.jar)|8.0|
|[7.0](https://cdn.deloma.de/others/libraries/primefaces/primefaces-mobile-7.0.jar)|7.0|

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

### 11.0    

- adapted Java classes to PrimeFaces 11.0 changes 
- removing build in Font-Awesome resource rendering in HeadRenderer

### 10.0    

- adapted Java classes to PrimeFaces 10.0 changes
- hardcoded OverlayPanel javascript position as 'overlay' since showEffect was removed

### 8.0    

- adapted Java classes to PrimeFaces 8.0 changes
- set java compilation level to 1.8

### 7.0

- set java compilation level to 1.5 
- adapted Java classes to PrimeFaces 7.0 changes
- migrated Renderer classes to new DOM / CSS of jquery 1.5
- replaced jquery mobile with version 1.5.0-rc1
- migrated widgets to new DOM / CSS
- added font-awesome icon support via CSS


**Components**

- Button: added new icon position options:  _float-left_  and  _float-right_  that replace old options  _left, right_  
    
**DOM / CSS changes**

- icon as child span
- CSS classes
    - _ui-li-divider_  →  _ui-listview-item-divider_
    - _ui-li-static_  →  _ui-listview-item-static_
    - _ui-tab_  →  _ui-tabs-tab_
    - _ui-btn_  →  _ui-button_
    - _ui-btn-icon-notext_  →  _ui-button-icon-only_
    - _ui-btn-left_  →  _ui-toolbar-header-button-left_  
    - _ui-btn-right_  →  _ui-toolbar-header-button-right_ 
    - _ui-input_ → _ui-textinput_  /  _ui-searchinput_  (at text / search / password input)

### jQuery

PrimeFaces 7.0 or higher uses jQuery 3+ whereas mobile version till 6.2 were only compatible up to 1.9 so an upgrade to jquery 3 was necessary.

jQuery mobile 1.5 is based on jquery 3. [Download mobile](https://releases.jquery.com/mobile/).

### Build from source

Following plugin is required to build it from source using Maven.

- 10+ [maven-jsf-plugin-1.3.4-SNAPSHOT.jar](https://cdn.deloma.de/others/libraries/primefaces/maven-jsf-plugin-1.3.3-SNAPSHOT.jar)
- 7.0 and 8.0 [maven-jsf-plugin-1.3.3-SNAPSHOT.jar](https://cdn.deloma.de/others/libraries/primefaces/maven-jsf-plugin-1.3.3-SNAPSHOT.jar)

## Inofficial fork

This project is an inoffical partial "fork" from legacy PrimeFaces **6.1** extracting mobile components which were removed in PrimeFaces 7.0. 

### Motivation

At [deloma](https://www.deloma.de/Agentur/wp/Logistik-Software), we use this for our ecommerce enterprise application for the driver of a daily delivery tour with google maps navigation, order editing, user receipt, mobile payment (cashcard, creditcard, paypal, apple pay, android pay, cash) and also for stock inventory managing of our merchant clients.

This hybrid app works very stable in production and its not worth the time to refactor with new _responsive_ versions of the _new_ components with later PrimeFaces releases and finding solutions for browser ajax _history back_ support.

## License

***
Licensed under the Apache License, Version 2.0 (the "License") [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)