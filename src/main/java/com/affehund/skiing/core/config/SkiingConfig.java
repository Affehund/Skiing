package com.affehund.skiing.core.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SkiingConfig {
    public static class SkiingCommonConfig {
        public static ForgeConfigSpec.DoubleValue MAX_SKI_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SKI_VELOCITY;
        public static ForgeConfigSpec.DoubleValue MAX_SKI_REVERSE_VELOCITY;
        public static ForgeConfigSpec.DoubleValue SKI_ACCELERATION;
        public static ForgeConfigSpec.DoubleValue SKI_ACCELERATION_WITH_STICK;

        public static ForgeConfigSpec.DoubleValue MAX_SLED_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SLED_VELOCITY;
        public static ForgeConfigSpec.ConfigValue<Double> MAX_SLED_REVERSE_VELOCITY;
        public static ForgeConfigSpec.ConfigValue<Double> SLED_ACCELERATION;

        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_HEALTH;
        public static ForgeConfigSpec.DoubleValue MAX_SNOWBOARD_VELOCITY;
        public static ForgeConfigSpec.ConfigValue<Double> MAX_SNOWBOARD_REVERSE_VELOCITY;
        public static ForgeConfigSpec.ConfigValue<Double> SNOWBOARD_ACCELERATION;

        public static void setup(ForgeConfigSpec.Builder builder) {
            builder.comment("Skiing Common Config");
            builder.push("general");

            builder.pop();
    
            builder.push("ski");
                MAX_SKI_HEALTH = builder.defineInRange("max_ski_health", 40D, 1D, 100D);
                MAX_SKI_VELOCITY = builder.defineInRange("max_ski_velocity", 1D, 0D, 1D);
                MAX_SKI_REVERSE_VELOCITY = builder.defineInRange("max_ski_reverse_velocity", 0.25D, 0D, 1.0D);
                SKI_ACCELERATION = builder.defineInRange("ski_acceleration", 0.25D, 0, 1D);
                SKI_ACCELERATION_WITH_STICK = builder.defineInRange("ski_acceleration_with_stick", 0.3D, 0, 1D);
            builder.pop();

            builder.push("sled");
                MAX_SLED_HEALTH = builder.defineInRange("max_sled_health", 40D, 1D, 100D);
                MAX_SLED_VELOCITY = builder.defineInRange("max_sled_velocity", 1D, 0D, 1D);
                MAX_SLED_REVERSE_VELOCITY = builder.defineInRange("max_sled_reverse_velocity", 0.25D, 0D, 1.0D);
                SLED_ACCELERATION = builder.defineInRange("sled_acceleration", 0.3D, 0, 1D);
            builder.pop();

            builder.push("snowboard");
                MAX_SNOWBOARD_HEALTH = builder.defineInRange("max_snowboard_health", 40D, 1D, 100D);
                MAX_SNOWBOARD_VELOCITY = builder.defineInRange("max_snowboard_velocity", 1D, 0D, 1D);
                MAX_SNOWBOARD_REVERSE_VELOCITY = builder.defineInRange("max_snowboard_reverse_velocity", 0.25D, 0D, 1.0D);
                SNOWBOARD_ACCELERATION = builder.defineInRange("snowboard_acceleration", 0.3D, 0, 1D);
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
