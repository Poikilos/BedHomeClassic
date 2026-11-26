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
