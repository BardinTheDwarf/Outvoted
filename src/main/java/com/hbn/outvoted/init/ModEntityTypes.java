package com.hbn.outvoted.init;

import com.hbn.outvoted.Outvoted;
import com.hbn.outvoted.entities.InfernoEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Outvoted.MOD_ID);

    // Entity Types
    public static final RegistryObject<EntityType<InfernoEntity>> INFERNO = ENTITY_TYPES.register("inferno", () -> EntityType.Builder.create(InfernoEntity::new, EntityClassification.MONSTER).immuneToFire().size(0.8F, 2.0F).build(new ResourceLocation(Outvoted.MOD_ID, "inferno").toString()));
}
