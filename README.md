# DeviceWhitelister
This is a program that blocks any unauthorized drive connected to the computer.

You need **Java JDK** for running this program.

## Usage
### Showing list of uuids
`java -jar DeviceWhitelister.jar --show`
#
### Setting the whitelist
*Make sure the adder is on the same folder as the whitelister.*

*Also make sure to have the JDK version of Java and make sure it has `jar` command, it's required.*

`java -jar DeviceWAdder.jar`
#
### Testing program
**_Make sure to set the UUID list first!_**

`java -jar DeviceWhitelister.jar`
#
### Running on startup
*After setting all the whitelist.*

<br>

#### Windows
1. Put the file *DeviceWhitelister.jar* somewhere in your C:\ drive or anywhere accessible.
2. Go to the route `HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\CurrentVersion\Run`
3. Create a new string value with value `"PATH_TO_JAVA\bin\javaw.exe" -jar "PATH_TO_FILE"`
   * **PATH_TO_JAVA:** Path to your java/jdk directory ex. `C:\Program Files\Java\jdk-15.0.2`
   * **PATH_TO_FILE:** Path to the DeviceWhitelister.jar ex. `C:\DeviceW\DeviceWhitelister.jar`

#### Linux
1. Put the file *DeviceWhitelister.jar* somewhere in your filesystem ex. /usr/bin/
2. Create a file named *devicew.service* inside */etc/systemd/system/* with content:
   * **PATH_TO_JAVA:** The path to your java/jdk directory ex. `/usr/lib/jvm/java-11-openjdk-amd64`
   * **PATH_TO_FILE_FOLDER:** The path to the folder of DeviceWhitelister.jar ex. `/usr/bin/DeviceWhitelister.jar`
````ini
[Unit]
Description=DeviceWhitelister service
After=syslog.target network.target

[Service]
SuccessExitStatus=143

User=root
Group=root

Type=simple

Environment="JAVA_HOME=PATH_TO_JAVA"
WorkingDirectory=PATH_TO_FILE_FOLDER
ExecStart=${JAVA_HOME}/bin/java -jar DeviceWhitelister.jar
ExecStop=/bin/kill -15 $MAINPID

[Install]
WantedBy=multi-user.target
````

#
###### Contact: PK2_Stimpy#7089