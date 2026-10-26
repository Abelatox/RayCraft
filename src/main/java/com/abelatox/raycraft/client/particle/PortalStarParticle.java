package com.abelatox.raycraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class PortalStarParticle extends SingleQuadParticle {

	private static final float SPIN = 0.2F;

	private PortalStarParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
		super(level, x, y, z, 0.0, 0.0, 0.0, sprites.first());
		this.xd = xd;
		this.yd = yd;
		this.zd = zd;
		this.friction = 1.0F;
		this.gravity = 0.0F;
		this.hasPhysics = false;
		this.quadSize = 0.06F + random.nextFloat() * 0.04F;
		this.roll = random.nextFloat() * Mth.TWO_PI;
		setColor(0.55F, 0.85F, 1.0F);
	}

	@Override
	public void tick() {
		super.tick();
		oRoll = roll;
		roll += SPIN;
		setAlpha(Math.min(1.0F, (lifetime - age) / 5.0F));
	}

	@Override
	protected SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public int getLightCoords(float partialTick) {
		return LightCoordsUtil.FULL_BRIGHT;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xd, double yd, double zd, RandomSource random) {
			PortalStarParticle star = new PortalStarParticle(level, x, y, z, xd, yd, zd, sprites);
			star.setLifetime(25 + random.nextInt(10));
			return star;
		}
	}
}
