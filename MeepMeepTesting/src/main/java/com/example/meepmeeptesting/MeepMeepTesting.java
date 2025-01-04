package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        Pose2d startpose = new Pose2d(-14,-58,Math.toRadians(90.0));

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setDimensions(18,18)
                .setStartPose(startpose)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(startpose)
                        .splineTo(new Vector2d(0,-34),Math.toRadians(90.0))
                        .setTangent(Math.toRadians(270))
                        .splineToConstantHeading(new Vector2d(-48,-39),Math.toRadians(90.0))
                        .splineToLinearHeading(new Pose2d(-56,-56,Math.toRadians(225.0)),Math.toRadians(225.0))
                        .setTangent(Math.toRadians(90))
                        .splineTo(new Vector2d(-57,-39),Math.toRadians(-45.0))
                        .splineToLinearHeading(new Pose2d(-56,-56,Math.toRadians(225.0)),Math.toRadians(225.0))
//                        .setTangent(Math.toRadians(270))
//                        .splineTo(new Vector2d(56,-58),Math.toRadians(90.0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}