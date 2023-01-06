package com.affehund.skiing.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkiingConfig {
    public static class SkiingCommonConfig {
        public static ForgeConfigSpec.DoubleValue MIN_REQUIRED_VELOCITY_FOR_ROTATION;

        public static ForgeConfigSpec.DoubleValue MAX_SKI_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SKI_VELOCITY;
        public static ForgeConfigSpec.DoubleValue MAX_SKI_REVERSE_VELOCITY;
        public static ForgeConfigSpec.DoubleValue SKI_ACCELERATION;
        public static ForgeConfigSpec.DoubleValue SKI_ACCELERATION_WITH_STICK;
        public static ForgeConfigSpec.DoubleValue MAX_SKI_UP_STEP;

        public static ForgeConfigSpec.DoubleValue MAX_SLED_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SLED_VELOCITY;
        public static ForgeConfigSpec.DoubleValue MAX_SLED_REVERSE_VELOCITY;
        public static ForgeConfigSpec.DoubleValue SLED_ACCELERATION;
        public static ForgeConfigSpec.DoubleValue MAX_SLED_UP_STEP;

        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_VELOCITY;
        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_REVERSE_VELOCITY;
        public static ForgeConfigSpec.DoubleValue SNOWBOARD_ACCELERATION;
        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_UP_STEP;

        public static void setup(ForgeConfigSpec.Builder builder) {
            builder.comment("Skiing Common Config");
            builder.push("general");
            MIN_REQUIRED_VELOCITY_FOR_ROTATION = builder
                    .comment("This sets the minimum required velocity you need to rotate your ski, sled or snowboard.")
                    .defineInRange("min_required_velocity_for_rotation", 0.02D, 0D, 0.1D);
            builder.pop();
    
            builder.push("ski");
                MAX_SKI_HEALTH = builder
                        .comment("This sets the maximum health of the ski.")
                        .defineInRange("max_ski_health", 40D, 1D, 100D);
                MAX_SKI_VELOCITY = builder
                        .comment("This sets the maximum velocity for the ski.")
                        .defineInRange("max_ski_velocity", 1D, 0D, 1D);
                MAX_SKI_REVERSE_VELOCITY = builder
                        .comment("This sets the maximum reverse velocity for the ski.")
                        .defineInRange("max_ski_reverse_velocity", 0.25D, 0D, 1.0D);
                SKI_ACCELERATION = builder
                        .comment("This sets the acceleration of the ski.")
                        .defineInRange("ski_acceleration", 0.25D, 0, 1D);
                SKI_ACCELERATION_WITH_STICK = builder
                        .comment("This sets the acceleration of the ski when using ski sticks.")
                        .defineInRange("ski_acceleration_with_stick", 0.3D, 0, 1D);
                MAX_SKI_UP_STEP = builder
                        .comment("This sets the height a ski can go upwards in blocks.")
                        .defineInRange("max_ski_up_step", 0.6D, 0, 1D);
            builder.pop();

            builder.push("sled");
                MAX_SLED_HEALTH = builder
                        .comment("This sets the maximum health of the sled.")
                        .defineInRange("max_sled_health", 40D, 1D, 100D);
                MAX_SLED_VELOCITY = builder
                        .comment("This sets the maximum velocity for the sled.")
                        .defineInRange("max_sled_velocity", 1D, 0D, 1D);
                MAX_SLED_REVERSE_VELOCITY = builder
                        .comment("This sets the maximum reverse velocity for the sled.")
                        .defineInRange("max_sled_reverse_velocity", 0.25D, 0D, 1.0D);
                SLED_ACCELERATION = builder
                        .comment("This sets the acceleration of the sled.")
                        .defineInRange("sled_acceleration", 0.3D, 0, 1D);
                MAX_SLED_UP_STEP = builder
                        .comment("This sets the height a sled can go upwards in blocks.")
                        .defineInRange("max_sled_up_step", 0.6D, 0, 1D);
            builder.pop();

            builder.push("snowboard");
                MAX_SNOWBOARD_HEALTH = builder
                        .comment("This sets the maximum health of the snowboard.")
                        .defineInRange("max_snowboard_health", 40D, 1D, 100D);
                MAX_SNOWBOARD_VELOCITY = builder
                        .comment("This sets the maximum velocity for the snowboard.")
                        .defineInRange("max_snowboard_velocity", 1D, 0D, 1D);
                MAX_SNOWBOARD_REVERSE_VELOCITY = builder
                        .comment("This sets the maximum reverse velocity for the snowboard.")
                        .defineInRange("max_snowboard_reverse_velocity", 0.25D, 0D, 1.0D);
                SNOWBOARD_ACCELERATION = builder
                        .comment("This sets the acceleration of the snowboard.")
                        .defineInRange("snowboard_acceleration", 0.3D, 0, 1D);
                MAX_SNOWBOARD_UP_STEP = builder
                        .comment("This sets the height a snowboard can go upwards in blocks.")
                        .defineInRange("max_snowboard_up_step", 0.6D, 0, 1D);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec COMMON_CONFIG_SPEC;

    static {
        ForgeConfigSpec.Builder configBuilder = new ForgeConfigSpec.Builder();
        SkiingCommonConfig.setup(configBuilder);
        COMMON_CONFIG_SPEC = configBuilder.build();
    }
}
