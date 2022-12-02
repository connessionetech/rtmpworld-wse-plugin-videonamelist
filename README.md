# rtmpworld-wse-plugin-videonamelist

# About
---

The **ModuleVideoNamesList** module for Wowza Streaming Engine™ media server software includes a wowza HTPProvider to help you list out media files from the `content` directory. The module initself has no functionality. To use the HTTPProvider, you need to enlist it in the `{WOWZA_HOME}/conf/VHost.xml` file. 


# Dependency Note
---

NA


# Usage
---

To use the HTTPProvider, you need to enlist it in the `{WOWZA_HOME}/conf/VHost.xml` file. 

```xml

<HTTPProvider>
    <BaseClass>com.rtmpworld.server.wowza.http.HTTPMediaListProvider</BaseClass>
    <RequestFilters>contentlisting*</RequestFilters>
    <AuthenticationMethod>none</AuthenticationMethod>
</HTTPProvider>

```

The HTTP endpoint will then be : `http://localhost:8086/contentlisting`. The endpoint will return a JSON array of media file details of all supported files found in the `content` directory.

> Authentication on the endpoint can be set as per convenience using wowza's standard mechanism. The discussion on that is beyond the scope here.


# Documentation
---

The detailed documentation on the subject can be found [here]
