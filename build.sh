#!/bin/bash
# Build BedHomeClassic against Java 8 without changing system default.
# Usage: ./build.sh
# Requires OpenJDK 8 installed (sudo apt install openjdk-8-jdk)

set -e

# Prefer explicit JAVA8_HOME, then common locations
if [ -n "$JAVA8_HOME" ]; then
  JAVA_HOME="$JAVA8_HOME"
elif [ -d /usr/lib/jvm/java-8-openjdk-amd64 ]; then
  JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
elif [ -d /usr/lib/jvm/java-1.8.0-openjdk-amd64 ]; then
  JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
else
  echo "Java 8 not found. Install it with: sudo apt install openjdk-8-jdk"
  echo "Or set JAVA8_HOME to your Java 8 install path."
  exit 1
fi

export JAVA_HOME
export PATH="$JAVA_HOME/bin:$PATH"

echo "Using Java: $($JAVA_HOME/bin/java -version 2>&1 | head -1)"

mkdir -p bin
find src -name "*.java" > /tmp/bedhomeclassic-sources.txt
"$JAVA_HOME/bin/javac" -source 1.8 -target 1.8 -cp "libs/Tekkit.jar:libs/Essentials.jar" -d bin @/tmp/bedhomeclassic-sources.txt
rm -f /tmp/bedhomeclassic-sources.txt

cd bin
jar cf ../BedHomeClassic.jar .
cd ..
jar uf BedHomeClassic.jar plugin.yml config.yml

echo "Built BedHomeClassic.jar"
