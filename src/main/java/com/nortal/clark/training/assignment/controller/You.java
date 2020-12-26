package com.nortal.clark.training.assignment.controller;

import com.nortal.clark.training.assignment.model.CityMap;
import com.nortal.clark.training.assignment.model.Clark;
import com.nortal.clark.training.assignment.model.Direction;
import com.nortal.clark.training.assignment.model.Position;
import com.nortal.clark.training.assignment.model.SpeedLevel;
import com.nortal.clark.training.assignment.model.VoiceCommand;

import java.util.List;

public class You {

    private List<Position> targetsToCapture;

    public VoiceCommand getNextStep(Clark clark, CityMap cityMap) {
        VoiceCommand voiceCommand = new VoiceCommand(Direction.SOUTH, SpeedLevel.L0_RUNNING_HUMAN);

        // TODO: Implement algorithm to return command to Clark to capture all the targets on the city map provided
        //<<SOLUTION START>>

        if (targetsToCapture == null) {
            targetsToCapture = cityMap.getTargets();
        }

        Position targetToCapture = targetsToCapture.get(0);
        System.out.println(clark + " ->> x=" + targetToCapture.x + ", y=" + targetToCapture.y);

        int diffX = Math.abs(targetToCapture.x - clark.getPosition().x);
        int diffY = Math.abs(targetToCapture.y - clark.getPosition().y);

        SpeedLevel horizontalSpeedLevel = thinkOfSpeedLevel(diffX, clark.getHorizontal());
        SpeedLevel verticalSpeedLevel = thinkOfSpeedLevel(diffY, clark.getVertical());

        if (diffX < 2 && diffY < 2) {
            System.out.println("Removing target");
            //Consider it captured
            targetsToCapture.remove(0);

        } else if (targetToCapture.x > clark.getPosition().x) {
            voiceCommand = new VoiceCommand(Direction.EAST, horizontalSpeedLevel);
        } else if (targetToCapture.y > clark.getPosition().y) {
            voiceCommand = new VoiceCommand(Direction.NORTH, verticalSpeedLevel);
        } else if (targetToCapture.x < clark.getPosition().x) {
            voiceCommand = new VoiceCommand(Direction.WEST, horizontalSpeedLevel);
        } else if (targetToCapture.y < clark.getPosition().y) {
            voiceCommand = new VoiceCommand(Direction.SOUTH, verticalSpeedLevel);
        }

        System.out.println(voiceCommand);
        //<<SOLUTION END>>
        return voiceCommand;
    }

    //<<SOLUTION START>>
    private SpeedLevel thinkOfSpeedLevel(int distanceDiff, double speed) {
        double realSpeed = speed - getDragAcceleration(speed);
        double timeCheck = distanceDiff / speed ;
        System.out.println("Дистанция " + distanceDiff );
        System.out.println("Скорость при силе отталкивания: " + realSpeed);
        System.out.println("Время " + timeCheck);

        // 167.5 m/s
        if(timeCheck < 16.75 && timeCheck > 0)
            return SpeedLevel.L1_TRAIN;
        // 340 m/s
        if(timeCheck > 34 && timeCheck < 16.75 && timeCheck > 0)
            return SpeedLevel.L2_SUB_SONIC;
        // 1700 m/s
        if(timeCheck > 170 && timeCheck < 34 && timeCheck > 0)
            return SpeedLevel.L3_SUPER_SONIC;
        // 370k m/s
        return SpeedLevel.L4_MACH_9350;
    }
    double getDragAcceleration(double currentSpeed) {
        double dragDirectionalModifier = -Math.signum(currentSpeed);
        double waterDrag = 1.6 + (Math.pow(currentSpeed, 2) / 200);
        return dragDirectionalModifier * waterDrag;
    }
    //<<SOLUTION END>>
}
