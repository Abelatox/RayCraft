package com.abelatox.raycraft.lib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.abelatox.raycraft.capabilities.IPlayerModelCapability;
import com.abelatox.raycraft.capabilities.ModCapabilities;
import com.abelatox.raycraft.entities.EntityBarrel;
import com.abelatox.raycraft.entities.EntityBaseFist;
import com.abelatox.raycraft.entities.EntityFist0;
import com.abelatox.raycraft.entities.EntityFist1;
import com.abelatox.raycraft.entities.EntityFist2;
import com.abelatox.raycraft.entities.EntityFist3;
import com.abelatox.raycraft.entities.EntityFist4;
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.render.IRayCraftRender;
import com.abelatox.raycraft.sounds.ModSounds;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public class Utils {

	public static IRayCraftRender getRender(IPlayerModelCapability props) {
		if (props != null) {
			if (props.getPlayerType().equals("robopirate") || props.getPlayerType().equals("robopirate2")) {
				return ModModels.renderPirate;
			}
			if (props.getPlayerType().equals("rayman")) {
				return ModModels.renderRayman;
			}
		}
		return null;
	}

	public static int getSlotFor(PlayerEntity player, ItemStack stack) {
		for (int i = 0; i < player.inventory.mainInventory.size(); ++i) {
			if (!player.inventory.mainInventory.get(i).isEmpty() && stackEqualExact(stack, player.inventory.mainInventory.get(i))) {
				return i;
			}
		}
		return -1;
	}

	public static ThrowableEntity getEntityShot(PlayerEntity player, boolean charged) {
		IPlayerModelCapability props = ModCapabilities.get(player);
		switch (props.getPlayerType()) {
		case Strings.ROBO_PIRATE_RED:
			return new EntityBarrel(player.world, player);
		case Strings.ROBO_PIRATE_GREEN:
			return new EntityBarrel(player.world, player);
		case Strings.RAYMAN:
			return getRaymanPunchLevel(player, charged);
		}
		return null;
	}

	private static EntityBaseFist getRaymanPunchLevel(PlayerEntity player, boolean charged) {
		IPlayerModelCapability props = ModCapabilities.get(player);
		int level = props.getShotLevel();
		if (charged && level < 4) {
			level++;
		}
		switch (level) {
		case 0:
			return new EntityFist0(player.world, player);
		case 1:
			return new EntityFist1(player.world, player);
		case 2:
			return new EntityFist2(player.world, player);
		case 3:
			return new EntityFist3(player.world, player, charged);
		case 4:
			return new EntityFist4(player.world, player, charged);
		}
		return null;

	}

	/**
	 * Checks item, NBT, and meta if the item is not damageable
	 */
	private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}

	public static void generateJSONModels() {
		Iterator<Item> i = Reference.items.iterator();

		while (i.hasNext()) {
			Item item = (Item) i.next();

			String itemName = getFancyName(item.getRegistryName().getPath());
			File itemFolder = new File(Reference.PROJECT_RESOURCES_FOLDER + "/assets/" + Reference.MODID + "/models/item/");
			itemFolder.mkdirs();

			if (itemFolder.exists()) {
				try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Reference.PROJECT_RESOURCES_FOLDER + "/assets/" + Reference.MODID + "/models/item/" + itemName + ".json"), "UTF-8"))) {
					writer.write("{\n");

					writer.write("\u0009\"parent\": \"item/generated\",\n");
					writer.write("\u0009\"textures\": {\n");
					writer.write("\u0009\u0009\"layer0\": \"" + Reference.MODID + ":item/" + itemName + "\"\n");
					writer.write("\u0009}\n");

					writer.write("}\n");
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void generateLangFiles() {
		Map<String, String> sorted = sortAlphabetically(Reference.langMap);
		Set set = sorted.entrySet();
		Iterator i = set.iterator();

		Map.Entry prevEntry = null;

		File langFolder = new File(Reference.PROJECT_RESOURCES_FOLDER + "/assets/" + Reference.MODID + "/lang/");
		langFolder.mkdirs();

		if (langFolder.exists()) {
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Reference.PROJECT_RESOURCES_FOLDER + "/assets/" + Reference.MODID + "/lang/en_US.json"), "UTF-8"))) {
				writer.write("{\n");
				while (i.hasNext()) {
					Map.Entry entry = (Map.Entry) i.next();

					if (prevEntry != null) {
						if (!((String) prevEntry.getKey()).substring(0, 2).equals(((String) entry.getKey()).substring(0, 2))) {
							writer.write("\n");
						}
					}

					if (i.hasNext())
						writer.write("\u0009\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n");
					else
						writer.write("\u0009\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"\n");

					prevEntry = entry;
				}
				writer.write("}\n");
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static <K extends Comparable, V extends Comparable> Map<K, V> sortAlphabetically(Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return o1.getKey().compareTo(o2.getKey());
			}
		});

		Map<K, V> sortedMap = new LinkedHashMap<K, V>();

		for (Map.Entry<K, V> entry : entries) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	public static String getFancyName(String text) {
		return text.replaceAll("\\s+", "").toLowerCase().replaceAll("'", "").replaceAll("-", "").replaceAll(":", "").replaceAll("#", "").replace(",", "");
	}

	public static void lockSelectedItem(PlayerEntity player, ItemStack stack) {
		player.inventory.currentItem = Utils.getSlotFor(player, stack);
	}

	public static BlockPos getAvailablePos(PlayerEntity player) {
		// player.world.getHeight(Type.WORLD_SURFACE, player.getPosition().north());
		if (player.world.getBlockState(player.getPosition().north()).getBlock() == Blocks.AIR) {
			return player.getPosition().north();
		} else if (player.world.getBlockState(player.getPosition().east()).getBlock() == Blocks.AIR) {
			return player.getPosition().east();
		} else if (player.world.getBlockState(player.getPosition().south()).getBlock() == Blocks.AIR) {
			return player.getPosition().south();
		} else if (player.world.getBlockState(player.getPosition().west()).getBlock() == Blocks.AIR) {
			return player.getPosition().west();
		}
		return null;
	}

	public static SoundEvent getShootSound(PlayerEntity player, boolean charged) {
		IPlayerModelCapability props = ModCapabilities.get(player);
		switch (props.getPlayerType()) {
		case Strings.RAYMAN:
			switch (props.getShotLevel()) {
			case 0:
				
			case 1:
				return ModSounds.fistShot1;
			case 2:
				if(charged)
					return ModSounds.fistShot2;
				return ModSounds.fistShot1;
			case 3:
				if(charged)
					return ModSounds.fistGoldCharged;
				return ModSounds.fistShot2;
			}
		case Strings.ROBO_PIRATE_GREEN:
			return ModSounds.pirateShot1;
		case Strings.ROBO_PIRATE_RED:
			return ModSounds.pirateShot2;
		}
		return null;
	}
}
