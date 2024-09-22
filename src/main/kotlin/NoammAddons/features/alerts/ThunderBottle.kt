package NoammAddons.features.alerts

import NoammAddons.NoammAddons.Companion.config
import NoammAddons.NoammAddons.Companion.mc
import NoammAddons.events.Chat
import NoammAddons.sounds.mariolikesound
import NoammAddons.sounds.notificationsound
import NoammAddons.utils.ChatUtils.addColor
import NoammAddons.utils.ChatUtils.removeFormatting
import NoammAddons.utils.ChatUtils.showTitle
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


object ThunderBottle {
    private val noThunderBottle = "&e&l⚠ &4No Thunder Bottle &e&l⚠ ".addColor()
    private val fullThunderBottle = "&e&l⚠ &9&lTHUNDER BOTTLE FULL &e&l⚠ ".addColor()
    private val regex = Regex("-+\\n.+entered (MM |)The Catacombs, Floor VII!\\n-+")
    // https://regex101.com/r/8dYYOL/1


    @SubscribeEvent
    fun onChat(event: Chat) {
		val msg = event.component.unformattedText.removeFormatting()
	    when {
		    msg.matches(regex) && config.NoThunderBottleAlert -> {
			    if (!(mc.thePlayer.inventory.mainInventory.any { it?.displayName?.removeFormatting() == "Empty Thunder Bottle" })) {
				    showTitle(noThunderBottle)
				    mariolikesound.play()
				    return
			    }
			}
		    msg == "> Your bottle of thunder has fully charged!" && config.FullThunderBottleAlert -> {
			    showTitle(fullThunderBottle)
			    notificationsound.play()
			    return
			}
		}
    }
}