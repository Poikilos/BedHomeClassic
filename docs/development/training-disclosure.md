# Training Disclosure for BedHomeClassic
This Training Disclosure, which may be more specifically titled above here (and in this document possibly referred to as "this disclosure"), is based on **Training Disclosure version 1.1.4** at https://github.com/Hierosoft/training-disclosure by Jake Gustafson. Jake Gustafson is probably *not* an author of the project unless listed as a project author, nor necessarily the disclosure editor(s) of this copy of the disclosure unless this copy is the original which among other places I, Jake Gustafson, state IANAL. The original disclosure is released under the [CC0](https://creativecommons.org/public-domain/cc0/) license, but regarding any text that differs from the original:

This disclosure also functions as a claim of copyright to the scope described in the paragraph below since potentially in some jurisdictions output not of direct human origin, by certain means of generation at least, may not be copyrightable (again, IANAL):

Various author(s) may make claims of authorship to content in the project not mentioned in this disclosure, which this disclosure by way of omission unless stated elsewhere implies is of direct human origin unless stated elsewhere. Such statements elsewhere are present and complete if applicable to the best of the disclosure editor(s) ability. Additionally, the project author(s) hereby claim copyright and claim direct human origin to any and all content in the subsections of this disclosure itself, where scope is defined to the best of the ability of the disclosure editor(s), including the subsection names themselves, unless where stated, and unless implied such as by context, being copyrighted or trademarked elsewhere, or other means of statement or implication according to law in applicable jurisdiction(s).

Disclosure editor(s): Hierosoft LLC

Project author: Hierosoft LLC

This disclosure is a voluntary of how and where content in or used by this project was produced by LLM(s) or any tools that are "trained" in any way.

The main section of this disclosure lists such tools. For each, the version, install location, and a scope of their training sources in a way that is specific as possible.

Subsections of this disclosure contain prompts used to generate content, in a way that is complete to the best ability of the disclosure editor(s).

tool(s) used:
- Grok

Scope of use: code described in subsections--typically modified by hand to improve logic, variable naming, integration, etc, but in this commit, unmodified.


## *
- 2026-11-26 https://grok.com/project/85e4da7a-af65-41cd-bb02-ef1505e60dc0?chat=1d2f7e9d-f65e-4a7e-a520-252fe385a47e

Tekkit Classic and CB 1.2.5 (and possibly Hexxit CB 1.5.2) compatibility build/fork/redo (whichever is necessary) of HeroCC's BedToHome.

Investigate original first, then decide
Modern modding experience, new to pre-1.7

Ok, lets see if we even need any modifications, need to revert to an earlier tag/commit, and/or just need to recompile against Tekkit.jar. Example workspace: https://github.com/Poikilos/WildPortal/raw/refs/heads/master/workspace-CB-1_2_5.zip

Lets call it BedHomeClassic. Lets make sure we set the real player home (compatible with Essentials 2.9.2) not the "/home bed" home nor make some third thing like /bedhome. Just set home (without requiring user to have sethome permission, preferably) so that "/home" works and goes back to there (1 unit above the block clicked, not point where player was standing when player clicked). See if there is some older mod to fork before continuing, otherwise it is looking like we are nearly rewriting this anyway.

Why are you guessing what API endpoints are available? Focus on Tekkit Classic.
- attach Tekkit.jar (CB 1.2.5)

Refer to the following content:
- attach Essentials.jar (2.9.2)

ok, make a BedHomeClassic folder representing the repo and implement the plan.

My website is poikilos.org, should we use org.poikilos instead of me.poikilos?

now update any other files as necessary where me.poikilos is mentioned.

Make the readme.md filename all lowercase. Explain the full environment setup for Debian/Ubuntu in 2026 in there.

The default java on my system is still:
```
redacted@redacted:~/metaprojects/nomcproxy-Grok$ java -version
openjdk version "22.0.2" 2024-07-16
OpenJDK Runtime Environment Corretto-22.0.2.9.1 (build 22.0.2+9-FR)
OpenJDK 64-Bit Server VM Corretto-22.0.2.9.1 (build 22.0.2+9-FR, mixed mode, sharing)
redacted@redacted:~/metaprojects/nomcproxy-Grok$ javac -version
javac 22.0.2
```
Without changing it, can we adjust the project file(s) to use the correct java?

