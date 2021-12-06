package uag.subsumption;

import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import uag.network.UDPClient;
import uag.network.TCPClient;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

public class HitWallColorSensor implements Behavior {

    private boolean suppressed = false;

    private final RegulatedMotor motorLeft;
    private final RegulatedMotor motorRight;
    private final EV3ColorSensor colorSensor;
    UDPClient clientUDP;
    TCPClient clientTCP;
    int color = Color.NONE;
    public HitWallColorSensor(
            final RegulatedMotor motorLeft,
            final RegulatedMotor motorRight,
            final EV3ColorSensor colorSensor) {
        this.motorLeft = motorLeft;
        this.motorRight = motorRight;
        this.colorSensor = colorSensor;
        try {
            // this.clientUDP = new UDPClient();
            this.clientTCP = new TCPClient("192.168.100.32", 4445);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean takeControl() {
        SampleProvider sp = colorSensor.getColorIDMode();
        int sampleSize = sp.sampleSize();
        float [] sample = new float[sampleSize];
        sp.fetchSample(sample, 0);
        this.color = (int)sample[0];
        return this.color ==  Color.BLUE || this.color ==  Color.RED;
    }

    public void suppress() {
        suppressed = true;
    }

    public void action() {
        suppressed = false;
        String command = null;
//        UDP
//        try {
//            command = this.clientUDP.sendMessage(String.valueOf(this.color));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//      TCP
        command = this.clientTCP.sendMessage(String.valueOf(this.color));

        System.out.println("command: " + command);
        System.out.println("command[0]: " + command.split("$"));
        String[] commandSplitted = command.split(",");
        if(commandSplitted[0].equals("rotate")) {
            System.out.println("rotating!!! ");
            int leftAngle = Integer.parseInt(commandSplitted[1]);
            int rightAngle = Integer.parseInt(commandSplitted[2]);
            motorLeft.rotate(leftAngle, true);
            motorRight.rotate(rightAngle, true);
        }

        while(motorRight.isMoving() && !suppressed )
            Thread.yield();

        motorLeft.stop();
        motorRight.stop();
    }
}
