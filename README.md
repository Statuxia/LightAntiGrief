<div id="header" align="center">
    <img src="https://github.com/Statuxia/LightAntiGrief/assets/60938251/fcf11780-ad84-4ea2-8b73-2229d9717495" width="200"/>
</div>

<div id="badges" align="center">
    <img src="https://shields.io/badge/build-passing-brightgreen?style=for-the-badge" alt="build"/>
    <img src="https://shields.io/badge/source%20code-open-brightgreen?style=for-the-badge" alt="codeopen"/>
    <img src="https://shields.io/badge/language-java-orange?style=for-the-badge" alt="javalang"/>
</div>

<div id="title" align="center">
  <h1>LightAntiGrief | LAG</h1>
  <h5>Paper build</h5>
</div>

<p>
  LightAntiGrief is a solution that helps prevent large-scale violations such as theft or griefing from players. It will be useful on servers with a focus on vanilla survival, where plugins like WorldGuard are not used to protect the territory.
  <br><br>
  LightAntiGrief depends on <a href="https://github.com/PlayPro/CoreProtect">CoreProtect</a>, so for it to work correctly you need to install it.
</p>
<h2>Theory</h2>
<p>
  Most violations occur from players with little online time who either do not know the rules or deliberately break the rules, so the plugin is aimed specifically at this group of players.
  <br><br>
  Also, different players play in different time zones, which can lead to cases where moderators are not online, so the plugin automatically blocks violators who engage in questionable activities. This may be due to the use of triggers that reduce the chance of false positives.
  <br><br>
  The plugin notifies moderators on the network and the console about the violator, the type of violation and the location of the violation, so that moderators will be notified of violations, and administrators will be able to see special actions (like putItem which triggers when player put items on storage entity) in the logs.
</p>

<div id="configuration">
    <h2>Configuration</h2>
  
Key | Default Value | Action
--- | --- | ---
trustedTime | 21600 | Time a player needs to be online to become trusted (in seconds)
fireCharge | 7 | Number of positives before blocking (If there are no moderators)
getItem | 12 | Number of positives before blocking (If there are no moderators)
putItem | 12 | Number of positives before blocking (If there are no moderators)
breakBlock | 5 | Number of positives before blocking (If there are no moderators)
placeBlock | 6 | Number of positives before blocking (If there are no moderators)
minecart | 3 | Number of positives before blocking (If there are no moderators)
explode | 4 | Number of positives before blocking (If there are no moderators)
triggerRandomBonus | true | Responsible for adding a random additional number of triggers
banReason | "You have been banned for suspected griefing" | Ban reason
</div>

<div id="commands">
    <h2>Configuration</h2>
  
Command | Permission | Action
--- | --- | ---
/tpworld {x} {y} {z} {world} | lag.moder | Teleports the moderator to the coordinates of a specific world.
/marktrusted {player} | lag.moder | Marks (or remove mark) player as trusted, as if he played trustedTime
</div>

Enjoy safe game ;)
