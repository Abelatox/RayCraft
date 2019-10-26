package com.abelatox.raycraft.handler;


import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.abelatox.raycraft.lib.Constants;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//TODO cleanup
public class InputHandler {

    public static LivingEntity lockOn = null;

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;
      //  SummonKeybladeCapability.ISummonKeyblade SUMMON = player.getCapability(ModCapabilities.SUMMON_KEYBLADE, null);

       /* if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
            Minecraft.getMinecraft().gameSettings.keyBindSwapHands.isPressed();
        }*/

        Keybinds key = getPressedKey();
        if (key != null)
            switch (key) {
               
                case LOCK_ON:
                    if (lockOn == null) {
                        RayTraceResult rtr = getMouseOverExtended(100);
                        if (rtr != null && rtr instanceof EntityRayTraceResult) {
                        	EntityRayTraceResult ert = (EntityRayTraceResult) rtr;
                            if (ert.getEntity() != null) {
                                double distanceSq = player.getDistanceSq(ert.getEntity());
                                double reachSq = 100 * 100;
                                if (reachSq >= distanceSq) {
                                    if (ert.getEntity() instanceof LivingEntity) {
                                        lockOn = (LivingEntity) ert.getEntity();
                                        LockOn.target = (LivingEntity) ert.getEntity();
                                        player.world.playSound((PlayerEntity) player, player.getPosition(), ModSounds.lockon, SoundCategory.MASTER, 1.0f, 1.0f);
                                    } /*else if (rtr.entityHit instanceof EntityPart) {
                                        EntityPart part = (EntityPart) rtr.entityHit;
                                        if (part.getParent() != null && part.getParent() instanceof EntityLivingBase) {
                                            lockOn = (EntityLivingBase) part.getParent();
                                            LockOn.target = (EntityLivingBase) part.getParent();
                                            player.world.playSound((EntityPlayer) player, player.getPosition(), ModSounds.lockon, SoundCategory.MASTER, 1.0f, 1.0f);

                                        }
                                    }*/
                                }
                            }
                        }
                    } else {
                        lockOn = null;
                    }
                    break;
            }
    }

    private Keybinds getPressedKey() {
        for (Keybinds key : Keybinds.values())
            if (key.isPressed())
                return key;
        return null;
    }

    @SubscribeEvent
    public void handleKeyInputEvent(InputEvent.MouseInputEvent event) {
        /*
         * if (player.getCapability(ModCapabilities.DRIVE_STATE, null).getInDrive()) {
         * if (player.getCapability(ModCapabilities.DRIVE_STATE,
         * null).getActiveDriveName().equals(Strings.Form_Wisdom)) {
         * event.setCanceled(true); } else { event.setCanceled(false); } }
         */

        if (event.getButton() == Constants.LEFT_MOUSE && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
        	System.out.println("aa");
        	//event.setCanceled(true);
        }
    }

    /*@SubscribeEvent
    public void OnMouseWheelScroll(online.kingdomkeys.kingdomkeys.handler.ScrollCallbackWrapper.MouseScrolledEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        World world = mc.world;

        if (!mc.isGameFocused() && !KeyboardHelper.isScrollActivatorDown()) {
            event.setCanceled(false);
            return;
        }

        if (event.getYOffset() == Constants.WHEEL_DOWN && KeyboardHelper.isScrollActivatorDown()) {
            commandDown();
            event.setCanceled(true);
        }
        if (event.getYOffset() == Constants.WHEEL_UP && KeyboardHelper.isScrollActivatorDown()) {
            commandUp();
            event.setCanceled(true);
        }

    }*/

    public enum Keybinds {
        LOCK_ON("key.raycraft.lockon",GLFW.GLFW_KEY_LEFT_CONTROL);

        private final KeyBinding keybinding;
        Keybinds(String name, int defaultKey) {
            keybinding = new KeyBinding(name, defaultKey, "key.categories.raycraft");
        }

        public KeyBinding getKeybind() {
            return keybinding;
        }

        public boolean isPressed() {
            return keybinding.isPressed();
        }
    }
    
    public static RayTraceResult getMouseOverExtended(float dist) {
		Minecraft mc = Minecraft.getInstance();
		Entity theRenderViewEntity = mc.getRenderViewEntity();
		AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.posX - 0.5D, theRenderViewEntity.posY - 0.0D, theRenderViewEntity.posZ - 0.5D, theRenderViewEntity.posX + 0.5D, theRenderViewEntity.posY + 1.5D, theRenderViewEntity.posZ + 0.5D);
		RayTraceResult returnMOP = null;
		if (mc.world != null) {
			double var2 = dist;
			returnMOP = theRenderViewEntity.func_213324_a(var2, 0, true);
			double calcdist = var2;
			Vec3d pos = theRenderViewEntity.getEyePosition(0);
			var2 = calcdist;
			if (returnMOP != null) {
				calcdist = returnMOP.getHitVec().distanceTo(pos);
			}

			Vec3d lookvec = theRenderViewEntity.getLook(0);
			Vec3d var8 = pos.add(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2);
			Entity pointedEntity = null;
			float var9 = 1.0F;
			@SuppressWarnings("unchecked")
			List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox.grow(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).grow(var9, var9, var9));
			double d = calcdist;

			for (Entity entity : list) {
				if (entity.canBeCollidedWith()) {
					float bordersize = entity.getCollisionBorderSize();
					AxisAlignedBB aabb = new AxisAlignedBB(entity.posX - entity.getWidth() / 2, entity.posY, entity.posZ - entity.getWidth() / 2, entity.posX + entity.getWidth() / 2, entity.posY + entity.getHeight(), entity.posZ + entity.getWidth() / 2);
					aabb.grow(bordersize, bordersize, bordersize);
					RayTraceResult mop0 = aabb.calculateIntercept(pos, var8);

					if (aabb.contains(pos)) {
						if (0.0D < d || d == 0.0D) {
							pointedEntity = entity;
							d = 0.0D;
						}
					} else if (mop0 != null) {
						double d1 = pos.distanceTo(mop0.getHitVec());

						if (d1 < d || d == 0.0D) {
							pointedEntity = entity;
							d = d1;
						}
					}
				}
			}

			if (pointedEntity != null && (d < calcdist || returnMOP == null)) {
				returnMOP = new EntityRayTraceResult(pointedEntity);
			}
		}
		return returnMOP;
	}
}
