# Usage of builder
```
java -jar builder.jar [clean] [repo sync] [use ccache] [ccache dir] [device target] [make target] [upload] [credentials]
```

### Arguments

1. **clean: 1 or 0: Whether clean working directory or not (int)**
2. **repo sync: 1 or 0: Whether sync working directory or not (int)**
3. **use ccache: 1 or 0: Whether use ccache or not (int)**
4. **ccache dir: working directory for ccache (String)**
5. **device target: device target for lunch (String)**
6. **make target: make target (String)**
7. **upload: 1 or 0: Whether upload output file (String)**
8. **credentials: user:passwd: Credentials for upload (String)**

## Example
```
java -jar builder.jar 1 1 1 ~/.ccache bullhead-userdebug otapackage 1 argraur:password
```
