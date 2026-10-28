package com.abelatox.raycraft.gui;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

/** A slider over [min, max] in fixed steps, showing its value with a translated label. */
public class ValueSlider extends AbstractSliderButton {

	private final float min;
	private final float max;
	private final float step;
	private final String label;
	private final Consumer<Float> onChange;
	private float current;

	public ValueSlider(int x, int y, float min, float max, float step, float initial, String label, Consumer<Float> onChange) {
		super(x, y, 200, 20, Component.empty(), (initial - min) / (max - min));
		this.min = min;
		this.max = max;
		this.step = step;
		this.label = label;
		this.onChange = onChange;
		this.current = initial;
		updateMessage();
	}

	@Override
	protected void updateMessage() {
		setMessage(Component.translatable(label, String.format(java.util.Locale.ROOT, "%.1f", current)));
	}

	@Override
	protected void applyValue() {
		current = Math.round((min + (float) value * (max - min)) / step) * step;
		onChange.accept(current);
	}
}
