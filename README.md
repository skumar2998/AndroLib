AndroLib
========

General purpose android library.

AndroLib Classes
----------------

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

### SimpleCrypto ###


```java
import net.compactsys.androlib.crypto.SimpleCrypto;

// Encrypt String
String encrypted = SimpleCrypto.encrypt(original, getContext());

// Decrypt String
String decrypted = SimpleCrypto.decrypt(encrypted, MyApplication.getInstance().getApplicationContext());

```