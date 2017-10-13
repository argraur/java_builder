# Usage of builder
```
java -jar builder.jar [clean] [repo sync] [use ccache] [ccache dir] [device target] [make target] [upload] [credentials]
```

clean: 1 or 0: Whether clean working directory or not (int)
repo sync: 1 or 0: Whether sync working directory or not (int)
use ccache: 1 or 0: Whether use ccache or not (int)
ccache dir: working directory for ccache (String)
device target: device target for lunch (String)
make target: make target (String)
upload: 1 or 0: Whether upload output file (String)
credentials: user:passwd: Credentials for upload (String)

## Example
```
java -jar builder.jar 1 1 1 ~/.ccache bullhead-userdebug otapackage 1 argraur:password
```
