package NoammAddons.features.dungeons


import NoammAddons.NoammAddons.Companion.config
import NoammAddons.NoammAddons.Companion.hudData
import NoammAddons.NoammAddons.Companion.mc
import NoammAddons.config.EditGui.HudElement
import NoammAddons.config.KeyBinds
import NoammAddons.events.ClickEvent
import NoammAddons.events.PacketEvent
import NoammAddons.events.RenderOverlay
import NoammAddons.utils.BlockUtils.blacklist
import NoammAddons.utils.BlockUtils.getBlockAt
import NoammAddons.utils.BlockUtils.toAir
import NoammAddons.utils.ItemUtils.getItemId
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemTool
import net.minecraft.network.play.client.C07PacketPlayerDigging
import net.minecraft.network.play.client.C0APacketAnimation
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent


object GhostPick {
    private val GhostPickElement = HudElement("&b&lGhostPick: &a&lEnabled", dataObj = hudData.getData().GhostPick)
    private var featureState = false
    private fun isAllowedTool(itemStack: ItemStack) = itemStack.item is ItemTool


    @SubscribeEvent
    fun onPacket(event: PacketEvent.Sent) {
        if (!featureState) return
        val heldItem = mc.thePlayer?.heldItem ?: return

        if (event.packet is C07PacketPlayerDigging && config.LegitGhostPick) {
            if (heldItem.getItemId() in listOf(261, 46)) return
            event.isCanceled = true
        }

        if (event.packet is C0APacketAnimation && config.MimicEffi10) {
            val blockPos = mc.objectMouseOver?.blockPos
            if (!isAllowedTool(heldItem)) return
            toAir(blockPos)
        }
    }

	
    @SubscribeEvent
    fun enable(event: ClientTickEvent) {
        if (!config.GhostPick) featureState = false

        if (KeyBinds.GhostPick.isPressed) featureState = !featureState
    }

	
    @SubscribeEvent
    fun ghostBlocks(event: ClickEvent.RightClickEvent) {
        if (mc.gameSettings.keyBindUseItem.isKeyDown && config.GhostBlocks && featureState) {
            if (!isAllowedTool(mc.thePlayer?.heldItem ?: return)) return
            val blockpos = mc.thePlayer.rayTrace(100.0, .0f)?.blockPos ?: return
            toAir(blockpos)
            if (mc.theWorld.getBlockAt(blockpos) !in blacklist) event.isCanceled = true
        }
    }

	
    @SubscribeEvent
    @Suppress("UNUSED_PARAMETER")
    fun draw(event: RenderOverlay) {
        if (!featureState) return
	    if (!config.GhostPick) return
	    
        GhostPickElement.draw()
    }
}
