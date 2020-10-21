# WIP
**This project isn't ready now.**

-----
Helper Tool for Minecraft Docker Images 

The tool allows different options.

## Start-parameter
There are multiple startparameter:
- download: Download and if necessary build or install the specified version.
- start: Start the specified version.

It is allowed to use both arguments to download and start the game.

## Environment variables
This tool is for docker-images. So the most parameters will be set via environment variables:
- WORKDIR: set the working directory from the game. Default: '/mnt/minecraft'
- MCDIR: set the directory containing the executable. Default: '/opt/minecraft'
- MXEXECUTABLE: set the file-name of the executable jar. Default depends on the type
- MINRAM / MAXRAM: minimum and maximum ram usage. It is advisable to set it to the same value. Default: '4G'
- TYPE: set the type of the server. Possible values:
  - BUKKIT
  - CRAFTBUKKIT
  - FABRIC (https://fabricmc.net/)
  - FORGE (http://files.minecraftforge.net/)
  - PAPER (https://papermc.io/)
  - SPIGOT (https://www.spigotmc.org/)
  - VANILLA 
- COMMAND: set the execution command. Default: 'TODO' Within the command are different values replaced:
  - %executable% -> the complete path to the executable (MCDIR + MCEXECUTABLE)
  - %minram% -> the value of MINRAM
  - %maxram% -> the value of MAXRAM
- RESTARTINTERVAL: Restarts the server in the given time in minutes. Default deactivated => no autorestart.
- STOPTIMEOUT: A time in seconds. After a stop signal the programm waits max this time before hard killing minecraft. Default: 60
- VERSION: The minecraft version to download. Defualt not set, so latest available will be used.
- SUBVERSION: The specific version to download. Usable at following types:
  - FORGE: The forge version. E.g. '34.1.25'
  - FABRIC: The fabric version. E.g. '0.10.3+build.211'
  - PAPER: The paper build number. E.g. '208'
