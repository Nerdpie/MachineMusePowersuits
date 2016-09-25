package net.machinemuse.powersuits.client.render.item;

import com.google.common.collect.Maps;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.client.render.entity.EntityRenderPlasmaBolt;
import net.machinemuse.powersuits.powermodule.weapon.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PowerFistModel implements IPerspectiveAwareModel {
    public IBakedModel powerFistIcon, powerFistleft, powerFistLeftFiring, powerFistRight, powerFistRightFiring, modelToRender;
    private final Map<ItemCameraTransforms.TransformType, IBakedModel> modelCache = Maps.newHashMap();
    private final Map<ItemCameraTransforms.TransformType, IBakedModel> altmodelCache = Maps.newHashMap();
    public ItemCameraTransforms.TransformType cameraTransformType = ItemCameraTransforms.TransformType.NONE;
    public int boltSize;


    public ItemStack itemStack;
    public World world;
    public EntityLivingBase entity;
    public boolean isFiring;

    Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem itemRenderer = mc.getRenderItem();


    public PowerFistModel(IBakedModel powerFistIconIn,
                          IBakedModel powerFistleftIn,
                          IBakedModel powerFistLeftFiringIn,
                          IBakedModel powerFistRightIn,
                          IBakedModel PowerFistRightFiringIn) {

        if (powerFistIconIn instanceof PowerFistModel) {
            powerFistIcon = ((PowerFistModel)powerFistIconIn).powerFistIcon;
        } else {
            powerFistIcon = powerFistIconIn;
        }
        if (!modelCache.containsKey(ItemCameraTransforms.TransformType.GUI))
            modelCache.put(ItemCameraTransforms.TransformType.GUI, powerFistIcon);


        if (powerFistleftIn instanceof PowerFistModel) {
            powerFistleft = ((PowerFistModel)powerFistleftIn).powerFistleft;
        } else {
            powerFistleft = powerFistleftIn;
        }
        if (!modelCache.containsKey(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND))
            modelCache.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, powerFistleft);
        if (!modelCache.containsKey(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND))
            modelCache.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, powerFistleft);

        if (powerFistLeftFiringIn instanceof PowerFistModel) {
            powerFistLeftFiring = ((PowerFistModel)powerFistLeftFiringIn).powerFistLeftFiring;
        } else {
            powerFistLeftFiring = powerFistLeftFiringIn;
        }
        if (!altmodelCache.containsKey(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND))
            altmodelCache.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, powerFistLeftFiring);
        if (!altmodelCache.containsKey(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND))
            altmodelCache.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, powerFistLeftFiring);

        if (powerFistRightIn instanceof PowerFistModel) {
            powerFistRight = ((PowerFistModel)powerFistRightIn).powerFistRight;
        } else {
            powerFistRight = powerFistRightIn;
        }
        if (!modelCache.containsKey(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND))
            modelCache.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, powerFistRight);
        if (!modelCache.containsKey(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND))
            modelCache.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, powerFistRight);

        if (PowerFistRightFiringIn instanceof PowerFistModel) {
            powerFistRightFiring = ((PowerFistModel)PowerFistRightFiringIn).powerFistRightFiring;
        } else {
            powerFistRightFiring = PowerFistRightFiringIn;
        }
        if (!altmodelCache.containsKey(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND))
            altmodelCache.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, powerFistRightFiring);
        if (!altmodelCache.containsKey(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND))
            altmodelCache.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, powerFistRightFiring);
    }


    public void handleItemState(ItemStack itemStackIn, World worldIn, EntityLivingBase entityLivingBaseIn) {
        itemStack = itemStackIn;
        world = worldIn;
        entity = entityLivingBaseIn;
    }


    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        this.cameraTransformType = cameraTransformType;
        if (entity instanceof EntityPlayer && entity.getActiveHand() != null) {
            getIsFiring((EntityPlayer) entity, itemStack);
        } else {
            isFiring = false;
        }

        switch (cameraTransformType) {
            case FIRST_PERSON_LEFT_HAND:
                GlStateManager.scale(0.62, 0.62, 0.62);
                GlStateManager.translate(14.50, 11.50, 14.25);
                GlStateManager.rotate(180, 1, 0, 0);
                GlStateManager.rotate(-6, 0, 1, 0);
                GlStateManager.rotate(180, 0, 0, 1);
                getAndRenderModel(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND);



                /*
                "display": {
    "thirdperson_right": {
        "rotation": [ 164, 0, 180 ],
        "translation": [ 8.00, 7.50, 9.00 ],
        "scale": [ 0.50, 0.50, 0.50 ]
    },
    "firstperson_right": {
        "rotation": [ 0, 202, 0 ],
        "translation": [ 18.50, 2.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    },
    "thirdperson_left": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    },
    "firstperson_left": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    },
    "gui": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    },
    "head": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    }
    "fixed": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    }
    "ground": {
        "rotation": [ 0, 0, 0 ],
        "translation": [ 0.00, 0.00, 0.00 ],
        "scale": [ 1.00, 1.00, 1.00 ]
    }
}
                 */









                break;
//
////                "firstperson_left": {
////                "rotation": [ 180, 354, 180 ],

//                break;
            case THIRD_PERSON_LEFT_HAND:
                GlStateManager.scale(0.70, 0.70, 0.70);
//                GlStateManager.translate(14.50, 11.50, 14.25);
                GlStateManager.translate(-0.5, 0.345, -0.5);
                GlStateManager.rotate(346, 1, 0, 0);
                GlStateManager.rotate(180, 0, 1, 0);





//                "rotation": [ 346, 180, 0 ],
//                "translation": [ 19.25, 8.00, 9.25 ],
//                "scale": [ 0.70, 0.70, 0.70 ]
                getAndRenderModel(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND);
                break;



            case FIRST_PERSON_RIGHT_HAND:
                getAndRenderModel(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);
                //                GlStateManager.rotate(45.0F, 0.0F, 0.0F, 1.0F);
//                GlStateManager.scale(0.42F, 0.42F, 0.42F);
//                GlStateManager.translate(-0.05F, -0.6F, 0.0F);
//                break;
                break;



            case THIRD_PERSON_RIGHT_HAND:
                GlStateManager.scale(0.70, 0.70, 0.70);
//                GlStateManager.translate(14.50, 11.50, 14.25);
                GlStateManager.translate(-0.5, 0.345, -0.5);
                GlStateManager.rotate(346, 1, 0, 0);
                GlStateManager.rotate(180, 0, 1, 0);


//                "rotation": [ 346, 180, 0 ],
//                "translation": [ 19.25, 8.00, 9.25 ],
//                "scale": [ 0.70, 0.70, 0.70 ]
                getAndRenderModel(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);

                System.out.println("powerfist texture: " + modelToRender.getParticleTexture().toString());

//                System.out.println("powerfist texture: " + modelToRender.getParticleTexture().);

                break;






//                "display": {
//                "thirdperson_right": {
//                    "rotation": [ 346, 180, 0 ],
//                    "translation": [ 8.00, 8.00, 9.00 ],
//                    "scale": [ 0.70, 0.70, 0.70 ]
//                },








//                GlStateManager.scale(0.625F, 0.625F, 0.625F);
//                GlStateManager.rotate(270, 1, 0, 0);
//                GlStateManager.rotate(45, 0, 1, 0);
//                GlStateManager.rotate(-90, 0, 0, 1);
//                GlStateManager.translate(0, 0, 4);

//                GL11.glRotatef();
//                GL11.glTranslatef(0, 0, 4);



//                GL11.glScaled(scale1, scale1, scale1);
//                // GL11.glDisable(GL11.GL_CULL_FACE);
//                GL11.glRotatef(270, 1, 0, 0);
//                GL11.glRotatef(45, 0, 1, 0);
//                GL11.glRotatef(-90, 0, 0, 1);
//                GL11.glTranslatef(0, 0, 4);

//                renderFirstPersonArm((EntityPlayerSP)entity, 0F, cameraTransformType);
//
//
//                if (isFiring)
//                    modelToRender = altmodelCache.get(cameraTransformType);
//                else
//                    modelToRender = modelCache.get(cameraTransformType);
//                if (modelToRender != null) {
//                    itemRenderer.renderModel(modelToRender, 5);
//                }
//                else
//                    System.out.println("MODEL IS NULL");



//            case GUI:
//                GlStateManager.rotate(0, 0.0F, -1.0F, -1.0F);
            default:
//                GlStateManager.rotate( 0.0F, 0.0F, 0.0F, 1.0F);
//                GlStateManager.scale(0.42F, 0.42F, 0.42F);
//                GlStateManager.translate(-0.5F, -0.5F, 0.0F);

                modelToRender = modelCache.get(ItemCameraTransforms.TransformType.GUI);
//                modelToRender = modelCache.get(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND);


//                itemRenderer.renderModel(modelToRender, itemStack );

                break;

//                RenderItem.renderItemModelIntoGUI
//
//
//                    RenderItem.applyVanillaTransform(baseModel.getItemCameraTransforms().gui);
//                    return Pair.of(baseModel, null);
                }
