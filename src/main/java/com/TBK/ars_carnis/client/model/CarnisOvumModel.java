package com.TBK.ars_carnis.client.model;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.anim.CarnisOvumAnim;
import com.TBK.ars_carnis.server.entity.CarnisOvumEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class CarnisOvumModel<T extends CarnisOvumEntity> extends HierarchicalModel<T> implements ArmedModel {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(ArsCarnis.MODID, "larva_carnis"), "root");
	private final ModelPart main;
	private final ModelPart head;
	private final ModelPart leftArm;
	private final ModelPart upperLeft;
	private final ModelPart lowerLeft;
	private final ModelPart leftHand;

	public CarnisOvumModel(ModelPart root) {
		this.main = root.getChild("main");
		this.head = this.main.getChild("head");
		this.leftArm = this.main.getChild("leftArm");
		this.upperLeft = this.leftArm.getChild("upperLeft");
		this.lowerLeft = this.leftArm.getChild("lowerLeft");
		this.leftHand = this.lowerLeft.getChild("leftHand");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Head = main.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -7.8333F, -5.6667F, 14.0F, 14.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.1667F, -1.3333F));

		PartDefinition eye_r1 = Head.addOrReplaceChild("eye_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -6.3333F, -0.6667F, 1.0522F, -0.0137F, 1.5873F));

		PartDefinition eye_r2 = Head.addOrReplaceChild("eye_r2", CubeListBuilder.create().texOffs(0, 28).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -4.8333F, -0.6667F, 2.7489F, 0.0F, 0.0F));

		PartDefinition LeftArm = main.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(6.0F, -1.0F, -7.0F));

		PartDefinition UpperLeft = LeftArm.addOrReplaceChild("upperLeft", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, 0.0F, -6.0F, 2.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LowerLeft = LeftArm.addOrReplaceChild("lowerLeft", CubeListBuilder.create().texOffs(34, 6).addBox(-1.0F, 0.0F, -8.0F, 2.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -6.0F));

		PartDefinition LeftHand = LowerLeft.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(46, 8).addBox(-2.0F, 0.0F, -4.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animateWalk(CarnisOvumAnim.MOVE,limbSwing,limbSwingAmount,5.0F,5.5F);
		this.animate(entity.idle, CarnisOvumAnim.IDLE,ageInTicks,1.0F);
		this.animate(entity.eat, CarnisOvumAnim.EAT,ageInTicks,1.0F);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.main;
	}

	@Override
	public void translateToHand(HumanoidArm p_102108_, PoseStack p_102109_) {
		p_102109_.translate(0.6F,1.8F,-0.3F);
		this.leftHand.translateAndRotate(p_102109_);

	}
}