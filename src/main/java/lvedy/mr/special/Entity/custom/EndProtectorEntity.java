package lvedy.mr.special.Entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Predicate;

public class EndProtectorEntity extends HostileEntity implements GeoEntity {
    private int age = 0;
    private static final TrackedData<Boolean>  SUMMON = DataTracker.registerData(EndProtectorEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>  ATTACK1 = DataTracker.registerData(EndProtectorEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>  ATTACK2 = DataTracker.registerData(EndProtectorEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final Predicate<Entity> IS_NOT_SELF = entity -> entity.isAlive() && !(entity instanceof EndProtectorEntity);
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS).setDarkenSky(true);
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public EndProtectorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (this.hasCustomName()) {
            this.bossBar.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Text name) {
        super.setCustomName(name);
        this.bossBar.setName(this.getDisplayName());
    }

    @Override
    protected void mobTick() {
        //bang!
        //this.playSound(SoundEvents.BLOCK_ANVIL_PLACE,5,5);
        LivingEntity livingEntity = this.getTarget();
        this.age++;
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
        this.setSummon(this.age <= 30);
        if(this.age>=100000){
            this.age = 31;
        }
        if(livingEntity == null){
            setSummon(false);
            setAttacking1(false);
            setAttacking2(false);
        }
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player) {
        super.onStartedTrackingBy(player);
        this.bossBar.addPlayer(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player) {
        super.onStoppedTrackingBy(player);
        this.bossBar.removePlayer(player);
    }

    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1,new Attack1(this));
        this.goalSelector.add(2,new Attack2(this));
        this.goalSelector.add(3,new EndProtectorMeleeAttackGoal(this,1.0,false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(2,new ActiveTargetGoal<>(this,PlayerEntity.class,true));
        this.targetSelector.add(3,new ActiveTargetGoal<>(this, SnowGolemEntity.class,true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createrEndProtector() {
        return MobEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_FOLLOW_RANGE,20f).
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE,12f).
                add(EntityAttributes.GENERIC_MAX_HEALTH, 270.0f).
                add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,1.0f).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25f).
                add(EntityAttributes.GENERIC_ARMOR,12).
                add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,4);
    }
    
    public boolean damage(DamageSource source, float amount) {
        if(source.getSource() instanceof ArrowEntity)
            return false;
        if ((!this.getWorld().isClient) && (!(source.getAttacker() instanceof PlayerEntity))) {
            amount = amount/10.0f;
            super.damage(source,amount);
            return true;
        }
        else
            super.damage(source,amount);
        return true;
    }
    public void setSummon(boolean attacking){
        this.dataTracker.set(SUMMON,attacking);
    }

    public boolean isSummon() {
        return this.dataTracker.get(SUMMON);
    }

    public void setAttacking1(boolean attacking){
        this.dataTracker.set(ATTACK1,attacking);
    }

    public boolean isAttacking1() {
        return this.dataTracker.get(ATTACK1);
    }

    public void setAttacking2(boolean attacking){
        this.dataTracker.set(ATTACK2,attacking);
    }

    public boolean isAttacking2() {
        return this.dataTracker.get(ATTACK2);
    }

    protected  void initDataTracker(){
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACK2,false);
        this.dataTracker.startTracking(ATTACK1,false);
        this.dataTracker.startTracking(SUMMON,false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,"controller",0,this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState){
        if(this.isSummon()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("summon", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(this.isAttacking1()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("attack1", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(this.isAttacking2()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("attack2", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(tAnimationState.isMoving() && !this.isAttacking2()) {
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

    static class Attack1 extends Goal{
        private final EndProtectorEntity EndProtector;
        private int Cooldown;
        private int frame;
        public Attack1(EndProtectorEntity EndProtector) {
            this.EndProtector = EndProtector;
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.EndProtector.getTarget();
            return livingEntity != null && livingEntity.isAlive();
        }

        @Override
        public void start() {
            this.frame = 0;
            this.Cooldown = 220;
        }

        @Override
        public void stop() {
            this.frame = 0;
            this.EndProtector.setAttacking1(false);
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.EndProtector.getTarget();
            this.EndProtector.setAttacking(false);
             double RxR = (Math.pow((livingEntity.getX() - this.EndProtector.getX()), 2) + Math.pow((livingEntity.getZ() - this.EndProtector.getZ()), 2));
            --this.Cooldown;
            if((this.Cooldown <= 0) && (!this.EndProtector.isAttacking2())){
                if((RxR <= 9) || this.EndProtector.isAttacking1()) {
                    this.EndProtector.setAttacking1(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 20) {
                        this.EndProtector.playSound(SoundEvents.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR,5,5);
                        List<LivingEntity> list = this.EndProtector.getWorld().getEntitiesByClass(LivingEntity.class, this.EndProtector.getBoundingBox().expand(3.0), IS_NOT_SELF);
                        for (LivingEntity entity : list) {
                            entity.setVelocity(this.EndProtector.getVelocity().add(0, 1, 0));
                            entity.damage(this.EndProtector.getDamageSources().mobAttack(this.EndProtector), 30);
                        }
                    }
                    if (this.frame == 40) {
                        this.frame = 0;
                        this.Cooldown = 220;
                        this.EndProtector.setAttacking1(false);
                    }
                }
                else{
                    this.Cooldown = 0;
                    this.frame = 0;
                }
            }
        }
    }

    static class Attack2 extends Goal{
        private final EndProtectorEntity EndProtector;
        private int Cooldown;
        private int frame;
        public Attack2(EndProtectorEntity EndProtector) {
            this.EndProtector = EndProtector;
        }
        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.EndProtector.getTarget();
            return ((livingEntity != null) && (livingEntity.isAlive()));
        }

        @Override
        public void start() {
            this.frame = 0;
            this.Cooldown = 400;
        }

        public void stop() {
            this.frame = 0;
            this.EndProtector.setAttacking2(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.EndProtector.getTarget();
            float amount = 4;
            --this.Cooldown;
            if(this.Cooldown<=0 && (!this.EndProtector.isAttacking1())){
                this.EndProtector.setAttacking2(true);
                this.EndProtector.setAttacking(false);
                this.frame++;
                this.Cooldown = 0;
                if(this.frame <= 10){
                    this.EndProtector.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0f);
                }
                if(this.frame<=180 && this.frame>10) {
                    this.EndProtector.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3f);
                    this.EndProtector.getMoveControl().moveTo(livingEntity.getX(),livingEntity.getY(),livingEntity.getZ(),2.0f);
                    List<LivingEntity> list = this.EndProtector.getWorld().getEntitiesByClass(LivingEntity.class, this.EndProtector.getBoundingBox().expand(2.5), IS_NOT_SELF);
                    for (LivingEntity entity : list) {
                        if((this.frame-10)%10 == 0) {
                            amount = Math.max(this.EndProtector.getTarget().getMaxHealth() / 50, 4);
                            entity.damage(this.EndProtector.getDamageSources().mobAttack(this.EndProtector), amount);
                        }
                    }
                }
                if(this.frame < 250 && this.frame>180){
                    this.EndProtector.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.0f);
                }
                if(this.frame >= 250){
                    this.frame = 0;
                    this.EndProtector.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3f);
                    this.EndProtector.setAttacking2(false);
                    this.Cooldown = 400;
                }
            }
        }
    }
    static class EndProtectorMeleeAttackGoal extends MeleeAttackGoal{
        private final EndProtectorEntity EndProtector;
        public EndProtectorMeleeAttackGoal(EndProtectorEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.EndProtector = mob;
        }

        @Override
        public void tick() {
            if((!this.EndProtector.isAttacking1()) && (!this.EndProtector.isAttacking2()) && (this.EndProtector.age>30))
                super.tick();
        }
    }
}
