package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import java.util.List;

public class DiamondPickUpgradeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_DIAMOND_PICK_UPGRADE = "Diamond Drill Upgrade";
    public static final ItemStack emulatedTool = new ItemStack(Items.DIAMOND_PICKAXE);

    public DiamondPickUpgradeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addInstallCost(new ItemStack(Items.DIAMOND, 3));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_DIAMOND_PICK_UPGRADE;
    }

    @Override
    public String getUnlocalizedName() { return "diamondPickUpgrade";
    }

    @Override
    public String getDescription() {
        return "Add diamonds to allow your pickaxe module to mine Obsidian. *REQUIRES PICKAXE MODULE TO WORK*";
    }

    @Override
    public String getTextureFile() {
        return "diamondupgrade1";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
//        if (!Items.IRON_PICKAXE.canHarvestBlock(block, stack)) {
//            if (Items.diamond_pickaxe.canHarvestBlock(block, stack)) {
//                if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, PickaxeModule.PICKAXE_ENERGY_CONSUMPTION)) {
//                    return true;
//                }
//            }
//        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
//        int meta = world.getBlockMetadata(x,y,z);
//        if (canHarvestBlock(stack, block, meta, player) && !PickaxeModule.harvestCheck(stack, block, meta, player)) {
//            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, PickaxeModule.PICKAXE_ENERGY_CONSUMPTION));
//            return true;
//        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
//        event.newSpeed = (float) ModuleManager.computeModularProperty(event.entityPlayer.inventory.getCurrentItem(),
//                PickaxeModule.PICKAXE_HARVEST_SPEED);
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.diamondPickUpgrade;
    }
}