When importing project into existing workspace, do I have to check "Add project to working sets"?
- No

- 2026-11-26

Make a plugin configuration option to set home even if not able to sleep, true by default. Bump version to 1.1.

Add a command to change this accepting true or false and saving the setting immediately. Do not bump version this time (or any time unless I say so).

Implement and document a reload subcommand.

make the main command bedhome for brevity

Make the permissions start with bedhome as well

Make the success message gray instead of green, and have period instead of exclamation point. We don't have to go up a whole unit to set the home point, just a half, because the bed is only half a unit high. Is it possible to detect whether the head or the foot of the bed was clicked? If so lets always set home based on the foot of the bed. Also, if the bed is obstructed (first or second block above either head or foot is not air), don't set home, and instead show a warning saying bed is obstructed (home not set).

Place any of the lines from GitHub's default Java.gitignore in .gitignore where applicable and remove any redundant lines you had:
- paste that
Then before any additional lines make a new commented section.
```
## Project-specific files
```

Add instructions in readme for disabling the "/home bed" point being set by essentials while clicking the bed, as that is now redundant.

Recommend disabling these when using this plugin:
   - `essentials.home.bed`
   - `essentials.sethome.bed`
Also add a subsection under that explaining applicable PermissionsEx operations:
```
/pex group Member add essentials.home
/pex group Member remove essentials.home.bed
/pex group Member remove essentials.sethome.bed
/pex group Member remove essentials.sethome
/pex reload
```

There is an issue where apparently the Member group still has those permissions by default. The remove commands didn't have an effect.

No, then guests may be able to do it. Explain how to edit the default granted permissions instead.
- paste plugins/Essentials/config.yml

You hallucinated that, maybe from a later version of essentials. It is not in the config I just provided. Focus on that file and CB 1.2.5. Maybe look into the permission list, which has: home, home.others. See also
sethome-multiple (recommend turning that off),  update-bed-at-daytime, and recommend respawn-at-home: true.
- It had recommended `bedSetsHome: false` and said it was in the EssentialsHome section of the config but that settings isn't there and that setting doesn't match the variable naming conventions.

What is "the vanilla bed spawn update"? When does a player respawn at bed in vanilla in MC 1.2.5? If it is vanilla why is respawn-at-home false (that would be a weird default--maybe it is something I set?)? What are we actually changing with update-bed-at-daytime, the same data as "/home" uses, or not? Does home.others only affect multi-home, or also use/set of the point "/home bed" uses?

You're wrong, “/home bed” has been working in Essentials 2.9.2 on this server. I want it to do nothing, and never be set (No "Home set." message or action but ours. No additional numbered homes. No interception of Vanilla behavior. Just "/home" which goes to the "real" home we are setting in BedHomeClassic).

Ok, everything works now with preventing setting home or home bed with commands for members, but now we have one issue: players can type "/home bed". Lets turn that command off via PermissionsEx.

You shouldn't be looking for essentials.home.bed in the Essentials jar. Maybe look for home.bed or even home and the surrounding code.

- 2026-11-26

At night, the home-set message (or bed is obstructed) happens twice. To keep the points synchronized, lets make a couple changes. Add the option always-set-foot that defaults to false. If false, skip foot detection and set home to whatever bed block was right-clicked. Add a set-if-obstructed option that defaults to true (and sets home and shows success instead of obstructed if true--skips obstructed check for optimization actually). Add a "set-spawn" option, true by default, which if true, sets the player's spawn point as if they had slept (same data as "/home bed" uses), under the scope of the same logic as when we set home. For example, assuming essentials "update-bed-at-daytime: false", we right-click the bed at daytime, and our plugin supersedes that and sets the spawn/bed point (if `set-home-even-if-cannot-sleep: true`). Just to cooperate even better and try to eliminate redundant messages, have a silent-night setting that defaults to true, so even if we set the point (if not obstructed, or set-if-obstructed is true), it will be silent so the player won't get two messages (One from essentials, one from us).

Bump version to 1.2.5.2
