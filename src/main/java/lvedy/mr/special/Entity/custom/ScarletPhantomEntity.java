package lvedy.mr.special.Entity.custom;

import lvedy.mr.registry.ModEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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

public class ScarletPhantomEntity extends HostileEntity implements GeoEntity {
    private static final TrackedData<Boolean> ATTACK = DataTracker.registerData(ScarletPhantomEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>  SHADOW = DataTracker.registerData(ScarletPhantomEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean>  TP = DataTracker.registerData(ScarletPhantomEntity.class,TrackedDataHandlerRegistry.BOOLEAN);
    private static final Predicate<Entity> IS_NOT_SELF = entity -> entity.isAlive() && !(entity instanceof ScarletPhantomEntity);
    private static final Predicate<Entity> IS_NOT_FRIEND = entity -> entity.isAlive() && !(entity instanceof ScarletPhantomEntity)&& !(entity instanceof ShadowEntity);
    private final ServerBossBar bossBar = (ServerBossBar)new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS).setDarkenSky(true);
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public int shadowAmount = 0;
    public ScarletPhantomEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

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
        if(livingEntity != null) {
            if ((this.getWorld().getBlockState(BlockPos.ofFloored(this.getPos().add(0,1,0))).getBlock() != Blocks.AIR) || (this.getWorld().getBlockState(BlockPos.ofFloored(this.getPos())).getBlock() == Blocks.LAVA)) {
                Vec3d vec3d = this.getPos();
                Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                Vec3d vec3d3 = vec3d2.normalize().multiply(0.7);
                Vec3d vec3d4 = vec3d.add(vec3d3);
                this.teleport(vec3d4.x, vec3d4.y, vec3d4.z);
            }
        }
        this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
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
        this.goalSelector.add(1, new MAttack(this,1.0,false));
        this.goalSelector.add(2,new Attack(this));
        this.goalSelector.add(3,new Tp(this));
        this.goalSelector.add(4,new SummonShadow(this));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(8, new LookAroundGoal(this));

        this.targetSelector.add(2,new ActiveTargetGoal<>(this, PlayerEntity.class,true));
        this.targetSelector.add(3,new ActiveTargetGoal<>(this, SnowGolemEntity.class,true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder createrScarletPhantom() {
        return MobEntity.createMobAttributes().
                add(EntityAttributes.GENERIC_FOLLOW_RANGE,20f).
                add(EntityAttributes.GENERIC_ATTACK_DAMAGE,0f).
                add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0f).
                add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,1.0f).
                add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f).
                add(EntityAttributes.GENERIC_ARMOR,8).
                add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS,8);
    }

    public boolean damage(DamageSource source, float amount) {
        LivingEntity livingEntity = this.getTarget();
        if(livingEntity == null) {
            if (source.getAttacker() instanceof LivingEntity) {
                livingEntity = (LivingEntity) source.getAttacker();
                Vec3d vec3d = this.getPos();
                Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                Vec3d vec3d3 = vec3d2.normalize().multiply(0.7);
                Vec3d vec3d4 = livingEntity.getPos().add(vec3d3);
                this.teleport(vec3d4.x, vec3d4.y, vec3d4.z);
            }
            else{
                List<PlayerEntity> list = this.getWorld().getEntitiesByClass(PlayerEntity.class,this.getBoundingBox().expand(35), IS_NOT_SELF);
                if(list != null) {
                    for (PlayerEntity player : list) {
                        livingEntity = player;
                        Vec3d vec3d = this.getPos();
                        Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                        Vec3d vec3d3 = vec3d2.normalize().multiply(0.7);
                        Vec3d vec3d4 = livingEntity.getPos().add(vec3d3);
                        this.teleport(vec3d4.x, vec3d4.y, vec3d4.z);
                        break;
                    }
                }
            }
        }
        int a = random.nextInt(10);
        if((a<=3) && ((this.squaredDistanceTo(livingEntity) > 4) || (this.getTarget() == null)) && !source.isOf(DamageTypes.GENERIC_KILL))
            return false;
        else {
            if ((!this.getWorld().isClient) && (!(source.getAttacker() instanceof PlayerEntity))) {
                amount = amount / 10.0f;
                super.damage(source, amount);
                return true;
            } else
                super.damage(source, amount);
            return true;
        }
    }

    public void setAttack(boolean attacking){
        this.dataTracker.set(ATTACK,attacking);
    }

    public boolean isAttack() {
        return this.dataTracker.get(ATTACK);
    }

    public void setShadow(boolean attacking){
        this.dataTracker.set(SHADOW,attacking);
    }

    public boolean isShadow() {
        return this.dataTracker.get(SHADOW);
    }

    public void setTp(boolean attacking){
        this.dataTracker.set(TP,attacking);
    }

    public boolean isTp() {
        return this.dataTracker.get(TP);
    }

    protected  void initDataTracker(){
        super.initDataTracker();
        this.dataTracker.startTracking(ATTACK,false);
        this.dataTracker.startTracking(SHADOW,false);
        this.dataTracker.startTracking(TP,false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,"controller",0,this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState){
        if(this.isShadow()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("shadow", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(isTp()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("tp", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(isAttack()) {
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("attack", Animation.LoopType.PLAY_ONCE));
            return PlayState.CONTINUE;
        }
        if(tAnimationState.isMoving() && !this.isAttack() && !this.isShadow() && !this.isTp()) {
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
        private final ScarletPhantomEntity ScarletPhantom;
        private int Cooldown;
        private int frame;

        public Attack(ScarletPhantomEntity scarletPhantom) {
            this.ScarletPhantom = scarletPhantom;
        }


        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            return livingEntity != null && livingEntity.isAlive();
        }

        public void start() {
            this.frame = 0;
            this.Cooldown = 20;
        }

        public void stop() {
            this.frame = 0;
            this.ScarletPhantom.setAttack(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            List<LivingEntity> list = null;
            --this.Cooldown;
            this.ScarletPhantom.getLookControl().lookAt(livingEntity);
            if((this.Cooldown <= 0) && (!this.ScarletPhantom.isShadow()) && (!this.ScarletPhantom.isTp())){
                if((this.ScarletPhantom.squaredDistanceTo(livingEntity) <= 4) || this.ScarletPhantom.isAttack()){
                    this.ScarletPhantom.setAttack(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 12) {
                        Vec3d vec3d = this.ScarletPhantom.getPos();
                        Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                        Vec3d vec3d3 = vec3d2.normalize();
                        for (int i = 0; i < 3; ++i) {
                            Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                            list = this.ScarletPhantom.getWorld().getEntitiesByClass(LivingEntity.class, Box.of(vec3d4, 1.4, 1.4, 1.4), IS_NOT_FRIEND);
                        }
                        for (LivingEntity entity : list) {
                            entity.damage(this.ScarletPhantom.getDamageSources().mobAttack(this.ScarletPhantom), 10);
                        }
                    }
                    if (this.frame == 18) {
                        this.frame = 0;
                        this.Cooldown = 20;
                        this.ScarletPhantom.setAttack(false);
                    }
                }
                else{
                    this.Cooldown = 0;
                    this.frame = 0;
                }
            }
        }
    }
    public class SummonShadow extends Goal{
        private final ScarletPhantomEntity ScarletPhantom;
        private int Cooldown;
        private int frame;

        SummonShadow(ScarletPhantomEntity scarletPhantom) {
            this.ScarletPhantom = scarletPhantom;
        }
        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            return ((livingEntity != null) && (livingEntity.isAlive()));
        }

        public void start() {
            this.frame = 0;
            this.Cooldown = 200;
        }

        public void stop() {
            this.frame = 0;
            this.ScarletPhantom.setShadow(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            World world = this.ScarletPhantom.getWorld();
            List<ShadowEntity> shadowlist = null;
            --this.Cooldown;
            if((this.Cooldown <= 0) && (!this.ScarletPhantom.isTp()) && (!this.ScarletPhantom.isAttack())){
                if(this.ScarletPhantom.shadowAmount < 3){
                    this.ScarletPhantom.setShadow(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 10) {
                        ShadowEntity shadow = ModEntity.SHADOW.create(world);
                        shadow.refreshPositionAfterTeleport(this.ScarletPhantom.getX(),this.ScarletPhantom.getY(),this.ScarletPhantom.getZ());
                        world.spawnEntity(shadow);
                        shadow.setMaster(this.ScarletPhantom);
                        this.ScarletPhantom.shadowAmount++;
                    }
                }
                else{
                    this.ScarletPhantom.setShadow(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 10) {
                        shadowlist = this.ScarletPhantom.getWorld().getEntitiesByClass(ShadowEntity.class, this.ScarletPhantom.getBoundingBox().expand(35), IS_NOT_SELF);
                        for (ShadowEntity shadow : shadowlist) {
                            Vec3d vec3d = shadow.getPos();
                            Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                            Vec3d vec3d3 = vec3d2.normalize();
                            livingEntity.damage(shadow.getDamageSources().mobAttack(shadow), 10);
                            Vec3d vec3d5 = livingEntity.getPos().add(vec3d3);
                            shadow.teleport(vec3d5.x, vec3d5.y, vec3d5.z);
                        }
                    }
                }
                if (this.frame == 15) {
                    this.frame = 0;
                    this.Cooldown = 200;
                    this.ScarletPhantom.setShadow(false);
                }
            }
        }
    }

    static class Tp extends Goal{
        private final ScarletPhantomEntity ScarletPhantom;
        private int Cooldown;
        private int frame;

        Tp(ScarletPhantomEntity scarletPhantom) {
            this.ScarletPhantom = scarletPhantom;
        }

        @Override
        public boolean canStart() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            return ((livingEntity != null) && (livingEntity.isAlive()));
        }

        public void start() {
            this.frame = 0;
            this.Cooldown = 300;
        }

        public void stop() {
            this.frame = 0;
            this.ScarletPhantom.setTp(false);
        }

        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.ScarletPhantom.getTarget();
            --this.Cooldown;
            List<LivingEntity> list = null;
            if((this.Cooldown <= 0) && (!this.ScarletPhantom.isShadow()) && (!this.ScarletPhantom.isAttack())){
                if((this.ScarletPhantom.squaredDistanceTo(livingEntity) <= 49) || this.ScarletPhantom.isTp()){
                    this.ScarletPhantom.setTp(true);
                    this.Cooldown = 0;
                    this.frame++;
                    if (this.frame == 10) {
                        Vec3d vec3d = this.ScarletPhantom.getPos();
                        Vec3d vec3d2 = livingEntity.getPos().subtract(vec3d);
                        Vec3d vec3d3 = vec3d2.normalize();
                        for (int i = 0; i < MathHelper.floor(vec3d2.length()) + 1; ++i) {
                            Vec3d vec3d4 = vec3d.add(vec3d3.multiply(i));
                            list = this.ScarletPhantom.getWorld().getEntitiesByClass(LivingEntity.class, Box.of(vec3d4, 1.6f, 1.6f, 1.6f), IS_NOT_FRIEND);
                        }
                        if (list != null) {
                            for (LivingEntity entity : list) {
                                entity.damage(this.ScarletPhantom.getDamageSources().mobAttack(this.ScarletPhantom), 10+livingEntity.getArmor());
                            }
                        }
                        Vec3d vec3d5 = livingEntity.getPos().add(vec3d3);
                        this.ScarletPhantom.teleport(vec3d5.x,vec3d5.y,vec3d5.z);
                    }
                    if (this.frame == 20) {
                        this.frame = 0;
                        this.Cooldown = 300;
                        this.ScarletPhantom.setTp(false);
                    }
                }
                else{
                    this.Cooldown = 0;
                    this.frame = 0;
                }
            }
        }
    }

    static class MAttack extends MeleeAttackGoal{

        public MAttack(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        protected void attack(LivingEntity target) {
            if (this.canAttack(target)) {
                this.resetCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
            }
        }
    }
}
