package me.andrew.gravitychanger.util;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class GravityChangerComponents implements EntityComponentInitializer {

    public static final ComponentKey<GravityComponent> GRAVITY_MODIFIER =
            ComponentRegistry.getOrCreate(new Identifier("gravitychanger", "gravity_direction"), GravityComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Entity.class, GRAVITY_MODIFIER, GravityDirectionComponent::new);
    }
}
