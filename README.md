# Usage of builder
```
java -jar builder.jar [clean] [repo sync] [use ccache] [ccache dir] [device target] [make target] [upload] [credentials]
```

clean: 1 or 0: Whether clean working directory or not (int)\n
repo sync: 1 or 0: Whether sync working directory or not (int)\n
use ccache: 1 or 0: Whether use ccache or not (int)\n
ccache dir: working directory for ccache (String)\n
device target: device target for lunch (String)\n
make target: make target (String)\n
upload: 1 or 0: Whether upload output file (String)\n
credentials: user:passwd: Credentials for upload (String)\n

## Example
```
java -jar builder.jar 1 1 1 ~/.ccache bullhead-userdebug otapackage 1 argraur:password
```