//            if (modelToRender != null)
//                itemRenderer.renderModel(modelToRender, itemStack );
//            else
//                System.out.println("MODEL IS NULL");

//        if (retModel.equals(powerFistIcon))
//            System.out.println("powerFistIcon");
//        if (retModel.equals(powerFistleft))
//            System.out.println("powerFistleft");
//        if (retModel.equals(powerFistLeftFiring))
//            System.out.println("powerFistLeftFiring");
//        if (retModel.equals(powerFistRight))
//            System.out.println("powerFistRight");
//        if (retModel.equals(powerFistRightFiring))
//            System.out.println("powerFistRightFiring");
//
//
//
//        System.out.println("powerfist model " + cameraTransformType.name());



        Matrix4f matrix, matrix2, matrix3;


//        try {
//            matrix2 = ((IPerspectiveAwareModel) modelToRender).handlePerspective(cameraTransformType).getValue();
//            matrix3 = matrix = TRSRTransformation.identity().getMatrix();
//
//            System.out.println("matrix2.toString(): " + matrix2.toString());
//            System.out.println("matrix3.toString(): " + matrix3.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        if (modelToRender != null && modelToRender instanceof IPerspectiveAwareModel) {
//            System.out.println("model is instance of IPerspectiveAwareModel");
            matrix = ((IPerspectiveAwareModel) modelToRender).handlePerspective(cameraTransformType).getValue();
