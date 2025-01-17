package teamdraco.unnamedanimalmod.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class BlubberJellyModel<T extends Entity> extends EntityModel<T> {
    public ModelRenderer bell;
    public ModelRenderer tentaclebase;
    public ModelRenderer tentacles;

    public BlubberJellyModel() {
        this.texWidth = 26;
        this.texHeight = 26;
        this.tentaclebase = new ModelRenderer(this, 0, 8);
        this.tentaclebase.setPos(0.0F, 1.5F, 0.0F);
        this.tentaclebase.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, 0.0F, 0.0F, 0.0F);
        this.bell = new ModelRenderer(this, 0, 0);
        this.bell.setPos(0.0F, 22.5F, 0.0F);
        this.bell.addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.tentacles = new ModelRenderer(this, 0, 12);
        this.tentacles.setPos(0.0F, 1.0F, 0.0F);
        this.tentacles.addBox(-2.5F, 0.0F, -2.5F, 5.0F, 3.0F, 5.0F, 0.0F, 0.0F, 0.0F);
        this.bell.addChild(this.tentaclebase);
        this.tentaclebase.addChild(this.tentacles);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) { 
        ImmutableList.of(this.bell).forEach((modelRenderer) -> { 
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
    public void setupAnim(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = 1.0f;
        float degree = 1.0f;
//        this.bell.rotateAngleY = MathHelper.cos(limbSwing * speed * 0.4F) * degree * 0.2F * limbSwingAmount;
//        this.bell.rotationPointY = MathHelper.cos(limbSwing * speed * 0.2F) * degree * 0.05F * limbSwingAmount;
//        this.tentaclebase.rotationPointY = MathHelper.cos(limbSwing * speed * 0.2F) * degree * 0.025F * limbSwingAmount - 0.025F;
//        this.tentacles.rotationPointY = MathHelper.cos(limbSwing * speed * 0.2F) * degree * 0.025F * limbSwingAmount;

    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
