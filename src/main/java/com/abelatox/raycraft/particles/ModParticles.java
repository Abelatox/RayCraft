package com.abelatox.raycraft.particles;

import com.abelatox.raycraft.lib.Reference;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticles {

	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, Reference.MODID);

	public static final Supplier<SimpleParticleType> PORTAL_STAR = PARTICLE_TYPES.register("portal_star", () -> new SimpleParticleType(false));
}
