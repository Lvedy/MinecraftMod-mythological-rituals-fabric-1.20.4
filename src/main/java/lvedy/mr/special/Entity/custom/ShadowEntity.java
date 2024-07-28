package lvedy.mr.special.Entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ShadowEntity extends HostileEntity implements GeoEntity {
    private ScarletPhantomEntity master;
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final TrackedData<Boolean> ATTACK = DataTracker.registerData(ShadowEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>  AOE = DataTracker.registerData(ShadowEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final Predicate<Entity> IS_NOT_SELF = entity -> entity.isAlive() && !(entity instanceof ScarletPhantomEntity) && !(entity instanceof ShadowEntity);
    public ShadowEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    public void move(MovementType movementType, Vec3d movement) {
        super.move(movementType, movement);
        this.checkBlockCollision();
    }

    public void setMaster(ScarletPhantomEntity master){
        this.master=master;
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new BirdNavigation(this, world);
    }

    protected void mobTick() {
        //a?
        //this.ScarletPhantom.playSound(SoundEvents.BLOCK_ANVIL_PLACE,5,5);
        this.noClip = true;
        this.setNoGravity(true);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        if(this.master!=null){
            this.master.shadowAmount--;
            this.master=null;
        }
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1,new Attack(this));
        this.goalSelector.add(2,new AOE(this));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(2,new ActiveTargetGoal<>(this, PlayerEntity.class,true));
        this.targetSelector.add(3,new ActiveTargetGoal<>(this, SnowGolemEntity.class,true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createrShadow() {
        return MobEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_FOLLOW_RANGE,20f).
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE,0.2f).
                add(EntityAttributes.GENERIC_MAX_HEALTH, 40).
                add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,0f).
                add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6f).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6f).
                add(EntityAttributes.GENERIC_ARMOR,4).
                add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,6);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.FALL)) {
            return false;
        }
        return super.damage(source, amount);
    }

    public void setAttack(boolean attacking){
        this.dataTracker.set(ATTACK,attacking);
    }

    public boolean isAttack() {
        return this.dataTracker.get(ATTACK);
    }

    public void setAOE(boolean attacking){
        this.dataTracker.set(AOE,attacking);
    }

    public boolean isAOE() {
        return this.dataTracker.get(AOE);
    }

    protected  void initDataTracker(){
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACK,false);
        this.dataTracker.startTracking(AOE,false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,"controller",0,this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState){
        if(this.isAOE()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("aoe", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(this.isAttack()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(tAnimationState.isMoving()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("move", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("rest", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    static class Attack extends Goal{
        private final ShadowEntity shadow;
        private int Cooldown;
        private int frame;

        public Attack(ShadowEntity mob) {
            this.shadow = mob;
            this.setControls(EnumSet.of(Control.MOVE));
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.shadow.getTarget();
            return livingEntity != null && livingEntity.isAlive();
        }

        public void start() {
            LivingEntity livingEntity = this.shadow.getTarget();
            if (livingEntity != null) {
                Vec3d vec3d = livingEntity.getPos();
                this.shadow.moveControl.moveTo(vec3d.x, vec3d.y + 1, vec3d.z, 1.0);
            }
            this.frame = 0;
            this.Cooldown = 30;
        }

        public void stop() {
            this.frame = 0;
            this.shadow.setAttack(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.shadow.getTarget();
            List<LivingEntity> list;
            --this.Cooldown;
            this.shadow.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
            if((this.Cooldown <= 0) && (!this.shadow.isAOE())){
                if((this.shadow.squaredDistanceTo(livingEntity) <= 4) || this.shadow.isAttack()){
                    this.shadow.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED).setBaseValue(0.1f);
                    this.shadow.setAttack(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 13) {
                        Vec3d vec3d = this.shadow.getPos();
                        Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                        Vec3d vec3d3 = vec3d2.normalize();
                        Vec3d vec3d4 = vec3d.add(vec3d3.multiply(1));
                        list = this.shadow.getWorld().getEntitiesByClass(LivingEntity.class, Box.of(vec3d4, 3, 2, 3), IS_NOT_SELF);
                        for (LivingEntity entity : list) {
                            entity.damage(this.shadow.getDamageSources().mobAttack(this.shadow), 10);
                        }
                    }
                    if (this.frame == 30) {
                        this.frame = 0;
                        this.Cooldown = 30;
                        this.shadow.setAttack(false);
                    }
                }
                else{
                    this.shadow.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED).setBaseValue(0.6f);
                    if (livingEntity != null) {
                        Vec3d vec3d = livingEntity.getPos();
                        this.shadow.moveControl.moveTo(vec3d.x, vec3d.y + 1, vec3d.z, 1.0);
                    }
                    this.Cooldown = 0;
                    this.frame = 0;
                }
            }
        }
    }

    static class AOE extends Goal{
        private final ShadowEntity shadow;
        private int Cooldown;
        private int frame;

        AOE(ShadowEntity shadow) {
            this.shadow = shadow;
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.shadow.getTarget();
            return livingEntity != null && livingEntity.isAlive();
        }

        public void start() {
            this.frame = 0;
            this.Cooldown = 200;
        }

        public void stop() {
            this.frame = 0;
            this.shadow.setAOE(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.shadow.getTarget();
            List<LivingEntity> list;
            --this.Cooldown;
            if((this.Cooldown <= 0) && (!this.shadow.isAttack())){
                if((this.shadow.squaredDistanceTo(livingEntity) <= 25) || this.shadow.isAOE()){
                    this.shadow.setAOE(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 15) {
                        list = this.shadow.getWorld().getEntitiesByClass(LivingEntity.class,this.shadow.getBoundingBox().expand(5), IS_NOT_SELF);
                        for (LivingEntity entity : list) {
                            entity.damage(this.shadow.getDamageSources().magic(), 10);
                        }
                    }
                    if (this.frame == 35) {
                        this.frame = 0;
                        this.Cooldown = 200;
                        this.shadow.setAOE(false);
                    }
                }
                else{
                    this.Cooldown = 0;
                    this.frame = 0;
                }
            }
        }
    }
}