//            System.out.println("matrix.toString(): " + matrix.toString());

        } else {
//            System.out.println("model is NOT an instance of IPerspectiveAwareModel");
            matrix = TRSRTransformation.identity().getMatrix();
        }




        return Pair.of(this, matrix);



    }

    private void getAndRenderModel(ItemCameraTransforms.TransformType cameraTransformTypeype) {
        if (this.isFiring)
            modelToRender = altmodelCache.get(cameraTransformType);
        else
            modelToRender = modelCache.get(cameraTransformType);
        if (modelToRender != null) {
            itemRenderer.renderModel(modelToRender, 2);
        }
        if (boltSize != 0) {
            GL11.glTranslated(-1, 1, 16);
            GL11.glPushMatrix();
            EntityRenderPlasmaBolt.doRender(boltSize);
            GL11.glPopMatrix();
        }

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return modelToRender.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
//        System.out.println("doing stuff here");
        return modelToRender.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return modelToRender == null? true : modelToRender.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        if (modelToRender == null)
            return false;

        return modelToRender.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return modelToRender.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return modelToRender.getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new PowerFistItemOverrideList();
    }



    public class PowerFistItemOverrideList extends ItemOverrideList {

        public PowerFistItemOverrideList() {
            super(Collections.EMPTY_LIST);
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
//            System.out.println("doing stuff here");

            if (originalModel instanceof PowerFistModel) {
                ((PowerFistModel)originalModel).handleItemState(stack, world, entity);
            }

            return originalModel;
        }
    }



    public void getIsFiring(EntityPlayer player, ItemStack itemStack) {
        if (player.getActiveItemStack() !=null && player.getActiveItemStack() == itemStack)
            if (ModuleManager.itemHasActiveModule(player.getActiveItemStack(), PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
                this.boltSize = player.getItemInUseMaxCount() > 50 ? 50 : player.getItemInUseMaxCount();
                isFiring = true;
            } else if (ModuleManager.itemHasActiveModule(player.getActiveItemStack(), BladeLauncherModule.MODULE_BLADE_LAUNCHER)) {
                isFiring = true;
            } else if (ModuleManager.itemHasActiveModule(player.getActiveItemStack(), LightningModule.MODULE_LIGHTNING)) {
                isFiring = true;
            } else if (ModuleManager.itemHasActiveModule(player.getActiveItemStack(), RailgunModule.MODULE_RAILGUN)) {
                isFiring = true;
            } else if (ModuleManager.itemHasActiveModule(player.getActiveItemStack(), SonicWeaponModule.MODULE_SONIC_WEAPON)) {
                isFiring = true;
            } else {
                isFiring = false;
                boltSize = 0;
            }
    }






    public void renderFirstPersonArm(EntityPlayerSP entityclientplayermp, float sp, ItemCameraTransforms.TransformType transformType) {
//        entityclientplayermp.swingArm(EnumHand.MAIN_HAND);
//        entityclientplayermp.renderArmPitch =0;
//        entityclientplayermp.renderArmYaw = 0;

//        float changeItemProgress = 0;
//
//        GL11.glPushMatrix();
//        float f4 = 0.8F;
//        float swingProgress = entityclientplayermp.swingProgress;
//        float swingProgressx = MathHelper.sin(swingProgress * (float) Math.PI);
//        float swingProgressy = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
//        GL11.glTranslatef(-swingProgressy * 0.3F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI * 2.0F) * 0.4F, -swingProgressx * 0.4F);
//        GL11.glTranslatef(0.8F * f4, -0.75F * f4 - (1.0F - changeItemProgress) * 0.6F, -0.9F * f4);
//        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        swingProgress = entityclientplayermp.swingProgress;
//        swingProgressx = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
//        swingProgressy = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float) Math.PI);
//        GL11.glRotatef(swingProgressy * 70.0F, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-swingProgressx * 20.0F, 0.0F, 0.0F, 1.0F);
////        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getTexture()));
////        mc.renderEngine.resetBoundTexture();
//        GL11.glTranslatef(-1.0F, 3.6F, 3.5F);
//        GL11.glRotatef(120.0F, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(200.0F, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
//        GL11.glScalef(1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef(5.6F, 0.0F, 0.0F);
//        Render render = mc.getRenderManager().getEntityRenderObject(mc.thePlayer);
//        RenderPlayer renderplayer = (RenderPlayer) render;
//        if (transformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
//            renderplayer.renderRightArm(entityclientplayermp);
//        else
//            renderplayer.renderLeftArm(entityclientplayermp);
//        GL11.glPopMatrix();
    }

    public void setNeutralPose() {
//        setPose(0.5f, -1, 0.5f, -1, 0.5f, -1);
        this.boltSize = 0;
    }
}



