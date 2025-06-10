package us.timinc.mc.cobblemon.optionscontim

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.neoforged.fml.common.Mod
import us.timinc.mc.cobblemon.optionscontim.config.ConfigBuilder
import us.timinc.mc.cobblemon.optionscontim.config.OptionsConTimConfig

@Mod(OptionsConTimMod.MOD_ID)
object OptionsConTimMod {
    const val MOD_ID = "optionscontim"

    @Suppress("MemberVisibilityCanBePrivate")
    var config: OptionsConTimConfig = ConfigBuilder.load(OptionsConTimConfig::class.java, MOD_ID)

    init {
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe { evt ->
            val pokemon = evt.pokemon.pokemon
            if (!evt.pokemon.isBattling && !config.outOfBattleCaptures.getValue(pokemon)) evt.cancel()
        }
    }
}