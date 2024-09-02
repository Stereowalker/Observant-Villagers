package com.stereowalker.obville.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.stereowalker.obville.interfaces.ICombinedBlock;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;

@Mixin(CompoundContainer.class)
public abstract class CompoundContainerMixin implements ICombinedBlock {
	@Shadow @Final private Container container1;
	@Shadow @Final private Container container2;
	
	@Override
	public Container getContainer1() {
		return container1;
	}
	
	@Override
	public Container getContainer2() {
		return container2;
	}
}
