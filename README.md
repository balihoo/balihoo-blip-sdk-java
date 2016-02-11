# BLIP Java SDK
A Java SDK for interacting with the Balihoo location information platform (BLIP).

## Requirements
- Using this SDK requires that you already have API keys and are interacting on behalf of a brand that already exists in Balihoo's system. Please contact Balihoo if you require API keys and/or would like to add a new brand to our system.

## Usage
```java
    // Construct a Blip object
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    
    // Make a method call and return the response
    BlipResponse brandResponse = blip.getBrandKeys();

    // Status Code (e.g. 200, 204, 404, etc.)
    int responseCode = brandResponse.StatusCode;

    // Response Content
    String myBrands = brandResponse.Body;
```

## Methods
All methods return a **BlipResponse** object with two properties:
- **StatusCode**
  - The HTTP response code (e.g. 200, 204, 404) as an integer.
- **Body**
  - The HTTP response content as a string.
  - For calls that return brand or location data this property will contain stringified JSON objects.

### **Ping**
Ping the BLIP API.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.Ping();

    if (blipResponse.StatusCode == 200)
    {
      // Success!
    }
```
---
### **GetBrandKeys**
Get a list of brandKeys that the API user is authorized to access.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.GetBrandKeys();

    String myBrandKeys = blipResponse.Body;
```
---
### **GetBrandSources**
Get a list of data sources available for an individual brand.

#### Parameters
- brandKey: The unique identifier for a single brand.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.GetBrandSources("mybrand");

    String sources = blipResponse.Body;
```
---
### **GetBrandProjections**
Get a list of data projections available for an individual brand.

#### Parameters
- brandKey: The unique identifier for a single brand.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.GetBrandProjections("mybrand");

    String projections = blipResponse.Body;
```
---
### **GetLocationKeys**
Get a list of locationKeys for all locations belonging to the specified brand.

#### Parameters
- brandKey: The unique identifier for a single brand.
- projection: Optionally filter data in a single projection. Defaults to "universal".

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.GetLocationKeys("mybrand");

    String locationKeys = blipResponse.Body;
```
---
### **GetLocation**
Get data for an individual location within the specified brand.

#### Parameters
- brandKey: The unique identifier for a single brand.
- locationKey: The unique identifier for a single location within the brand.
- projection: Optionally filter data in a single projection. Defaults to "universal".
- includeRefs: Optionally include objects referenced by the location in its data. Defaults to false.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.GetLocation("mybrand", "mylocation");

    String locationData = blipResponse.Body;
```
---
### **QueryLocations**
Get data for locations in a single brand filtered by the specified BLIP query.

#### Parameters
- brandKey: The unique identifier for a single brand.
- query: A stringified JSON query used to filter locations.
- view: Optionally specify the view returned. Defaults to "full".

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    String query = "{\"address.state\":{\"equals\":\"ID\"}}";
    BlipResponse blipResponse = blip.QueryLocations("mybrand", query);

    String matchingLocations = blipResponse.Body;
```
---
### **PutLocation**
Add a new location or update an existing location's data.

#### Parameters
- brandKey: The unique identifier for a single brand.
- locationKey: The unique identifier for a single location within the brand.
- source: The name of the data source being used to add/update the location
- locationData: The stringified JSON location document.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    String locationDocument = "{\"document\":{\"name\":\"Balihoo, Inc.\",\"address\":{\"city\":\"Boise\",\"state\":\"ID\"}}}";
    BlipResponse blipResponse = blip.PutLocation("mybrand", "mylocation", "mysource", locationDocument);

    if (blipResponse.StatusCode == 204)
    {
        // location was successfully added/updated
    }
```
---
### **DeleteLocation**
Delete an individual location.

#### Parameters
- brandKey: The unique identifier for a single brand.
- locationKey: The unique identifier for a single location within the brand.
- source: The name of the data source being used to delete the location

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.DeleteLocation("mybrand", "mylocation", "mysource");

    if (blipResponse.StatusCode == 204)
    {
        // location was successfully deleted
    }
```
