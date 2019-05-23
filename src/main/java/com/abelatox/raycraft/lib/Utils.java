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
import com.abelatox.raycraft.models.ModModels;
import com.abelatox.raycraft.models.render.IRayCraftRender;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;

public class Utils {

	public static IRayCraftRender getRender(IPlayerModelCapability props) {
		if (props != null) {
			if (props.getModel().equals("robopirate") || props.getModel().equals("robopirate2")) {
				return ModModels.renderPirate;
			}
			if(props.getModel().equals("rayman")) {
				return ModModels.renderRayman;
			}
		}
		return null;
	}

	public static void generateJSONModels() {
		Iterator i = Reference.items.iterator();

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

	public static void lockSelectedItem(EntityPlayer player, int slot) {
		player.inventory.currentItem = slot;
	}

	public static BlockPos getAvailablePos(EntityPlayer player) {
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
}
