jsontojava
==========

Generate Java class from JSON file.

Convert this

```javascript
{
  "Home": {
    "loginUsername": "log_username",
    "loginPassword": "log_password"
  }
}
```

to

```java
public class Root {
  public static class Home {
    public final String loginUsername = "log_username";
    public final String loginPassword = "log_password";
  }
}
```


I needed this little utility library for a project where I had to share HTML element IDs between Python Selenium tests and Java based GWT code. Python understands JSON quite well and for Java side I wanted something statically compiled and that's the reason for converting the JSON to one Java class with internal static classes.

Currently only String and long attributes are supported but adding some other types should be trivial.
