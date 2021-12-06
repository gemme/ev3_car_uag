package uag;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import uag.subsumption.DriveForward;
import uag.subsumption.HitWallColorSensor;
import ev3dev.sensors.ev3.EV3ColorSensor;

public class Ev3Robot {

    Ev3Robot() {
        this.init();
    }

    public void init(){
        EV3LargeRegulatedMotor motorLeft = new EV3LargeRegulatedMotor(MotorPort.A);
        EV3LargeRegulatedMotor motorRight = new EV3LargeRegulatedMotor(MotorPort.B);
        final int motorSpeed = 200;
        motorLeft.resetTachoCount();
        motorRight.resetTachoCount();
        motorLeft.setSpeed(motorSpeed);
        motorRight.setSpeed(motorSpeed);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run(){
                motorLeft.stop();
                motorRight.stop();
            }
        }));

        EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S1);

        Behavior b1 = new DriveForward(motorLeft, motorRight, colorSensor);
        Behavior b2 = new HitWallColorSensor(motorLeft, motorRight, colorSensor);
        Behavior []bArray = {b1, b2};
        Arbitrator arby = new Arbitrator(bArray);
        arby.go();
    }
}
