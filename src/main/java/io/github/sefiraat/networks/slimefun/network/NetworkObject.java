package io.github.sefiraat.networks.slimefun.network;

import io.github.sefiraat.networks.NetworkStorage;
import io.github.sefiraat.networks.network.NodeDefinition;
import io.github.sefiraat.networks.network.NodeType;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import lombok.Getter;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class NetworkObject extends SlimefunItem {

    @Getter
    private final NodeType nodeType;

    public NetworkObject(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, NodeType type) {
        super(itemGroup, item, recipeType, recipe);
        this.nodeType = type;
        addItemHandler(
            new BlockTicker() {

                @Override
                public boolean isSynchronized() {
                    return true;
                }

                @Override
                public void tick(Block b, SlimefunItem item, Config data) {
                    addToRegistry(b);
                }
            },
            new BlockBreakHandler(false, false) {
                @Override
                @ParametersAreNonnullByDefault
                public void onPlayerBreak(BlockBreakEvent event, ItemStack item, List<ItemStack> drops) {
                    NetworkStorage.getAllNetworkObjects().remove(event.getBlock().getLocation());
                    BlockStorage.clearBlockInfo(event.getBlock().getLocation());
                }
            }
        );
    }

    public void addToRegistry(@Nonnull Block block) {
        if (!NetworkStorage.getAllNetworkObjects().containsKey(block.getLocation())) {
            NetworkStorage.getAllNetworkObjects().put(block.getLocation(), new NodeDefinition(nodeType));
        }
    }
}
