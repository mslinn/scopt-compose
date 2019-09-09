# SCOPT 4 Composable Parser

[![License](https://licensebuttons.net/p/zero/1.0/88x31.png)](https://creativecommons.org/share-your-work/public-domain/cc0/)
[![Build Status](https://travis-ci.org/mslinn/scopt-compose.svg?branch=master)](https://travis-ci.org/mslinn/scopt-compose)
[![GitHub version](https://badge.fury.io/gh/mslinn%2Fscopt-compose.svg)](https://badge.fury.io/gh/mslinn%2Fscopt-compose)

Working version of the last sample program listing shown in
[composing configuration datatype](http://eed3si9n.com/scopt4) code for 
[scopt](https://github.com/scopt/scopt).

## Running the Program
The `bin/run` Bash script assembles this project into a fat jar and runs it.
Sample usage, which runs the `Main` entry point in `src/main/scala/Main.scala`:

```
$ bin/run Main
```

The `-j` option forces a rebuild of the fat jar. 
Use it after modifying the source code.

```
$ bin/run -j Main
```