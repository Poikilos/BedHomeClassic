# BedHomeClassic

Sets your **real Essentials home** when you sleep in a bed. Compatible with Tekkit Classic (CraftBukkit 1.2.5) and Essentials 2.9.2.

## What it does

- On `PlayerBedEnterEvent` (sleeping in a bed), it sets the Essentials home named `"home"` (configurable).
- The location is **1 block above the bed block** (center of the top face), not where you were standing.
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

## Permissions

- `bedhomeclassic.use` — allow the bed to set home (default: true)
- `bedhomeclassic.admin` — admin (default: op)

## Config

```yaml
enabled: true
home-name: "home"
messages:
  home-set: "&aHome set to your bed!"
```

## License

Use freely. Based on the idea of HeroCC's BedHome, rewritten for Essentials integration on pre-1.7 Bukkit.
