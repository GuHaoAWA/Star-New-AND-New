package com.guhao.star.item;

import com.guhao.star.item.renderer.WuSongRenderer;
import net.jobin.stellariscraft.init.StellariscraftModTabs;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import yesman.epicfight.world.item.WeaponItem;

import java.util.List;
import java.util.function.Consumer;

public class WuSongItem extends WeaponItem implements IAnimatable {
    public final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public WuSongItem(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
    }
    public WuSongItem() {
        super(new Tier() {

                  public int getUses() {return 10000;}

                  public float getSpeed() {
                      return 9.0f;
                  }

                  public float getAttackDamageBonus() {
                      return 16f;
                  }

                  public int getLevel() {
                      return 4;
                  }

                  public int getEnchantmentValue() {
                      return 99;
                  }

                  public @NotNull Ingredient getRepairIngredient() {return Ingredient.of(new ItemStack(Items.ENDER_EYE));}

              }, 3, -2.5f,
                new Item.Properties().tab(
                        StellariscraftModTabs.TAB_STELLARIS_CRAFT
                ).fireResistant());
    }
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "idleController", 1, animationEvent -> {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.wusong.new", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }));
    }
    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level p_41422_, @NotNull List<Component> list, @NotNull TooltipFlag p_41424_) {
        super.appendHoverText(itemStack, p_41422_, list, p_41424_);
        list.add(Component.nullToEmpty(I18n.get("star.wusong.explain")));
    }
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private WuSongRenderer renderer = null;
            @Override public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                if (this.renderer == null)
                    this.renderer = new WuSongRenderer();

                return renderer;
            }
        });
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
