# neur0flow - Knockback Refined for neur0pvp

**neur0flow** is a fork of the renowned KnockbackSync plugin, specifically configured and optimized for the **neur0pvp**
server.

Tired of inconsistent knockback ruining your PvP experience on neur0pvp? Our plugin, neur0flow, recalculates knockback
as if it were done clientside, leveling the playing field and ensuring every player enjoys a fair fight, no matter their
connection quality.

Minecraft doesn’t factor in network latency when determining a player's actions on the server. This causes the server to
receive outdated information that doesn’t reflect the player's clientside position, leading to varying knockback effects
based on connection quality.

This plugin intercepts and adjusts knockback calculations to match what would occur clientside, effectively mitigating
the disadvantages caused by high latency. By synchronizing knockback handling with neur0flow, we ensure that players on
neur0pvp experience consistent and fair knockback, providing a balanced and competitive environment for all.

Showcase (Original KnockbackSync): https://www.youtube.com/watch?v=SVokpr3v-TA

Original KnockbackSync Discord (for general queries about the base mechanics): https://discord.gg/nnpqpAtyVW
*(For neur0pvp specific support, please use neur0pvp community channels)*

## Frequently Asked Questions (FAQ)

### Does this change put high ping players at a disadvantage?

**It depends on the player.** Some may notice a difference if they're used to relying on high ping to reduce knockback.
For others, it could actually be an advantage.

### How does this change benefit high ping players?

**Knockback control.** For example, it will be easier to escape crit chains and punish crit.

### Why is the ping offset configured as it is in neur0flow?

**It promotes consistency across neur0pvp.** Extensive testing with top players has shown that the chosen offset for
neur0flow (based on the original's findings of 25ms being a good general baseline) provides a balanced experience for
everyone on our server. This ensures that the PvP mechanics remain stable and predictable for neur0pvp's competitive
environment.

### How do I change the ping offset (for development/testing purposes)?

**neur0flow is pre-configured for neur0pvp.** The ping offset variable is set within the `PlayerData` class. Modifying
this requires altering the source code and recompiling the plugin. This approach maintains a standardized experience on
neur0pvp.

## Primary Server Using This Fork

neur0flow is developed and maintained for:
| Server | Focus | Ping Offset (Reference) | spike_threshold (Reference) |
|----------------|-----------|-------------------------|-----------------------------|
| `neur0pvp.com` (Example IP) | Competitive PvP | 25 (Default)            | 20 (Default)                |
*(Please update the IP and confirm offset/threshold values for neur0pvp)*

## Commands
---

### /neur0flow ping [target]

**Description:**

This command allows you to check the ping of a player, including jitter. If no target is specified, it will check your
own ping.

**Permissions:**

* `neur0flow.ping` (defaults to true for players)

**Examples:**

* `/neur0flow ping`: Checks your own ping.
* `/neur0flow ping Steve`: Checks the ping of a player named Steve.

**Output:**

* If a pong packet has been received: "Your last ping packet took [ping]ms. Jitter: [jitter]ms." or "[Player]'s last
  ping packet took [ping]ms. Jitter: [jitter]ms."
* If a pong packet has not been received: "Pong not received. Your estimated ping is [estimated ping]ms." or "Pong not
  received. [Player]'s estimated ping is [estimated ping]ms."

**Notes:**

* The estimated ping is based on the player's platform reported ping.
* Jitter represents the variation in ping over time.

---

### /neur0flow status [target]

**Description:**

This command allows you to check the neur0flow status of a player or the server. If no target is specified, it will show
both the global status and your own status.

**Permissions:**

* `neur0flow.status.self` (defaults to true for players): Allows checking your own status.
* `neur0flow.status.other` (defaults to op only): Allows checking the status of other players.

**Examples:**

* `/neur0flow status`: Shows the global status and your own status.
* `/neur0flow status Steve`: Shows the status of a player named Steve.

**Output:**

* **Global status:** "Global neur0flow status: [Enabled/Disabled]"
* **Player status:** "[Player]'s neur0flow status: [Enabled/Disabled]" (or "Disabled (Global toggle is off)" if the
  global toggle is off)

**Notes:**

* The player status will be "Disabled" if the global toggle is off, even if the player has individually enabled
  neur0flow.
* The messages displayed by this command are configurable in the `config.yml` file.

---

### /neur0flow toggle [target]

**Description:**

This command allows you to toggle neur0flow for yourself, another player, or globally.

**Permissions:**

* `neur0flow.toggle.self` (defaults to true for players): Allows toggling neur0flow for yourself.
* `neur0flow.toggle.other` (defaults to op only): Allows toggling neur0flow for other players.
* `neur0flow.toggle.global` (defaults to op only): Allows toggling neur0flow globally for the server.

**Examples:**

* `/neur0flow toggle`: Toggles neur0flow globally (if you have permission) or for yourself.
* `/neur0flow toggle Steve`: Toggles neur0flow for a player named Steve.

**Output:**

* **Global toggle:** Sends a message indicating whether neur0flow has been enabled or disabled globally. The messages
  are configurable in the `config.yml` file.
* **Player toggle:** Sends a message indicating whether neur0flow has been enabled or disabled for the specified player.
  The messages are configurable in the `config.yml` file.
* **Ineligible player:** If a player is ineligible for neur0flow (e.g., due to permissions), a configurable message will
  be sent.
* **neur0flow disabled:** If neur0flow is disabled globally and you try to toggle it for a player, a message will be
  sent indicating that neur0flow is disabled.

**Notes:**

* If neur0flow is disabled globally, toggling it for a player will have no effect until neur0flow is enabled globally.
* Players can only toggle neur0flow for themselves if they have the `neur0flow.toggle.self` permission.
* Operators can toggle neur0flow for other players and globally.

---

### /neur0flow reload

**Description:**

This command reloads the neur0flow plugin's configuration file.

**Permissions:**

* `neur0flow.reload` (defaults to op only)

**Examples:**

* `/neur0flow reload`

**Output:**

* Sends a message to the command sender indicating that the configuration has been reloaded. The message is configurable
  in the `config.yml` file.

**Notes:**

* This command is useful for applying changes made to the configuration file without restarting the server.

---

### /neur0flow toggleoffground

**Description:**

This command toggles the experimental off-ground knockback synchronization feature.

**Permissions:**

* `neur0flow.toggleoffground` (defaults to op only)

**Examples:**

* `/neur0flow toggleoffground`

**Output:**

* Sends a message indicating whether the experimental off-ground knockback synchronization has been enabled or disabled.
  The messages are configurable in the `config.yml` file.

**Notes:**
* This feature is experimental and may not work as expected. Use with caution on neur0pvp.

## License

This fork, neur0flow, inherits the license of the original KnockbackSync plugin:
GNU General Public License v3.0 or later

See [LICENSE](https://www.gnu.org/licenses/gpl-3.0.en.html) to see the full text.
