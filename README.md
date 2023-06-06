# LastSeen

A plugin for viewing when a player was last online.

## Commands

|Command|Description|Permission|
|-------|-----------|----------|
|`/seen <name or UUID>`|Shows when a player was last online.|`lastseen.use`|

Use the permission `lastseen.location` to also show the player's last location when using `/seen`.

## Configuration

The default configuration file can be found [here](https://github.com/Meeples10/LastSeen/blob/main/src/main/resources/config.yml).

|Key|Description|
|---|-----------|
|`date-format`|The format string to use for dates. See [here](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/text/SimpleDateFormat.html) for more information.|
|`messages`|Messages displayed by the plugin.|
