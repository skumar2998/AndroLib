AndroLib
========

General purpose android library.

AndroLib Classes
----------------

#### /data ####
### SQLParser ###

```java
import net.compactsys.androlib.data.SQLParser;

public class DbHelper extends SQLiteOpenHelper {
	//...
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// Load SQL Script from raw resources
		Resources res = mContext.getResources();
		InputStream ins = res.openRawResource(R.raw.database_sql);		
		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		
		try {
			// Extract all statements && execute
			String[] sqlstrings = SQLParser.parseSqlFile(br);
			for (String sql : sqlstrings) {
				db.execSQL(sql);
			}
		} catch (Exception ex) {
			//...
		}
	}
}
```

### /crypto ###
### SimpleCrypto ###


```java
import net.compactsys.androlib.crypto.SimpleCrypto;

// Encrypt String
String encrypted = SimpleCrypto.encrypt(original, getContext());

// Decrypt String
String decrypted = SimpleCrypto.decrypt(encrypted, getContext());

```

### /io ###
#### Filesystem ####

```java
net.compactsys.androlib.io;

// Copy - Copies an existing file to a new file.
int copy(String sourceFileName, String destFileName) {}
int copy(InputStream istream, OutputStream ostream) {}

// SDCard   
String getSDCardPath() {}
boolean isSDCardAvailable() {}
boolean isSDCardWritable(){}

// getFiles() - Gets a list of matching files in the directory.
File[] getFiles(File directory, final String fileRegExpFilter) {}
```

### /util ###
#### StringUtils ####

```java	
import net.compactsys.androlib.util;

// isEmpty
StringUtils.isEmpty("bob")   // false
StringUtils.isNotEmpty("bob") // true

// repeat
StringUtils.repeat("ab", 2)       // "abab"
StringUtils.repeat("?", ", ", 3)  // "?, ?, ?"
StringUtils.repeat('e', 3)        // "eee"

// padding
StringUtils.padRight("bat", 5)       // "bat  "
StringUtils.padRight("bat", 5, '.')  // "bat.."
StringUtils.padRight("bat", 5, "yz") // "batyz"

StringUtils.padLeft("bat", 5)        // "  bat"
StringUtils.padLeft("bat", 5, '.')   // "..bat"
StringUtils.padLeft("bat", 5, "yz")  // "yzbat"

// remove
StringUtils.removeStart("www.domain.com", "www.")           // "domain.com"
StringUtils.removeStartIgnoreCase("www.domain.com", "WWW.") // "domain.com"
StringUtils.removeEnd("www.domain.com", ".com")             // "www.domain
StringUtils.removeEndIgnoreCase("www.domain.com", ".COM")   // "www.domain"
StringUtils.removeFirstN("0000012345", '0')                 // "1235"

```


