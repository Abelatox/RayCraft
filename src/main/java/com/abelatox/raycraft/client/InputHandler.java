package com.abelatox.raycraft.client;

import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.abelatox.raycraft.capabilities.IPlayerCapabilities;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.gui.GUISelectModel;
import com.abelatox.raycraft.lib.Constants;
import com.abelatox.raycraft.network.PacketHandler;
import com.abelatox.raycraft.network.packets.PacketShoot;
import com.abelatox.raycraft.network.packets.PacketSyncCapabilityToAllFromClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class InputHandler {
	public static LivingEntity lockOn = null;

	public enum Keybinds {
		SELECT("key.raycraft.select", GLFW.GLFW_KEY_P), LOCK_ON("key.raycraft.lockon", GLFW.GLFW_KEY_LEFT_CONTROL);

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

	@SubscribeEvent
	public void handleKeyInputEvent(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		World world = mc.world;

		Keybinds key = getPressedKey();
		if (key != null) {
			switch (key) {
			case SELECT:
				mc.displayGuiScreen(new GUISelectModel());
				break;

			case LOCK_ON:
				/*if (lockOn == null) {
					RayTraceResult rtr = getMouseOverExtended(100);
					if (rtr != null && rtr instanceof EntityRayTraceResult) {
						EntityRayTraceResult ert = (EntityRayTraceResult) rtr;
						if (ert.getEntity() != null) {
							double distanceSq = player.getDistanceSq(ert.getEntity());
							double reachSq = 100 * 100;
							if (reachSq >= distanceSq) {
								if (ert.getEntity() instanceof LivingEntity) {
									lockOn = (LivingEntity) ert.getEntity();
									// LockOn.target = (LivingEntity) ert.getEntity();
									// player.world.playSound((PlayerEntity) player, player.getPosition(),
									// ModSounds.lockon, SoundCategory.MASTER, 1.0f, 1.0f);
								}
							}
						}
					}
				} else {
					lockOn = null;
				}
				break;*/
			}
		}
	}

	long time = 0;
	boolean shouldShoot = false;

	@SubscribeEvent
	public void handleMouseInputEvent(InputEvent.MouseInputEvent event) {

		ClientPlayerEntity player = Minecraft.getInstance().player;
		if (player != null) {
			IPlayerCapabilities props = ModCapabilities.get(player);

			// System.out.println("F");

			if (Minecraft.getInstance().currentScreen == null) {
				switch (event.getAction()) { // Check if press / release
				case 1: // Press
					time = System.currentTimeMillis();
					// If empty hand should shoot, if not it shouldn't (barrel + fist)
					shouldShoot = false;

					if (player != null && ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
						shouldShoot = true;
						props.setCharging(true);
					}
					break;
				case 0: // Release
					boolean charged = false;
					if (time + 1000 < System.currentTimeMillis()) {
						charged = true;
					}

					switch (event.getButton()) {
					case Constants.RIGHT_MOUSE:
						if (shouldShoot && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
							PacketHandler.sendToServer(new PacketShoot(charged));
							props.setCharging(false);
						}
						break;
					case Constants.LEFT_MOUSE:
						break;
					}
					break;
				}
				PacketHandler.sendToServer(new PacketSyncCapabilityToAllFromClient());
			}
		}

		if (event.getButton() == Constants.LEFT_MOUSE && KeyboardHelper.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			// Shoot
			// event.setCanceled(true);
		}
	}

	private Keybinds getPressedKey() {
		for (Keybinds key : Keybinds.values())
			if (key.isPressed())
				return key;
		return null;
	}

	/*public static RayTraceResult getMouseOverExtended(float dist) {
		Minecraft mc = Minecraft.getInstance();
		Entity theRenderViewEntity = mc.getRenderViewEntity();
		AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(theRenderViewEntity.getPosX() - 0.5D, theRenderViewEntity.getPosY() - 0.0D, theRenderViewEntity.getPosZ() - 0.5D, theRenderViewEntity.getPosX() + 0.5D, theRenderViewEntity.getPosY() + 1.5D, theRenderViewEntity.getPosZ() + 0.5D);
		RayTraceResult returnMOP = null;
		if (mc.world != null) {
			double var2 = dist;
			returnMOP = theRenderViewEntity.pick(var2, 0, true);
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
			List<Entity> list = mc.world.getEntitiesWithinAABBExcludingEntity(theRenderViewEntity, theViewBoundingBox.grow(lookvec.x * var2, lookvec.y * var2, lookvec.z * var2).grow(var9, var9, var9));
			double d = calcdist;

			for (Entity entity : list) {
				if (entity.canBeCollidedWith()) {
					float bordersize = entity.getCollisionBorderSize();
					AxisAlignedBB aabb = new AxisAlignedBB(entity.getPosX() - entity.getWidth() / 2, entity.getPosY(), entity.getPosZ() - entity.getWidth() / 2, entity.getPosX() + entity.getWidth() / 2, entity.getPosY() + entity.getHeight(), entity.getPosZ() + entity.getWidth() / 2);
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
	}*/
}
