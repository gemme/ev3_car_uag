package uag.subsumption;

import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

public class DriveForward  implements Behavior {

    private boolean suppressed = false;

    private final RegulatedMotor motorLeft;
    private final RegulatedMotor motorRight;
    private final EV3ColorSensor colorSensor;

    public DriveForward(
            final RegulatedMotor motorLeft,
            final RegulatedMotor motorRight,
            final EV3ColorSensor colorSensor
) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.colorSensor = colorSensor;
    }

    public boolean takeControl() {
        SampleProvider sp = colorSensor.getColorIDMode();
        int sampleSize = sp.sampleSize();
        float [] sample = new float[sampleSize];
        sp.fetchSample(sample, 0);
        int color = (int)sample[0];
        return color ==  Color.BLACK;
    }

    public void suppress() {
        suppressed = true;
    }

    public void action() {
        suppressed = false;
        motorLeft.forward();
        motorRight.forward();
        while( !suppressed )
            Thread.yield();
        motorLeft.stop(); // clean up
        motorRight.stop();
    }
}