//// Date: 1/13/2013 3:17:20 AM
//// Template version 1.1
//// Java generated by Techne
//// Keep in mind that you still need to fill in some blanks
//// - ZeuX
//

//
//import net.machinemuse.api.ModuleManager;
//import net.machinemuse.numina.general.MuseLogger;
//import net.machinemuse.numina.geometry.Colour;
//import net.machinemuse.numina.render.MuseTextureUtils;
//import net.machinemuse.numina.render.RenderState;
//import net.machinemuse.powersuits.client.render.entity.EntityRenderPlasmaBolt;
//import net.machinemuse.powersuits.common.Config;
//import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.GL11;
//
//public class PowerFistModel extends ModelBase {
//    public int boltSize;
//
//    // fields
//    ModelRenderer mainarm;
//    ModelRenderer armorright;
//    ModelRenderer armorleft;
//    ModelRenderer wristtopright;
//    ModelRenderer wristtopleft;
//    ModelRenderer wristbottomright;
//    ModelRenderer wristbottomleft;
//    ModelRenderer index1;
//    ModelRenderer index2;
//    ModelRenderer middlefinger1;
//    ModelRenderer middlefinger2;
//    ModelRenderer ringfinger1;
//    ModelRenderer ringfinger2;
//    ModelRenderer pinky1;
//    ModelRenderer pinky2;
//    ModelRenderer thumb1;
//    ModelRenderer thumb2;
//    ModelRenderer fingerguard;
//    ModelRenderer crystalholder;
//    ModelRenderer crystal;
//    ModelRenderer supportright1;
//    ModelRenderer supportright2;
//    ModelRenderer supportright3;
//    ModelRenderer supportright4;
//    ModelRenderer supportright5;
//    ModelRenderer supportbaseright;
//    ModelRenderer palm;
//    ModelRenderer supportbaseleft;
//    ModelRenderer supportleftfront;
//    ModelRenderer supportrightfront;
//    ModelRenderer supportleft1;
//    ModelRenderer supportleft2;
//    ModelRenderer supportleft3;
//    ModelRenderer supportleft4;
//    ModelRenderer supportleft5;
//
//    public PowerFistModel() {
//        textureWidth = 64;
//        textureHeight = 32;
//
//        mainarm = new ModelRenderer(this, 0, 16);
//        mainarm.addBox(-3F, 0F, -8F, 6, 6, 10);
//        mainarm.setRotationPoint(0F, 0F, 0F);
//        mainarm.setTextureSize(64, 32);
//        mainarm.mirror = true;
//        setRotation(mainarm, 0.2617994F, 0F, 0F);
//        armorright = new ModelRenderer(this, 42, 0);
//        armorright.mirror = true;
//        armorright.addBox(1F, -1F, -9F, 3, 5, 8);
//        armorright.setRotationPoint(0F, 0F, 0F);
//        armorright.setTextureSize(64, 32);
//        armorright.mirror = true;
//        setRotation(armorright, 0.2617994F, 0F, 0F);
//        armorright.mirror = false;
//        armorleft = new ModelRenderer(this, 42, 0);
//        armorleft.addBox(-4F, -1F, -9F, 3, 5, 8);
//        armorleft.setRotationPoint(0F, 0F, 0F);
//        armorleft.setTextureSize(64, 32);
//        armorleft.mirror = true;
//        setRotation(armorleft, 0.2617994F, 0F, 0F);
//        wristtopright = new ModelRenderer(this, 0, 11);
//        wristtopright.addBox(1F, 1F, 2F, 1, 1, 4);
//        wristtopright.setRotationPoint(0F, 0F, 0F);
//        wristtopright.setTextureSize(64, 32);
//        wristtopright.mirror = true;
//        setRotation(wristtopright, 0.2617994F, 0F, 0F);
//        wristtopleft = new ModelRenderer(this, 0, 11);
//        wristtopleft.addBox(-2F, 1F, 2F, 1, 1, 4);
//        wristtopleft.setRotationPoint(0F, 0F, 0F);
//        wristtopleft.setTextureSize(64, 32);
//        wristtopleft.mirror = true;
//        setRotation(wristtopleft, 0.2617994F, 0F, 0F);
//        wristbottomright = new ModelRenderer(this, 0, 11);
//        wristbottomright.addBox(1F, 3F, 2F, 1, 1, 4);
//        wristbottomright.setRotationPoint(0F, 0F, 0F);
//        wristbottomright.setTextureSize(64, 32);
//        wristbottomright.mirror = true;
//        setRotation(wristbottomright, 0.2617994F, 0F, 0F);
//        wristbottomleft = new ModelRenderer(this, 0, 11);
//        wristbottomleft.addBox(-2F, 3F, 2F, 1, 1, 4);
//        wristbottomleft.setRotationPoint(0F, 0F, 0F);
//        wristbottomleft.setTextureSize(64, 32);
//        wristbottomleft.mirror = true;
//        setRotation(wristbottomleft, 0.2617994F, 0F, 0F);
//        index1 = new ModelRenderer(this, 34, 13);
//        index1.addBox(-.5F, -.5F, 0F, 1, 1, 5);
//        index1.setRotationPoint(-3.5F, -1.5F, 10F);
//        index1.setTextureSize(64, 32);
//        index1.mirror = true;
//        setRotation(index1, 0.2617994F, 0F, 0F);
//        index2 = new ModelRenderer(this, 34, 13);
//        index2.addBox(-.5F, -.5F, 0F, 1, 1, 4);
//        index2.setRotationPoint(0, 0, 5F);
//        index2.setTextureSize(64, 32);
//        index2.mirror = true;
//        index1.addChild(index2);
//        setRotation(index2, -0.2617994F * 2, 0F, 0F);
//        middlefinger1 = new ModelRenderer(this, 34, 13);
//        middlefinger1.addBox(-.5F, -.5F, 0F, 1, 1, 6);
//        middlefinger1.setRotationPoint(-1.5F, -1.5F, 10F);
//        middlefinger1.setTextureSize(64, 32);
//        middlefinger1.mirror = true;
//        setRotation(middlefinger1, 0.2617994F, 0F, 0F);
//        middlefinger2 = new ModelRenderer(this, 34, 13);
//        middlefinger2.addBox(-.5F, -.5F, 0F, 1, 1, 4);
//        middlefinger2.setRotationPoint(0, 0, 6F);
//        middlefinger2.setTextureSize(64, 32);
//        middlefinger2.mirror = true;
//        setRotation(middlefinger2, -0.3444116F, 0F, 0F);
//        ringfinger1 = new ModelRenderer(this, 34, 13);
//        ringfinger1.addBox(-.5F, -.5F, 0F, 1, 1, 5);
//        ringfinger1.setRotationPoint(0.5F, -1.5F, 10F);
//        ringfinger1.setTextureSize(64, 32);
//        ringfinger1.mirror = true;
//        setRotation(ringfinger1, 0.2617994F, 0F, 0F);
//        ringfinger2 = new ModelRenderer(this, 34, 13);
//        ringfinger2.addBox(-.5F, -.5F, 0F, 1, 1, 4);
//        ringfinger2.setRotationPoint(0, 0, 5F);
//        ringfinger2.setTextureSize(64, 32);
//        ringfinger2.mirror = true;
//        setRotation(ringfinger2, -0.2617994F, 0F, 0F);
//        pinky1 = new ModelRenderer(this, 34, 13);
//        pinky1.addBox(-.5F, -.5F, 0F, 1, 1, 4);
//        pinky1.setRotationPoint(2.5F, -1.5F, 10F);
//        pinky1.setTextureSize(64, 32);
//        pinky1.mirror = true;
//        setRotation(pinky1, 0.2617994F, 0F, 0F);
//        pinky2 = new ModelRenderer(this, 34, 13);
//        pinky2.addBox(-.5F, -.5F, 0F, 1, 1, 4);
//        pinky2.setRotationPoint(0, 0, 4F);
//        pinky2.setTextureSize(64, 32);
//        pinky2.mirror = true;
//        setRotation(pinky2, -0.4537856F, 0F, 0F);
//        thumb1 = new ModelRenderer(this, 16, 9);
//        thumb1.addBox(-.5F, -1F, 0F, 1, 2, 4);
//        thumb1.setRotationPoint(-4F, 1.5F, 8F);
//        thumb1.setTextureSize(64, 32);
//        thumb1.mirror = true;
//        setRotation(thumb1, 0F, -0.4014257F, 0F);
//        thumb2 = new ModelRenderer(this, 10, 0);
//        thumb2.addBox(-.5F, -.5F, 0F, 1, 1, 3);
//        thumb2.setRotationPoint(0, 0, 4F);
//        thumb2.setTextureSize(64, 32);
//        thumb2.mirror = true;
//        setRotation(thumb2, 0F, 0F, 0F);
//        fingerguard = new ModelRenderer(this, 28, 9);
//        fingerguard.addBox(-3F, -2F, 8F, 5, 2, 2);
//        fingerguard.setRotationPoint(0F, 0F, 0F);
//        fingerguard.setTextureSize(64, 32);
//        fingerguard.mirror = true;
//        setRotation(fingerguard, 0F, 0F, 0F);
//        crystalholder = new ModelRenderer(this, 48, 13);
//        crystalholder.addBox(-2F, -1F, -3F, 4, 4, 4);
//        crystalholder.setRotationPoint(0F, 0F, 0F);
//        crystalholder.setTextureSize(64, 32);
//        crystalholder.mirror = true;
//        setRotation(crystalholder, 0F, 0F, 0F);
//        crystal = new ModelRenderer(this, 32, 27);
//        crystal.addBox(-1F, -2F, -2F, 2, 2, 2);
//        crystal.setRotationPoint(0F, 0F, 0F);
//        crystal.setTextureSize(64, 32);
//        crystal.mirror = true;
//        setRotation(crystal, 0F, 0F, 0F);
//        supportright1 = new ModelRenderer(this, 54, 27);
//        supportright1.addBox(-1.8F, -0.8F, -6.066667F, 4, 1, 1);
//        supportright1.setRotationPoint(0F, 0F, 0F);
//        supportright1.setTextureSize(64, 32);
//        supportright1.mirror = true;
//        setRotation(supportright1, 0.2722714F, -1.066972F, 0F);
//        supportright2 = new ModelRenderer(this, 52, 21);
//        supportright2.addBox(4F, 0.4666667F, 2.5F, 2, 2, 1);
//        supportright2.setRotationPoint(0F, 0F, 0F);
//        supportright2.setTextureSize(64, 32);
//        supportright2.mirror = true;
//        setRotation(supportright2, 0F, 0.6329786F, 0F);
//        supportright3 = new ModelRenderer(this, 52, 21);
//        supportright3.addBox(5.1F, 1F, -0.8333333F, 1, 1, 5);
//        supportright3.setRotationPoint(0F, 0F, 0F);
//        supportright3.setTextureSize(64, 32);
//        supportright3.mirror = true;
//        setRotation(supportright3, 0F, 0F, 0F);
//        supportright4 = new ModelRenderer(this, 52, 21);
//        supportright4.addBox(5.633333F, 0.4666667F, 1.7F, 2, 2, 1);
//        supportright4.setRotationPoint(0F, 0F, 0F);
//        supportright4.setTextureSize(64, 32);
//        supportright4.mirror = true;
//        setRotation(supportright4, 0F, -0.3688404F, 0F);
//        supportright5 = new ModelRenderer(this, 54, 27);
//        supportright5.addBox(-2.866667F, 1F, 6.333333F, 4, 1, 1);
//        supportright5.setRotationPoint(0F, 0F, 0F);
//        supportright5.setTextureSize(64, 32);
//        supportright5.mirror = true;
//        setRotation(supportright5, 0F, 0.7714355F, 0F);
//        supportbaseright = new ModelRenderer(this, 47, 21);
//        supportbaseright.addBox(1.433333F, -0.6666667F, -5.4F, 3, 3, 5);
//        supportbaseright.setRotationPoint(0F, 0F, 0F);
//        supportbaseright.setTextureSize(64, 32);
//        supportbaseright.mirror = true;
//        setRotation(supportbaseright, 0.2617994F, 0F, 0F);
//        palm = new ModelRenderer(this, 18, 0);
//        palm.addBox(-4F, -1F, 5F, 7, 4, 5);
//        palm.setRotationPoint(0F, 0F, 0F);
//        palm.setTextureSize(64, 32);
//        palm.mirror = true;
//        setRotation(palm, 0F, 0F, 0F);
//        supportbaseleft = new ModelRenderer(this, 47, 21);
//        supportbaseleft.addBox(-4.4F, -0.6666667F, -5.4F, 3, 3, 5);
//        supportbaseleft.setRotationPoint(0F, 0F, 0F);
//        supportbaseleft.setTextureSize(64, 32);
//        supportbaseleft.mirror = true;
//        setRotation(supportbaseleft, 0.2617994F, 0F, 0F);
//        supportleftfront = new ModelRenderer(this, 49, 23);
//        supportleftfront.addBox(-4.333333F, 0.3333333F, 4.666667F, 1, 2, 3);
//        supportleftfront.setRotationPoint(0F, 0F, 0F);
//        supportleftfront.setTextureSize(64, 32);
//        supportleftfront.mirror = true;
//        setRotation(supportleftfront, 0F, 0F, 0F);
//        supportrightfront = new ModelRenderer(this, 49, 23);
//        supportrightfront.addBox(2.3F, 0.3333333F, 4.666667F, 1, 2, 3);
//        supportrightfront.setRotationPoint(0F, 0F, 0F);
//        supportrightfront.setTextureSize(64, 32);
//        supportrightfront.mirror = true;
//        setRotation(supportrightfront, 0F, 0F, 0F);
//        supportleft1 = new ModelRenderer(this, 54, 27);
//        supportleft1.addBox(-2.2F, -0.4F, -6.066667F, 4, 1, 1);
//        supportleft1.setRotationPoint(0F, 0F, 0F);
//        supportleft1.setTextureSize(64, 32);
//        supportleft1.mirror = true;
//        setRotation(supportleft1, 0.2722714F, 1.066978F, 0F);
//        supportleft2 = new ModelRenderer(this, 52, 21);
//        supportleft2.addBox(-6F, 0.4666667F, 2.5F, 2, 2, 1);
//        supportleft2.setRotationPoint(0F, 0F, 0F);
//        supportleft2.setTextureSize(64, 32);
//        supportleft2.mirror = true;
//        setRotation(supportleft2, 0F, -0.6329727F, 0F);
//        supportleft3 = new ModelRenderer(this, 52, 21);
//        supportleft3.addBox(-6.5F, 1F, -0.5F, 1, 1, 5);
//        supportleft3.setRotationPoint(0F, 0F, 0F);
//        supportleft3.setTextureSize(64, 32);
//        supportleft3.mirror = true;
//        setRotation(supportleft3, 0F, 0F, 0F);
//        supportleft4 = new ModelRenderer(this, 52, 21);
//        supportleft4.addBox(-7.9F, 0.4666667F, 1.7F, 2, 2, 1);
//        supportleft4.setRotationPoint(0F, 0F, 0F);
//        supportleft4.setTextureSize(64, 32);
//        supportleft4.mirror = true;
//        setRotation(supportleft4, 0F, 0.3688462F, 0F);
//        supportleft5 = new ModelRenderer(this, 54, 27);
//        supportleft5.addBox(-0.8666667F, 1F, 7F, 4, 1, 1);
//        supportleft5.setRotationPoint(0F, 0F, 0F);
//        supportleft5.setTextureSize(64, 32);
//        supportleft5.mirror = true;
//        setRotation(supportleft5, 0F, -0.7714355F, 0F);
//        index1.addChild(index2);
//        middlefinger1.addChild(middlefinger2);
//        ringfinger1.addChild(ringfinger2);
//        pinky1.addChild(pinky2);
//        thumb1.addChild(thumb2);
//        palm.addChild(index1);
//        palm.addChild(middlefinger1);
//        palm.addChild(ringfinger1);
//        palm.addChild(pinky1);
//        palm.addChild(thumb1);
//        // makeChild(index2, index1);
//        // makeChild(middlefinger2, middlefinger1);
//        // makeChild(ringfinger2, ringfinger1);
//        // makeChild(pinky2, pinky1);
//        // makeChild(thumb2, thumb1);
//        // makeChild(index1, palm);
//        // makeChild(middlefinger1, palm);
//        // makeChild(ringfinger1, palm);
//        // makeChild(pinky1, palm);
//        // makeChild(thumb1, palm);
//        // setRotation(index1, 1.2617994F, 0F, 0F);
//    }
//
//    public static int xtap;
//    public static int ytap;
//    public static int ztap;
//    public static boolean tap;
//
//    public void makeChild(ModelRenderer child, ModelRenderer parent) {
//        parent.addChild(child);
//        child.rotationPointX -= parent.rotationPointX;
//        child.rotationPointY -= parent.rotationPointY;
//        child.rotationPointZ -= parent.rotationPointZ;
//        child.rotateAngleX -= parent.rotateAngleX;
//        child.rotateAngleY -= parent.rotateAngleY;
//        child.rotateAngleZ -= parent.rotateAngleZ;
//    }
//
//    public void render(Entity entity, float scale, boolean renderTypeIsFirstPerson, Colour c1, Colour glow) {
//        // super.render(entity, f, f1, f2, f3, f4, f5);
//        int numsegments = 16;
//        if (!tap) {
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
//                xtap += 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
//                ytap += 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
//                ztap += 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
//                xtap -= 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5)) {
//                ytap -= 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
//                ztap -= 1;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
//                xtap = 0;
//                ytap = 0;
//                ztap = 0;
//                tap = true;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0)) {
//                MuseLogger.logDebug(xtap + ", " + ytap + ", " + ztap);
//                tap = true;
//            }
//        } else {
//            if (!Keyboard.isKeyDown(Keyboard.KEY_NUMPAD0) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)
//                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)
//                    && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5) && !Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
//                tap = false;
//            }
//            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//                tap = false;
//            }
//        }
//        GL11.glPushMatrix();
//        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
//        GL11.glDisable(GL11.GL_CULL_FACE);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//
//        MuseTextureUtils.pushTexture(Config.SEBK_TOOL_TEXTURE());
//
//        if (c1 != null) {
//            c1.doGL();
//        }
//        double scale1 = 1.0 / 16.0;
//        if (renderTypeIsFirstPerson) {
//            // if (entity instanceof EntityPlayer) {
//            // EntityPlayer player = (EntityPlayer) entity;
//            // RenderPlayer rp = new RenderPlayer();
//            // // Render first person hand:
//            // rp.func_82441_a(player);
//            // }
//            GL11.glScaled(scale1, scale1, scale1);
//            // GL11.glDisable(GL11.GL_CULL_FACE);
//            GL11.glRotatef(270, 1, 0, 0);
//            GL11.glRotatef(45, 0, 1, 0);
//            GL11.glRotatef(-90, 0, 0, 1);
//            GL11.glTranslatef(0, 0, 4);
//            // GL11.glRotatef(xtap, 1, 0, 0);
//            // GL11.glRotatef(ytap, 0, 1, 0);
//            // GL11.glRotatef(ztap, 0, 0, 1);
//
//        } else {
//            GL11.glScaled(-scale1, scale1, scale1);
//            GL11.glRotatef(-90, 0, 1, 0);
//            GL11.glRotatef(180, 0, 0, 1);
//            GL11.glRotatef(35, 1, 0, 0);
//            GL11.glRotatef(-5, 0, 1, 0);
//            GL11.glRotatef(1.5F, 0, 0, 1);
//            GL11.glTranslatef(2 / 4.0F, 3 / 4.0F, 1 / 4.0F);
//            GL11.glTranslatef(-2, -1, 4);
//
//        }
//        GL11.glPushMatrix();
//        // Compensate for offset when Sebk was doing his rendering
//        GL11.glRotatef(-15, 1, 0, 0);
//        GL11.glTranslatef(3, 0, 8);
//        GL11.glScalef(1 / 1.5F, 1 / 1.5F, 1 / 1.5F);
//        if (renderTypeIsFirstPerson) {
//            mainarm.render(scale);
//        }
//        armorright.render(scale);
//        armorleft.render(scale);
//        wristtopright.render(scale);
//        wristtopleft.render(scale);
//        wristbottomright.render(scale);
//        wristbottomleft.render(scale);
//        // index1.render(scale);
//        // index2.render(scale);
//        // middlefinger1.render(scale);
//        // middlefinger2.render(scale);
//        // ringfinger1.render(scale);
//        // ringfinger2.render(scale);
//        // pinky1.render(scale);
//        // pinky2.render(scale);
//        // thumb1.render(scale);
//        // thumb2.render(scale);
//        fingerguard.render(scale);
//        crystalholder.render(scale);
//        supportright1.render(scale);
//        supportright2.render(scale);
//        supportright3.render(scale);
//        supportright4.render(scale);
//        supportright5.render(scale);
//        supportbaseright.render(scale);
//        palm.render(scale);
//        supportbaseleft.render(scale);
//        supportleftfront.render(scale);
//        supportrightfront.render(scale);
//        supportleft1.render(scale);
//        supportleft2.render(scale);
//        supportleft3.render(scale);
//        supportleft4.render(scale);
//        supportleft5.render(scale);
//        RenderState.glowOn();
//        glow.doGL();
//        crystal.render(scale);
//        Colour.WHITE.doGL();
//
//        if (boltSize != 0) {
//            GL11.glTranslated(-1, 1, 16);
//            GL11.glPushMatrix();
//            EntityRenderPlasmaBolt.doRender(boltSize);
//            GL11.glPopMatrix();
//        }
//        RenderState.glowOff();
//        MuseTextureUtils.popTexture();
//        GL11.glPopMatrix();
//        GL11.glPopAttrib();
//        GL11.glPopMatrix();
//    }
//
//    private void setRotation(ModelRenderer model, float x, float y, float z) {
//        model.rotateAngleX = x;
//        model.rotateAngleY = y;
//        model.rotateAngleZ = z;
//    }
//
//    /**
//     * Sets the model's various rotation angles. For bipeds, par1 and par2 are
//     * used for animating the movement of arms and legs, where par1 represents
//     * the time(so that arms and legs swing back and forth) and par2 represents
//     * how "far" arms and legs can swing at most.
//     */
//    public void setPose(float indexOpen, float indexFlex, float thumbOpen, float thumbFlex, float otherFingersOpen, float otherFingersFlex) {
//        index1.rotateAngleX = indexOpen;
//        index2.rotateAngleX = indexFlex;
//        middlefinger1.rotateAngleX = otherFingersOpen;
//        middlefinger2.rotateAngleX = otherFingersFlex;
//        ringfinger1.rotateAngleX = otherFingersOpen;
//        ringfinger2.rotateAngleX = otherFingersFlex;
//        pinky1.rotateAngleX = otherFingersOpen - 0.1f;
//        pinky2.rotateAngleX = otherFingersFlex;
//        thumb1.rotateAngleY = -thumbOpen;
//        thumb2.rotateAngleY = -thumbFlex;
//
//    }
//
//    public void getIsFiring(EntityPlayer player, ItemStack itemStack) {
//        if (player.isHandActive() && player.getHeldItemMainhand() != null
//                && ModuleManager.itemHasActiveModule(player.getHeldItemMainhand(), PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
//            setPose(1.5f, -1, 1.5f, -1, 1.5f, -1);
//            this.boltSize = player.getItemInUseCount() > 50 ? 50 : player.getItemInUseCount();
//        } else {
//            setPose(0.5f, -1, 0.5f, -1, 0.5f, -1);
//            this.boltSize = 0;
//        }
//
//    }
//
//    public void setNeutralPose() {
//        setPose(0.5f, -1, 0.5f, -1, 0.5f, -1);
//        this.boltSize = 0;
//    }
//}
