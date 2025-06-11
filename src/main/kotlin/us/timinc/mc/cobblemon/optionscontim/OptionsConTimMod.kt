package us.timinc.mc.cobblemon.optionscontim

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.neoforged.fml.common.Mod
import org.apache.logging.log4j.LogManager
import us.timinc.mc.cobblemon.optionscontim.config.ConfigBuilder
import us.timinc.mc.cobblemon.optionscontim.config.OptionsConTimConfig

@Mod(OptionsConTimMod.MOD_ID)
object OptionsConTimMod {
    const val MOD_ID = "optionscontim"

    @Suppress("MemberVisibilityCanBePrivate")
    var config: OptionsConTimConfig = ConfigBuilder.load(OptionsConTimConfig::class.java, MOD_ID)

    private val logger = LogManager.getLogger(MOD_ID)

    init {
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe { evt ->
            val pokemon = evt.pokemon.pokemon
            if (!config.outOfBattleCaptures.getValue(pokemon)) {
                if (!evt.pokemon.isBattling) {
                    debug("[outOfBattleCaptures][${pokemon.uuid}](${pokemon.species.name}) Blocked from capture.")
                    evt.cancel()
                }
            }
        }
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST) { evt ->
            val pokemonEntity = (evt.entity as? PokemonEntity ?: return@subscribe)
            val pokemon = pokemonEntity.pokemon
            if (config.onlyDropInBattle.getValue(pokemon)) {
                if (!pokemonEntity.isBattling) {
                    debug("[onlyDropInBattle][${pokemon.uuid}](${pokemon.species.name}) Blocked from dropping.")
                    evt.cancel()
                }
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun debug(msg: String, bypassConfig: Boolean = false) {
        if (!config.debug && !bypassConfig) return
        logger.info(msg)
    }
}