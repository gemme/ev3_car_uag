package uag.subsumption;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

public class MyDelay implements Behavior {

    private boolean suppressed = false;

    private final RegulatedMotor motorLeft;
    private final RegulatedMotor motorRight;

    public MyDelay(
            final RegulatedMotor motorLeft,
            final RegulatedMotor motorRight) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
    }

    public boolean takeControl() {
        Delay.msDelay(2000);
        return true;
    }

    public void suppress() {
        suppressed = true;
    }

    public void action() {
        suppressed = false;
        while(!suppressed )
            Thread.yield();
        motorLeft.stop();
        motorRight.stop();
    }
}
