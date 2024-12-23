package com.TBK.ars_carnis.client.model;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.TBK.ars_carnis.ArsCarnis;
import com.TBK.ars_carnis.client.anim.CarnisOvumAnim;
import com.TBK.ars_carnis.client.anim.CarnisSpinaAnim;
import com.TBK.ars_carnis.server.entity.CarnisSpinaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CarnisSpinaEntityModel<T extends CarnisSpinaEntity> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "spinacarnis"), "main");
	private final ModelPart main;
	private final ModelPart Head;
	private final ModelPart Jaw;
	private final ModelPart Eyeball;
	private final ModelPart Torso;
	private final ModelPart Chest;
	private final ModelPart Waist;
	private final ModelPart LeftArm;
	private final ModelPart UpperLeft;
	private final ModelPart LowerLeft;
	private final ModelPart Scythe;
	private final ModelPart RightArm;
	private final ModelPart UpperRight;
	private final ModelPart LowerRight;
	private final ModelPart Scythe2;
	private final ModelPart LeftLeg;
	private final ModelPart UpperLeftLeg;
	private final ModelPart LowerLeftLeg;
	private final ModelPart RightLeg;
	private final ModelPart UpperRightLeg;
	private final ModelPart LowerRightLeg;

	public CarnisSpinaEntityModel(ModelPart root) {
		this.main = root.getChild("main");
		this.Head = this.main.getChild("Head");
		this.Jaw = this.Head.getChild("Jaw");
		this.Eyeball = this.Jaw.getChild("Eyeball");
		this.Torso = this.main.getChild("Torso");
		this.Chest = this.Torso.getChild("Chest");
		this.Waist = this.Torso.getChild("Waist");
		this.LeftArm = this.main.getChild("LeftArm");
		this.UpperLeft = this.LeftArm.getChild("UpperLeft");
		this.LowerLeft = this.LeftArm.getChild("LowerLeft");
		this.Scythe = this.LowerLeft.getChild("Scythe");
		this.RightArm = this.main.getChild("RightArm");
		this.UpperRight = this.RightArm.getChild("UpperRight");
		this.LowerRight = this.RightArm.getChild("LowerRight");
		this.Scythe2 = this.LowerRight.getChild("Scythe2");
		this.LeftLeg = this.main.getChild("LeftLeg");
		this.UpperLeftLeg = this.LeftLeg.getChild("UpperLeftLeg");
		this.LowerLeftLeg = this.LeftLeg.getChild("LowerLeftLeg");
		this.RightLeg = this.main.getChild("RightLeg");
		this.UpperRightLeg = this.RightLeg.getChild("UpperRightLeg");
		this.LowerRightLeg = this.RightLeg.getChild("LowerRightLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(1.0F, 15.0F, 0.0F));

		PartDefinition Head = main.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(64, 5).addBox(-1.95F, -0.3972F, -6.0141F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.05F)), PartPose.offset(-0.725F, -19.6028F, -3.7859F));

		PartDefinition tendon_r1 = Head.addOrReplaceChild("tendon_r1", CubeListBuilder.create().texOffs(64, -8).addBox(3.0F, -0.725F, -4.55F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(64, -8).addBox(0.0F, -0.725F, -4.55F, 0.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.525F, -2.1722F, 1.2859F, 0.5672F, 0.0F, 0.0F));

		PartDefinition Jaw = Head.addOrReplaceChild("Jaw", CubeListBuilder.create().texOffs(44, 3).addBox(-2.0F, -7.5F, -9.5F, 4.0F, 7.0F, 12.0F, new CubeDeformation(0.15F)), PartPose.offset(0.05F, 0.1028F, 3.4859F));

		PartDefinition Eyeball = Jaw.addOrReplaceChild("Eyeball", CubeListBuilder.create().texOffs(47, 22).addBox(-2.0F, -3.5F, -5.5F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.65F, -3.125F));

		PartDefinition Torso = main.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(-1.675F, -6.5F, -0.275F));

		PartDefinition Chest = Torso.addOrReplaceChild("Chest", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -3.5F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.5F, 0.0F));

		PartDefinition Waist = Torso.addOrReplaceChild("Waist", CubeListBuilder.create().texOffs(0, 15).addBox(-2.0F, -6.0F, -1.5F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 0.0F));

		PartDefinition LeftArm = main.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(2.325F, -18.725F, -0.3F));

		PartDefinition UpperLeft = LeftArm.addOrReplaceChild("UpperLeft", CubeListBuilder.create().texOffs(30, 1).addBox(-0.075F, -0.6F, -0.9F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LowerLeft = LeftArm.addOrReplaceChild("LowerLeft", CubeListBuilder.create().texOffs(38, 3).addBox(-1.0F, -0.525F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.15F)), PartPose.offset(0.925F, 11.65F, 0.1F));

		PartDefinition Scythe = LowerLeft.addOrReplaceChild("Scythe", CubeListBuilder.create(), PartPose.offset(0.0F, 10.975F, -2.5F));

		PartDefinition ScytheLeftArm_r1 = Scythe.addOrReplaceChild("ScytheLeftArm_r1", CubeListBuilder.create().texOffs(30, 8).addBox(-7.95F, -10.0F, -3.5F, 0.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 3.5F, 5.6F, 0.0F, 3.1416F, 0.0F));

		PartDefinition RightArm = main.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offset(-5.775F, -18.725F, -0.3F));

		PartDefinition UpperRight = RightArm.addOrReplaceChild("UpperRight", CubeListBuilder.create().texOffs(30, 1).mirror().addBox(-1.925F, -0.6F, -0.9F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition LowerRight = RightArm.addOrReplaceChild("LowerRight", CubeListBuilder.create().texOffs(38, 3).mirror().addBox(-1.0F, -0.525F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation(-0.15F)).mirror(false), PartPose.offset(-0.925F, 11.65F, 0.1F));

		PartDefinition Scythe2 = LowerRight.addOrReplaceChild("Scythe2", CubeListBuilder.create(), PartPose.offset(0.0F, 10.975F, -2.5F));

		PartDefinition ScytheRightArm_r1 = Scythe2.addOrReplaceChild("ScytheRightArm_r1", CubeListBuilder.create().texOffs(30, 8).mirror().addBox(0.0F, -10.0F, -3.5F, 0.0F, 16.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 3.5F, 5.225F, 0.0F, 3.1416F, 0.0F));

		PartDefinition LeftLeg = main.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(0.2083F, -6.1667F, -0.45F));

		PartDefinition UpperLeftLeg = LeftLeg.addOrReplaceChild("UpperLeftLeg", CubeListBuilder.create().texOffs(16, 15).mirror().addBox(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.0333F, -0.3333F, -0.825F));

		PartDefinition LowerLeftLeg = LeftLeg.addOrReplaceChild("LowerLeftLeg", CubeListBuilder.create().texOffs(0, -3).mirror().addBox(0.025F, 1.5F, 1.7375F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(46, 2).mirror().addBox(-1.025F, -0.5F, -0.2375F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0167F, 4.6667F, -0.0875F));

		PartDefinition RightLeg = main.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-3.2083F, -6.1667F, -0.45F));

		PartDefinition UpperRightLeg = RightLeg.addOrReplaceChild("UpperRightLeg", CubeListBuilder.create().texOffs(16, 15).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0333F, -0.3333F, -0.825F));

		PartDefinition LowerRightLeg = RightLeg.addOrReplaceChild("LowerRightLeg", CubeListBuilder.create().texOffs(0, -3).addBox(-0.025F, 1.5F, 1.7375F, 0.0F, 7.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(46, 2).addBox(-0.975F, -0.5F, -0.2375F, 2.0F, 11.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.0167F, 4.6667F, -0.0875F));

		return LayerDefinition.create(meshdefinition, 84, 31);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.animate(entity.idle,CarnisSpinaAnim.IDLE,ageInTicks,1.0F);

		if(!(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F)){
			this.RightArm.getAllParts().forEach(ModelPart::resetPose);
			this.LeftArm.getAllParts().forEach(ModelPart::resetPose);
		}

		if(entity.getDeltaMovement().length()<0.1F){
			this.animateWalk(CarnisSpinaAnim.MOVE_LEGS,limbSwing,limbSwingAmount,2.0F,2.5F);
			this.animateWalk(CarnisSpinaAnim.MOVE,limbSwing,limbSwingAmount,2.0F,2.5F);
		}else {
			this.animateWalk(CarnisSpinaAnim.RUN,limbSwing,limbSwingAmount,3.0F,3.5F);
			this.animateWalk(CarnisSpinaAnim.RUN_LEGS,limbSwing,limbSwingAmount,3.0F,3.5F);
		}


		if(entity.attack1.isStarted() || entity.attack2.isStarted() || entity.attack_double.isStarted()){
			this.RightArm.getAllParts().forEach(ModelPart::resetPose);
			this.LeftArm.getAllParts().forEach(ModelPart::resetPose);
		}
		this.animate(entity.attack1,CarnisSpinaAnim.SLASH1,ageInTicks,1.0F);
		this.animate(entity.attack2,CarnisSpinaAnim.SLASH2,ageInTicks,1.0F);
		this.animate(entity.attack_double,CarnisSpinaAnim.DOUBLE_SLASH,ageInTicks,1.0F);
		this.animate(entity.jaw,CarnisSpinaAnim.HEAD_OPEN,ageInTicks,1.0F);

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.main;
	}
}