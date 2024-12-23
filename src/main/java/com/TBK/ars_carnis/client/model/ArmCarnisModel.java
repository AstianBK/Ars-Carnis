package com.TBK.ars_carnis.client.model;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.ars_carnis.ArsCarnis;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class ArmCarnisModel<T extends Player> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ArsCarnis.MODID, "armfleshspike"), "main2");
	public final ModelPart leftArm;
	public final ModelPart VorpalBlade;

	public ArmCarnisModel(ModelPart root) {
		this.leftArm = root.getChild("LeftArm");
		this.VorpalBlade = this.leftArm.getChild("VorpalBlade");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition VorpalBlade = LeftArm.addOrReplaceChild("VorpalBlade", CubeListBuilder.create().texOffs(17, -7).addBox(1.0F, 4.825F, -1.0F, 0.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}
	public void copyOfLimbs(ModelPart part){
		this.leftArm.copyFrom(part);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}