# BLIP Java SDK
A Java SDK for interacting with the Balihoo Local Information Platform (BLIP).

## Requirements
- Using this SDK requires that you already have API keys and are interacting on behalf of a brand that already exists in Balihoo's system. Please contact Balihoo if you require API keys and/or would like to add a new brand to our system.

## Usage
```java
    // Construct a Blip object
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    
    // Make a method call and return the response
    BlipResponse brandResponse = blip.getBrandKeys();

    // Status Code (e.g. 200, 204, 404, etc.)
    int responseCode = brandResponse.STATUS_CODE;

    // Response Content
    String myBrands = brandResponse.BODY;
```

## Methods
All methods return a **BlipResponse** object with two properties:
- **STATUS_CODE**
  - The HTTP response code (e.g. 200, 204, 404) as an integer.
- **BODY**
  - The HTTP response content as a string.
  - For calls that return brand or location data this property will contain stringified JSON objects.

### **ping**
Ping the BLIP API.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.ping();

    if (blipResponse.STATUS_CODE == 200)
    {
      // Success!
    }
```
---
### **getBrandKeys**
Get a list of brandKeys that the API user is authorized to access.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.getBrandKeys();

    String myBrandKeys = blipResponse.BODY;
```
---
### **getBrandSources**
Get a list of data sources available for an individual brand.

#### Parameters
- brandKey: The unique identifier for a single brand.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.getBrandSources("mybrand");

    String sources = blipResponse.BODY;
```
---
### **getBrandProjections**
Get a list of data projections available for an individual brand.

#### Parameters
- brandKey: The unique identifier for a single brand.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.getBrandProjections("mybrand");

    String projections = blipResponse.BODY;
```
---
### **getLocationKeys**
Get a list of locationKeys for all locations belonging to the specified brand.

#### Parameters
- brandKey: The unique identifier for a single brand.
- projection: Optionally filter data in a single projection. Defaults to "universal".

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.getLocationKeys("mybrand");

    String locationKeys = blipResponse.BODY;
```
---
### **getLocation**
Get data for an individual location within the specified brand.

#### Parameters
- brandKey: The unique identifier for a single brand.
- locationKey: The unique identifier for a single location within the brand.
- projection: Optionally filter data in a single projection. Defaults to "universal".
- includeRefs: Optionally include objects referenced by the location in its data. Defaults to false.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.getLocation("mybrand", "mylocation");

    String locationData = blipResponse.BODY;
```
---
### **queryLocations**
Get data for locations in a single brand filtered by the specified BLIP query.

#### Parameters
- brandKey: The unique identifier for a single brand.
- query: A stringified JSON query used to filter locations.
- view: Optionally specify the view returned. Defaults to "full".

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    String query = "{\"address.state\":{\"equals\":\"ID\"}}";
    BlipResponse blipResponse = blip.queryLocations("mybrand", query);

    String matchingLocations = blipResponse.BODY;
```
---
### **putLocation**
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
    BlipResponse blipResponse = blip.putLocation("mybrand", "mylocation", "mysource", locationDocument);

    if (blipResponse.STATUS_CODE == 204)
    {
        // location was successfully added/updated
    }
```
---
### **deleteLocation**
Delete an individual location.

#### Parameters
- brandKey: The unique identifier for a single brand.
- locationKey: The unique identifier for a single location within the brand.
- source: The name of the data source being used to delete the location

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.deleteLocation("mybrand", "mylocation", "mysource");

    if (blipResponse.STATUS_CODE == 204)
    {
        // location was successfully deleted
    }
```
---
### **bulkLoad**
Load a single file containing multiple locations.
 
#### Parameters
- brandKey: The unique identifier for a single brand.
- source: The unique identifier for the data source being used to add/update the location.
- filePath: The full path to the bulk location file.
- implicitDelete: Whether or not to delete locations from BLIP if they're missing from the file.
- expectedRecordCount: The number of location records to expect in the file.
- successEmail: An optional email address to notify upon success. Can be a comma-delimited list.
- failEmail: An optional email address to notify upon failure. Can be a comma-delimited list.
- successCallbackUrl: An optional URL to call upon success.
- failCallbackUrl: An optional URL to call upon failure.

#### Example
```java
    Blip blip = new Blip("<Your API Key>", "<Your Secret Key>");
    BlipResponse blipResponse = blip.bulkLoad("mybrand", "mysource", "/tmp/myfile.json", true, 50, 
                                              "success@mycompany.com", "error@mycompany.com,me@mycompany.com",
                                              "http://mycompany.com/api?success=true", "http://mycompany.com/api?error=true");

    if (blipResponse.STATUS_CODE == 204)
    {
        // File load has been successfully initiated.
        // Optional success and failure notifications will be made once the load process compeltes.
    }
```

#### Bulk Load File Format
The file for the bulkLoad process should contain each location's data on a single line and each line delimited by a line-feed character (\n). If a location is omitted from the file and implicitDelete is set to true, the location will be deleted.

##### Example
{"brandKey":"mybrand","locationKey":"ABC123","document":{...}}\n
{"brandKey":"mybrand","locationKey":"ABC124","document":{...}}\n
{"brandKey":"mybrand","locationKey":"ABC125","document":{...}}\n