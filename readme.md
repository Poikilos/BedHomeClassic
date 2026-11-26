# BedHomeClassic

Sets your **real Essentials home** when you sleep in a bed. Compatible with Tekkit Classic (CraftBukkit 1.2.5) and Essentials 2.9.2.

## What it does

- On `PlayerBedEnterEvent` (sleeping in a bed), it sets the Essentials home named `"home"` (configurable).
- The location is **half a block above the clicked bed block** by default. Set `always-set-foot: true` to always use the foot instead.
- If `set-if-obstructed` is false and the first or second block above the bed is not air, the home is not set and a warning is shown.
- Uses the Essentials API (`User.setHome(String, Location)`) so it does **not** require the `essentials.sethome` permission.
- Soft-depends on Essentials. No extra commands; `/home` just works.

## Requirements

- CraftBukkit 1.2.5 (Tekkit Classic)
- Essentials 2.9.2 (or compatible 2.x)

## Environment setup (Debian/Ubuntu, 2026)

CraftBukkit 1.2.5 and Essentials 2.9.2 were built against Java 6/7. Use **OpenJDK 8** for compiling and running against these jars. Newer JDKs can work with flags but are more likely to cause issues.

### 1. Install Java 8 alongside your existing JDK

Your system default can stay on Java 22. Install Java 8 only for this project:

```bash
sudo apt update
sudo apt install openjdk-8-jdk
```

Java 8 will land in `/usr/lib/jvm/java-8-openjdk-amd64` (or similar). Do **not** run `update-alternatives` to make it the system default.

### 2. Point the project at Java 8

**Eclipse (recommended):**
- The project already requests Java 8 via `.classpath` (`JavaSE-1.8`) and `.settings/org.eclipse.jdt.core.prefs`.
- In Eclipse: Window → Preferences → Java → Installed JREs → Add → Standard VM → point it at the Java 8 directory (`/usr/lib/jvm/java-8-openjdk-amd64`).
- Name it `JavaSE-1.8` if Eclipse does not detect it automatically.
- Right-click the project → Properties → Java Build Path → Libraries → ensure the JRE is JavaSE-1.8.
- Right-click the project → Properties → Java Compiler → set compliance level to 1.8.

**Command line:**
Use the included `build.sh` (it looks for Java 8 without changing your default):

```bash
chmod +x build.sh
./build.sh
```

Or manually:

```bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export PATH="$JAVA_HOME/bin:$PATH"
javac -source 1.8 -target 1.8 -cp "libs/Tekkit.jar:libs/Essentials.jar" -d bin $(find src -name "*.java")
```

### 2. (Optional) Install Eclipse

If you prefer the Eclipse workspace style (matching the CB 1.2.5 example workspace):

```bash
sudo apt install eclipse
```

Or download the Eclipse IDE for Java Developers from eclipse.org and extract it.

### 3. Prepare the libraries

```bash
mkdir -p ~/BedHomeClassic/libs
# Copy your Tekkit.jar and Essentials.jar into libs/
# Example:
# cp /path/to/Tekkit.jar ~/BedHomeClassic/libs/
# cp /path/to/Essentials.jar ~/BedHomeClassic/libs/
```

### 4. Import / set up the project

**Eclipse:**
1. File → Import → Existing Projects into Workspace.
2. Select the BedHomeClassic folder.
3. The `.classpath` already points at `libs/Tekkit.jar` and `libs/Essentials.jar`.
4. Project → Clean if needed.
5. Export → JAR file. Include `plugin.yml` and `config.yml` at the root of the JAR (not inside a folder).

**Command line (uses the build script above):**

```bash
chmod +x build.sh
./build.sh
```

### 5. Install on the server

Copy the resulting `BedHomeClassic.jar` into your Tekkit Classic server’s `plugins/` folder alongside Essentials.jar. Restart the server.

## Essentials config recommendations

These settings are from the Essentials 2.9.2 config. Adjust them in `plugins/Essentials/config.yml`.

In this version, `/home bed` is not a named home. `Commandhome` treats the argument `bed` as a special case and teleports to the vanilla bed spawn (`Player.getBedSpawnLocation()`), if that location still contains a bed. The “Home set.” message on right-click comes from the same path: when `update-bed-at-daytime` is true, a bed click calls `setBedSpawnLocation` and then sends the `homeSet` message.

### Stop Essentials from setting or announcing the bed point

```yaml
# Allows people to set their bed at daytime
# Set false so right-clicking a bed does nothing (no setBedSpawnLocation, no "Home set." message).
# Vanilla still sets the bed spawn only when the player successfully sleeps at night.
update-bed-at-daytime: false
```

There is no config switch that disables the `/home bed` special case itself. It only works when a vanilla bed spawn is set. With the option above false, a daytime click never sets it. Sleeping at night still sets it (vanilla behavior, not intercepted).

### No extra numbered homes

```yaml
# Recommend limiting to a single home so only the home BedHomeClassic sets is used.
sethome-multiple:  
  default: 1
```

Or remove the `sethome-multiple` block if you want to disable multiple homes entirely. The `home.others` entry in `player-commands` only controls `/home <otherplayer>` and is unrelated.

### Do not intercept vanilla respawn

```yaml
# Stock default. Leave false so Essentials does not override vanilla respawn.
# Players respawn at the bed only if they actually slept and the bed is still there.
respawn-at-home: false
```

With these, `/home` (no arguments) uses the Essentials home that BedHomeClassic sets, and Essentials does not set or announce a bed home on click.

After editing, restart or run `/essentials reload`.

## Permissions

- `bedhome.use` — allow the bed to set home (default: true)
- `bedhome.admin` — admin commands (default: op)

## Commands

- `/bedhome reload` — reload the config from disk (requires `bedhome.admin`)
- `/bedhome set-home-even-if-cannot-sleep <true|false>` — change the setting and save immediately (requires `bedhome.admin`)

## Config

```yaml
enabled: true
home-name: "home"
# Set home on right-click even if the player cannot sleep (daytime, etc.)
set-home-even-if-cannot-sleep: true
# If true, always set home at the foot of the bed. If false, use the clicked block.
always-set-foot: false
# If true, set home even when the bed is obstructed (skips the check).
set-if-obstructed: true
# If true, also set the vanilla bed spawn (the point /home bed uses).
# Set on the next tick as well so vanilla/Essentials cannot overwrite it.
set-spawn: true
# If true, suppress the success message at night to avoid a double message with Essentials.
silent-night: true
messages:
  home-set: "&7Home set to your bed."
  obstructed: "&cBed is obstructed (home not set)."
```

## License

Use freely. Based on the idea of HeroCC's BedHome, rewritten for Essentials integration on pre-1.7 Bukkit.
